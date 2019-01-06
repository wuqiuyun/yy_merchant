package com.yiyue.store.module.mine.wallet.transfer;

import android.annotation.SuppressLint;
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
import com.yiyue.store.databinding.ActivityTransferBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.local.preferences.AccountSharedPreferences;
import com.yiyue.store.model.local.preferences.CommonSharedPreferences;
import com.yiyue.store.model.vo.bean.daobean.UserFriendsBean;
import com.yiyue.store.module.im.recommend.GivenGiftActivity;
import com.yiyue.store.module.mine.settings.security.paypassword.PayPasswordActivity;
import com.yiyue.store.module.mine.settings.security.paypassword.forgetpwd.ForgetPayPasswordActivity;
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
 * 转赠
 * <p>
 * Create by zm on 2018/10/9
 */
@CreatePresenter(TransferPresenter.class)
public class TransferActivity extends BaseMvpAppCompatActivity<ITransferView, TransferPresenter>
        implements ITransferView, ClickHandler {

    private String mCurrency,mCoincost;
    private String toUserId;
    private ActivityTransferBinding mTransferBinding;
    public static final int WALLET_TRANSFER = 0x001;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_transfer;
    }

    @Override
    protected void init() {

        initView();
    }

    @SuppressLint("StringFormatInvalid")
    private void initView() {
        StatusBarUtil.setLightMode(this);
        mTransferBinding = (ActivityTransferBinding) viewDataBinding;
        mTransferBinding.setClick(this);
        // back
        mTransferBinding.titleView.setLeftClickListener(v -> finish());
        mCurrency = getIntent().getStringExtra("currency");
        mCoincost = getIntent().getStringExtra("coincost");
        mCurrency = mCurrency.split("\\.")[0];
        mTransferBinding.tvCurrencyPrice.setText(String.format(getString(R.string.transfer_currency_price),
                FormatUtil.Formatstring(CommonSharedPreferences.getInstance().getBasicDataBean().getSysCoinDefault())));
        mTransferBinding.etTransferNumber.setHint(String.format(getString(R.string.transfer_number_input) , FormatUtil.Formatstring(CommonSharedPreferences.getInstance().getBasicDataBean().getSysCoinDefault())));
        mTransferBinding.tvCashWithdrawAmount.setText(mCoincost);
        //为EditText设置监听，注意监听类型为TextWatcher
        mTransferBinding.etTransferNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                long l=Long.parseLong(mCurrency);
                DLog.e("onTextChanged","------------"+s.toString().trim());
                if(s == null || s.length() == 0){
                    return;
                }
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        mTransferBinding.etTransferNumber.setText(s);
                        mTransferBinding.etTransferNumber.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    mTransferBinding.etTransferNumber.setText(s);
                    mTransferBinding.etTransferNumber.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        mTransferBinding.etTransferNumber.setText(s.subSequence(0, 1));
                        mTransferBinding.etTransferNumber.setSelection(1);
                        return;
                    }
                }

                if(s.toString().trim().substring(0,1).equals(".") ||
                        s.toString().trim().substring(s.toString().trim().length()-1,s.toString().trim().length()).equals(".")){
                    return;
                }
                String amount = s.toString().trim();
                if(amount.endsWith(".")){
                    amount = amount.substring(0,amount.length()-1);
                }
                double amountDouble = Double.parseDouble(amount);
                if (amountDouble>l){
                    mTransferBinding.etTransferNumber.setText("");
                    ToastUtils.shortToast("输入金额大于可转赠金额");
                    return;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.transfer_object: // 转赠对象
                Intent intent = new Intent(this, GivenGiftActivity.class);
                startActivityForResult(intent, WALLET_TRANSFER);
                break;
            case R.id.btn_ok: // 确定
                if (TextUtils.isEmpty(toUserId)||toUserId==null){
                    ToastUtils.shortToast("请选择转赠对象");
                    return;
                }
                String moenys =  mTransferBinding.etTransferNumber.getText().toString();
                if (TextUtils.isEmpty(moenys)||moenys==null){
                    ToastUtils.shortToast("请输入转赠金额");
                    return;
                }
                if (moenys.substring(moenys.length()-1,moenys.length()).equals(".")||moenys.substring(0,1).equals(".")){
                    ToastUtils.shortToast("请输入正确的转赠金额");
                    return;
                }
                if (moenys.equals("0")){
                    ToastUtils.shortToast("请输入转赠金额");
                    return;
                }
                getMvpPresenter().initPayWord(this);
                break;
        }
    }
    private void showDLDialog() {
        new YLDialog.Builder(this)
                .setType(YLDialog.DIALOG_TYPE_NORMAL)
                .setMessage("转赠成功")
                .setPositiveButton("查看记录", (dialog, which) ->{
                    dialog.dismiss();
                    setResult(Activity.RESULT_OK);
                    finish();
                })
                .setNegativeButton("继续转赠", (dialog, which) ->{
                    dialog.dismiss();
                })
                .create()
                .show();
    }
    @Override
    public void onSuccess() {
        showDLDialog();
    }

    @Override
    public void checkPasswordSuccess() {
        getMvpPresenter().present(mTransferBinding.etTransferNumber.getText().toString().trim(), AccountManager.getInstance().getUserId(),toUserId,this);
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
                            startActivity(TransferActivity.this, ForgetPayPasswordActivity.class);
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
                                getMvpPresenter().checkPayWord(pwd,TransferActivity.this);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Constants.GIFT_BACK_CODE) {
            switch (requestCode) {
                case WALLET_TRANSFER: // 返回转账对象ID昵称等
                    if (data != null) {
                        UserFriendsBean userFriendsBean = (UserFriendsBean) data.getSerializableExtra(Constants.GIFT_BACK);
                        if (userFriendsBean != null) {
                            String userName = userFriendsBean.getNickname();
                            mTransferBinding.transferObject.setRightText(userName);
                            toUserId = String.valueOf(userFriendsBean.getFriendId());
                        } else {
                            ToastUtils.shortToast("选择好友出错!");
                        }
                    }
                    break;
            }
        }
    }


}
