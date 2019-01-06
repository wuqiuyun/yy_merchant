package com.yiyue.store.module.mine.pplarz.details;

import com.yiyue.store.api.RecomUserApi;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.model.vo.result.RecommendUserListResult;
import com.yl.core.component.net.exception.ApiException;

/**
 * Created by zm on 2019/1/4.
 */
public class IncomeRolePresenter extends BasePresenter<IncomeRoleView> {
    private RecomUserApi recomUserApi;

    private RecomUserApi getRecomUserApi() {
       return recomUserApi == null ? new RecomUserApi() : recomUserApi;
    }

    /**
     * 推荐用户列表
     * @param roleType
     */
    public void recommendUserList(int roleType) {
        getRecomUserApi().recommendUserList(roleType, new YLRxSubscriberHelper<RecommendUserListResult>() {
            @Override
            public void _onNext(RecommendUserListResult recommendUserListResult) {
                getMvpView().setData(recommendUserListResult.getData());
            }

            @Override
            public void _onError(ApiException error) {
                super._onError(error);
                getMvpView().onLoadFail();
            }
        });
    }
}
