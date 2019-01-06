package com.yiyue.store.module.mine.pplarz.qrcode;


import com.yiyue.store.api.BasicSettingApi;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;

/**
 * Created by zm on 2018/12/29.
 */
public class InviteQRCodePresenter extends BasePresenter<InviteQRCodeView> {

    public void getWXShareQrCode(String inviteCode) {
        new BasicSettingApi().getWXShareQrCode(inviteCode, new YLRxSubscriberHelper<BaseResponse<String>>() {
            @Override
            public void _onNext(BaseResponse<String> result) {
                getMvpView().setShareQrCodeUrl(result.getData());
            }
        });
    }
}
