package com.yiyue.store.module.home.orders.manager;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yl.core.util.DateUtil;
import com.yl.core.util.StatusBarUtil;
import com.yiyue.store.R;
import com.yiyue.store.api.StoreManageApi;
import com.yiyue.store.base.BaseAppCompatActivity;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.databinding.ActivityOrderManagerBinding;
import com.yiyue.store.model.vo.bean.DayBean;
import com.yiyue.store.model.vo.bean.ShowtimeBean;
import com.yiyue.store.util.ColorUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 订单预约管理
 * Create by zm on 2018/12/17.
 */
public class OrderManagerActivity extends BaseAppCompatActivity {
    private static final String FORMAT_YMD = "yyyyMMdd";

    ActivityOrderManagerBinding mBinding;
    private List<DayBean> dayBeans;
    private List<Fragment> fragments = new ArrayList<>();

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_order_manager;
    }

    @Override
    protected void init() {
        initView();
        initEvent();
        initData();
    }

    private void initView() {
        mBinding = (ActivityOrderManagerBinding) viewDataBinding;
        StatusBarUtil.setDarkMode(this);
    }

    private void initFragment(int size) {
        dayBeans = setDays(size);
        setTable(mBinding.tableLayout, getLayoutInflater());
        setFragment();

        mBinding.viewPage.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
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
        mBinding.viewPage.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mBinding.tableLayout));
        mBinding.tableLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mBinding.viewPage) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mBinding.viewPage.setCurrentItem(tab.getPosition(), false);
            }
        });
        // 是否允许viewPage滑动切换
        mBinding.viewPage.setScanScroll(true);
        // viewPage预加载页面
        mBinding.viewPage.setOffscreenPageLimit(3);
        // 加载第几个页面
        mBinding.viewPage.setCurrentItem(0, false);
        // tablayout 是否滚动
        mBinding.tableLayout.setTabMode(size > 7 ? TabLayout.MODE_SCROLLABLE : TabLayout.MODE_FIXED);
    }

    private void initEvent() {
        // go back event
        mBinding.titleView.setLeftClickListener(v -> {
            finish();
        });
    }

    private void initData() {
        new StoreManageApi().getShowTime(new YLRxSubscriberHelper<BaseResponse<ShowtimeBean>>(this, true) {
            @Override
            public void _onNext(BaseResponse<ShowtimeBean> baseResponse) {
                String showTime = baseResponse.getData().getShowtime();
                int size;
                try {
                    size = Integer.valueOf(showTime);
                }catch (Exception e) {
                    size = 7;
                }
                initFragment(size);
            }
        });
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ColorUtil.getColor(R.color.color_FF6600));
    }

    /**
     * 设置Tab
     * @param tabLayout
     * @param inflater
     */
    private void setTable(TabLayout tabLayout, LayoutInflater inflater) {
        for (DayBean dayBean : dayBeans) {
            TabLayout.Tab newTab = tabLayout.newTab();
            View tabView = inflater.inflate(R.layout.view_order_date, null);
            newTab.setCustomView(tabView);

            TextView tvDay = tabView.findViewById(R.id.tv_day);
            tvDay.setText(dayBean.getDay());
            TextView tvWeek = tabView.findViewById(R.id.tv_week);
            tvWeek.setText(dayBean.getWeek());
            tabLayout.addTab(newTab);
        }
    }

    private void setFragment() {
        for (DayBean dayBean : dayBeans) {
            fragments.add(OrderManagerFragment.newInstance(dayBean.getDate()));
        }
    }


    private List<DayBean> setDays(int dayNum) {
        List<DayBean> dayBeans = new ArrayList<>();
        // 今天
        Date date = new Date();
        for (int i=0; i<dayNum; i++){
            Date newDate = DateUtil.addDay(date, i);
            dayBeans.add(new DayBean(DateUtil.getWeekOfDate(newDate).replace("星期", ""), String.valueOf(DateUtil.getDay(newDate)), DateUtil.date2Str(newDate, FORMAT_YMD)));
        }
        return dayBeans;
    }
}
