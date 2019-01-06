package com.yiyue.store.module.mine.wallet.cochain;

import android.annotation.SuppressLint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.yiyue.store.model.local.preferences.CommonSharedPreferences;
import com.yiyue.store.util.FormatUtil;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;
import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityCochainBinding;
import com.yiyue.store.util.ColorUtil;

/**
 * 上链
 * <p>
 * Create by zm on 2018/10/9
 */
@CreatePresenter(CochainPresenter.class)
public class CochainActivity extends BaseMvpAppCompatActivity<ICochainView, CochainPresenter>
        implements ICochainView, ClickHandler{

    private ActivityCochainBinding mCochainBinding;
    private String mCurrency;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_cochain;
    }

    @Override
    protected void init() {
        initView();
    }

    @SuppressLint("StringFormatInvalid")
    private void initView() {
        StatusBarUtil.setLightMode(this);
        mCochainBinding = (ActivityCochainBinding) viewDataBinding;
        mCurrency = getIntent().getStringExtra("currency");
        mCurrency = mCurrency.split("\\.")[0];
        mCochainBinding.tvCurrencyNumOwn.setText(mCurrency);
        mCochainBinding.setClick(this);
        mCochainBinding.tvDaibi.setText(String.format(getString(R.string.cochain_currency_number_own) , FormatUtil.Formatstring(CommonSharedPreferences.getInstance().getBasicDataBean().getSysCoinDefault())));
        mCochainBinding.tvDaibi1.setText(String.format(getString(R.string.cochain_currency_number) ,FormatUtil.Formatstring(CommonSharedPreferences.getInstance().getBasicDataBean().getSysCoinDefault())));
        mCochainBinding.etCurrencyNum.setHint(String.format(getString(R.string.cochain_currency_number_input) ,FormatUtil.Formatstring(CommonSharedPreferences.getInstance().getBasicDataBean().getSysCoinDefault())));

        mCochainBinding.titleView.setLeftClickListener(v -> finish());
        mCochainBinding.titleView.setRightTextClickListener(v -> {
            ToastUtils.shortToast("right");
        });
        //为EditText设置监听，注意监听类型为TextWatcher
        mCochainBinding.etCurrencyNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                long l=Long.parseLong(mCurrency);
                if (mCochainBinding.etCurrencyNum.getText().toString().trim().length()!=0){
                    long l2=Long.parseLong(mCochainBinding.etCurrencyNum.getText().toString().trim());
                    if (l<l2){
                        mCochainBinding.etCurrencyNum.setText(mCurrency);
                    }
                }
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

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_all: // 全部
                mCochainBinding.etCurrencyNum.setText(mCurrency);
                break;
            case R.id.btn_ok: // 确定
                ToastUtils.shortToast("未与后台联调接口");
                getMvpPresenter().cochain("1",mCochainBinding.etCurrencyNum.getText().toString().trim(),this);
                break;
        }
    }

    @Override
    public void onSuccess() {

    }
}
