package com.yiyue.store.module.mine.info.service;

import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.bean.CategoryBean;

import java.util.List;

/**
 * Created by lvlong on 2018/10/13.
 */
public interface ServicesScopeView extends IBaseView {
    /**
     * 获取平台所有服务类目
     * @param categoryBeans
     */
    void onGetCategoryAllSuccess(List<CategoryBean> categoryBeans);

    /**
     * 提交选中的服务类目
     */
    void onUpdateServicesSuccess();
}
