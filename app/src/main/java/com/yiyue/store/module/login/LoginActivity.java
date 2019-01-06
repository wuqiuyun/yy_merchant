package com.yiyue.store.module.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityLoginBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.UserBean;
import com.yiyue.store.model.vo.bean.WeiXin;
import com.yiyue.store.module.certification.CertificationActivity;
import com.yiyue.store.module.certification.information.BasicInformationActivity;
import com.yiyue.store.module.common.h5.WebActivity;
import com.yiyue.store.module.login.bindphone.BindPhoneActivity;
import com.yiyue.store.module.login.forgetpwd.ForgetPasswordActivity;
import com.yiyue.store.module.login.invitecode.InviteCodeActivity;
import com.yiyue.store.module.main.MainActivity;
import com.yl.core.component.log.DLog;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.KeyboardUtil;
import com.yl.core.util.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * 登陆
 * Create by zm on 2019/10/29
 */
@CreatePresenter(LoginPresenter.class)
public class LoginActivity extends BaseMvpAppCompatActivity<ILoginView, LoginPresenter> implements ClickHandler, ILoginView {

    ActivityLoginBinding mBinding;
    //拉起微信登录界面
    /**
     * 微信登录方法
     */
    private IWXAPI wxapi; // IWXAPI 是第三方app和微信通信的openapi接口
    private String wechatUrl;//微信返回拼接地址
    private String wechatOpenid;//微信的OPENID
    public static LoginActivity mLoginActivity = null;
    private boolean isLoginWx = false;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_login;
    }

    @Override
    protected void init() {
        StatusBarUtil.setLightMode(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mBinding = (ActivityLoginBinding) viewDataBinding;
        mBinding.setClick(this);

        mLoginActivity = this;

        wxapi = WXAPIFactory.createWXAPI(this, Constants.WEIXIN_APPID, true);
        wxapi.registerApp(Constants.WEIXIN_APPID);

        // 修改状态栏字体颜色
        StatusBarUtil.setLightMode(this);
        // 检测本地用户信息
        mBinding.etPhone.setText(AccountManager.getInstance().getMobile());
        mBinding.etAccount.setText(AccountManager.getInstance().getMobile());
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.tv_get_code:  //获取验证码
                getMvpPresenter().getPhoneCode(mBinding.etPhone.getText().toString().trim());
                break;

            case R.id.tv_forget_password:  //忘记密码
                Intent intent = new Intent(this , ForgetPasswordActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("type" , 1);
                intent.putExtra("bundle" , bundle);
                startActivity(intent);
                break;

            case R.id.btn_login: //登录
                KeyboardUtil.closeSoftKeyboard(this);

                if (mBinding.llLoginCode.getVisibility() == View.VISIBLE){
                    getMvpPresenter().mobileLogin(
                            this,
                            mBinding.etPhone.getText().toString().trim(),
                            mBinding.etCode.getText().toString().trim());

                } else {
                    getMvpPresenter().passwordLogin(
                            this,
                            mBinding.etAccount.getText().toString().trim(),
                            mBinding.etPassword.getText().toString().trim());
                }
                break;

            case R.id.ll_weix:  //微信登录
            case R.id.iv_weix:  //微信登录
                wxlogin();
                break;
            case R.id.tv_switch: // 登陆切换
                if (mBinding.llLoginCode.getVisibility() == View.VISIBLE) { // 密码登陆
                    mBinding.llLoginCode.setVisibility(View.GONE);
                    mBinding.llAccountPassword.setVisibility(View.VISIBLE);
                    mBinding.tvSwitch.setText(getString(R.string.use_Verification_login));

                    //清空手机登录的输入内容
                    mBinding.etPhone.getText().clear();
                    mBinding.etCode.getText().clear();
                }else { // 验证码登陆
                    mBinding.llLoginCode.setVisibility(View.VISIBLE);
                    mBinding.llAccountPassword.setVisibility(View.GONE);
                    mBinding.tvSwitch.setText(getString(R.string.use_password_login));

                    //清空密码登录的输入内容
                    mBinding.etAccount.getText().clear();
                    mBinding.etPassword.getText().clear();
                }
                break;
            case R.id.iv_exit: // 退出
                finish();
                break;
            case R.id.tv_agreement: // 协议
                String url = Constants.WEB_SERVICE_DEAL;
                WebActivity.startActivity(LoginActivity.this,url,"协议",true);
                break;
        }

    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageView(this, 0, null);
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }

    @Override
    public void onLoginSuccess(UserBean userBean) {
        switch (userBean.getUserStatus()) {
            case 0: // 跳转至首页
                MainActivity.startActivity(this, MainActivity.class);
                break;
            case 1: // 跳转至邀请码
                InviteCodeActivity.startActivity(this, InviteCodeActivity.class);
                break;
            case 2: // 跳转至基本信息
                BasicInformationActivity.startActivity(this, BasicInformationActivity.class);
                break;
            case 3: // 跳转至认证
                CertificationActivity.startActivity(this, CertificationActivity.class);
                break;
        }
        finish();
    }


    @Override
    public void onWxLoginSuccess(UserBean userBean) {
        wechatOpenid = userBean.getOpenId();
        switch (userBean.getUserStatus()) {
            case 0: // 跳转至首页
                MainActivity.startActivity(this, MainActivity.class);
                finish();
                break;
            case 1: // 跳转至邀请码
                InviteCodeActivity.startActivity(this, InviteCodeActivity.class);
                finish();
                break;
            case 2: // 跳转至基本信息
                BasicInformationActivity.startActivity(this, BasicInformationActivity.class);
                finish();
                break;
            case 3: // 跳转至认证
                CertificationActivity.startActivity(this, CertificationActivity.class);
                finish();
                break;
            case 4: // 绑定手机号码
                //提现
                Bundle bundle = new Bundle();
                bundle.putString(Constants.WECHAT_OPENID,wechatOpenid);
                BindPhoneActivity.startActivity(this, BindPhoneActivity.class, bundle);
                break;
        }

    }

    @Override
    public void updateCountdownTime(long time) {
        mBinding.tvGetCode.setEnabled(false); // 禁止再次点击
        mBinding.tvGetCode.setText(String.format(getString(R.string.login_get_code_time), time + ""));
    }

    @Override
    public void onCountdownFinish() {
        mBinding.tvGetCode.setText(getString(R.string.login_get_again));
        mBinding.tvGetCode.setEnabled(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * EventBus接受指令类
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(WeiXin weiXin) {
        DLog.e("收到eventbus请求 type:" + weiXin.getType());
        if (weiXin.getType() == 1&&isLoginWx) {//微信登录
//            getAccessToken(weiXin.getCode());
            if (!TextUtils.isEmpty(weiXin.getCode())&&weiXin.getCode()!=null) {
                getMvpPresenter().wxlogin(weiXin.getCode(), this);
            }
        }
    }

    /**
     * 微信登陆(三个步骤)
     * 1.微信授权登陆
     * 2.根据授权登陆code 获取该用户token
     * 3.根据token获取用户资料
     */
    public void wxlogin() {
        if (wxapi != null && wxapi.isWXAppInstalled()) {
            isLoginWx = true;
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = String.valueOf(System.currentTimeMillis());
            wxapi.sendReq(req);
        } else {
            ToastUtils.shortToast("您尚未安装微信");
        }
    }

}
