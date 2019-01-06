package com.yiyue.store.base.fragment;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yiyue.store.R;

import java.util.ArrayList;

/**
 * Created by zm on 2018/9/27.
 */
public abstract class BasePageFragment<T> extends BaseFragment implements OnLoadMoreListener, OnRefreshListener {
    protected int pageIndx; //第几页

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
