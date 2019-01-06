package com.yiyue.store.module.mine.settings.security.paypassword.forgetpwd;

import android.os.Bundle;
import android.view.View;

import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;
import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityPayPasswordForgetBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.module.mine.settings.security.paypassword.PayPasswordActivity;
import com.yiyue.store.util.ColorUtil;
import com.yiyue.store.util.StringUtil;

/**
 * 绑定手机号
 * <p>
 * Create by lyj on 2018/10/30
 */
@CreatePresenter(ForgetPayPasswordPresenter.class)
public class ForgetPayPasswordActivity extends BaseMvpAppCompatActivity<IForgetPayPasswordView, ForgetPayPasswordPresenter>
        implements IForgetPayPasswordView, ClickHandler{

    private ActivityPayPasswordForgetBinding mBinding;
    private String openid;
    private boolean isIdNumber = true;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_pay_password_forget;
    }

    @Override
    protected void init() {
        initView();
    }

    private void initView() {
        StatusBarUtil.setLightMode(this);
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            openid = bundle.getString(Constants.WECHAT_OPENID);
        }
        mBinding = (ActivityPayPasswordForgetBinding) viewDataBinding;
        mBinding.setClick(this);
        mBinding.titleView.setLeftClickListener(v -> finish());
        mBinding.titleView.setRightTextClickListener(v -> finish());
        mBinding.tvPhone.setText(StringUtil.getPhoneNumber(AccountManager.getInstance().getMobile()));
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ColorUtil.getColor(R.color.white));
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_get_code: // 获取验证码
                getMvpPresenter().getSelfPhoneCode(AccountManager.getInstance().getMobile(),this);
                break;
            case R.id.btn_forget_next: // 确定
                if (isIdNumber){//检验身份证
                    getMvpPresenter().checkIdNumber(mBinding.etIdnumber.getText().toString().trim(),this);
                }else {//检验短信
                    getMvpPresenter().checkSelfCode(AccountManager.getInstance().getMobile(),mBinding.etCode.getText().toString().trim(),this);
                }
                break;
        }
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onCodePhoneSuccess() {
        ToastUtils.shortToast("短信验证成功");
        Bundle bundle = new Bundle();
            bundle.putBoolean("isrepin", false);
        startActivity(this, PayPasswordActivity.class, bundle);
        finish();
    }

    @Override
    public void onInitIdNumberSuccess() {
        isIdNumber = false;
        ToastUtils.shortToast("身份证号验证成功");
        mBinding.llForgetCode.setVisibility(View.VISIBLE);
        mBinding.rlForgetIdnumber.setVisibility(View.GONE);
        mBinding.btnForgetNext.setText("确定");
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
}
