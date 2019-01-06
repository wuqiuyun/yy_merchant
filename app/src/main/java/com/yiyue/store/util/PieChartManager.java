package com.yiyue.store.util;

import android.content.Context;
import android.text.TextUtils;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.yiyue.store.R;
import com.yiyue.store.model.vo.bean.MonthSumBean;
import com.yiyue.store.widget.piechart.MyPieChart;

import java.util.ArrayList;
import java.util.List;

public class PieChartManager {
    private PieChart pieChart;
    private Context mContext;
    private static final String FORMAT_M = "MM月";

    public PieChartManager(MyPieChart pieChart, Context context) {
        this.pieChart = pieChart;
        mContext = context;
        //初始化饼图
        initChartPie();
    }

    private void initChartPie() {
        //隐藏描述
        pieChart.getDescription().setEnabled(false);
        //不可触摸
        pieChart.setTouchEnabled(false);
        //中间圆洞
        pieChart.setHoleRadius(50f);
        pieChart.setHoleColor(ColorUtil.getColor(R.color.white));
        //透明圆
        pieChart.setTransparentCircleRadius(0f);
        //圆环距离屏幕上下上下左右的距离
        pieChart.setExtraOffsets(5f, 8.f, 5.f, 8.f);
//        //设置标签
        pieChart.getLegend().setEnabled(false);
        //饼图内部显示内容
        pieChart.setDrawSliceText(false);
    }
    
    public void showPieChart(List<MonthSumBean.ClassifyInBean> classifyIn) {
        List<PieEntry> strings = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (MonthSumBean.ClassifyInBean classifyInBean:classifyIn) {
            if (!TextUtils.isEmpty(classifyInBean.getName())){
                strings.add(new PieEntry((float) classifyInBean.getPercent(),classifyInBean.getName()));
                switch (classifyInBean.getInType()){
                    case 1:
                        colors.add(ColorUtil.getColor(R.color.color_43BBD1));//注册奖金
                        break;
                    case 2:
                        colors.add(ColorUtil.getColor(R.color.color_4AB35C));//推广佣金
                        break;
                    case 3:
                        colors.add(ColorUtil.getColor(R.color.color_E5342B));//红包
                        break;
                    case 4:
                        colors.add(ColorUtil.getColor(R.color.color_F1B34E));//服务收入
                        break;
                    case 5:
                        colors.add(ColorUtil.getColor(R.color.color_F3427D));//转账
                        break;
                    default:
                        colors.add(ColorUtil.getColor(R.color.color_BCD15A));//其他
                        break;
                }
            }
        }
        PieDataSet dataSet = new PieDataSet(strings,null);
        dataSet.setColors(colors);
//        //将饼图内部文字颜色设置为透明
//        dataSet.setValueTextColor(ColorUtil.getColor(R.color.transparent));无用
//        dataSet.setValueTextColors(colors);无用
        //设置连接线
        dataSet.setValueLinePart1OffsetPercentage(50);
        dataSet.setValueLinePart1Length(0.3f);
        dataSet.setValueLinePart2Length(0.5f);
        dataSet.setValueLineColor(ColorUtil.getColor(R.color.color_FFB848));
        dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        PieData pieData = new PieData(dataSet);
        pieData.setDrawValues(true);
        pieData.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                PieEntry p= (PieEntry) entry;
                return p.getLabel();
            }
        });
        pieData.setValueTextSize(12f);
        pieData.setValueTextColor(ColorUtil.getColor(R.color.color_343434));
        pieChart.setData(pieData);
        pieChart.invalidate();
    }
}
