package com.yiyue.store.module.mine.settings.security.phone.password;

import android.text.TextUtils;
import android.view.View;

import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityPasswordVerifyBinding;
import com.yiyue.store.module.mine.settings.security.phone.update.UpdatePhoneActivity;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

/**
 * 密码验证
 * Created by lvlong on 2018/10/12.
 */
@CreatePresenter(PasswordVerifyPresenter.class)
public class PasswordVerifyActivity extends BaseMvpAppCompatActivity<PasswordVerifyView , PasswordVerifyPresenter> implements ClickHandler , PasswordVerifyView {

    ActivityPasswordVerifyBinding mBinding;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_password_verify;
    }

    @Override
    protected void init() {

        mBinding = (ActivityPasswordVerifyBinding) viewDataBinding;
        mBinding.setClick(this);

        initView();

    }

    private void initView() {
        StatusBarUtil.setLightMode(this);
        mBinding.titleView.setLeftClickListener(view -> finish());

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_next :        //下一步

                String password = mBinding.etPassword.getText().toString().trim();

                if (!TextUtils.isEmpty(password)){
                    if (password.length() >= 6){
                        getMvpPresenter().checkPassword(password);
                    }else {
                        ToastUtils.shortToast("密码最少6位");
                    }
                }else {
                    ToastUtils.shortToast("请输入密码");
                }



                break;

        }

    }

    @Override
    public void showToast(String message) {

    }

    @Override
    public void onCheckPassword() {
        startActivity(this , UpdatePhoneActivity.class);
        finish();
    }
}
