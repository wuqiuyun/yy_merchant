package com.yiyue.store.module.mine.wallet.withdraw;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.jungly.gridpasswordview.GridPasswordView;
import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityWithdrawBinding;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.CashAliBean;
import com.yiyue.store.module.mine.settings.security.cashaccount.CashAccountManageActivity;
import com.yiyue.store.module.mine.settings.security.paypassword.PayPasswordActivity;
import com.yiyue.store.module.mine.settings.security.paypassword.forgetpwd.ForgetPayPasswordActivity;
import com.yiyue.store.module.mine.wallet.withdraw.account.AccountWithdrawActivity;
import com.yiyue.store.util.BankImgUtil;
import com.yiyue.store.util.ColorUtil;
import com.yiyue.store.util.FormatUtil;
import com.yiyue.store.widget.dialog.BaseEasyDialog;
import com.yiyue.store.widget.dialog.EasyDialog;
import com.yiyue.store.widget.dialog.ViewConvertListener;
import com.yiyue.store.widget.dialog.ViewHolder;
import com.yiyue.store.widget.dialog.YLDialog;
import com.yl.core.component.log.DLog;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

/**
 * 提现
 * <p>
 * Create by zm on 2018/10/8
 */
@CreatePresenter(WithdrawPresenter.class)
public class WithdrawActivity extends BaseMvpAppCompatActivity<IWithdrawView, WithdrawPresenter>
    implements IWithdrawView, ClickHandler{
    public static final int ACCOUNTWITHDRAW = 0x005;

    private double mMoney = 0.00;//提现金额
    private double amount1;
    private boolean ischoosepaytype = false;//是否选择提现方式

    ActivityWithdrawBinding withdrawBinding;
    private CashAliBean cashAliBean;
    private String selectAccount;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_withdraw;
    }

    @Override
    protected void init() {

        initView();
    }

    private void initView() {
        StatusBarUtil.setLightMode(this);
        withdrawBinding = (ActivityWithdrawBinding) viewDataBinding;
        withdrawBinding.setClick(this);
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            mMoney = bundle.getDouble(Constants.WITHDRAW_MONEY);
            withdrawBinding.tvCashWithdrawAmount.setText(FormatUtil.FormatDouble(mMoney)+"元");
        }

        // back
        withdrawBinding.titleView.setLeftClickListener(v -> {
            finish();
        });
        withdrawBinding.trWithdrawNobankcard.setVisibility(View.VISIBLE);
        withdrawBinding.trWithdrawAccount.setVisibility(View.GONE);
        withdrawBinding.etWithdrawAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                DLog.e("onTextChanged","------------"+s.toString().trim());
                //优惠券必须要达到的金额Integer.parseInt(fullPrice);
                if(s == null || s.length() == 0){
                    withdrawBinding.tvReceivedAmount.setText("0");
                    return;
                }
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        withdrawBinding.etWithdrawAmount.setText(s);
                        withdrawBinding.etWithdrawAmount.setSelection(s.length());
                        withdrawBinding.tvReceivedAmount.setText(s.toString());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    withdrawBinding.etWithdrawAmount.setText(s);
                    withdrawBinding.etWithdrawAmount.setSelection(2);
                    withdrawBinding.tvReceivedAmount.setText(s.toString());
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        withdrawBinding.etWithdrawAmount.setText(s.subSequence(0, 1));
                        withdrawBinding.etWithdrawAmount.setSelection(1);
                        withdrawBinding.tvReceivedAmount.setText(s.subSequence(0, 1));
                        return;
                    }
                }

                if(s.toString().trim().substring(0,1).equals(".") ||
                        s.toString().trim().substring(s.toString().trim().length()-1,s.toString().trim().length()).equals(".")){
                    withdrawBinding.tvReceivedAmount.setText("0");
                    return;
                }
                String amount = s.toString().trim();
                if(amount.endsWith(".")){
                    amount = amount.substring(0,amount.length()-1);
                }
                double amountDouble = Double.parseDouble(amount);
                if (amountDouble>mMoney){
                    withdrawBinding.etWithdrawAmount.setText("");
                    withdrawBinding.tvReceivedAmount.setText("0");
                    ToastUtils.shortToast("输入金额大于可提现金额");
                    return;
                }
                //balance（可提现金额） -1（手续费）>=提现金额； 如果成立  到账金额就是提现金额，如果不成立则减1元
//                if (mMoney-1>=amountDouble){//可提现金额-1 >= 输入提现金额
                withdrawBinding.tvReceivedAmount.setText(String.valueOf(amountDouble-1));
