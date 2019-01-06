package com.yiyue.store.module.mine.settings.security.paypassword;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.jungly.gridpasswordview.GridPasswordView;
import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityPayPasswordBinding;
import com.yiyue.store.module.mine.settings.security.paypassword.forgetpwd.ForgetPayPasswordActivity;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

/**
 * 支付密码
 * Created by lyj on 2018/11/9.
 */
@CreatePresenter(PayPasswordPresenter.class)
public class PayPasswordActivity extends BaseMvpAppCompatActivity<PayPasswordView, PayPasswordPresenter>
        implements ClickHandler , PayPasswordView {

    ActivityPayPasswordBinding mBinding;

    private boolean isrepin = false;//默认false 不是重置密码

    private String psdold,psdnew,psdnew1;
    private int setpPin = 1;//设置密码 步骤1
    private int setpRePin = 1;//修改密码 步骤1

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_pay_password;
    }

    @Override
    protected void init() {
        StatusBarUtil.setLightMode(this);
        mBinding= (ActivityPayPasswordBinding) viewDataBinding;
        mBinding.setClick(this);
        initView();
    }

    private void initView() {
        Bundle bundle = getIntent().getExtras();
        if (bundle!=null){
            isrepin = bundle.getBoolean("isrepin");
        }
        if (isrepin){
            mBinding.titleView.setTitleText("修改支付密码");
            mBinding.btnPaypwdNext.setVisibility(View.VISIBLE);
            mBinding.tvPayTitle.setVisibility(View.VISIBLE);
            mBinding.tvPayTitle1.setVisibility(View.INVISIBLE);
            mBinding.tvPayForget.setVisibility(View.VISIBLE);
        }else {
            mBinding.titleView.setTitleText("设置支付密码");
            mBinding.btnPaypwdNext.setVisibility(View.INVISIBLE);
            mBinding.tvPayTitle.setVisibility(View.INVISIBLE);
            mBinding.tvPayTitle1.setVisibility(View.VISIBLE);
            mBinding.tvPayForget.setVisibility(View.INVISIBLE);
        }
        mBinding.titleView.setLeftClickListener(view -> finish());
        mBinding.pswView.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
            @Override
            public void onTextChanged(String psw) {
                Log.e("tv_pin_reset111", "ing-输入的密码为: "+psw.toString().trim()+"");
                String pwd = psw.toString().trim();
                if (!TextUtils.isEmpty(pwd)&&pwd!=null){
                    if (pwd.length()==6){
                        mBinding.btnPaypwdNext.setSelected(true);
                    }else {
                        mBinding.btnPaypwdNext.setSelected(false);
                    }
                }
            }

            @Override
            public void onInputFinish(String psw) {
                //输入法取消
                Log.e("tv_pin_reset2222", "结束-输入的密码为: "+psw.toString().trim()+"");
                if (isrepin){//修改密码
                    if (setpRePin==1){//步骤一 旧密码

                    }else if (setpRePin==2){//新密码1
                        mBinding.tvPayTitle.setVisibility(View.INVISIBLE);
                        mBinding.tvPayTitle1.setVisibility(View.VISIBLE);
                        mBinding.tvPayTitle1.setText("请再次输入");
                        mBinding.btnPaypwdNext.setVisibility(View.VISIBLE);
                        mBinding.tvPayForget.setVisibility(View.INVISIBLE);
                        psdnew = mBinding.pswView.getPassWord().toString().trim();
                        setpRePin=3;
                        mBinding.pswView.clearPassword();
                        mBinding.btnPaypwdNext.setSelected(false);
                    }else if (setpRePin==3){//新密码2

                    }
                }else {//设置密码
                    if (setpPin==1){//步骤一
                        mBinding.tvPayTitle.setVisibility(View.INVISIBLE);
                        mBinding.tvPayTitle1.setVisibility(View.VISIBLE);
                        mBinding.tvPayTitle1.setText("请再次输入");
                        mBinding.btnPaypwdNext.setVisibility(View.VISIBLE);
                        mBinding.tvPayForget.setVisibility(View.INVISIBLE);
                        psdnew = mBinding.pswView.getPassWord().toString().trim();
                        setpPin=2;
                        mBinding.pswView.clearPassword();
                        mBinding.btnPaypwdNext.setSelected(false);
                    }else if (setpPin==2){

                    }
                }

            }
        });
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btn_paypwd_next :      //下一步
                if (mBinding.btnPaypwdNext.isSelected()){
                    if (isrepin){//修改密码
                        if (setpRePin==1){//验证旧密码
                            psdold = mBinding.pswView.getPassWord().toString().trim();
                            getMvpPresenter().checkPayWord(psdold,this);
                        }else if (setpRePin==2){
                        }else if (setpRePin==3){
                            psdnew1 = mBinding.pswView.getPassWord().toString().trim();
                            getMvpPresenter().setPaypassword(psdnew,psdnew1,this);
                        }
                    }else {//设置密码
                        if (setpPin==1){//步骤一
                        }else if (setpPin==2){
                            psdnew1 = mBinding.pswView.getPassWord().toString().trim();
                            getMvpPresenter().setPaypassword(psdnew,psdnew1,this);
                        }
                    }
                }
                break;
            case R.id.tv_pay_forget :      //忘记支付密码
                startActivity(this, ForgetPayPasswordActivity.class);
                finish();
                break;
        }

    }


    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }

    @Override
    public void upDatePasswordSuccess() {
        finish();
    }

    @Override
    public void upDatePasswordFail(String s) {
//        ToastUtils.shortToast("密码修改失败");
    }

    @Override
    public void checkPasswordSuccess() {
//        ToastUtils.shortToast("支付密码验证成功");
        setpRePin=2;
        mBinding.titleView.setTitleText("设置支付密码");
        mBinding.btnPaypwdNext.setVisibility(View.INVISIBLE);
        mBinding.tvPayTitle.setVisibility(View.INVISIBLE);
        mBinding.tvPayTitle1.setVisibility(View.VISIBLE);
        mBinding.tvPayForget.setVisibility(View.INVISIBLE);
        mBinding.pswView.clearPassword();
    }
}
