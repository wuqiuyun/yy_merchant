package com.yiyue.store.module.mine.works.many;

import android.text.TextUtils;

import com.yiyue.store.api.StoreStylistApi;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.model.vo.result.StylistOpusListResult;
import com.yl.core.component.net.exception.ApiException;

/**
 * Created by zm on 2018/10/13.
 */
public class ManyWorksPrenster extends BasePresenter<IManyWorksView> {

    public void opusList(String stylistId){

        new StoreStylistApi().opusList(stylistId, new YLRxSubscriberHelper<StylistOpusListResult>() {
            @Override
            public void _onNext(StylistOpusListResult stylistOpusListResult) {
                    getMvpView().onOpusList(stylistOpusListResult.getData());
            }
        });

    }

    public void getOpusListScreen(String stylistId, long screenId, int type) {
        if (TextUtils.isEmpty(stylistId)) {
            getMvpView().showToast("美发师Id为空");
            return;
        }

        new StoreStylistApi().getOpusListScreen(stylistId, String.valueOf(screenId), String.valueOf(type), new YLRxSubscriberHelper<StylistOpusListResult>() {
            @Override
            public void _onNext(StylistOpusListResult opusListResult) {
                if (null != opusListResult.getData()) getMvpView().getOpusListScreenSuc(opusListResult.getData());
            }

            @Override
            public void _onError(ApiException error) {
            }
        });
    }
}
