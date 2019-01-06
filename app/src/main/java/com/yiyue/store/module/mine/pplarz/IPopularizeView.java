package com.yiyue.store.module.mine.pplarz;

import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.bean.FindInviteBean;
import com.yiyue.store.model.vo.bean.ReCodeBean;
import com.yiyue.store.model.vo.result.RecommendResult;

/**
 * Created by zm on 2018/12/28.
 */
public interface IPopularizeView extends IBaseView {
    void setIncomeData(RecommendResult.Data data);
    void findInviteSuc(FindInviteBean findInvite);
    void findReCodeSuc(ReCodeBean reCode);
}
