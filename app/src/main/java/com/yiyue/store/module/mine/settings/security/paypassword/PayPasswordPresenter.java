package com.yiyue.store.module.mine.settings.security.paypassword;

import android.content.Context;
import android.text.TextUtils;

import com.yl.core.component.net.exception.ApiException;
import com.yiyue.store.api.SettingsApi;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;

/**
 * Created by lyj on 2018/11/9.
 */
public class PayPasswordPresenter extends BasePresenter<PayPasswordView> {
    //变更账户密码（知道原密码）
    public void upDatePwd(String userId, String oldPassword ,String password ,String nextPassword , Context context) {
        if (TextUtils.isEmpty(oldPassword)) {
            getMvpView().showToast("旧密码不能为空");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            getMvpView().showToast("新密码不能为空");
            return;
        }
        if (TextUtils.isEmpty(nextPassword)) {
            getMvpView().showToast("重复密码项不能为空");
            return;
        }

        if (!password.equals(nextPassword)) {
            getMvpView().showToast("两次填写的密码不一致");
            return;
        }

        new SettingsApi().upDatePwd(userId, oldPassword,password,nextPassword, new YLRxSubscriberHelper<BaseResponse>(context,true) {
            @Override
            public void _onNext(BaseResponse result) {

            }

            @Override
            public void _onError(ApiException error) {
                getMvpView().upDatePasswordFail(error.code+"");
            }
        });
    }

    //设置支付密码
    public void setPaypassword(String pwd,String rePwd ,Context context) {
        if (TextUtils.isEmpty(pwd)) {
            getMvpView().showToast("新密码不能为空");
            return;
        }
        if (TextUtils.isEmpty(rePwd)) {
            getMvpView().showToast("重复密码项不能为空");
            return;
        }

        if (!pwd.equals(rePwd)) {
            getMvpView().showToast("两次填写的密码不一致");
            return;
        }

        new SettingsApi().setPaypassword(pwd, rePwd,new YLRxSubscriberHelper<BaseResponse>(context,true) {
            @Override
            public void _onNext(BaseResponse baseResponse) {
                getMvpView().upDatePasswordSuccess();
            }

            @Override
            public void _onError(ApiException error) {
                super._onError(error);
                getMvpView().upDatePasswordFail("");
            }
        });
    }

    //验证支付密码
    public void checkPayWord(String pwd,Context context) {
        if (TextUtils.isEmpty(pwd)) {
            getMvpView().showToast("旧密码不能为空");
            return;
        }
        new SettingsApi().checkPayWord(pwd,new YLRxSubscriberHelper<BaseResponse>(context,true) {
            @Override
            public void _onNext(BaseResponse baseResponse) {
                getMvpView().checkPasswordSuccess();
            }

            @Override
            public void _onError(ApiException error) {
                super._onError(error);
//                getMvpView().showToast("旧支付密码验证失败");
            }
        });
    }

}
