package com.yiyue.store.module.login.invitecode;

import com.yiyue.store.base.mvp.IBaseView;

/**
 * Created by lvlong on 2018/9/27.
 */
public interface InviteCodeView extends IBaseView {
    /**
     * 验证码提交成功
     */
    void onInviteCodeSuccess();
}
