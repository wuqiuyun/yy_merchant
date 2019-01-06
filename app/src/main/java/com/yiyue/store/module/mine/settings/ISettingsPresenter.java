package com.yiyue.store.module.mine.settings;

import com.yiyue.store.api.RecomUserApi;
import com.yiyue.store.api.SettingsApi;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.model.vo.result.FindReCodeResult;
import com.yl.core.component.net.exception.ApiException;

/**
 * Created by zm on 2018/9/29.
 */
public class ISettingsPresenter extends BasePresenter<ISettingsView> {
    public void changeNotice(int shutdown){
        new SettingsApi().changeNotice(shutdown, new YLRxSubscriberHelper<BaseResponse>() {
            @Override
            public void _onNext(BaseResponse baseResponse) {
                getMvpView().changeNoticeSuc();
            }
        });
    }

    /**
     * 获取我的推荐码
     */
    public void findReCode(){
        new RecomUserApi().findReCode(new YLRxSubscriberHelper<FindReCodeResult>() {
            @Override
            public void _onNext(FindReCodeResult findReCodeResult) {
                if (null != findReCodeResult.getData()) getMvpView().findReCodeSuc(findReCodeResult.getData());
            }

            @Override
            public void _onError(ApiException error) {
                super._onError(error);
            }
        });
    }
}
