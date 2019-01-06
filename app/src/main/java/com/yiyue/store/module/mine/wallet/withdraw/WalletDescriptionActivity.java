package com.yiyue.store.module.mine.wallet.withdraw;

import com.yiyue.store.R;
import com.yiyue.store.base.BaseAppCompatActivity;
import com.yiyue.store.databinding.ActivityWalletDescriptionBinding;
import com.yl.core.util.StatusBarUtil;

/**
 *
 * 钱包说明
 *
 * Created by lvlong on 2018/10/13.
 */
public class WalletDescriptionActivity extends BaseAppCompatActivity {

    ActivityWalletDescriptionBinding mBinding;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_wallet_description;
    }

    @Override
    protected void init() {
        StatusBarUtil.setLightMode(this);
        mBinding = (ActivityWalletDescriptionBinding) viewDataBinding;
        mBinding.titleView.setLeftClickListener(view -> finish());

    }
}
