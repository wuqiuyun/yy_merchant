package com.yiyue.store.base.fragment;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yiyue.store.R;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.base.mvp.IBaseView;

import java.util.ArrayList;

/**
 * 支持上拉加载下拉刷新
 * <p>
 * Created by zm on 2018/9/20.
 */
public abstract class BasePageMvpFragment<V extends IBaseView, P extends BasePresenter<V>, T> extends BaseMvpFragment<V, P>
        implements OnLoadMoreListener, OnRefreshListener{

    protected int pageIndx = 1; //第几页
    protected int pageSize = 10; // 每页数量

    protected ArrayList<T> data = new ArrayList<T>();

    /**
     * 支持上拉加载和下拉刷新
     */
    protected void initRefreshLoadLayout() {
        SmartRefreshLayout refreshLayout = rootView.findViewById(R.id.refresh_layout);
        if (refreshLayout != null) {
            refreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
            refreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));
            refreshLayout.setOnLoadMoreListener(this);
            refreshLayout.setOnRefreshListener(this);
        }
    }

    /**
     * 支持下拉刷新
     */
    protected void initRefreshLayout() {
        SmartRefreshLayout refreshLayout = rootView.findViewById(R.id.refresh_layout);
        if (refreshLayout != null) {
            refreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
            refreshLayout.setFooterHeight(0);
            refreshLayout.setOnRefreshListener(this);
        }
    }

    /**
     * 支持上拉加载
     */
    protected void initLoadLayout() {
        SmartRefreshLayout refreshLayout = rootView.findViewById(R.id.refresh_layout);
        if (refreshLayout != null) {
            refreshLayout.setRefreshFooter(new ClassicsFooter(getContext()));
            refreshLayout.setHeaderHeight(0);
            refreshLayout.setOnLoadMoreListener(this);
        }
    }

    @Override
    public void onLoadMore(RefreshLayout refreshLayout) {
        // 上拉加载
    }

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        // 下拉刷新
    }
}
