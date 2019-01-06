package com.yiyue.store.module.main;

import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.bean.AppInfoBean;

/**
 * Created by zm on 2018/9/10.
 */
public interface IMainView extends IBaseView{

    /**
     * 更新版本信息回调
     * @param infoBean
     */
    void onUpdateAppInfo(AppInfoBean infoBean);
}
