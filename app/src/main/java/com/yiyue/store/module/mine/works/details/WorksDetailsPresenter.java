package com.yiyue.store.module.mine.works.details;

import android.text.TextUtils;

import com.yiyue.store.api.RecomUserApi;
import com.yiyue.store.api.StoreStylistApi;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.vo.result.FindReCodeResult;
import com.yiyue.store.model.vo.result.OpusDetailResult;
import com.yl.core.component.net.exception.ApiException;

/**
 * Created by zm on 2018/10/12.
 */
public class WorksDetailsPresenter extends BasePresenter<IWorksDetailsView> {

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
    
    //获取作品详情
    public void getOpusDetail(String opusId) {
        String userId = AccountManager.getInstance().getUserId();
        if (TextUtils.isEmpty(userId)){
            getMvpView().showToast("用户ID为空");
            return;
        }
        if (TextUtils.isEmpty(opusId)){
            getMvpView().showToast("作品ID为空");
            return;
        }
        
        new StoreStylistApi().opusDetail(opusId, userId, new YLRxSubscriberHelper<OpusDetailResult>() {
            @Override
            public void _onNext(OpusDetailResult opusDetailResult) {
                if (null != opusDetailResult.getData()) {
                    getMvpView().getDetailSuccess(opusDetailResult.getData());
                } else {
                    getMvpView().getDetailFail();
                }
            }

            @Override
            public void _onError(ApiException error) {
                super._onError(error);
                getMvpView().getDetailFail();
            }
        });
    }
    
    //收藏/取消收藏
    public void oupsCollection(String opusId, int type, String userId) {
        new StoreStylistApi().oupsCollection(String.valueOf(opusId), String.valueOf(type), userId, new YLRxSubscriberHelper<BaseResponse>() {
            @Override
            public void _onNext(BaseResponse baseResponse) {
                getMvpView().collectSuccess();
            }

            @Override
            public void _onError(ApiException error) {
                super._onError(error);
                getMvpView().collectFail();
            }
        });
    }
}
