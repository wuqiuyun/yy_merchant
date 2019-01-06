package com.yiyue.store.module.home.time;

import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.bean.StoreSettingTimeBean;

/**
 * Created by lvlong on 2018/10/11.
 */
public interface TimeManageView extends IBaseView {
    /**
     * 获取门店时间管理成功
     */
    void getStoreTimeSuccess(StoreSettingTimeBean storeSettingTimeBean);

    /**
     * 更新门店时间管理成功
     */
    void setStoreTimeSuccess(StoreSettingTimeBean storeSettingTimeBean);
}
