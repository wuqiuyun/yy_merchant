package com.yiyue.store.module.im.groups.groupsearch;

import android.text.TextUtils;

import com.yl.core.component.net.exception.ApiException;
import com.yiyue.store.api.GroupApi;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.model.vo.result.GroupListResult;

/**
 * Created by Lizhuo on 2018/10/15.
 */
public class GroupSearchPresenter extends BasePresenter<GroupSearchView> {
    /**
     * 分页查询群组
     *
     * @param param    查询关键字
     * @param pageNo   页码
     * @param pageSize 分页数量
     */
    public void searchGroupPage(String param, int pageNo, int pageSize) {
        if (TextUtils.isEmpty(param)) {
            ToastUtils.shortToast("param不能为空");
            return;
        }

        new GroupApi().searchGroupPage(param, String.valueOf(pageNo), String.valueOf(pageSize), new YLRxSubscriberHelper<GroupListResult>() {
            @Override
            public void _onNext(GroupListResult result) {
                getMvpView().searchGroupSuccess(result.getData());
            }

            @Override
            public void _onError(ApiException error) {
                getMvpView().searchGroupFail();
            }
        });
    }
}
