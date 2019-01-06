package com.yiyue.store.module.mine.settings.security.phone.password;

import com.yiyue.store.api.SettingsApi;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;

/**
 * Created by lvlong on 2018/10/12.
 */
public class PasswordVerifyPresenter extends BasePresenter<PasswordVerifyView> {

    public void checkPassword(String password){
        new SettingsApi().checkPassword(password, new YLRxSubscriberHelper<BaseResponse>() {
            @Override
            public void _onNext(BaseResponse baseResponse) {
                getMvpView().onCheckPassword();
            }
        });
    }
}
