package com.yiyue.store.module.im.friends;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yiyue.store.R;
import com.yiyue.store.base.fragment.BaseMvpFragment;
import com.yiyue.store.component.greendao.DaoCallBackInterface;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.FragmentFriendsBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.EventBean;
import com.yiyue.store.model.vo.bean.daobean.UserFriendsBean;
import com.yiyue.store.module.im.daoutil.UserFriendDaoUtils;
import com.yiyue.store.module.im.friendinfo.FriendInfoActivity;
import com.yiyue.store.module.im.friendinfo.PinyinComparator;
import com.yiyue.store.module.im.imsearch.ImSearchActivity;
import com.yiyue.store.util.StringUtil;
import com.yiyue.store.widget.WaveSideBar;
import com.yl.core.component.log.DLog;
import com.yl.core.component.mvp.factory.CreatePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by zhangzz on 2018/9/21
 * 我的好友页面fragment
 */

@CreatePresenter(FriendsPresenter.class)
public class FriendsFragment extends BaseMvpFragment<FriendsView, FriendsPresenter> implements FriendsView, OnRefreshListener {
    private FragmentFriendsBinding mBinding;

    private BaseQuickAdapter friendAdapter;
    List<UserFriendsBean> frindLists = null;
    List<UserFriendsBean> frindListsLocal = null;

    UserFriendDaoUtils userFriendDaoUtils;
    private SmartRefreshLayout refreshLayout;

    public static FriendsFragment getInstance() {
        return new FriendsFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_friends;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBean.FriendChangeEventBean event) {
        if (event != null) {
            if (Constants.EVENT_FRIEND_CHANGE == event.getTag()) {
                refreshLocalList();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * 在同意添加好友操作、删除好友、屏蔽好友、取消屏蔽好友、修改备注后
     * 刷新本地数据库列表
     */
    public void refreshLocalList() {
        requestListData();
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        mBinding = (FragmentFriendsBinding) viewDataBinding;
        mBinding.rvFriendList.setHasFixedSize(true);
        mBinding.rvFriendList.setNestedScrollingEnabled(false);
        mBinding.rvFriendList.setLayoutManager(new LinearLayoutManager(getActivity()));

        frindLists = new ArrayList<>();
        frindListsLocal = new ArrayList<>();

        friendAdapter = new UserFriendsAdapter(R.layout.adapter_user_friends, frindLists);
        friendAdapter.openLoadAnimation();
        mBinding.rvFriendList.setAdapter(friendAdapter);


        userFriendDaoUtils = new UserFriendDaoUtils(getActivity());
        userFriendDaoUtils.setOnQueryAllInterface(new DaoCallBackInterface.OnQueryAllInterface<UserFriendsBean>() {
            @Override
            public void onQueryAllBatchFailInterface() {
                requestListData(); //首次进入静默加载接口 用户也可以下拉刷新 此处不做调用
            }

            @Override
            public void onQueryAllBatchInterface(List list) {
                if (list == null || list.size() == 0) {
                    DLog.d("数据库中没有数据");
                    requestListData();
                } else {
                    frindListsLocal.clear();
                    frindListsLocal.addAll((List<UserFriendsBean>) list);
                    makeAdapterData(frindListsLocal);
                }
            }
        });

        userFriendDaoUtils.setOnDeleteInterface(new DaoCallBackInterface.OnDeleteInterface() {
            @Override
            public void onDeleteInterface(boolean type) {
                userFriendDaoUtils.insertBatchUser(frindLists);
            }
        });

        userFriendDaoUtils.setOnIsertInterface(new DaoCallBackInterface.OnIsertInterface() {
            @Override
            public void onIsertInterface(boolean type) {
                if (type) {
                    DLog.d("插入成功");
                } else {
                    DLog.d("插入失败");
                }
            }
        });

        mBinding.sideBar.setOnSelectIndexItemListener(new WaveSideBar.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String index) {
                for (int i = 0; i < frindLists.size(); i++) {
                    if (frindLists.get(i).getIndex().equals(index)) {
                        ((LinearLayoutManager) mBinding.rvFriendList.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                        return;
                    }
                }
            }
        });

        initEvent();
        userFriendDaoUtils.queryAllUser();
        initRefreshLoadLayout();
        requestListData();
    }

    protected void initEvent() {
        friendAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (frindLists != null && frindLists.size() > 0) {
                    Intent intent = new Intent(getActivity(), FriendInfoActivity.class);
                    intent.putExtra(Constants.EXTRA_CHAT_TYPE, Constants.CHATTYPE_SINGLE);
                    intent.putExtra(Constants.EXTRA_USER_ID, frindLists.get(position).getImusername());
                    intent.putExtra(Constants.EXTRA_FRIEND_ID, frindLists.get(position).getFriendId() + "");//朋友的用户id
                    intent.putExtra(Constants.EXTRA_FRIEND_IS_FROM_FRIEND, true);
                    startActivity(intent);
                } else {
                    showToast(getResources().getString(R.string.im_error_data));
                }
            }
        });

        rootView.findViewById(R.id.layout_search).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ImSearchActivity.class);
            startActivity(intent);
        });
    }

    protected void initRefreshLoadLayout() {
        refreshLayout = mBinding.refreshLayout;
        if (refreshLayout != null) {
            refreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
            refreshLayout.setOnRefreshListener(this);
        }
    }

    @Override
    protected void loadData() {

    }

    private void requestListData() {
        getMvpPresenter().findAllContacts(AccountManager.getInstance().getUserId());
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }


    @Override
    public void onFindAllContactsSuccess(List<UserFriendsBean> data) {
        refreshLayout.finishRefresh();
        if (data != null && data.size() > 0) {
            makeAdapterData(data);
            if (frindListsLocal.size() != data.size()) {
                userFriendDaoUtils.deleteAll();
                frindListsLocal = data;
            }
        } else {
            if (frindListsLocal.size() != 0) {
                frindLists.clear();
                frindListsLocal.clear();
                userFriendDaoUtils.deleteAll();
            }
            friendAdapter.setNewData(null);
        }
    }

    @Override
    public void onFindAllContactsFail() {
        refreshLayout.finishRefresh();
    }

    private void makeAdapterData(List<UserFriendsBean> data) {
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); ++i) {
                String name = null;
                if (!TextUtils.isEmpty(data.get(i).getRemarks())) {
                    name = data.get(i).getRemarks();
                } else if (!TextUtils.isEmpty(data.get(i).getNickname())) {
                    name = data.get(i).getNickname();
                } else {
                    data.get(i).setIndex("#");
                }
                if (!TextUtils.isEmpty(name)) {
                    String pinyin = StringUtil.getPinYin(name);
                    String Fpinyin = pinyin.substring(0, 1).toUpperCase();
                    // 正则表达式，判断首字母是否是英文字母
                    if (Fpinyin.matches("[A-Z]")) {
                        data.get(i).setIndex(Fpinyin);
                    } else {
                        data.get(i).setIndex("#");
                    }
                }
            }
            PinyinComparator pinyinComparator = new PinyinComparator();
            Collections.sort(data, pinyinComparator);
            frindLists = data;
            friendAdapter.setNewData(frindLists);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (userFriendDaoUtils != null) {
            userFriendDaoUtils.closeConnection();
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        frindListsLocal.clear();
        requestListData();
    }
}