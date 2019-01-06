package com.yiyue.store.module.mine.pplarz.banding;

import android.text.TextUtils;

import com.yiyue.store.api.RecomUserApi;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;

/**
 * Created by Lizhuo on 2018/10/29.
 */
public class BandingRefereesPresenter extends BasePresenter<IBandingRefereesView> {
    public void bindInviteCode(String code) {
        if (TextUtils.isEmpty(code)) {
            getMvpView().showToast("推荐码不可为空");
            return;
        }
        
        new RecomUserApi().bindInviteCode(code, new YLRxSubscriberHelper<BaseResponse>() {
            @Override
            public void _onNext(BaseResponse baseResponse) {
                getMvpView().bindingSuc();
            }
        });
    }
}
