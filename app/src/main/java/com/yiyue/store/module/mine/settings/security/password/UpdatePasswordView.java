package com.yiyue.store.module.mine.settings.security.password;

import com.yiyue.store.base.mvp.IBaseView;

/**
 * Created by lvlong on 2018/10/12.
 */
public interface UpdatePasswordView extends IBaseView {
    void upDatePasswordSuccess();
    void upDatePasswordFail(String s);
}
