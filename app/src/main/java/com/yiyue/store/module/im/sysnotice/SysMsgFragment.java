package com.yiyue.store.module.im.sysnotice;

import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yiyue.store.component.greendao.DaoCallBackInterface;
import com.yiyue.store.model.vo.bean.daobean.ImMessageBean;
import com.yiyue.store.module.im.daoutil.ImMessageDaoUtils;
import com.yl.core.component.log.DLog;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yiyue.store.R;
import com.yiyue.store.base.fragment.BaseMvpFragment;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.FragmentSystemMsgBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.EventBean;
import com.yiyue.store.model.vo.bean.SysMsgBean;
import com.yiyue.store.model.vo.bean.daobean.UserFriendsBean;
import com.yiyue.store.module.im.daoutil.UserFriendDaoUtils;
import com.yiyue.store.widget.dialog.BaseEasyDialog;
import com.yiyue.store.widget.dialog.EasyDialog;
import com.yiyue.store.widget.dialog.ViewConvertListener;
import com.yiyue.store.widget.dialog.ViewHolder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by zhangzz on 2018/9/28
 * 互动消息fragment
 */
@CreatePresenter(SysMsgPresenter.class)
public class SysMsgFragment extends BaseMvpFragment<SysMsgView, SysMsgPresenter> implements SysMsgView {
    private FragmentSystemMsgBinding mBinding;
    private SysMsgAdapter2 adapter;
    private List<ImMessageBean> mList = new ArrayList<>();
    private ImMessageBean currSysMsgBean;//当前点击的item
    private String yesOrNo = "0";//1为同意添加 2位否
    private UserFriendDaoUtils userFriendDaoUtils;
    private ImMessageDaoUtils imMessageDaoUtils;


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_system_msg;
    }

    public static SysMsgFragment getInstance() {
        return new SysMsgFragment();
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        mBinding = (FragmentSystemMsgBinding) viewDataBinding;
        mBinding.rvSysMsgs.setHasFixedSize(true);
        mBinding.rvSysMsgs.setNestedScrollingEnabled(false);
        mBinding.rvSysMsgs.setLayoutManager(new LinearLayoutManager(getActivity()));

        userFriendDaoUtils = new UserFriendDaoUtils(getActivity());
        imMessageDaoUtils = new ImMessageDaoUtils(getActivity());

        imMessageDaoUtils.setOnQueryAllInterface(new DaoCallBackInterface.OnQueryAllInterface<ImMessageBean>() {
            @Override
            public void onQueryAllBatchInterface(List<ImMessageBean> list) {
                if (null != list && list.size() > 0) {//查询数据库数据后 重置本地的list 再加载
                    mList.clear();
                    Collections.reverse(list);//倒序排放，使时间最后的排上面
                    mList.addAll(list);
                    adapter.setNewData(mList);
                }
            }

            @Override
            public void onQueryAllBatchFailInterface() {
                DLog.e("OrderMessageDao", "--------------------onQueryAllBatchFailInterface");
            }
        });

        imMessageDaoUtils.setOnDeleteInterface(type -> {
            if (type){
                mList.clear();
                adapter.setNewData(mList);
            }
        });

        imMessageDaoUtils.setOnUpdateInterface(type -> {
            if (type){//更新完成
                loadData();
            }
        });

        adapter = new SysMsgAdapter2(R.layout.adapter_ststem_msg);
        adapter.openLoadAnimation();
        mBinding.rvSysMsgs.setAdapter(adapter);

        adapter.setOnItemChildClickListener((adapter, view, position) -> {
            currSysMsgBean = mList.get(position);
            if (view.getId() == R.id.tv_agree) {
                if (currSysMsgBean != null) {
                    yesOrNo = "1";
                    if (currSysMsgBean.getType() == 1) {
                        showToast("同意好友申请");
                        getMvpPresenter().receiveAddFriend(currSysMsgBean.getId(), yesOrNo);
                    } else if (currSysMsgBean.getType() == 2) {
                        showToast("同意加群申请");
                        getMvpPresenter().requestReceiveAddGroup(currSysMsgBean.getId(), yesOrNo);
                    }
                }
            }

            if (view.getId() == R.id.tv_refuse) {
                currSysMsgBean = mList.get(position);
                if (currSysMsgBean != null) {
                    yesOrNo = "2";
                    if (currSysMsgBean.getType() == 1) {
                        showToast("拒绝好友申请");
                        getMvpPresenter().receiveAddFriend(currSysMsgBean.getId(), yesOrNo);
                    } else if (currSysMsgBean.getType() == 2) {
                        showToast("拒绝加群申请");
                        getMvpPresenter().requestReceiveAddGroup(currSysMsgBean.getId(), yesOrNo);
                    }
                }
            }
        });
    }

    @Override
    protected void loadData() {
//        getMvpPresenter().findAddFriend(AccountManager.getInstance().getUserId() + "");
        imMessageDaoUtils.clearDaoSession();
        imMessageDaoUtils.queryAll();//查询数据库的全部内容
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }

