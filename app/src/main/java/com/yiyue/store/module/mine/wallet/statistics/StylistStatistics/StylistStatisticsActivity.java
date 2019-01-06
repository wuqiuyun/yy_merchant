package com.yiyue.store.module.mine.wallet.statistics.StylistStatistics;

import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.databinding.ActivityStylistStatisticsBinding;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.EventOrderBean;
import com.yiyue.store.model.vo.bean.OrderChartBean;
import com.yiyue.store.model.vo.bean.StoreOrderBean;
import com.yiyue.store.model.vo.bean.StoreSuccessOrdersBean;
import com.yiyue.store.module.mine.wallet.statistics.OrderStatisticsPresenter;
import com.yiyue.store.module.mine.wallet.statistics.OrderStatisticsView;
import com.yiyue.store.util.Utils;
import com.yiyue.store.widget.popwindow.PopupUtil;
import com.yiyue.store.widget.popwindow.TriangleDrawable;
import com.yiyue.store.widget.popwindow.XGravity;
import com.yiyue.store.widget.popwindow.YGravity;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 美发师订单统计
 * Created by lvlong on 2018/10/13.
 */
@CreatePresenter(OrderStatisticsPresenter.class)
public class StylistStatisticsActivity
        extends BaseMvpAppCompatActivity<OrderStatisticsView, OrderStatisticsPresenter>
        implements ClickHandler, OrderStatisticsView, ViewPager.OnPageChangeListener {

    private List<StylistStatisticsFragment> mFragments;
    private static int ORDER_STYLIST_TYPE1 = 0;//平台
    private static int ORDER_STYLIST_TYPE2 = 1;//自有
    private static int ORDER_STYLIST_TYPE3 = 2;//全部
    ActivityStylistStatisticsBinding mBinding;
    private int nexus;
    private int orderDate;
    private PopupUtil mPopWindow;
    private final String[] labels = new String[]{
            "昨日","今日","近7日","近30日"
    };
    private EventOrderBean.OrderStatisticsBean mEventBean;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_stylist_statistics;
    }

    @Override
    protected void init() {
        StatusBarUtil.setLightMode(this);
        mBinding = (ActivityStylistStatisticsBinding) viewDataBinding;
        mBinding.setClick(this);
        initView();
    }


    private void initView() {
        mEventBean = new EventOrderBean.OrderStatisticsBean();
        mBinding.titleView.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mBinding.titleView.setRightTextClickListener(view -> {
            showPop(view);
        });
        initPop();
        setTable(mBinding.tableLayout, getLayoutInflater());
        setFragment();

        mBinding.tableLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mBinding.viewPage) {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        orderDate = Constants.ORDER_YESTERDAY;
                        break;
                    case 1:
                        orderDate = Constants.ORDER_TODAY;
                        break;
                    case 2:
                        orderDate = Constants.ORDER_RECENT7;
                        break;
                    case 3:
                        orderDate = Constants.ORDER_RECENT30;
                        break;
                }
                mBinding.viewPage.setCurrentItem(tab.getPosition(), false);
            }
        });
    }

    private void initPop() {
        mPopWindow = PopupUtil.create()
                .setContext(this)
                .setContentView(R.layout.popwin_order_layout)
                .setAnimationStyle(R.style.AnimImPopwindow)
                .setOnViewListener(new PopupUtil.OnViewListener() {
                    @Override
                    public void initViews(View view, PopupUtil basePopup) {
                        View arrowView = view.findViewById(R.id.v_arrow);
                        arrowView.setBackground(new TriangleDrawable(TriangleDrawable.TOP, Color.WHITE));
                        TextView tv_all = view.findViewById(R.id.tv_all);
                        TextView tv_complete = view.findViewById(R.id.tv_complete);
                        tv_complete.setText(getString(R.string.stylist_join));
                        TextView tv_settlement = view.findViewById(R.id.tv_settlement);
                        tv_settlement.setText(getString(R.string.stylist_signing));
                        tv_all.setOnClickListener(v -> {
                            //全部
                            nexus = ORDER_STYLIST_TYPE3;
                            upFragmentData();
                        });

                        tv_complete.setOnClickListener(v -> {
                            //平台
                            nexus = ORDER_STYLIST_TYPE1;
                            upFragmentData();
                        });
                        tv_settlement.setOnClickListener(v -> {
                            //店内
                            nexus = ORDER_STYLIST_TYPE2;
                            upFragmentData();
                        });
                    }
                })
                .setFocusAndOutsideEnable(true)
                .setBackgroundDimEnable(true)
                .setDimValue(0.4f)
                .apply();
    }

    private void upFragmentData() {
        mEventBean.setType(String.valueOf(orderDate));
        mEventBean.setNexus(String.valueOf(nexus));
        EventBus.getDefault().post(mEventBean);
        mPopWindow.dismiss();
    }

    private void showPop(View view) {
        int offsetX = Utils.dp2px(this, 20) - view.getWidth() / 2;
        int offsetY = (mBinding.titleView.getHeight() - view.getHeight()) / 2;
        mPopWindow.showAtAnchorView(view, YGravity.BELOW, XGravity.ALIGN_RIGHT, offsetX, offsetY - 10);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }
    @Override
    public void showToast(String message) {

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
    private void setFragment() {
        mFragments = new ArrayList<>();
        StylistStatisticsFragment stylistStatisticsFragment1 = (StylistStatisticsFragment) StylistStatisticsFragment.newInstance(Constants.ORDER_YESTERDAY);
        StylistStatisticsFragment stylistStatisticsFragment2 = (StylistStatisticsFragment) StylistStatisticsFragment.newInstance(Constants.ORDER_TODAY);
        StylistStatisticsFragment stylistStatisticsFragment3 = (StylistStatisticsFragment) StylistStatisticsFragment.newInstance(Constants.ORDER_RECENT7);
        StylistStatisticsFragment stylistStatisticsFragment4 = (StylistStatisticsFragment) StylistStatisticsFragment.newInstance(Constants.ORDER_RECENT30);
        mFragments.add(stylistStatisticsFragment1);
        mFragments.add(stylistStatisticsFragment2);
        mFragments.add(stylistStatisticsFragment3);
        mFragments.add(stylistStatisticsFragment4);
        mBinding.viewPage.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                super.destroyItem(container, position, object);
            }
        });
        mBinding.viewPage.addOnPageChangeListener(this);
        // 是否允许viewPage滑动切换
        mBinding.viewPage.setScanScroll(false);
        // viewPage预加载1个页面
        mBinding.viewPage.setOffscreenPageLimit(1);
        // 加载第几个页面
        mBinding.viewPage.setCurrentItem(0, false);
    }

    @Override
    public void onGetStoreOrderCountSuccess(StoreOrderBean bean) {

    }

    @Override
    public void getStoreTimeSliceOrder(List<List<OrderChartBean>> orderChartBeans) {

    }

    @Override
    public void getStoreSuccessOrders(ArrayList<StoreSuccessOrdersBean> storeSuccessOrdersBeans) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
