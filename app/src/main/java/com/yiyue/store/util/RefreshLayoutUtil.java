package com.yiyue.store.util;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;

/**
 * Created by zm on 2018/9/20.
 */
public class RefreshLayoutUtil {
    /**
     * 关闭刷新或加载状态
     * @param refreshLayout
     */
    public static void finishRefreshLayout(RefreshLayout refreshLayout) {
        if (refreshLayout == null) return;
        if (refreshLayout.getState() == RefreshState.Refreshing) {
            refreshLayout.finishRefresh();
        }else if (refreshLayout.getState() ==  RefreshState.Loading) {
            refreshLayout.finishLoadMore();
        }
    }

}
