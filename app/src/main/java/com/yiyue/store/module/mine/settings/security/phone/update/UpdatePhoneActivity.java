package com.yiyue.store.module.mine.settings.security.phone.update;

import android.view.View;

import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityUpdatePhoneBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.module.mine.settings.security.phone.PhoneVerifyPresenter;
import com.yiyue.store.module.mine.settings.security.phone.PhoneVerifyView;
import com.yiyue.store.util.FormatUtil;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

/**
 * Created by lvlong on 2018/10/12.
 */
@CreatePresenter(PhoneVerifyPresenter.class)
public class UpdatePhoneActivity extends BaseMvpAppCompatActivity<PhoneVerifyView, PhoneVerifyPresenter> implements ClickHandler , PhoneVerifyView {

    ActivityUpdatePhoneBinding mBinding;
    private String mMobile;
    private String mNewPhone;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_update_phone;
    }

    @Override
    protected void init() {

        mBinding = (ActivityUpdatePhoneBinding) viewDataBinding;
        mBinding.setClick(this);

        initView();

    }

    private void initView() {
        StatusBarUtil.setLightMode(this);
        mMobile = FormatUtil.Formatstring(AccountManager.getInstance().getMobile());
        mBinding.titleView.setLeftClickListener(view -> finish());

        mBinding.tvCurrentPhone.setText(mMobile);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.tv_get_code :     //获取验证码
                getMvpPresenter().getPhoneCode(mBinding.etPhone.getText().toString().trim());
                break;

            case R.id.btn_complete :    //完成
                mNewPhone = mBinding.etPhone.getText().toString().trim();
                String code = mBinding.etCode.getText().toString().trim();
                getMvpPresenter().updataPhone(mMobile, mNewPhone,code);
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

    }

    @Override
    public void onUpdataPhoneSuccess() {
        showToast("手机号更改成功");
        AccountManager.getInstance().insertingLogout();
    }

}