//    @Override
//    public void onFindAddFriendSuccess(List<SysMsgBean> data) {
//        if (data != null && data.size() > 0) {
//            mList = data;
//            adapter.setNewData(mList);
//        }
//    }

    @Override
    public void onReceiveAddFriendSuccess() {
        //同意添加好友后 添加好友到好友库，并帅新好友列表
        if (mList != null && mList.size() > 0 && currSysMsgBean != null && "1".equals(yesOrNo)) {
            if (1 == currSysMsgBean.getType()) {
                UserFriendsBean userFriendsBean = new UserFriendsBean();
                userFriendsBean.setFriendId(currSysMsgBean.getFriendId());
                userFriendsBean.setImusername(currSysMsgBean.getImusername());
                userFriendsBean.setLabel(currSysMsgBean.getLabel());
                userFriendsBean.setCreatetime(currSysMsgBean.getCreatetime());
                userFriendsBean.setNickname(currSysMsgBean.getRequestNickname());
                userFriendsBean.setPath(currSysMsgBean.getRequestPtah());
                userFriendDaoUtils.insertUser(userFriendsBean);
                EventBus.getDefault().post(new EventBean.FriendChangeEventBean(Constants.EVENT_FRIEND_CHANGE));
            }
        }
        //更新本地互动消息数据状态
        updateImMessage(yesOrNo);
    }
    
    //更新本地数据库状态 1 同意 2拒绝
    private void updateImMessage(String tag){
        if (null != currSysMsgBean){
            if (tag.equals("1")){//同意
                currSysMsgBean.setStatus(1);
            } else if (tag.equals("2")){//拒绝
                currSysMsgBean.setStatus(2);
            }
        }
        imMessageDaoUtils.updateSingle(currSysMsgBean);
    }

    /**
     * 收到新的推送 刷新显示
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBean.NewImMessage event) {
        if (event != null) {
            loadData();
        }
    }
    
    @Override
    public void onReceiveAddGroupSuccess() {
        //更新本地互动消息数据状态
        updateImMessage(yesOrNo);
        EventBus.getDefault().post(new EventBean.GroupChangeEvent(Constants.EVENT_GROUP_CHANGE));
    }

    //好友添加申请对话框
    private void showRafDialog(Integer type, String id, String name) {
        EasyDialog.init()
                .setLayoutId(R.layout.dialog_receive_addfriend)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseEasyDialog dialog) {
                        String content = String.format(getResources().getString(type == 1 ? R.string.sg_receive_add_desc : R.string.sg_receive_addgroup_desc), name);
                        holder.setText(R.id.tv_raf_content, content);
                        holder.setText(R.id.tv_dialog_title, type == 1 ? R.string.sg_receive_add_friend : R.string.sg_receive_add_group);
                        holder.getView(R.id.tv_raf_ok).setOnClickListener(v -> dialog.dismiss());
                        holder.getView(R.id.tv_raf_cancel).setOnClickListener(v -> dialog.dismiss());
                    }
                })
                .setPosition(Gravity.CENTER)
                .setMargin(45)
                .setOutCancel(true)
                .show(getActivity().getSupportFragmentManager());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (userFriendDaoUtils != null) {
            userFriendDaoUtils.closeConnection();
        }
        if (imMessageDaoUtils != null) {
            imMessageDaoUtils.closeConnection();
        }
    }

    //清空数据
    public void clearData(){
        if (null != imMessageDaoUtils){
            imMessageDaoUtils.deleteAll();
        }
    }
}
