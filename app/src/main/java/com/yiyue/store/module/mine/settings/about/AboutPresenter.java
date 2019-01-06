package com.yiyue.store.module.mine.settings.about;

import android.content.Context;

import com.yiyue.store.api.SettingsApi;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.model.vo.result.GetAppInfoResult;

/**
 * Created by lvlong on 2018/10/8.
 */
public class AboutPresenter extends BasePresenter<IAboutView> {
    //获取账户安全详情
    public void getAppInfo(Context context) {
        new SettingsApi().getAppInfos(new YLRxSubscriberHelper<GetAppInfoResult>(context,true) {
            @Override
            public void _onNext(GetAppInfoResult getAppInfoResult) {
                getMvpView().onAppInfoSuccess(getAppInfoResult.getData());
            }
        });
    }
}
