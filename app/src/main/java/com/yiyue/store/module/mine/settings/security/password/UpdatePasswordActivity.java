package com.yiyue.store.module.mine.settings.security.password;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;

import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityUpdatePasswordBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.module.login.forgetpwd.ForgetPasswordActivity;
import com.yl.core.component.log.DLog;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

/**
 * 修改密码
 * Created by lvlong on 2018/10/12.
 */
@CreatePresenter(UpdatePasswordPresenter.class)
public class UpdatePasswordActivity extends BaseMvpAppCompatActivity<UpdatePasswordView , UpdatePasswordPresenter> implements ClickHandler , UpdatePasswordView {

    ActivityUpdatePasswordBinding mBinding;
    private boolean hidden1=false;
    private boolean hidden2=false;
    private boolean hidden3=false;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_update_password;
    }

    @Override
    protected void init() {
        mBinding= (ActivityUpdatePasswordBinding) viewDataBinding;
        mBinding.setClick(this);
        initView();

    }

    private void initView() {
        StatusBarUtil.setLightMode(this);
        mBinding.titleView.setLeftClickListener(view -> finish());
    }

    /**
     * 设置输入显示隐藏
     */
    private void setInPutVisible(EditText et, boolean isVisible) {
        if (isVisible){
            et.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);// 输入为密码且可见
        }else {
            et.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);// 设置文本类密码（默认不可见），这两个属性必须同时设置
        }
    }
    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.ll_hidden1 :      //旧密码是否可见
                hidden1=!hidden1;
                setInPutVisible(mBinding.etOldPassword,hidden1);
                break;
            case R.id.ll_hidden2 :      //新密码是否可见
                hidden2=!hidden2;
                setInPutVisible(mBinding.etPassword,hidden2);
                break;
            case R.id.ll_hidden3 :      //重复密码是否可见
                hidden3=!hidden3;
                setInPutVisible(mBinding.etNextPassword,hidden3);
                break;
            case R.id.tv_forget_password :      //忘记密码
                Intent intent = new Intent(this , ForgetPasswordActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("type" , 2);
                intent.putExtra("bundle" , bundle);
                startActivity(intent);
                finish();
                break;

            case R.id.btn_commit :      //提交
                commit();
                break;

        }

    }

    private void commit() {
        String oldPassword = mBinding.etOldPassword.getText().toString().trim();
        String password = mBinding.etPassword.getText().toString().trim();
        String nextPassword = mBinding.etNextPassword.getText().toString().trim();
        String userId = AccountManager.getInstance().getUserId();
       getMvpPresenter().upDatePwd(userId
       ,oldPassword
       ,password,nextPassword,this);
    }


    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }

    @Override
    public void upDatePasswordSuccess() {
        //修改密码成功回调
        DLog.d("**********************************************");
        ToastUtils.shortToast("密码修改成功");
        AccountManager.getInstance().insertingLogout();
    }

    @Override
    public void upDatePasswordFail(String s) {
//        ToastUtils.shortToast("密码修改失败,请联系客服:"+s);
    }
}
