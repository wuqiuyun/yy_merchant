package com.yiyue.store.module.main;

import android.content.Context;

import com.yiyue.store.api.SettingsApi;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.model.vo.result.GetAppInfoResult;

/**
 * Created by zm on 2018/9/10.
 */
public class MainPresenter extends BasePresenter<IMainView> {

    //获取账户安全详情
    public void getAppInfo(Context context) {
        new SettingsApi().getAppInfos(new YLRxSubscriberHelper<GetAppInfoResult>(context,true) {
            @Override
            public void _onNext(GetAppInfoResult getAppInfoResult) {
                getMvpView().onUpdateAppInfo(getAppInfoResult.getData());
            }
        });
    }
}
