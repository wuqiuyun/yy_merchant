package com.yiyue.store.module.mine.settings.security.paypassword.forgetpwd;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;

import com.yiyue.store.api.StoreSmsApi;
import com.yl.core.component.net.exception.ApiException;
import com.yiyue.store.api.SettingsApi;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.util.AccountValidatorUtil;

/**
 * Created by lyj on 2018/10/9.
 */
public class ForgetPayPasswordPresenter extends BasePresenter<IForgetPayPasswordView>{
    private static final long TOTAL_TIME = 60_000L;
    private static final long ONECE_TIME = 1000L;

    private CountDownTimer mCountDownTimer;

    /**
     * 检验手机号码
     * @param mobile
     * @param phoneCode
     * @param
     */
    public void checkCode(String mobile, String phoneCode, Context context) {
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
        new StoreSmsApi().checkCode(mobile,"5", phoneCode, new YLRxSubscriberHelper<BaseResponse>(context,true) {
            @Override
            public void _onNext(BaseResponse baseResponse) {
                getMvpView().onCodePhoneSuccess();
            }

            @Override
            public void _onError(ApiException error) {
                super._onError(error);
                getMvpView().showToast(error.message);
            }
        });
    }


    /**
     * 检验手机号码
     * @param mobile
     * @param phoneCode
     * @param
     */
    public void checkSelfCode(String mobile, String phoneCode, Context context) {
        if (TextUtils.isEmpty(phoneCode)) {
            getMvpView().showToast("验证码不能为空");
            return;
        }
        new StoreSmsApi().checkCode(mobile,"3", phoneCode, new YLRxSubscriberHelper<BaseResponse>(context,true) {
            @Override
            public void _onNext(BaseResponse baseResponse) {
                getMvpView().onCodePhoneSuccess();
            }

            @Override
            public void _onError(ApiException error) {
                super._onError(error);
                getMvpView().showToast(error.message);
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
        if (mobile.length() != 11) {
            getMvpView().showToast("请输入正确的手机号码");
            return;
        }
        // 限制验证码获取
        startCountdownTime();
        new StoreSmsApi().getPhoneCode(mobile, "2", new YLRxSubscriberHelper<BaseResponse>(context,true) {
            @Override
            public void _onNext(BaseResponse baseResponse) {
                getMvpView().showToast("验证码获取成功");
            }
        });
    }

    /**
     * 获取验证码
     * @param mobile
     */
    public void getSelfPhoneCode(String mobile, Context context) {
        // 限制验证码获取
        startCountdownTime();
        new StoreSmsApi().getSelfPhoneCode(mobile, new YLRxSubscriberHelper<BaseResponse>(context,true) {
            @Override
            public void _onNext(BaseResponse baseResponse) {
                getMvpView().showToast("验证码获取成功");
            }
        });
    }
    /**
     * 检验身份证
     * @param idnumber
     * @param
     */
    public void checkIdNumber(String idnumber, Context context) {
        if (TextUtils.isEmpty(idnumber)) {
            getMvpView().showToast("身份证号码不能为空");
            return;
        }
        new SettingsApi().checkIDcard(idnumber, new YLRxSubscriberHelper<BaseResponse>(context,true) {
            @Override
            public void _onNext(BaseResponse baseResponse) {
                getMvpView().onInitIdNumberSuccess();
            }

            @Override
            public void _onError(ApiException error) {
                super._onError(error);
                getMvpView().showToast(error.message);
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
