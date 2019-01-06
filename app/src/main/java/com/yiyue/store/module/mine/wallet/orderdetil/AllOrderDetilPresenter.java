package com.yiyue.store.module.mine.wallet.orderdetil;

import com.yiyue.store.api.StoreOrderDetailApi;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.model.vo.result.OrderDetailResult;
import com.yiyue.store.model.vo.result.RegisterGapBetweenResult;
import com.yl.core.component.net.exception.ApiException;

/**
 * Created by wqy on 2018/12/18.
 */

public class AllOrderDetilPresenter extends BasePresenter<IAllOrderDetilView> {
    //订单明细
    public void findOrderDetail(int type,int pageNo,int pageSize) {
        new StoreOrderDetailApi().findOrderDetail(type,pageNo,pageSize,new YLRxSubscriberHelper<OrderDetailResult>() {
            @Override
            public void _onNext(OrderDetailResult orderDetailResult) {
                getMvpView().findOrderDetail(orderDetailResult.getData());
            }

            @Override
            public void _onError(ApiException error) {
                super._onError(error);
                getMvpView().findOrderDetailFail();
            }
        });
    }

    //注册奖励差距
    public void getRegisterGapBetween() {
        new StoreOrderDetailApi().getRegisterGapBetween(new YLRxSubscriberHelper<RegisterGapBetweenResult>() {
            @Override
            public void _onNext(RegisterGapBetweenResult registerGapBetweenResult) {
                getMvpView().getRegisterGapBetween(registerGapBetweenResult.getData());
            }
        });
    }
}
