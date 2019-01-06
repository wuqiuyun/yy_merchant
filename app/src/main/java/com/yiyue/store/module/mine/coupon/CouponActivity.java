package com.yiyue.store.module.mine.coupon;

import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.databinding.ActivityMineCouponBinding;
import com.yl.core.util.StatusBarUtil;

/**
 * 我的优惠券
 * <p>
 * Create by zm on 2018/9/29
 */
public class CouponActivity extends BaseMvpAppCompatActivity<ICouponView, CouponPresenter> implements ICouponView {

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_mine_coupon;
    }

    @Override
    protected void init() {

        initView();
    }

    private void initView() {
        StatusBarUtil.setLightMode(this);
        ActivityMineCouponBinding couponBinding = (ActivityMineCouponBinding) viewDataBinding;
        // 返回
        couponBinding.titleView.setLeftClickListener(v -> finish());
    }

    @Override
    public void showToast(String message) {

    }
}
