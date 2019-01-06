package com.yiyue.store.module.mine.settings.about;

import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.bean.AppInfoBean;

/**
 * Created by lvlong on 2018/10/8.
 */
public interface IAboutView extends IBaseView {
    /**
     * 获取更新信息成功
     * */
    void onAppInfoSuccess(AppInfoBean appInfoBean);

}
