package com.yiyue.store.module.home.service;

import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.bean.CategoryBean;
import com.yiyue.store.model.vo.bean.StoreSettingBean;

import java.util.List;

/**
 * Created by lvlong on 2018/10/9.
 */
public interface ServiceSettingView extends IBaseView {
    void onCommitSuccess(List<CategoryBean> beans);
    void onSuccess(StoreSettingBean storeSettingBean);
    void onCommitServiceData();
}
