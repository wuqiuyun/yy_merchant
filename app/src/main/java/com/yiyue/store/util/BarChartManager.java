package com.yiyue.store.util;

import android.content.Context;
import android.graphics.Color;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.yiyue.store.R;
import com.yiyue.store.model.vo.bean.MonthSumBean;
import com.yl.core.util.DateUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BarChartManager {

    private BarChart mBarChart;
    private YAxis leftAxis;
    private YAxis rightAxis;
    private XAxis xAxis;
    private DecimalFormat mFormat;
    private List<String> mXValues;
    private Context mContext;
    private static final String FORMAT_M = "MM月";
    private List<String> mXData;
    private Date date;

    public Date getDate() {
        return date;
    }

    public BarChartManager(BarChart mBarChart, Context context) {
        this.mBarChart = mBarChart;
        leftAxis = mBarChart.getAxisLeft();
        rightAxis = mBarChart.getAxisRight();
        xAxis = mBarChart.getXAxis();
        mContext = context;
        //初始化柱状图
        initBarChart();
    }

    /**
     * 初始化BarChart图表
     */
    private void initBarChart() {
        /***图表设置***/
        //隐藏描述
        mBarChart.getDescription().setEnabled(false);
        //纵向缩放
        mBarChart.setScaleYEnabled(false);
        //横向缩放
        mBarChart.setScaleXEnabled(false);
        //不可触摸
        mBarChart.setTouchEnabled(false);
        //背景颜色
        mBarChart.setBackgroundColor(Color.WHITE);
        //不显示图表网格背景
        mBarChart.setDrawGridBackground(false);
        //背景阴影
        mBarChart.setDrawBarShadow(false);
        mBarChart.setHighlightFullBarEnabled(false);
        //显示边框
        mBarChart.setDrawBorders(false);
        //设置动画效果
        mBarChart.animateY(1000, Easing.EasingOption.Linear);
        mBarChart.animateX(1000, Easing.EasingOption.Linear);
        //隐藏标签
        mBarChart.getLegend().setEnabled(false);
        //增加顶部标签
//        mBarChart.setMarker(new ChartMarkerView(getContext(),R.layout.view_marker));
        /***XY轴的设置***/
        initX();
        initY();
        //标签显示位置
//        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
//        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        //标签是否绘制在图表里面
//        legend.setDrawInside(true);
    }


    /**
     * 初始化Y轴
     */
    private void initY() {
        leftAxis = mBarChart.getAxisLeft();
        leftAxis.setDrawAxisLine(false);//隐藏左侧Y轴线
        leftAxis.setStartAtZero(false);
        leftAxis.setTextColor(Color.WHITE);
        //设置Y轴的最小值和最大值
        leftAxis.setAxisMaximum(0);
        //横向网格线
        leftAxis.setEnabled(true);
        leftAxis.setStartAtZero(true);

        YAxis rightYAxis = mBarChart.getAxisRight();
        rightYAxis.setDrawAxisLine(false); //右侧Y轴不显示
        rightYAxis.setEnabled(false);
        rightYAxis.setTextColor(Color.WHITE);
    }
    /**
     * 初始化X轴
     */
    private void initX() {
        upXValue(new Date());
        xAxis = mBarChart.getXAxis();
        //隐藏竖网格线
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        //设置X轴颜色/宽度
        xAxis.setGridColor(ColorUtil.getColor(R.color.color_FFDA99));
        xAxis.setAxisLineWidth(0.5f);
        //设置X轴位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//值：BOTTOM,BOTH_SIDED,BOTTOM_INSIDE,TOP,TOP_INSIDE
        //X轴多少刻度
        xAxis.setLabelCount(5, false);
    }

    /**
     * 更新X轴值
     */
    private void upXValue(Date date) {
        if (mXData==null){
            mXData = new ArrayList<>();
        }else {
            mXData.clear();
        }
        for (int i = 4; i > 0; i--) {
            Date newDate = DateUtil.addMonth(date, -i);
            mXData.add(DateUtil.date2Str(newDate, FORMAT_M));
        }
        mXData.add(DateUtil.date2Str(date, FORMAT_M));
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            int i=0;
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                if (i>=5)i=0;
                String s= mXData.get(i);
                i++;
                return s;
            }
        });
    }

    public void showBarChart(List<MonthSumBean.ShowfyMonthBean> showfyMonthBeans, Date date) {
        this.date=date;
        //设置X值
        upXValue(date);
        ArrayList<BarEntry> entries = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        float axisMaximum=0;
        for (int i = 0; i < mXData.size(); i++) {
            colors.add(ColorUtil.getColor(R.color.color_FFDA99));
            String month = mXData.get(i).replace("月","");
            BarEntry barEntry = new BarEntry((float)i, 0);
            entries.add(barEntry);
            /**
             * 此处还可传入Drawable对象 BarEntry(float x, float y, Drawable icon)
             * 即可设置柱状图顶部的 icon展示
             */
            for (MonthSumBean.ShowfyMonthBean showfyMonthBean:showfyMonthBeans) {
                if (axisMaximum<showfyMonthBean.getSumMoney())axisMaximum=showfyMonthBean.getSumMoney();
                String[] s = showfyMonthBean.getDate().split("-");
                if (s.length<2)return;
                if (month.equals(s[1])){
                    entries.set(i,new BarEntry((float)i,showfyMonthBean.getSumMoney()));
                    break;
                }
            }
        }
        BarDataSet barDataSet = new BarDataSet(entries, "");
        //柱形颜色
        colors.set(4, ColorUtil.getColor(R.color.color_FF5A19));
        barDataSet.setColors(colors);
        //顶部文字颜色
        barDataSet.setValueTextColors(colors);
        //不显示柱状图顶部值
        barDataSet.setDrawValues(true);
        //顶部显示数值
        barDataSet.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.format(mContext.getString(R.string.RMB),String.valueOf(value));
            }
        });
        BarData data = new BarData(barDataSet);
        //      设置宽度
        data.setBarWidth(0.4f);
        //设置Y最大值
        leftAxis.setAxisMaximum(axisMaximum+10);
        mBarChart.setData(data);
        mBarChart.invalidate();
    }

}
