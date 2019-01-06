package com.yiyue.store.module.login.forgetpwd;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;

import com.yiyue.store.api.StoreSmsApi;
import com.yiyue.store.api.StoreUserApi;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;

/**
 * Created by lvlong on 2018/9/27.
 */
public class ForgetPasswordPresenter extends BasePresenter<IForgetPasswordView> {
    private static final long TOTAL_TIME = 60_000L;
    private static final long ONECE_TIME = 1000L;

    private CountDownTimer mCountDownTimer;

    /**
     * 更新密码
     * @param mobile
     * @param phoneCode
     * @param newPassword
     * @param confirmPwd
     */
    public void updatePwd(String mobile, String phoneCode, String newPassword, String confirmPwd, Context context) {
        if (TextUtils.isEmpty(mobile)) {
            getMvpView().showToast("手机号码不能为空");
            return;
        }
        if (mobile.length() != 11) {
            getMvpView().showToast("请输入正确的手机号码");
            return;
        }
        if (TextUtils.isEmpty(phoneCode)) {
            getMvpView().showToast("验证码不能为空");
            return;
        }
        if (TextUtils.isEmpty(newPassword)) {
            getMvpView().showToast("密码不能为空");
            return;
        }
        if (newPassword.length() < 6) {
            getMvpView().showToast("密码不能小于6位数");
            return;
        }
        if (TextUtils.isEmpty(confirmPwd)) {
            getMvpView().showToast("确认密码不能为空");
            return;
        }
        if (!newPassword.equals(confirmPwd)) {
            getMvpView().showToast("两次密码输入不一致");
            return;
        }
        new StoreUserApi().updatePwd(mobile, phoneCode, newPassword, new YLRxSubscriberHelper<BaseResponse>(context,true) {
            @Override
            public void _onNext(BaseResponse baseResponse) {
                getMvpView().onUpdatePwdSuccess();
            }
        });
    }

    /**
     * 获取验证码
     * @param mobile
     */
    public void getPhoneCode(String mobile, Context context) {
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
        new StoreSmsApi().getPhoneCode(mobile,"3", new YLRxSubscriberHelper<BaseResponse>(context,true) {
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
