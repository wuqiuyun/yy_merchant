package com.yiyue.store.module.mine.settings;

import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.bean.ReCodeBean;

/**
 * Created by zm on 2018/9/29.
 */
public interface ISettingsView extends IBaseView{
    void changeNoticeSuc();
    
    void findReCodeSuc(ReCodeBean recode);
}
