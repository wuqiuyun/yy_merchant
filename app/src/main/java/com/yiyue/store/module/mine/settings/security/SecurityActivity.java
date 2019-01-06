package com.yiyue.store.module.mine.settings.security;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivitySecurityBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.local.preferences.SharePreferencesUtils;
import com.yiyue.store.model.vo.bean.SecurityInfoBean;
import com.yiyue.store.model.vo.bean.WeiXin;
import com.yiyue.store.module.mine.settings.security.cashaccount.CashAccountManageActivity;
import com.yiyue.store.module.mine.settings.security.password.UpdatePasswordActivity;
import com.yiyue.store.module.mine.settings.security.paypassword.PayPasswordActivity;
import com.yiyue.store.module.mine.settings.security.phone.PhoneVerifyActivity;
import com.yl.core.component.log.DLog;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by lyj on 2018/10/31.
 *
 * 账号安全管理
 */
@CreatePresenter(SecurityPresenter.class)
public class SecurityActivity extends BaseMvpAppCompatActivity<ISecurityView, SecurityPresenter> implements ClickHandler , ISecurityView {

    ActivitySecurityBinding mBinding;
    private int bindWX = 0;//绑定微信，1绑定，0未绑定 ,
    private String mMobile;//当前手机号
    private boolean isBindWx = false;
    private int isAuth = 0;//1已实名，0未实名
    //拉起微信登录界面
    /**
     * 微信登录方法
     */
    private IWXAPI wxapi; // IWXAPI 是第三方app和微信通信的openapi接口
    private String wechatUrl;//微信返回拼接地址
    private String wechatOpenid;//微信的OPENID


    private String accountNo, ccountType, realName;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_security;
    }

    @Override
    protected void init() {
        StatusBarUtil.setLightMode(this);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        wxapi = WXAPIFactory.createWXAPI(this, Constants.WEIXIN_APPID, true);
        wxapi.registerApp(Constants.WEIXIN_APPID);

        mBinding = (ActivitySecurityBinding) viewDataBinding;
        mBinding.setClick(this);

        setTitleView();
        String mobile = AccountManager.getInstance().getMobile();
        mBinding.tvVersionCode.setText(mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
        getMvpPresenter().getAccountInfoAccount();//获取账户信息
    }

    private void setTitleView() {
        mBinding.titleView.setLeftClickListener(view -> finish());
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.rl_modify_phone :         //修改手机号
                startActivity(this , PhoneVerifyActivity.class);
                break;

            case R.id.rl_modify_password :      //修改密码
                startActivity(this,UpdatePasswordActivity.class);
                break;

            case R.id.rl_binding_wx :           //绑定微信号
                if (bindWX==0){
                    wxlogin();
                }else {
                    ToastUtils.shortToast("已绑定微信");
                }
                break;

            case R.id.rl_binding_bank :          //提现账户管理
                if (isAuth==1){
                    startActivity(this,CashAccountManageActivity.class);
                }else {
                    ToastUtils.shortToast("实名认证未通过");
                }
                break;

            case R.id.rl_binding_zfb :          //支付密码
                getMvpPresenter().initPayWord(this);
                break;
        }
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
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
        if (weiXin.getType() == 1&&isBindWx) {//微信登录
            if (!TextUtils.isEmpty(weiXin.getCode())&&weiXin.getCode()!=null){
                getMvpPresenter().bindWXAccount(weiXin.getCode(), this);
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
            isBindWx = true;
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = String.valueOf(System.currentTimeMillis());
            wxapi.sendReq(req);
        } else {
            ToastUtils.shortToast("您尚未安装微信");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        String mobile = AccountManager.getInstance().getMobile();
        mBinding.tvVersionCode.setText(mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
        getMvpPresenter().getAccountInfoAccount();//获取账户信息

    }

    @Override
    public void onAccountInfoSuccess(SecurityInfoBean bean) {
        if (bean!=null){
            bindWX = bean.getBindWX();
            isAuth = bean.getIsAuth();
            if(isAuth==1){
                realName = bean.getRealName();
                SharePreferencesUtils.getSharePreferencesUtils().setRealName(realName);
            }
            if (bindWX==0){
                mBinding.tvBindingWx.setText("未绑定");
            }else {
                mBinding.tvBindingWx.setText("已绑定");
            }
        }
    }

    @Override//支付密码状态获取成功
    public void oninitPayWordInfoSuccess(String json) {
        //data=0(未设置)，data=1(已设置)。若未设置支付密码，弹窗跳转到设置支付密码页面
        Log.e("---------","-----"+json);
        Bundle bundle = new Bundle();
        if (json.equals("0")||json.equals("0.0")){
            bundle.putBoolean("isrepin", false);
        }else {
            bundle.putBoolean("isrepin", true);
        }
        startActivity(this, PayPasswordActivity.class, bundle);
    }

    @Override
    public void oninitPayWordInfoFail() {

    }

    @Override
    public void onBindWxSuccess() {
        showToast("绑定微信号成功");
        getMvpPresenter().getAccountInfoAccount();//获取账户信息
    }
}
