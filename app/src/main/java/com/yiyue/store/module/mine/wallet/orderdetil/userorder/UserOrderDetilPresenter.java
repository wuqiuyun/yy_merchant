package com.yiyue.store.module.mine.wallet.orderdetil.userorder;

import com.yiyue.store.api.StoreOrderDetailApi;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.model.vo.result.StoreStylistResult;
import com.yiyue.store.model.vo.result.UserOrderResult;
import com.yl.core.component.net.exception.ApiException;

/**
 * Created by wqy on 2018/12/18.
 */

public class UserOrderDetilPresenter extends BasePresenter<IUserOrderDetilView> {
    //门店-美发师统计-用户订单统计
    public void findOrderIncomeSumByStoreAndStylist(String stylistId,String date) {
        new StoreOrderDetailApi().findOrderIncomeSumByStoreAndStylist(stylistId,date,new YLRxSubscriberHelper<StoreStylistResult>() {
            @Override
            public void _onNext(StoreStylistResult storeStylistResult) {
                    getMvpView().getOrderIncomeSum(storeStylistResult.getData());
            }


        });
    }

    //门店-美发师统计-用户订单列表
    public void findOrderDetailByStoreAndStylist(String stylistId,String date,int pageNo,int pageSize) {
        new StoreOrderDetailApi().findOrderDetailByStoreAndStylist( stylistId, date, pageNo, pageSize
                ,new YLRxSubscriberHelper<UserOrderResult>() {
            @Override
            public void _onNext(UserOrderResult userOrderResult) {
                getMvpView().getUserOrder(userOrderResult.getData());
            }
            @Override
            public void _onError(ApiException error) {
                super._onError(error);
                getMvpView().getFail();
            }
        });
    }
}
