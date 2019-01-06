package com.yiyue.store.module.login;

import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.bean.UserBean;

/**
 * Created by Lizhuo on 2018/9/17.
 */
public interface ILoginView extends IBaseView {
    /**
     * 登陆成功
     */
    void onLoginSuccess(UserBean userBean);

    /**
     * 微信登陆成功
     */
    void onWxLoginSuccess(UserBean userBean);

    /**
     * 更新倒计时
     *
     * @param time
     */
    void updateCountdownTime(long time);

    /**
     * 倒计时结束
     */
    void onCountdownFinish();
}
