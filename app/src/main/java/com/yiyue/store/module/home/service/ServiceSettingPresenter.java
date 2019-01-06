package com.yiyue.store.module.home.service;

import com.yiyue.store.api.CategoryApi;
import com.yiyue.store.api.StoreApi;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.model.vo.requestbody.CommitSetServiceBody;
import com.yiyue.store.model.vo.result.GetCategoryResult;
import com.yiyue.store.model.vo.result.StoreSettinResult;
import com.yl.core.component.net.exception.ApiException;

/**
 * Created by lvlong on 2018/10/9.
 */
public class ServiceSettingPresenter extends BasePresenter<ServiceSettingView> {
    /**
     * 获取服务项目
     */
    public void getAll() {
        new CategoryApi().getAll(new YLRxSubscriberHelper<GetCategoryResult>() {
            @Override
            public void _onNext(GetCategoryResult baseResponse) {
                getMvpView().onCommitSuccess(baseResponse.getData());
            }
        });
    }
    /**
     * 提交服务设置
     */
    public void commitServiceData(CommitSetServiceBody commitSetServiceBody) {
        if (!commitSetServiceBody.checkStoreParams())return;
        new StoreApi().setting(commitSetServiceBody,new YLRxSubscriberHelper<BaseResponse>() {
            @Override
            public void _onNext(BaseResponse baseResponse) {
                getMvpView().onCommitServiceData();
            }

            @Override
            public void _onError(ApiException error) {
                super._onError(error);
            }
        });
    }
    /**
     * 获取服务设置
     */
    public void getStoreSetting(String  storeId) {
        new StoreApi().getStoreSetting(storeId,new YLRxSubscriberHelper<StoreSettinResult>() {
            @Override
            public void _onNext(StoreSettinResult result) {
                getMvpView().onSuccess(result.getData());
            }
        });
    }
}
