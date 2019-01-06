package com.yiyue.store.module.home;

import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.bean.BannerBean;
import com.yiyue.store.model.vo.bean.StoreAttachByTypeBean;
import com.yiyue.store.model.vo.bean.StoreAuthApplyBean;
import com.yiyue.store.model.vo.bean.StoreOrderStatisticalBean;
import com.yiyue.store.model.vo.result.BannerResult;

import java.util.List;

/**
 * Created by zm on 2018/9/19.
 */
public interface IHomeView extends IBaseView {

    /**
     * 获取认证信息成功回调
     */
    void onGetStoreAuthApplyInfoSuccess(StoreAuthApplyBean bean);

    void onGetStoreOrderStatisticalSuccess(StoreOrderStatisticalBean bean);

      /**
     * banner
     * @param beans
     */
    void getBannerSuccess(List<BannerBean> beans);
}
