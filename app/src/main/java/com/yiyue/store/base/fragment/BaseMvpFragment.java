package com.yiyue.store.base.fragment;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yl.core.component.log.DLog;
import com.yl.core.component.mvp.view.AbstractFragment;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.base.mvp.IBaseView;

/**
 * Created by zm on 2018/9/14.
 */

public abstract class BaseMvpFragment<V extends IBaseView, P extends BasePresenter<V>> extends AbstractFragment<V, P> {

    protected View rootView;
    protected ViewDataBinding viewDataBinding;
    protected Bundle savedInstanceState;

    // Fragment的View加载完毕的标记
    private boolean isViewCreated;

    // Fragment对用户可见的标记
    private boolean isUIVisible;

    @LayoutRes
    protected abstract int getLayoutId();

    protected abstract void initView();

    protected abstract void loadData();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.isViewCreated = true;
        lazyLoad();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.savedInstanceState = savedInstanceState;
        if (rootView == null) {
            this.rootView = inflater.inflate(getLayoutId(), container, false);
            viewDataBinding = DataBindingUtil.bind(rootView);
            initView();
        }
        return rootView;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // isVisibleToUser这个boolean值表示:该Fragment的UI 用户是否可见
        if (isVisibleToUser) {
            isUIVisible = true;
            lazyLoad();
        } else {
            isUIVisible = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (viewDataBinding != null) {
            viewDataBinding.unbind();
        }
    }

    private void lazyLoad() {
        // 这里进行双重标记判断,是因为setUserVisibleHint会多次回调,并且会在onCreateView执行前回调,必须确保onCreateView加载完毕且页面可见,才加载数据
        if (isViewCreated && isUIVisible) {
            loadData();
            // 数据加载完毕,恢复标记,防止重复加载
            isViewCreated = false;
            isUIVisible = false;
        }
    }

}
