package com.yiyue.store.module.mine.stylist.invitejoin;

import android.app.Activity;
import android.content.Context;

import com.yiyue.store.api.StoreApi;
import com.yiyue.store.api.StoreStylistApi;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.model.vo.result.CheckMsgResult;
import com.yiyue.store.model.vo.result.SendMsgResult;
import com.yiyue.store.model.vo.result.StoreInfoResult;
import com.yl.core.component.net.exception.ApiException;

/**
 * Created by zm on 2018/10/11.
 */
public class InviteJoinPresenter extends BasePresenter<IInviteJoinView> {

    /**
     * 门店信息接口
     *
     * @param storeId  门店id
     * @param activity
     */
    public void getStylistCardList(String storeId, Activity activity) {

        new StoreStylistApi().getStoreInforamtion(storeId, new YLRxSubscriberHelper<StoreInfoResult>(activity, true) {
            @Override
            public void _onNext(StoreInfoResult storeInfoResult) {
                // callback
                getMvpView().getStoreInforamtion(storeInfoResult);
            }
        });
    }


    /**
     * 美发师入驻签约/入驻发送消息接口
     * nexus (integer, optional): 签约类型0入驻平台，1签约门店 ,
     * @param storeId
     * @param stylistId
     * @param context
     */
    public void sendMsg(String storeId, String stylistId, int type, Context context) {
        new StoreApi().sendMsg(storeId, stylistId, type, new YLRxSubscriberHelper<SendMsgResult>(context, true) {
            @Override
            public void _onNext(SendMsgResult sendMsgResult) {
                getMvpView().sendMsg(sendMsgResult, type);
            }

            @Override
            public void _onError(ApiException error) {
                super._onError(error);
            }
        });
    }
}
