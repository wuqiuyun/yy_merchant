package com.yiyue.store.module.mine.settings.security.phone;

import android.view.View;

import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityPhoneVerifyBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.module.mine.settings.security.phone.password.PasswordVerifyActivity;
import com.yiyue.store.module.mine.settings.security.phone.update.UpdatePhoneActivity;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

/**
 * 手机验证
 * Created by lvlong on 2018/10/12.
 */
@CreatePresenter(PhoneVerifyPresenter.class)
public class PhoneVerifyActivity extends BaseMvpAppCompatActivity<PhoneVerifyView, PhoneVerifyPresenter>
        implements ClickHandler , PhoneVerifyView {

    ActivityPhoneVerifyBinding mBinding;
    private String mMobile;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_phone_verify;
    }

    @Override
    protected void init() {

        mBinding = (ActivityPhoneVerifyBinding) viewDataBinding;
        mBinding.setClick(this);
        initView();

    }

    private void initView() {
        StatusBarUtil.setLightMode(this);
        mBinding.titleView.setLeftClickListener(view -> finish());
        mMobile = AccountManager.getInstance().getMobile();
        mBinding.etPhone.setText(mMobile);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.tv_get_code :         //获取验证码
                getMvpPresenter().getPhoneCode(mMobile);
                break;

            case R.id.tv_no_phone :         //号码无法使用

                startActivity(this , PasswordVerifyActivity.class);

                break;
            case R.id.btn_next :            //下一步
                getMvpPresenter().checkCode(mBinding.etPhone.getText().toString().trim(),mBinding.etCode.getText().toString().trim());
                break;

        }

    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);

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
    public void onSuccess() {
        startActivity(this , UpdatePhoneActivity.class);
        finish();
    }

    @Override
    public void onUpdataPhoneSuccess() {
    }
}
