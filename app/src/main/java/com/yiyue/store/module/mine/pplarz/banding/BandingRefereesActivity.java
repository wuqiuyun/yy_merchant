package com.yiyue.store.module.mine.pplarz.banding;

import android.view.View;

import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityBandingRefereesBinding;
import com.yiyue.store.model.local.preferences.CommonSharedPreferences;
import com.yiyue.store.model.vo.bean.EventBean;
import com.yiyue.store.util.FormatUtil;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;


/*
 *  @创建者:   27407
 *  @创建时间:  2018/10/17 16:59
 *  @描述：    绑定推荐人
 */

@CreatePresenter(BandingRefereesPresenter.class)
public class BandingRefereesActivity extends BaseMvpAppCompatActivity<IBandingRefereesView, BandingRefereesPresenter>
        implements ClickHandler, IBandingRefereesView {

    ActivityBandingRefereesBinding mBinding;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_banding_referees;
    }

    @Override
    protected void init() {
        StatusBarUtil.setLightMode(this);
        mBinding = (ActivityBandingRefereesBinding) viewDataBinding;
        mBinding.setClick(this);

        mBinding.titleView.setLeftClickListener(v -> finish());
        mBinding.tvInviteReward.setText(FormatUtil.Formatstring(CommonSharedPreferences.getInstance().getBasicDataBean().getInviteReward()));
        StatusBarUtil.setLightMode(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btn_commit:   //提交
                getMvpPresenter().bindInviteCode(mBinding.etInviteCode.getText().toString());
                break;
        }

    }

    @Override
    public void bindingSuc() {
        showToast("绑定成功！");
        //通知上一页刷新绑定的数据
        EventBus.getDefault().post(new EventBean.BandingRefereesSuc());
        finish();
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }
}
