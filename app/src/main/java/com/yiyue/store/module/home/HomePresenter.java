package com.yiyue.store.module.home;

import com.yiyue.store.api.StoreApi;
import com.yiyue.store.api.StoreAttachApi;
import com.yiyue.store.api.StoreAuthApplyApi;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.model.vo.bean.BannerBean;
import com.yiyue.store.model.vo.bean.StoreAttachByTypeBean;
import com.yiyue.store.model.vo.result.BannerResult;
import com.yiyue.store.model.vo.result.GetStoreAttachByTypeResult;
import com.yiyue.store.model.vo.result.GetStoreAuthApplyResult;
import com.yiyue.store.model.vo.result.GetStoreOrderStatisticalResult;

import java.util.List;

/**
 * Created by zm on 2018/9/19.
 */
public class HomePresenter extends BasePresenter<IHomeView> {


    /**
     * 获取认证信息
     */
    public void getStoreAuthApplyInfo() {
        new StoreAuthApplyApi().getStoreAuthApplyByStoreId(new YLRxSubscriberHelper<GetStoreAuthApplyResult>() {
            @Override
            public void _onNext(GetStoreAuthApplyResult baseResponse) {
                getMvpView().onGetStoreAuthApplyInfoSuccess(baseResponse.getData());
            }
        });
    }

    /**
     * 获取订单统计
     */
    public void getStoreOrderStatistical() {
        new StoreApi().getStoreOrderStatistical(new YLRxSubscriberHelper<GetStoreOrderStatisticalResult>() {
            @Override
            public void _onNext(GetStoreOrderStatisticalResult baseResponse) {
                getMvpView().onGetStoreOrderStatisticalSuccess(baseResponse.getData());
            }
        });
    }

    /**
     * banner
     */
    public void getBanner() {
        new StoreAttachApi().getBanner(new YLRxSubscriberHelper<BannerResult>() {
            @Override
            public void _onNext(BannerResult baseResponse) {
                if (null != baseResponse.getData()) {
                    getMvpView().getBannerSuccess(baseResponse.getData());
                }
            }
        });
    }
}
