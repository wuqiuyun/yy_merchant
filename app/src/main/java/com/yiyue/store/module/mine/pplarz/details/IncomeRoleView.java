package com.yiyue.store.module.mine.pplarz.details;


import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.result.RecommendUserListResult;

/**
 * Created by zm on 2019/1/4.
 */
public interface IncomeRoleView extends IBaseView {
     void setData(RecommendUserListResult.Data data);
     void onLoadFail();
}
