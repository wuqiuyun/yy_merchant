package com.yiyue.store.module.home.evaluation;

import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.bean.StoreCommentListBean;
import com.yiyue.store.model.vo.bean.StoreManageScopeBean;
import com.yiyue.store.model.vo.bean.StylistEvaluationBean;

import java.util.ArrayList;

/**
 * Created by lvlong on 2018/10/11.
 */
public interface EvaluationManagerView extends IBaseView {
    /**
     * 获取门店总评分成功
     */
    void getStoreScoreSucceed(StoreManageScopeBean storeManageScopeBean);

    /**
     * 获取门店顾客评价列表成功
     */
    void getStoreCommentListSucceed(ArrayList<StoreCommentListBean> storeManageScopeBean);

    /**
     * 获取门店顾客评价列表成功
     */
    void getStoreCommentListFail();

    /**
     * 回复顾客评价成功
     */
    void replyStoreCommentSucceed();

    void onGetEvaluate(StylistEvaluationBean bean);

}
