package com.yiyue.store.module.home.evaluation;

import android.content.Context;

import com.yiyue.store.api.StoreCommentApi;
import com.yiyue.store.api.StoreManageApi;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.model.vo.bean.StoreCommentListBean;
import com.yiyue.store.model.vo.bean.StoreManageScopeBean;
import com.yiyue.store.model.vo.result.StoreCommentListResult;
import com.yiyue.store.model.vo.result.StoreManageScopeResult;
import com.yiyue.store.model.vo.result.StylistEvaluationResult;
import com.yl.core.component.net.exception.ApiException;

import java.util.ArrayList;

/**
 * Created by lvlong on 2018/10/11.
 */
public class EvaluationManagerPresenter extends BasePresenter<EvaluationManagerView> {
    /**
     *  回复客户评论
     *
     * context (string, optional): 内容
     * storeId (integer, optional): 门店id（非本人门店必传） ,
     * userId (integer, optional): 用户id
     *
     */
    public void replyStoreComment(String msg, String storeReviewsId, Context context) {
        new StoreCommentApi().replyStoreComment(msg,storeReviewsId,new YLRxSubscriberHelper<BaseResponse>(context,true) {
            @Override
            public void _onNext(BaseResponse baseResponse) {
                getMvpView().replyStoreCommentSucceed();
            }
        });
    }


    /**
     * 门店顾客评价列表
     *
     * page 页数
     * id (integer, optional): 用户id
     * size (integer, optional): 每一页的大小
     *
     * */
    public void getStoreCommentList(String storeId,int page,int size,Context context) {
        new StoreCommentApi().getStoreCommentList(storeId,page, size,new YLRxSubscriberHelper<StoreCommentListResult>(context,true) {
            @Override
            public void _onNext(StoreCommentListResult baseResponse) {
                ArrayList<StoreCommentListBean> storeCommentListBean = baseResponse.getData();
                // callback
                getMvpView().getStoreCommentListSucceed(storeCommentListBean);
            }

            @Override
            public void _onError(ApiException error) {
                super._onError(error);
                getMvpView().getStoreCommentListFail();
            }
        });
    }

    /**
     * 门店顾客评价
     *
     * storeId (integer, optional): 门店ID，非本人门店必传 ,
     * userId (integer, optional): 用户id
     * */
    public void getStoreScore(String storeId,Context context) {

        new StoreManageApi().getStoreScore(storeId,new YLRxSubscriberHelper<StoreManageScopeResult>(context,true) {
            @Override
            public void _onNext(StoreManageScopeResult baseResponse) {
                StoreManageScopeBean storeManageScopeBean = baseResponse.getData();
                // callback
                getMvpView().getStoreScoreSucceed(storeManageScopeBean);
            }
        });
    }

    //查看美发师评分
    public void getEvaluate(String stylistId,Context context){
        new StoreCommentApi().getEvaluate(stylistId, new YLRxSubscriberHelper<StylistEvaluationResult>(context,true) {
            @Override
            public void _onNext(StylistEvaluationResult result) {
                getMvpView().onGetEvaluate(result.getData());
            }
        });
    }

    public void getStylistCommentList(String stylistId , int page , int size,Context context){
        new StoreCommentApi().getStylistCommentList(stylistId, page, size, new YLRxSubscriberHelper<StoreCommentListResult>(context,true) {
            @Override
            public void _onNext(StoreCommentListResult storeCommentListResult) {
                getMvpView().getStoreCommentListSucceed(storeCommentListResult.getData());
            }

            @Override
            public void _onError(ApiException error) {
                super._onError(error);
                getMvpView().getStoreCommentListFail();
            }
        });
    }

    @Override
    public void onDetachMvpView() {
        super.onDetachMvpView();
    }

}
