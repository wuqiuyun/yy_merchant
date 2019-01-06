package com.yiyue.store.module.mine.pplarz;

import android.content.Context;

import com.yiyue.store.api.RecomUserApi;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.model.vo.result.FindInviteResult;
import com.yiyue.store.model.vo.result.FindReCodeResult;
import com.yiyue.store.model.vo.result.RecommendResult;
import com.yl.core.component.net.exception.ApiException;

/**
 * Created by zm on 2018/12/28.
 */
public class PopularizePresenter extends BasePresenter<IPopularizeView> {

    public void recommend(Context context) {
        new RecomUserApi().recommend(new YLRxSubscriberHelper<RecommendResult>(context, true) {
            @Override
            public void _onNext(RecommendResult baseResponse) {
                getMvpView().setIncomeData(baseResponse.getData());
            }
        });
    }

    /**
     * 获取自己的推荐人(上级)
     */
    public void findInvite(){
        new RecomUserApi().findInvite(new YLRxSubscriberHelper<FindInviteResult>() {
            @Override
            public void _onNext(FindInviteResult findInviteResult) {
                getMvpView().findInviteSuc(findInviteResult.getData());
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
                else getMvpView().showToast("获取我的推荐码失败");
            }

            @Override
            public void _onError(ApiException error) {
                super._onError(error);
            }
        });
    }
}
