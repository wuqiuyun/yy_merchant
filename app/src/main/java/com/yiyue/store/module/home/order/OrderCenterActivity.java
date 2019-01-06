package com.yiyue.store.module.home.order;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yiyue.store.R;
import com.yiyue.store.YLApplication;
import com.yiyue.store.base.BaseAppCompatActivity;
import com.yiyue.store.databinding.ActivityOrderBinding;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.constant.OrderStatus;
import com.yl.core.util.StatusBarUtil;

import java.util.ArrayList;

/**
 * 订单中心
 * <p>
 *  Create by zm on 2018/9/27.
 */
public class OrderCenterActivity extends BaseAppCompatActivity {

    ActivityOrderBinding orderBinding;

    @OrderStatus.OrderType
    private int orderType = OrderStatus.ORDER_CONFIRM;

    private final String[] labels = new String[]{
            YLApplication.getContext().getString(R.string.label_title_confirm),
            YLApplication.getContext().getString(R.string.label_title_service),
            YLApplication.getContext().getString(R.string.label_title_complete),
            YLApplication.getContext().getString(R.string.label_title_refund)
    };

    private final ArrayList<Fragment> fragments = new ArrayList<>();
    {
        fragments.add(OrderFragment.newInstance(OrderStatus.ORDER_CONFIRM));
        fragments.add(OrderFragment.newInstance(OrderStatus.ORDER_SERVICE));
        fragments.add(OrderFragment.newInstance(OrderStatus.ORDER_COMPLETE));
        fragments.add(OrderFragment.newInstance(OrderStatus.ORDER_REFUND));
    }

    /**
     * start activity
     * @param context
     * @param orderType 订单状态{@link com.yiyue.store.model.constant.OrderStatus}
     */
    public static void startActivity (Context context, @OrderStatus.OrderType int orderType) {
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.EXTRA_ORDER_TYPE, orderType);
        startActivity(context, OrderCenterActivity.class, bundle);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_order;
    }

    @Override
    protected void init() {
        hasExtras();
        initView();
        initEvent();
    }

    private void hasExtras () {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            orderType = bundle.getInt(Constants.EXTRA_ORDER_TYPE, OrderStatus.ORDER_CONFIRM);
        }
    }

    private void initView() {
        StatusBarUtil.setLightMode(this);

        orderBinding = (ActivityOrderBinding) viewDataBinding;
        setTable(orderBinding.tableLayout, getLayoutInflater());
        orderBinding.viewPage.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                super.destroyItem(container, position, object);
            }
        });
        orderBinding.viewPage.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(orderBinding.tableLayout));
        orderBinding.tableLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(orderBinding.viewPage) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
               orderBinding.viewPage.setCurrentItem(tab.getPosition(), false);
            }
        });
        // 是否允许viewPage滑动切换
        orderBinding.viewPage.setScanScroll(true);
        // viewPage预加载1个页面
        orderBinding.viewPage.setOffscreenPageLimit(1);
        // 加载第几个页面
        orderBinding.viewPage.setCurrentItem(orderType, false);
    }

    private void initEvent() {
        // 返回
        orderBinding.titleView.setLeftClickListener(v -> {
            finish();
        });
    }

    /**
     * 设置Tab
     * @param tabLayout
     * @param inflater
     */
    private void setTable(TabLayout tabLayout, LayoutInflater inflater) {
        for (String label : labels) {
            TabLayout.Tab newTab = tabLayout.newTab();
            View tabView = inflater.inflate(R.layout.tab_layout_home, null);
            newTab.setCustomView(tabView);

            TextView tvLabel = tabView.findViewById(R.id.tv_label);
            tvLabel.setText(label);
            tabLayout.addTab(newTab);
        }
    }

}