//                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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
            case R.id.tv_withdraw_addbankcard: // 选择提现账户
            case R.id.tr_withdraw_account: // 提现账户
                Intent intent = new Intent(this, CashAccountManageActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("isWithdraw", true);
                intent.putExtras(bundle);
                startActivityForResult(intent, Constants.BANK_BACK_CODE);
                break;
            case R.id.tv_all: // 全部
                withdrawBinding.etWithdrawAmount.setText(mMoney+"");
                break;
            case R.id.btn_ok: // 确定
                String amount = withdrawBinding.etWithdrawAmount.getText().toString();
                if (amount!=null&&!TextUtils.isEmpty(amount)){
                    amount1 = Double.parseDouble(amount);
                    if (mMoney<=0){
                        ToastUtils.shortToast("可提现余额不足");
                        return;
                    }else if (!ischoosepaytype){
                        ToastUtils.shortToast("请选择提现账户");
                        return;
                    }else if (TextUtils.isEmpty(amount)&&amount==null){
                        ToastUtils.shortToast("请输入提现金额");
                        return;
                    }else if (amount1>mMoney){
                        ToastUtils.shortToast("提现金额不能大于余额");
                        return;
                    }else if (amount1<100){
                        ToastUtils.shortToast("提现金额不能小于100");
                        return;
                    }else if (amount1>20000){
                        ToastUtils.shortToast("提现金额不能大于20000");
                        return;
                    }
                    getMvpPresenter().initPayWord(this);
                }

//                if (paytype==0){
//                    getMvpPresenter().cash2Wx(amount1);
//                }else {
//                    getMvpPresenter().cash2ALi(amount1);
//                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Constants.BANK_BACK_CODE) {
            switch (requestCode) {
                case Constants.BANK_BACK_CODE: // 选择提现账户
                    if (data == null) return;
                    cashAliBean = (CashAliBean) data.getExtras().getSerializable("selectAccount");
                    selectAccount = cashAliBean.getBindId()+"";
                    String imgNa = cashAliBean.getShortName();
                    String bankcard = cashAliBean.getAccountno();
                    String subs = "";
                    withdrawBinding.trWithdrawNobankcard.setVisibility(View.GONE);
                    withdrawBinding.trWithdrawAccount.setVisibility(View.VISIBLE);
                    if (!cashAliBean.getType().equals("ALI")){
                        subs = "尾号"+bankcard.substring(bankcard.length()-4,bankcard.length())+"储蓄卡";
                    }else {
                        subs = "支付宝"+"—("+bankcard+")";
                    }
                    withdrawBinding.tvWithdrawAccount.setText(cashAliBean.getTypeName());
                    withdrawBinding.tvWithdrawAccount1.setText(subs);
                    withdrawBinding.ivWithdrawAccount.setImageResource(BankImgUtil.getBankImg(imgNa));
                    ischoosepaytype = true;
                    break;
            }
        }
    }

    @Override
    public void onWxCashSuccess() {
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void onALiCashSuccess() {
        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void checkPasswordSuccess() {
        getMvpPresenter().toCash(amount1,selectAccount,this);
    }

    @Override
    public void onInitPayWord(String json) {

    }

    @Override
    public void oninitPayWordInfoSuccess(String json) {
        //data=0(未设置)，data=1(已设置)。若未设置支付密码，弹窗跳转到设置支付密码页面
        Log.e("---------","-----"+json);
        if (json.equals("0")||json.equals("0.0")){
            ToastUtils.shortToast("请先设置支付密码");
            Bundle bundle = new Bundle();
            bundle.putBoolean("isrepin", false);
            startActivity(this, PayPasswordActivity.class, bundle);
        }else {
            showPayDialog();
        }

    }

    @Override
    public void oninitPayWordInfoFail() {

    }

    private void showPayDialog() {
        EasyDialog.init()
                .setLayoutId(R.layout.dialog_input_paypassword)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseEasyDialog dialog) {
                        holder.getView(R.id.ll_pay_cancle).setOnClickListener(v -> {
                            dialog.dismiss();
                        });
                        holder.getView(R.id.tv_pay_forget).setOnClickListener(v -> {
                            startActivity(WithdrawActivity.this, ForgetPayPasswordActivity.class);
                            dialog.dismiss();
                        });
                        GridPasswordView pswView = (GridPasswordView) holder.getView(R.id.pswView);
                        pswView.setOnPasswordChangedListener(new GridPasswordView.OnPasswordChangedListener() {
                            @Override
                            public void onTextChanged(String psw) {
                            }

                            @Override
                            public void onInputFinish(String psw) {
                                //输入法取消
                                Log.e("showPayDialog", "结束-输入的密码为: "+psw.toString().trim()+"");
                                String pwd = psw.toString().trim();
                                getMvpPresenter().checkPayWord(pwd,WithdrawActivity.this);
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .setPosition(Gravity.BOTTOM)
                .setMargin(0)
                .setOutCancel(true)
                .show(this.getSupportFragmentManager());
    }

}