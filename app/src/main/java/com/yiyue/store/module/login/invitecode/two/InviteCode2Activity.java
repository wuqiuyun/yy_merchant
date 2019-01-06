package com.yiyue.store.module.login.invitecode.two;

import android.annotation.SuppressLint;
import android.view.View;

import com.yiyue.store.R;
import com.yiyue.store.base.BaseAppCompatActivity;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.databinding.ActivityInviteCode2Binding;
import com.yiyue.store.model.local.preferences.CommonSharedPreferences;
import com.yiyue.store.module.login.information.PerfectInformationActivity;
import com.yl.core.util.StatusBarUtil;

public class InviteCode2Activity extends BaseAppCompatActivity implements ClickHandler{

    ActivityInviteCode2Binding mBinding;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_invite_code2;
    }

    @SuppressLint("StringFormatInvalid")
    @Override
    protected void init() {
        StatusBarUtil.setLightMode(this);
        mBinding = (ActivityInviteCode2Binding) viewDataBinding;
        mBinding.setClick(this);

        mBinding.tvInviteReward.setText(CommonSharedPreferences.getInstance().getBasicDataBean().getInviteReward());
        // 修改状态栏字体颜色
        StatusBarUtil.setLightMode(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.btn_invite_ok:    //确定
                startActivity(this , PerfectInformationActivity.class);
                finish();
                break;

        }
    }
}
