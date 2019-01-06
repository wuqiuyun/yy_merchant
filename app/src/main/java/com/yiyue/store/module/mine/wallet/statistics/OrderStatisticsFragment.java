package com.yiyue.store.module.mine.wallet.statistics;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.yiyue.store.R;
import com.yiyue.store.base.fragment.BaseMvpFragment;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.FragmentOrderStatisticsBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.vo.bean.OrderChartBean;
import com.yiyue.store.model.vo.bean.StoreOrderBean;
import com.yiyue.store.model.vo.bean.StoreSuccessOrdersBean;
import com.yiyue.store.module.home.invite.IUpDataFragment;
import com.yiyue.store.widget.viewpage.OrderViewPager;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 订单统计
 * Created by wqy on 2018/12/4.
 */
@CreatePresenter(OrderStatisticsPresenter.class)
public class OrderStatisticsFragment extends BaseMvpFragment<OrderStatisticsView, OrderStatisticsPresenter>
        implements OrderStatisticsView, ClickHandler {

    private static OrderViewPager mViewPager;
    private OrderStatisticsAdapter adapter;
    private FragmentOrderStatisticsBinding mBinding;
    private IUpDataFragment mIUpDataFragment;
    private String orderType;
    private List<String> mXData;
    private YAxis mLeftYAxis;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_order_statistics;
    }

    public static Fragment newInstance(OrderViewPager viewPager, int orderType) {
        mViewPager = viewPager;
        OrderStatisticsFragment orderStatisticsFragment = new OrderStatisticsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("orderType", orderType);
        orderStatisticsFragment.setArguments(bundle);
        return orderStatisticsFragment;
    }

    public void setIUpDataFragment(IUpDataFragment IUpDataFragment) {
        this.mIUpDataFragment = IUpDataFragment;
    }

    @Override
    protected void initView() {
        Bundle bundle = getArguments();
        int position = bundle.getInt("orderType", 0);
        orderType = String.valueOf(position);
        mViewPager.setObjectForPosition(rootView, position);

        mBinding = (FragmentOrderStatisticsBinding) viewDataBinding;
        mBinding.setClick(this);
        mBinding.recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // 添加自定义分割线
        DividerItemDecoration divider = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(getActivity(), R.drawable.shape_divider_line_1dp)));
        mBinding.recycleView.addItemDecoration(divider);
        adapter = new OrderStatisticsAdapter(getActivity());
        mBinding.recycleView.setAdapter(adapter);
        //K线图
        initLineChart();
    }

    @Override
    protected void loadData() {
        getMvpPresenter().getStoreOrderCount(orderType);
        getMvpPresenter().getStoreTimeSliceOrder(orderType, AccountManager.getInstance().getStoreId());
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }

    @Override
    public void onGetStoreOrderCountSuccess(StoreOrderBean bean) {
        mBinding.tvOrderReservation.setText(String.valueOf(bean.getAllReceiptNum()));
        mBinding.tvOrderFinish.setText(String.valueOf(bean.getAllSuccessNum()));
        mBinding.tvIncomeEstimate.setText(String.valueOf(bean.getAllReceiptMoney()));
        mBinding.tvIncomeTotal.setText(String.valueOf(bean.getAllSuccessMoney()));
        if (null != bean.getOrderCategoryCount() && bean.getOrderCategoryCount().size() > 0) {
            adapter.setDatas((ArrayList<StoreOrderBean.OrderCategoryCountBean>) bean.getOrderCategoryCount(), true);
        } else {
            mBinding.llEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public void getStoreTimeSliceOrder(List<List<OrderChartBean>> orderChartBeans) {
//        //预约订单K线
        List<Entry> entries = initEntry(orderChartBeans.get(0));
        LineDataSet lineDataSet = new LineDataSet(entries, "预约订单");
        lineDataSet.setCircleColor(getResources().getColor(R.color.login_text_bg));
        lineDataSet.setColor(getResources().getColor(R.color.login_text_bg));
        lineDataSet.setDrawValues(false);
//        //完成订单K线
        List<Entry> entries2 = initEntry(orderChartBeans.get(1));
        LineDataSet lineDataSet2 = new LineDataSet(entries2, "预约订单");
        lineDataSet2.setCircleColors(initColors(orderChartBeans));
        lineDataSet2.setColor(getResources().getColor(R.color.color_738CED));
        lineDataSet2.setDrawValues(false);
//        //设置数据
        LineData data = new LineData();
        if (entries.size() != 0) data.addDataSet(lineDataSet);
        if (entries2.size() != 0) data.addDataSet(lineDataSet2);
        setMaxY();
        mBinding.lineChart.setData(data);
        mBinding.lineChart.invalidate();
    }

    /**
     * 完成订单的点会覆盖预约订单的单,所以要给重合点设置另外一个颜色
     *
     * @param orderChartBeans
     */
    private ArrayList<Integer> initColors(List<List<OrderChartBean>> orderChartBeans) {
        ArrayList<Integer> colors = new ArrayList<>();
        List<OrderChartBean> orderChartBeans1 = orderChartBeans.get(0);
        List<OrderChartBean> orderChartBeans2 = orderChartBeans.get(1);

        for (int i = 0; i < orderChartBeans2.size(); i++) {
            colors.add(getResources().getColor(R.color.color_738CED));
            OrderChartBean orderChartBean = orderChartBeans2.get(i);
            for (int j = 0; j < orderChartBeans1.size(); j++) {
                OrderChartBean orderChartBean2 = orderChartBeans1.get(j);
                if (orderChartBean.getDate().equals(orderChartBean2.getDate()) && orderChartBean.getNum() == orderChartBean2.getNum()) {
                    colors.set(i, Color.GREEN);
                    break;
                }
            }
        }
        return colors;
    }

    @Override
    public void getStoreSuccessOrders(ArrayList<StoreSuccessOrdersBean> storeSuccessOrdersBeans) {

    }


    private void initLineChart() {
        //隐藏描述
        Description description = new Description();
        description.setEnabled(false);
        mBinding.lineChart.setDescription(description);
        mBinding.lineChart.setScaleYEnabled(false);//纵向缩放
        mBinding.lineChart.setScaleXEnabled(false);//横向缩放
        mBinding.lineChart.setTouchEnabled(false);//不可触摸
        //设置标签
        Legend legend = mBinding.lineChart.getLegend();
        legend.setEnabled(false);
        //设置X轴
        initX(orderType);
        //设置Y轴
        initY();
    }

    private int maxY = 0;

    /**
     * 初始化Y轴
     */
    private void initY() {
        mLeftYAxis = mBinding.lineChart.getAxisLeft();
        YAxis rightYAxis = mBinding.lineChart.getAxisRight();
        mLeftYAxis.setDrawAxisLine(false);//隐藏左侧Y轴线
//        leftYAxis.setEnabled(false); //左侧侧Y轴不显示
        rightYAxis.setEnabled(false); //右侧Y轴不显示
        mLeftYAxis.setAxisMaximum(0);
        mLeftYAxis.setStartAtZero(true);
    }

    /**
     * 初始化X轴
     *
     * @param orderType
     */
    private void initX(String orderType) {
        mXData = new ArrayList<>();
        Date date = new Date();
        switch (orderType) {
            case "0":
            case "1":
                for (int i = 0; i < 25; i++) {
                    mXData.add(i + "时");
                }
                break;
            case "2":
                for (int i = 6; i >= 0; i--) {
                    Date newDate = DateUtil.addDay(date, -i);
                    mXData.add(DateUtil.date2Str(newDate, FORMAT_MD));
                }
                break;
            case "3":
                for (int i = 30; i >= 0; i--) {
                    Date newDate = DateUtil.addDay(date, -i);
                    mXData.add(DateUtil.date2Str(newDate, FORMAT_MD));
                }
                break;
        }
        XAxis xAxis = mBinding.lineChart.getXAxis();
        xAxis.setDrawGridLines(false);//隐藏竖网格线
        xAxis.setDrawAxisLine(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//值：BOTTOM,BOTH_SIDED,BOTTOM_INSIDE,TOP,TOP_INSIDE
        //X轴多少刻度
        switch (orderType) {
            case "0":
            case "1":
                xAxis.setLabelCount(24, false);
                break;
            case "2":
                xAxis.setLabelCount(7, true);
                break;
            case "3":
                xAxis.setLabelCount(30, true);
                break;
        }
        xAxis.setAxisMinimum(0);//最小
        xAxis.setAxisMaximum(mXData.size());//最大
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                switch (orderType) {
                    case "0":
                    case "1":
                        if (value % 3 == 0) {
                            return mXData.get((int) value);
                        } else {
                            return "";
                        }
                    case "2":
                        if ((int) value == 6) return "今日";
                        if ((int) value == 5) return "昨日";
                        return mXData.get((int) value);
                    case "3":
                        if ((int) value == 0) {
                            return mXData.get((int) value);
                        } else if ((int) value % 5 == 0) {
                            if ((int) value == 30) {
                                return "今日";
                            } else {
                                return mXData.get((int) value);
                            }
                        }
                        return "";
                }
                return "";
            }
        });
    }

    private static String FORMAT_MD = "MM.dd";

    private List<Entry> initEntry(List<OrderChartBean> orderChartBeans) {
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < orderChartBeans.size(); i++) {
            OrderChartBean orderChartBean = orderChartBeans.get(i);
            if (orderChartBean.getDate() != null) {
                if (orderChartBean.getNum() > maxY) maxY = orderChartBean.getNum();
                switch (orderType) {
                    case "0":
                    case "1":
                        entries.add(new Entry(Integer.valueOf(orderChartBean.getDate()), orderChartBean.getNum()));
                        break;
                    case "2":
                    case "3":
                        String[] split = orderChartBean.getDate().split("-");
                        orderChartBean.setDate(split[1] + "." + split[2]);
                        for (int j = 0; j < mXData.size(); j++) {
                            if (mXData.get(j).equals(orderChartBean.getDate())) {
                                entries.add(new Entry(j + 1, orderChartBean.getNum()));
                                break;
                            }
                        }
                        break;
                }
            }
        }
        return entries;
    }

    private void setMaxY() {
        if (maxY < 10) {
            mLeftYAxis.setAxisMaximum(10);
        } else {
            mLeftYAxis.setAxisMaximum(maxY + 10);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_more:
                if (mBinding.llChart.getVisibility() == View.GONE) {
                    mBinding.llChart.setVisibility(View.VISIBLE);

                    Drawable drawable = getResources().getDrawable(R.drawable.icon_arrow3);
                    setRightDrawable(mBinding.tvLookMore, drawable);
                } else {
                    mBinding.llChart.setVisibility(View.GONE);
                    Drawable drawable = getResources().getDrawable(R.drawable.icon_arrow2);
                    setRightDrawable(mBinding.tvLookMore, drawable);
                }
                break;
        }
    }

    private void setRightDrawable(TextView textView, Drawable drawable) {
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        textView.setCompoundDrawables(null, null, drawable, null);
    }
}
