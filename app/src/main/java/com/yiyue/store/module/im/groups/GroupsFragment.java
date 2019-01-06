package com.yiyue.store.module.im.groups;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yiyue.store.R;
import com.yiyue.store.base.fragment.BaseMvpFragment;
import com.yiyue.store.component.greendao.DaoCallBackInterface;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.FragmentGroupsBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.EventBean;
import com.yiyue.store.model.vo.bean.daobean.GroupBean;
import com.yiyue.store.module.im.chat.ChatActivity;
import com.yiyue.store.module.im.daoutil.GroupDaoUtils;
import com.yiyue.store.module.im.imsearch.ImSearchGroupActivity;
import com.yl.core.component.log.DLog;
import com.yl.core.component.mvp.factory.CreatePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzz on 2018/9/28
 * 我的群组fragment
 */
@CreatePresenter(GroupsPresenter.class)
public class GroupsFragment extends BaseMvpFragment<GroupsView, GroupsPresenter> implements GroupsView, OnRefreshListener {
    private FragmentGroupsBinding mBinding;
    private BaseQuickAdapter groupAdapter;
    private List<GroupBean> groupList = new ArrayList<>();
    private String mUserId;

    private GroupDaoUtils groupDaoUtils;

    private List<GroupBean> groupListLocal = new ArrayList<>();//本地数据库中的群列表


    private SmartRefreshLayout refreshLayout;

    public static GroupsFragment getInstance() {
        return new GroupsFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_groups;
    }

    /**
     * 进入群或者退出群
     * 0：进入 1：退出
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBean.GroupJoinOrOut event) {
        if (event != null) {
            if (event.getTag() == 1) {
                groupDaoUtils.deleteByIdSingleUser(event.getId());
                requestData();
            } else {
                requestData();
            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBean.GroupChangeEvent event) {
        if (event != null) {
            if (Constants.EVENT_GROUP_CHANGE == event.getTag()) {//创建群成功刷新群列表
                requestData();
            }
        }
    }

    @Override
    protected void initView() {
        mBinding = (FragmentGroupsBinding) viewDataBinding;
        EventBus.getDefault().register(this);
        mBinding.rvGroups.setHasFixedSize(true);
        mBinding.rvGroups.setNestedScrollingEnabled(false);
        mBinding.rvGroups.setLayoutManager(new LinearLayoutManager(getActivity()));
        mUserId = AccountManager.getInstance().getUserId();

        groupDaoUtils = new GroupDaoUtils(getActivity());
        groupDaoUtils.setOnQueryAllInterface(new DaoCallBackInterface.OnQueryAllInterface<GroupBean>() {
            @Override
            public void onQueryAllBatchFailInterface() {
                requestData();
            }

            @Override
            public void onQueryAllBatchInterface(List list) {
                if (list == null || list.size() == 0) {
                    requestData();
                } else {
                    groupListLocal.clear();
                    groupListLocal.addAll(list);
                    groupAdapter.setNewData(groupListLocal);
                }
            }
        });

        groupDaoUtils.setOnDeleteInterface(new DaoCallBackInterface.OnDeleteInterface() {
            @Override
            public void onDeleteInterface(boolean type) {
                if (groupList != null && groupList.size() > 0)
                    groupDaoUtils.insertBatchUser(groupList);
            }
        });

        groupDaoUtils.setOnIsertInterface(new DaoCallBackInterface.OnIsertInterface() {
            @Override
            public void onIsertInterface(boolean type) {
                DLog.d("数据库中添加数据成功");
            }
        });

        groupAdapter = new GroupAdapter(R.layout.adapter_groups_item);
        groupAdapter.openLoadAnimation();
        mBinding.rvGroups.setAdapter(groupAdapter);

        initRefreshLoadLayout();
        initEvent();
        groupDaoUtils.queryAllUser();//查询本地数据库中的群列表

        requestData();
    }

    protected void initRefreshLoadLayout() {
        refreshLayout = mBinding.refreshLayout;
        if (refreshLayout != null) {
            refreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
            refreshLayout.setOnRefreshListener(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    protected void initEvent() {
        groupAdapter.setOnItemClickListener((adapter, view, position) ->
        {
            if (groupAdapter.getData() != null && groupAdapter.getData().size() > 0 && groupAdapter.getData().get(position) != null) {
                ChatActivity.startFromGroupActivity(getActivity(), (GroupBean) groupAdapter.getData().get(position));
            }
        });

        rootView.findViewById(R.id.layout_search).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ImSearchGroupActivity.class);
            startActivity(intent);
        });
    }

    private void requestData() {
        getMvpPresenter().getMyGroups(mUserId);
    }

    @Override
    protected void loadData() {
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }

    @Override
    public void getGroupsSuccess(List<GroupBean> list) {
        refreshLayout.finishRefresh();
        if (list != null && list.size() > 0) {
            groupList = list;
            groupAdapter.setNewData(groupList);
            if (groupListLocal.size() != list.size()) {
                groupDaoUtils.deleteAll();
                groupListLocal = list;
            }
        } else {
            if (groupListLocal.size() != 0) {
                groupList.clear();
                groupListLocal.clear();
                groupDaoUtils.deleteAll();
            }
            groupAdapter.setNewData(null);
        }
    }

    @Override
    public void getGroupsEmpty() {
        refreshLayout.finishRefresh();
        groupList.clear();
        groupListLocal.clear();
        groupDaoUtils.deleteAll();
        groupAdapter.setNewData(null);
        DLog.d("群组列表为空");
    }

    @Override
    public void getGroupsFail() {
        refreshLayout.finishRefresh();
        DLog.d("群组列表获取失败");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (groupDaoUtils != null) {
            groupDaoUtils.closeConnection();
        }
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        groupListLocal.clear();
        requestData();
    }
}
