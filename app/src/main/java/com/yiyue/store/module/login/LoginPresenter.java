package com.yiyue.store.module.login;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;

import com.yiyue.store.api.StoreSmsApi;
import com.yiyue.store.api.StoreUserApi;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.TokenManager;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.vo.bean.UserBean;
import com.yiyue.store.model.vo.result.LoginResult;

/**
 * Created by Lizhuo on 2018/9/17.
 */
public class LoginPresenter extends BasePresenter<ILoginView> {
    private static final long TOTAL_TIME = 60_000L;
    private static final long ONECE_TIME = 1000L;

    private CountDownTimer mCountDownTimer;

    /**
     * 手机号登陆
     * @param mobile
     * @param code
     */
    public void mobileLogin(Context context, String mobile, String code) {
        if (TextUtils.isEmpty(mobile)) {
            getMvpView().showToast("手机号码不能为空");
            return;
        }
        if (mobile.length()!=11) {
            getMvpView().showToast("请输入正确的手机号码");
            return;
        }
        if (TextUtils.isEmpty(code)) {
            getMvpView().showToast("验证码不能为空");
            return;
        }
        login(context, mobile, code, "");
    }

    /**
     * 密码登陆
     * @param mobile
     * @param password
     */
    public void passwordLogin(Context context, String mobile, String password) {
        if (TextUtils.isEmpty(mobile)) {
            getMvpView().showToast("手机号码不能为空");
            return;
        }
        if (mobile.length()!=11) {
            getMvpView().showToast("请输入正确的手机号码");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            getMvpView().showToast("密码不能为空");
            return;
        }
        login(context, mobile, "", password);
    }


    /**
     * 登陆
     * @param mobile
     * @param phoneCode
     * @param password
     */
    private void login(Context context, String mobile, String phoneCode, String password) {
        new StoreUserApi().login(mobile, phoneCode, password, new YLRxSubscriberHelper<LoginResult>(context, true) {
            @Override
            public void _onNext(LoginResult loginResult) {
                UserBean userBean = loginResult.getData();
                // save userInfo
                AccountManager.getInstance().updateAccount(userBean);
                // save token
                TokenManager.updateToken(userBean.getToken());
                // callback
                getMvpView().onLoginSuccess(userBean);
            }
        });
    }


    /**
     * 微信登陆
     * @param code
     */
    public void wxlogin(String code, Context context) {
        new StoreUserApi().wxlogin(code, new YLRxSubscriberHelper<LoginResult>(context,true) {
            @Override
            public void _onNext(LoginResult loginResult) {
                // revert logoutFlag
                AccountManager.getInstance().setLogoutFlag(false);

                UserBean userBean = loginResult.getData();
                // save userInfo
                AccountManager.getInstance().updateAccount(userBean);
                // save token
                TokenManager.updateToken(userBean.getToken());
                // callback
                getMvpView().onWxLoginSuccess(userBean);
            }
        });
    }

    /**
     * 获取验证码
     * @param mobile
     */
    public void getPhoneCode(String mobile) {
        if (TextUtils.isEmpty(mobile)){
            getMvpView().showToast("手机号码不能为空");
            return;
        }
        if (mobile.length()!=11) {
            getMvpView().showToast("请输入正确的手机号码");
            return;
        }
        // 限制验证码获取
        startCountdownTime();
        new StoreSmsApi().getPhoneCode(mobile, "2", new YLRxSubscriberHelper<BaseResponse>() {
            @Override
            public void _onNext(BaseResponse baseResponse) {
                getMvpView().showToast("验证码获取成功");
            }
        });
    }

    /**
     * 开启倒计时
     */
    private void startCountdownTime() {
        if (null == mCountDownTimer) {
            mCountDownTimer = new CountDownTimer(TOTAL_TIME, ONECE_TIME) {
                @Override
                public void onTick(long millisUntilFinished) {
                    getMvpView().updateCountdownTime(millisUntilFinished / 1000);
                }

                @Override
                public void onFinish() {
                    getMvpView().onCountdownFinish();
                }
            };
        }
        mCountDownTimer.start();
    }

    /**
     * 关闭倒计时
     */
    public void stopCountdownTime() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
            mCountDownTimer = null;
        }
    }

    @Override
    public void onDetachMvpView() {
        super.onDetachMvpView();
        stopCountdownTime();
    }
}
