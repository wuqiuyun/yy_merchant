package com.yiyue.store.module.mine.store;

import com.yl.core.component.net.exception.ApiException;
import com.yiyue.store.api.CollectionApi;
import com.yiyue.store.api.FootApi;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.model.vo.result.StoreListResult;

/**
 * Created by zm on 2018/10/10.
 */
public class StorePresenter extends BasePresenter<IStoreView> {
    //我的收藏——门店列表
    public void getStoreCollection(double lat, double lng, int page, int size, String userId) {
        new CollectionApi().getStoreCollection(lat, lng, page, size, userId, new YLRxSubscriberHelper<StoreListResult>() {
            @Override
            public void _onNext(StoreListResult result) {
                    getMvpView().getStoreListSuccess(result.getData());
            }

            @Override
            public void _onError(ApiException error) {
                if (getMvpView()!=null)getMvpView().getStoreListFail();
            }
        });
    }

    //我的足迹——门店列表
    public void getStoreFoot(double lat, double lng, int page, int size, String userId) {
        new FootApi().getStoreFoot(lat, lng, page, size, userId, new YLRxSubscriberHelper<StoreListResult>() {
            @Override
            public void _onNext(StoreListResult result) {
                if (null != result.getData() && result.getData().size() > 0) {
                    getMvpView().getStoreListSuccess(result.getData());
                } else {
                    getMvpView().getStoreListFail();
                }
            }

            @Override
            public void _onError(ApiException error) {
                if (getMvpView()!=null)getMvpView().getStoreListFail();
            }
        });
    }
}
