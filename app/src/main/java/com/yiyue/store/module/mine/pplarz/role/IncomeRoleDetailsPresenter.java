package com.yiyue.store.module.mine.pplarz.role;

import android.content.Context;

import com.yiyue.store.api.RecomUserApi;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.model.vo.result.IncomeRecordsResult;


/**
 * Created by zm on 2019/1/4.
 */
public class IncomeRoleDetailsPresenter extends BasePresenter<IncomeRoleDetailsView> {

    public void recommendUserIncome(Context context, String inviteUserId, int roleType, int page) {
        new RecomUserApi().recommendUserIncome(inviteUserId, roleType, page, new YLRxSubscriberHelper<IncomeRecordsResult>(context, true) {
            @Override
            public void _onNext(IncomeRecordsResult baseResponse) {
                getMvpView().setData(baseResponse.getData());
            }
        });
    }
}
