package com.yiyue.store.module.im.msgnotice;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityMsgNoticeBinding;
import com.yiyue.store.model.vo.bean.BulletinBean;
import com.yiyue.store.model.vo.bean.MsgNoticeBean;
import com.yiyue.store.module.im.msgnotice.msgdetail.MsgDetailActivity;
import com.yiyue.store.module.im.sysnotice.MsgNoticeAdapter;
import com.yiyue.store.util.RefreshLayoutUtil;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzz on 2018/9/27
 * 公告页面
 */
@CreatePresenter(MsgNoticePresenter.class)
public class MsgNoticeActivity extends BaseMvpAppCompatActivity<MsgNoticeView, MsgNoticePresenter>
        implements MsgNoticeView, OnRefreshListener, OnLoadMoreListener {
    private ActivityMsgNoticeBinding mBinding;
    private MsgNoticeAdapter adapter;
    private SmartRefreshLayout mRefreshLayout;
    private int page = 1;
    private int size = 10;//每页数量

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_msg_notice;
    }

    @Override
    protected void init() {
        mBinding = (ActivityMsgNoticeBinding) viewDataBinding;
        mBinding.titleView.setLeftClickListener(v -> finish());
        mBinding.recycleView.setHasFixedSize(true);
        mBinding.recycleView.setNestedScrollingEnabled(false);
        mBinding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        StatusBarUtil.setLightMode(this);

        adapter = new MsgNoticeAdapter(R.layout.adapter_msg_notice);
        adapter.openLoadAnimation();
        mBinding.recycleView.setAdapter(adapter);

        adapter.setOnItemClickListener((adapter, view, position) -> {
            Bundle bundle = new Bundle();
            bundle.putLong("id", ((BulletinBean) adapter.getData().get(position)).getId());
            MsgDetailActivity.startActivity(this, MsgDetailActivity.class, bundle);
        });

        initRefreshLoadLayout();
    }

    @Override
    protected void onResume() {
        super.onResume();
        page = 1;
        getMvpPresenter().getMsgList(page, size);
    }

    protected void initRefreshLoadLayout() {
        mRefreshLayout = mBinding.refreshLayout;
        if (mRefreshLayout != null) {
            mRefreshLayout.setRefreshHeader(new ClassicsHeader(this));
            mRefreshLayout.setRefreshFooter(new ClassicsFooter(this));
            mRefreshLayout.setOnLoadMoreListener(this);
            mRefreshLayout.setOnRefreshListener(this);
        }
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        page++;
        getMvpPresenter().getMsgList(page, size);
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        page = 1;
        getMvpPresenter().getMsgList(page, size);
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }

    @Override
    public void getMsgListSuc(List<BulletinBean> list) {
        RefreshLayoutUtil.finishRefreshLayout(mRefreshLayout);
        if (null != list && list.size() > 0){
            if (page == 1) {
                adapter.setNewData(list);
            } else {
                adapter.addData(list);
            }

            if (list.size() < size) {// 加载的数据不够页面数量 则认为没有下一页
                mRefreshLayout.setNoMoreData(true);
            } else {
                mRefreshLayout.setNoMoreData(false);
            }
        } else {
            showToast("没有更多数据");
        }
    }

    @Override
    public void getMsgListFail() {
        RefreshLayoutUtil.finishRefreshLayout(mRefreshLayout);
        mRefreshLayout.setNoMoreData(true);
    }
}
