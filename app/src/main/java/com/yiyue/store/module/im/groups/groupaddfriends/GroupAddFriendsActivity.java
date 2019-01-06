package com.yiyue.store.module.im.groups.groupaddfriends;


import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.hyphenate.chat.EMMessage;
import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityGroupAddFriendsBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.EventBean;
import com.yiyue.store.model.vo.bean.GroupAddReqBean;
import com.yiyue.store.model.vo.bean.GroupJoinMemberBean;
import com.yiyue.store.model.vo.bean.SelfDefinedInfoBean;
import com.yiyue.store.model.vo.bean.daobean.GroupUserBean;
import com.yiyue.store.model.vo.bean.daobean.UserFriendsBean;
import com.yiyue.store.util.easyutils.EasyUtil;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 邀请好友进群
 * Created by lizhuo on 2018/10/13.
 * <p>
 * 接口调试：zhangzz
 */
@CreatePresenter(GroupAddFriendsPresenter.class)
public class GroupAddFriendsActivity extends BaseMvpAppCompatActivity<GroupAddFriendsView, GroupAddFriendsPresenter> implements GroupAddFriendsView {
    private ActivityGroupAddFriendsBinding mBinding;
    private List<UserFriendsBean> mFrindList = null;
    private BaseQuickAdapter mFriendsAdapter;

    //上个页面传入
    private String groupId;
    private String imgroup;
    private String groupName;
    private String groupPath;

    protected List<GroupUserBean> groupMemberBeans = new ArrayList<>();//群成员bean， 资料页带入
    List<GroupJoinMemberBean> groupJoinMemberBeans = new ArrayList<>();

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_group_add_friends;
    }

    @Override
    protected void init() {
        StatusBarUtil.setLightMode(this);
        mBinding = (ActivityGroupAddFriendsBinding) viewDataBinding;
        mBinding.titleView.setLeftClickListener(view -> finish());
        mBinding.titleView.setRightTextClickListener(view -> {
            getSelectedFriends();
        });

        groupId = getIntent().getStringExtra("groupId");
        imgroup = getIntent().getStringExtra("imgroup");
        groupName = getIntent().getStringExtra("groupName");
        groupPath = getIntent().getStringExtra("groupPath");
        groupMemberBeans = getIntent().getParcelableArrayListExtra("groupBeanReq");
        initView();
        initData();
    }

    private void initData() {
        mFrindList = new ArrayList<>();
        getMvpPresenter().findAllContacts(AccountManager.getInstance().getUserId(), this);//本地数据库好友列表查询为空或者失败时  要做网络请求并更新数据库
    }

    private void initView() {
        mBinding.rvAddTogroup.setHasFixedSize(true);
        mBinding.rvAddTogroup.setNestedScrollingEnabled(false);
        mBinding.rvAddTogroup.setLayoutManager(new LinearLayoutManager(this));

        mFriendsAdapter = new GroupAddFriendsAdapter(R.layout.adapter_group_add_friends, true);
        mFriendsAdapter.openLoadAnimation();
        mBinding.rvAddTogroup.setAdapter(mFriendsAdapter);
    }

    private void getSelectedFriends() {
        List<GroupAddReqBean> groupAddReqBeans = new ArrayList<>();

        AccountManager.getInstance().getUserId();
        for (int i = 0; i < mFrindList.size(); i++) {
            UserFriendsBean bean = mFrindList.get(i);
            if (bean.isSelect()) {
                groupAddReqBeans.add(new GroupAddReqBean(mFrindList.get(i).getImusername(), mFrindList.get(i).getFriendId() + ""));
                groupJoinMemberBeans.add(new GroupJoinMemberBean(mFrindList.get(i).getNickname(), mFrindList.get(i).getImusername()));
            }
        }

        if (groupAddReqBeans.size() > 0 && groupAddReqBeans.size() == 1) {//邀请单个用户入群
            getMvpPresenter().addSingleToGroup(groupId, imgroup, groupAddReqBeans.get(0).getImusername(), groupAddReqBeans.get(0).getUserId() + "");
        } else if (groupAddReqBeans.size() > 1) {//邀请批量用户入群
            getMvpPresenter().addBatchToGroup(groupId, imgroup, groupAddReqBeans);
        } else {
            showToast("您未选择好友");
        }
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }

    @Override
    public void addSingleToGroupSuccess() {
        showToast("邀请单个用户入群成功");

        sendMessage();
        EventBus.getDefault().post(new EventBean.GroupUpdate(0));//刷新群信息页面
        finish();
    }

    @Override
    public void addBatchToGroupSuccess() {
        showToast("邀请批量用户入群成功");
        sendMessage();
        EventBus.getDefault().post(new EventBean.GroupUpdate(0));//刷新群信息页面
        finish();
    }

    protected void sendMessage() {
        if (groupJoinMemberBeans != null && groupJoinMemberBeans.size() > 0) {
            StringBuffer groupJoinContent = new StringBuffer();
            if (groupJoinMemberBeans.size() == 1){
                groupJoinContent.append(" 邀请" + groupJoinMemberBeans.get(0).getNickName() + "加入群聊");
            } else {
                groupJoinContent.append(" 邀请" + groupJoinMemberBeans.get(0).getNickName() + " ...加入群聊");
            }
            EMMessage message = EasyUtil.getEmManager().createTxtSendMessage(groupJoinContent.toString(), imgroup);
            message.setAttribute(Constants.MESSAGE_ATTR_IS_VOICE_CALL, false);
            SelfDefinedInfoBean selfBean = new SelfDefinedInfoBean();
            selfBean.setImusername(AccountManager.getInstance().getAccount().getImusername());
            selfBean.setPath(AccountManager.getInstance().getAccount().getHeadImg());
            selfBean.setNickname(AccountManager.getInstance().getAccount().getNickname());
            String usid = AccountManager.getInstance().getUserId();
            if (!TextUtils.isEmpty(usid)) {
                selfBean.setUserId(Long.parseLong(usid));
            }

            message.setChatType(EMMessage.ChatType.GroupChat);    //如果是群聊，设置chattype，默认是单聊
            selfBean.setRecvImusername(imgroup);
            selfBean.setRecvNickname(groupName);
            if (!TextUtils.isEmpty(groupId)) {
                selfBean.setRecvUserId(Long.parseLong(groupId));
            }
            selfBean.setRecvPath(groupPath);
            selfBean.setDefinedMsgType(401);
            selfBean.setGroupJoinMembers(groupJoinMemberBeans);

            message.setAttribute(Constants.IM_MESSAGE_ORDER_ADDMONEY_KEY, new Gson().toJson(selfBean));//自定义消息类型  用这个key传值/ios端也能识别
            // 调用发送消息的方法
            EasyUtil.getEmManager().sendMessage(message);
        }
    }

    @Override
    public void onFindAllContactsSuccess(List<UserFriendsBean> data) {
        if (data != null && data.size() > 0) {
            mFrindList = data;
            if (groupMemberBeans != null && groupMemberBeans.size() > 0) {
                for (UserFriendsBean userFriendsBean : data) {
                    userFriendsBean.setEnable(true);
                    for (GroupUserBean groupUserBean : groupMemberBeans) {
                        if (userFriendsBean.getImusername().equals(groupUserBean.getImusername())) {
                            userFriendsBean.setEnable(false);
                            break;
                        }
                    }
                }
            }

            mFriendsAdapter.setNewData(mFrindList);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
