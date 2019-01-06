package com.yiyue.store.module.login.forgetpwd;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityForgetPasswordBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.util.FormatUtil;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

/**
 * 忘记密码
 * <p>
 * Created by lvlong on 2018/9/27.
 */
@CreatePresenter(ForgetPasswordPresenter.class)
public class ForgetPasswordActivity extends BaseMvpAppCompatActivity<IForgetPasswordView, ForgetPasswordPresenter>
        implements ClickHandler , IForgetPasswordView {

    ActivityForgetPasswordBinding mBinding;

    private boolean isChecked1 = false;
    private boolean isChecked2 = false;
    private int mType;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_forget_password;
    }

    @Override
    protected void init() {
        StatusBarUtil.setLightMode(this);
        mBinding = (ActivityForgetPasswordBinding) viewDataBinding;
        mBinding.setClick(this);

        setTitles();

        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("bundle");
        mType = bundle.getInt("type");
        if (mType == 2){
            mBinding.etPhone.setText(AccountManager.getInstance().getMobile());
            mBinding.etPhone.setEnabled(false);
        }

        StatusBarUtil.setLightMode(this);
    }

    private void setTitles() {

        //返回按钮
        mBinding.titleView.setLeftClickListener(view -> {
            finish();
        });

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.tv_get_code: //获取验证码

                getMvpPresenter().getPhoneCode(mBinding.etPhone.getText().toString().trim(),this);
                break;

            case R.id.ll_hidden_1: //第一次输入密码的显示按钮
                isChecked1 = !isChecked1;
                setVisiblePwd(mBinding.etPassword, isChecked1);
                break;

            case R.id.ll_hidden_2: //第二次输入密码的显示按钮
                isChecked2 = !isChecked2;
                setVisiblePwd(mBinding.etNextPassword, isChecked2);
                break;

            case R.id.btn_ensure: //确定按钮

                getMvpPresenter().updatePwd(mBinding.etPhone.getText().toString().trim(),
                        mBinding.etCode.getText().toString().trim(),
                        mBinding.etPassword.getText().toString().trim(),
                        mBinding.etNextPassword.getText().toString().trim(),this);
                break;

        }

    }

    /**
     * 设置密码是否可见
     * @param edtPassword
     * @param isChecked
     */
    private void setVisiblePwd(EditText edtPassword, boolean isChecked) {
        if (isChecked){
            // 输入为密码且可见
            edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }else {
            // 设置文本类密码（默认不可见），这两个属性必须同时设置
            edtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
        }
    }


    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(FormatUtil.Formatstring(message));
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
    public void onUpdatePwdSuccess() {
        ToastUtils.shortToast("设置密码成功");
        if (mType == 2){

            AccountManager.getInstance().insertingLogout();
        }else {
            finish();
        }
    }
}
