package com.yiyue.store.widget.piechart;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IPieDataSet;
import com.github.mikephil.charting.renderer.PieChartRenderer;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.List;

public class MyPieChartRenderer extends PieChartRenderer {
    private static final String TAG = "MyPieChartRenderer";

    public MyPieChartRenderer(PieChart chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
    }

    String text = "2.0%";

    /**
     * 这个方法，左边是从下往上画，右边是从上往下画
     * @param c
     */
    @Override
    public void drawValues(Canvas c) {

        Rect rect = new Rect();
        getPaintEntryLabels().getTextBounds(text, 0, text.length(), rect);
        int textHeight = rect.height();//文本的高度

        MPPointF center = mChart.getCenterCircleBox();

        // get whole the radius
        float radius = mChart.getRadius();
        float rotationAngle = mChart.getRotationAngle();
        float[] drawAngles = mChart.getDrawAngles();
        float[] absoluteAngles = mChart.getAbsoluteAngles();

        float phaseX = mAnimator.getPhaseX();
        float phaseY = mAnimator.getPhaseY();

        final float holeRadiusPercent = mChart.getHoleRadius() / 100.f;
        float labelRadiusOffset = radius / 10f * 3.6f;

        if (mChart.isDrawHoleEnabled()) {
            labelRadiusOffset = (radius - (radius * holeRadiusPercent)) / 2f;
        }

        final float labelRadius = radius - labelRadiusOffset;

        PieData data = mChart.getData();
        List<IPieDataSet> dataSets = data.getDataSets();

        float yValueSum = data.getYValueSum();

        boolean drawEntryLabels = mChart.isDrawEntryLabelsEnabled();

        float angle;
        int xIndex = 0;

        c.save();

        float offset = Utils.convertDpToPixel(5.f);

        for (int i = 0; i < dataSets.size(); i++) {

            IPieDataSet dataSet = dataSets.get(i);

            final boolean drawValues = dataSet.isDrawValuesEnabled();

            if (!drawValues && !drawEntryLabels)
                continue;

            final PieDataSet.ValuePosition xValuePosition = dataSet.getXValuePosition();
            final PieDataSet.ValuePosition yValuePosition = dataSet.getYValuePosition();

            // apply the text-styling defined by the DataSet
            applyValueTextStyle(dataSet);

            float lineHeight = Utils.calcTextHeight(mValuePaint, "Q")
                    + Utils.convertDpToPixel(4f);

            IValueFormatter formatter = dataSet.getValueFormatter();

            int entryCount = dataSet.getEntryCount();

            mValueLinePaint.setColor(dataSet.getValueLineColor());
            mValueLinePaint.setStrokeWidth(Utils.convertDpToPixel(dataSet.getValueLineWidth()));

            final float sliceSpace = getSliceSpace(dataSet);

            MPPointF iconsOffset = MPPointF.getInstance(dataSet.getIconsOffset());
            iconsOffset.x = Utils.convertDpToPixel(iconsOffset.x);
            iconsOffset.y = Utils.convertDpToPixel(iconsOffset.y);

            float lastPositionOfLeft = 0;
            float lastPositionOfRight = 0;

            for (int j = 0; j < entryCount; j++) {

                PieEntry entry = dataSet.getEntryForIndex(j);

                if (xIndex == 0)
                    angle = 0.f;
                else
                    angle = absoluteAngles[xIndex - 1] * phaseX;

                final float sliceAngle = drawAngles[xIndex];
                final float sliceSpaceMiddleAngle = sliceSpace / (Utils.FDEG2RAD * labelRadius);

                // offset needed to center the drawn text in the slice
                final float angleOffset = (sliceAngle - sliceSpaceMiddleAngle / 2.f) / 2.f;

                angle = angle + angleOffset;

                final float transformedAngle = rotationAngle + angle * phaseY;

                float value = mChart.isUsePercentValuesEnabled() ? entry.getY()
                        / yValueSum * 100f : entry.getY();

                final float sliceXBase = (float) Math.cos(transformedAngle * Utils.FDEG2RAD);
                final float sliceYBase = (float) Math.sin(transformedAngle * Utils.FDEG2RAD);

                final boolean drawXOutside = drawEntryLabels &&
                        xValuePosition == PieDataSet.ValuePosition.OUTSIDE_SLICE;
                final boolean drawYOutside = drawValues &&
                        yValuePosition == PieDataSet.ValuePosition.OUTSIDE_SLICE;
                final boolean drawXInside = drawEntryLabels &&
                        xValuePosition == PieDataSet.ValuePosition.INSIDE_SLICE;
                final boolean drawYInside = drawValues &&
                        yValuePosition == PieDataSet.ValuePosition.INSIDE_SLICE;

                if (drawXOutside || drawYOutside) {

                    final float valueLineLength1 = dataSet.getValueLinePart1Length();
                    final float valueLineLength2 = dataSet.getValueLinePart2Length();
                    final float valueLinePart1OffsetPercentage = dataSet.getValueLinePart1OffsetPercentage() / 100.f;

                    float pt2x, pt2y;
                    float labelPtx, labelPty;

                    float line1Radius;

                    if (mChart.isDrawHoleEnabled())
                        line1Radius = (radius - (radius * holeRadiusPercent))
                                * valueLinePart1OffsetPercentage
                                + (radius * holeRadiusPercent);
                    else
                        line1Radius = radius * valueLinePart1OffsetPercentage;

                    final float polyline2Width = dataSet.isValueLineVariableLength()
                            ? labelRadius * valueLineLength2 * (float) Math.abs(Math.sin(
                            transformedAngle * Utils.FDEG2RAD))
                            : labelRadius * valueLineLength2;

                    final float pt0x = line1Radius * sliceXBase + center.x;
                    final float pt0y = line1Radius * sliceYBase + center.y;

                    final float pt1x = labelRadius * (1 + valueLineLength1) * sliceXBase + center.x;
                    final float pt1y = labelRadius * (1 + valueLineLength1) * sliceYBase + center.y;

                    //左右算法不一样,左边是从下往上排的，即你可以理解为饼图是顺时针方向，从零点排到12点的360度圆形，建议先看else里的，即右边的，方便理解
                    if (transformedAngle % 360.0 >= 90.0 && transformedAngle % 360.0 <= 270.0) {//左边部分
//                        pt2x = pt1x - polyline2Width;
                        pt2x = center.x - radius - 5;
                        if (lastPositionOfLeft == 0) {//第一个不用管
                            pt2y = pt1y;
                        } else {
                            if (lastPositionOfLeft - pt1y < textHeight) {//如果上一个labley的位置减去计算出的pt1y的位置间距小于lable的高度，则需要在计算出的y基础上加上差的间距
                                pt2y = pt1y - (textHeight - (lastPositionOfLeft - pt1y));//例如：lable高度5，计算出的pt1y位置10，上一个labley的位置12，则间距只有lastPositionOfRight-pt1y=2，还需要减去少的textHeight-(lastPositionOfRight-pt1y)=3才行
                            } else {
                                pt2y = pt1y;
                            }
                        }
                        lastPositionOfLeft = pt2y;//记录上一个lableY方向的位置

                        mValuePaint.setTextAlign(Paint.Align.RIGHT);

                        if (drawXOutside)
                            getPaintEntryLabels().setTextAlign(Paint.Align.RIGHT);

                        labelPtx = pt2x - offset;
                        labelPty = pt2y;
                    } else {//右边部分
//                        pt2x = pt1x + polyline2Width;
                        pt2x = center.x + radius + 5;
                        if (lastPositionOfRight == 0) {//第一个不用管
                            pt2y = pt1y;
                        } else {
                            if (pt1y - lastPositionOfRight < textHeight) {//如果计算出的pt1y的位置减去上一个labley的位置间距小于lable的高度，则需要在计算出的y基础上加上差的间距
                                pt2y = pt1y + (textHeight - (pt1y - lastPositionOfRight));//例如：lable高度5，计算出的pt1y位置10，上一个labley的位置8，则间距只有pt1y-lastPositionOfRight=2，还需要加上少的textHeight-(pt1y-lastPositionOfRight)=3才行
                            } else {
                                pt2y = pt1y;
                            }
                        }
                        lastPositionOfRight = pt2y;//记录上一个lableY方向的位置
                        mValuePaint.setTextAlign(Paint.Align.LEFT);

                        if (drawXOutside)
                            getPaintEntryLabels().setTextAlign(Paint.Align.LEFT);

                        labelPtx = pt2x + offset;
                        labelPty = pt2y;
                    }

                    if (dataSet.getValueLineColor() != ColorTemplate.COLOR_NONE) {
                        c.drawLine(pt0x, pt0y, pt1x, pt1y, mValueLinePaint);
                        c.drawLine(pt1x, pt1y, pt2x, pt2y, mValueLinePaint);
                    }

                    // draw everything, depending on settings
                    if (drawXOutside && drawYOutside) {

                        drawValue(c,
                                formatter,
                                value,
                                entry,
                                0,
                                labelPtx,
                                labelPty,
                                dataSet.getValueTextColor(j));

                        if (j < data.getEntryCount() && entry.getLabel() != null) {
                            drawEntryLabel(c, entry.getLabel(), labelPtx, labelPty + lineHeight);
                        }

                    } else if (drawXOutside) {
                        if (j < data.getEntryCount() && entry.getLabel() != null) {
                            drawEntryLabel(c, entry.getLabel(), labelPtx, labelPty + lineHeight / 2.f);
                        }
                    } else if (drawYOutside) {

                        drawValue(c, formatter, value, entry, 0, labelPtx, labelPty + lineHeight / 2.f, dataSet
                                .getValueTextColor(j));
                    }
                }

                if (drawXInside || drawYInside) {
                    // calculate the text position
                    float x = labelRadius * sliceXBase + center.x;
                    float y = labelRadius * sliceYBase + center.y;

                    mValuePaint.setTextAlign(Paint.Align.CENTER);

                    // draw everything, depending on settings
                    if (drawXInside && drawYInside) {

                        drawValue(c, formatter, value, entry, 0, x, y, dataSet.getValueTextColor(j));

                        if (j < data.getEntryCount() && entry.getLabel() != null) {
                            drawEntryLabel(c, entry.getLabel(), x, y + lineHeight);
                        }

                    } else if (drawXInside) {
                        if (j < data.getEntryCount() && entry.getLabel() != null) {
                            drawEntryLabel(c, entry.getLabel(), x, y + lineHeight / 2f);
                        }
                    } else if (drawYInside) {

                        drawValue(c, formatter, value, entry, 0, x, y + lineHeight / 2f, dataSet.getValueTextColor(j));
                    }
                }

                if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {

                    Drawable icon = entry.getIcon();

                    float x = (labelRadius + iconsOffset.y) * sliceXBase + center.x;
                    float y = (labelRadius + iconsOffset.y) * sliceYBase + center.y;
                    y += iconsOffset.x;

                    Utils.drawImage(
                            c,
                            icon,
                            (int) x,
                            (int) y,
                            icon.getIntrinsicWidth(),
                            icon.getIntrinsicHeight());
                }

                xIndex++;
            }

            MPPointF.recycleInstance(iconsOffset);
        }
        MPPointF.recycleInstance(center);
        c.restore();
    }
/**
 * 这个方法，是两侧都上往下排列，保证上面整齐
 */
//    @Override
//    public void drawValues(Canvas c) {
//
//
//        Rect rect = new Rect();
//        getPaintEntryLabels().getTextBounds(text, 0, text.length(), rect);
//        int textHeight = rect.height();//文本的高度
//
//        MPPointF center = mChart.getCenterCircleBox();
//
//        // get whole the radius
//        float radius = mChart.getRadius();
//        float rotationAngle = mChart.getRotationAngle();
//        float[] drawAngles = mChart.getDrawAngles();
//        float[] absoluteAngles = mChart.getAbsoluteAngles();
//
//        float phaseX = mAnimator.getPhaseX();
//        float phaseY = mAnimator.getPhaseY();
//
//        final float holeRadiusPercent = mChart.getHoleRadius() / 100.f;
//        float labelRadiusOffset = radius / 10f * 3.6f;
//
//        if (mChart.isDrawHoleEnabled()) {
//            labelRadiusOffset = (radius - (radius * holeRadiusPercent)) / 2f;
//        }
//
//        final float labelRadius = radius - labelRadiusOffset;
//
//        PieData data = mChart.getData();
//        List<IPieDataSet> dataSets = data.getDataSets();
//
//        float yValueSum = data.getYValueSum();
//
//        boolean drawEntryLabels = mChart.isDrawEntryLabelsEnabled();
//
//        float angle;
//        int xIndex = 0;
//
//        c.save();
//
//        float offset = Utils.convertDpToPixel(5.f);
//
//        for (int i = 0; i < dataSets.size(); i++) {
//
//            IPieDataSet dataSet = dataSets.get(i);
//
//            final boolean drawValues = dataSet.isDrawValuesEnabled();
//
//            if (!drawValues && !drawEntryLabels)
//                continue;
//
//            final PieDataSet.ValuePosition xValuePosition = dataSet.getXValuePosition();
//            final PieDataSet.ValuePosition yValuePosition = dataSet.getYValuePosition();
//
//            // apply the text-styling defined by the DataSet
//            applyValueTextStyle(dataSet);
//
//            float lineHeight = Utils.calcTextHeight(mValuePaint, "Q")
//                    + Utils.convertDpToPixel(4f);
//
//            IValueFormatter formatter = dataSet.getValueFormatter();
//
//            int entryCount = dataSet.getEntryCount();
//
//            mValueLinePaint.setColor(dataSet.getValueLineColor());
//            mValueLinePaint.setStrokeWidth(Utils.convertDpToPixel(dataSet.getValueLineWidth()));
//
//            final float sliceSpace = getSliceSpace(dataSet);
//
//            MPPointF iconsOffset = MPPointF.getInstance(dataSet.getIconsOffset());
//            iconsOffset.x = Utils.convertDpToPixel(iconsOffset.x);
//            iconsOffset.y = Utils.convertDpToPixel(iconsOffset.y);
//
//            float lastPositionOfLeft = 0;
//            float lastPositionOfRight = 0;
//
//            //画右边
//            for (int j = 0; j < entryCount; j++) {
//
//                PieEntry entry = dataSet.getEntryForIndex(j);
//
//                if (xIndex == 0)
//                    angle = 0.f;
//                else
//                    angle = absoluteAngles[xIndex - 1] * phaseX;
//
//                final float sliceAngle = drawAngles[xIndex];
//                final float sliceSpaceMiddleAngle = sliceSpace / (Utils.FDEG2RAD * labelRadius);
//
//                // offset needed to center the drawn text in the slice
//                final float angleOffset = (sliceAngle - sliceSpaceMiddleAngle / 2.f) / 2.f;
//
//                angle = angle + angleOffset;
//
//                final float transformedAngle = rotationAngle + angle * phaseY;
//
//                float value = mChart.isUsePercentValuesEnabled() ? entry.getY()
//                        / yValueSum * 100f : entry.getY();
//
//                final float sliceXBase = (float) Math.cos(transformedAngle * Utils.FDEG2RAD);
//                final float sliceYBase = (float) Math.sin(transformedAngle * Utils.FDEG2RAD);
//
//                final boolean drawXOutside = drawEntryLabels &&
//                        xValuePosition == PieDataSet.ValuePosition.OUTSIDE_SLICE;
//                final boolean drawYOutside = drawValues &&
//                        yValuePosition == PieDataSet.ValuePosition.OUTSIDE_SLICE;
//                final boolean drawXInside = drawEntryLabels &&
//                        xValuePosition == PieDataSet.ValuePosition.INSIDE_SLICE;
//                final boolean drawYInside = drawValues &&
//                        yValuePosition == PieDataSet.ValuePosition.INSIDE_SLICE;
//
//                if (drawXOutside || drawYOutside) {
//
//                    final float valueLineLength1 = dataSet.getValueLinePart1Length();
//                    final float valueLineLength2 = dataSet.getValueLinePart2Length();
//                    final float valueLinePart1OffsetPercentage = dataSet.getValueLinePart1OffsetPercentage() / 100.f;
//
//                    float pt2x, pt2y;
//                    float labelPtx, labelPty;
//
//                    float line1Radius;
//
//                    if (mChart.isDrawHoleEnabled())
//                        line1Radius = (radius - (radius * holeRadiusPercent))
//                                * valueLinePart1OffsetPercentage
//                                + (radius * holeRadiusPercent);
//                    else
//                        line1Radius = radius * valueLinePart1OffsetPercentage;
//
//                    final float polyline2Width = dataSet.isValueLineVariableLength()
//                            ? labelRadius * valueLineLength2 * (float) Math.abs(Math.sin(
//                            transformedAngle * Utils.FDEG2RAD))
//                            : labelRadius * valueLineLength2;
//
//                    final float pt0x = line1Radius * sliceXBase + center.x;
//                    final float pt0y = line1Radius * sliceYBase + center.y;
//
//                    final float pt1x = labelRadius * (1 + valueLineLength1) * sliceXBase + center.x;
//                    final float pt1y = labelRadius * (1 + valueLineLength1) * sliceYBase + center.y;
//
//                    //左右算法不一样,左边是从下往上排的，即你可以理解为饼图是顺时针方向，从零点排到12点的360度圆形，建议先看else里的，即右边的，方便理解
//                    if (transformedAngle % 360.0 >= 90.0 && transformedAngle % 360.0 <= 270.0) {//左边部分
////                        pt2x = center.x - radius - 5;
////                        if (lastPositionOfLeft == 0) {//第一个不用管
////                            pt2y = pt1y;
////                        } else {
////                            if (lastPositionOfLeft - pt1y < textHeight) {//如果上一个labley的位置减去计算出的pt1y的位置间距小于lable的高度，则需要在计算出的y基础上加上差的间距
////                                pt2y = pt1y - (textHeight - (lastPositionOfLeft - pt1y));//例如：lable高度5，计算出的pt1y位置10，上一个labley的位置12，则间距只有lastPositionOfRight-pt1y=2，还需要减去少的textHeight-(lastPositionOfRight-pt1y)=3才行
////                            } else {
////                                pt2y = pt1y;
////                            }
////                        }
////                        lastPositionOfLeft = pt2y;//记录上一个lableY方向的位置
////
////                        mValuePaint.setTextAlign(Paint.Align.RIGHT);
////
////                        if (drawXOutside)
////                            getPaintEntryLabels().setTextAlign(Paint.Align.RIGHT);
////
////                        labelPtx = pt2x - offset;
////                        labelPty = pt2y;
//                        break;
//                    } else {//右边部分
////                        pt2x = pt1x + polyline2Width;
//                        pt2x = center.x + radius + 5;
//                        if (lastPositionOfRight == 0) {//第一个不用管
//                            pt2y = pt1y;
//                        } else {
//                            if (pt1y - lastPositionOfRight < textHeight) {//如果计算出的pt1y的位置减去上一个labley的位置间距小于lable的高度，则需要在计算出的y基础上加上差的间距
//                                pt2y = pt1y + (textHeight - (pt1y - lastPositionOfRight));//例如：lable高度5，计算出的pt1y位置10，上一个labley的位置8，则间距只有pt1y-lastPositionOfRight=2，还需要加上少的textHeight-(pt1y-lastPositionOfRight)=3才行
//                            } else {
//                                pt2y = pt1y;
//                            }
//                        }
//                        lastPositionOfRight = pt2y;//记录上一个lableY方向的位置
//                        mValuePaint.setTextAlign(Paint.Align.LEFT);
//
//                        if (drawXOutside)
//                            getPaintEntryLabels().setTextAlign(Paint.Align.LEFT);
//
//                        labelPtx = pt2x + offset;
//                        labelPty = pt2y;
//                    }
//
//                    if (dataSet.getValueLineColor() != ColorTemplate.COLOR_NONE) {
//                        c.drawLine(pt0x, pt0y, pt1x, pt1y, mValueLinePaint);
//                        c.drawLine(pt1x, pt1y, pt2x, pt2y, mValueLinePaint);
//                    }
//
//                    // draw everything, depending on settings
//                    if (drawXOutside && drawYOutside) {
//
//                        drawValue(c,
//                                formatter,
//                                value,
//                                entry,
//                                0,
//                                labelPtx,
//                                labelPty,
//                                dataSet.getValueTextColor(j));
//
//                        if (j < data.getEntryCount() && entry.getLabel() != null) {
//                            drawEntryLabel(c, entry.getLabel(), labelPtx, labelPty + lineHeight);
//                        }
//
//                    } else if (drawXOutside) {
//                        if (j < data.getEntryCount() && entry.getLabel() != null) {
//                            drawEntryLabel(c, entry.getLabel(), labelPtx, labelPty + lineHeight / 2.f);
//                        }
//                    } else if (drawYOutside) {
//
//                        drawValue(c, formatter, value, entry, 0, labelPtx, labelPty + lineHeight / 2.f, dataSet
//                                .getValueTextColor(j));
//                    }
//                }
//
//                if (drawXInside || drawYInside) {
//                    // calculate the text position
//                    float x = labelRadius * sliceXBase + center.x;
//                    float y = labelRadius * sliceYBase + center.y;
//
//                    mValuePaint.setTextAlign(Paint.Align.CENTER);
//
//                    // draw everything, depending on settings
//                    if (drawXInside && drawYInside) {
//
//                        drawValue(c, formatter, value, entry, 0, x, y, dataSet.getValueTextColor(j));
//
//                        if (j < data.getEntryCount() && entry.getLabel() != null) {
//                            drawEntryLabel(c, entry.getLabel(), x, y + lineHeight);
//                        }
//
//                    } else if (drawXInside) {
//                        if (j < data.getEntryCount() && entry.getLabel() != null) {
//                            drawEntryLabel(c, entry.getLabel(), x, y + lineHeight / 2f);
//                        }
//                    } else if (drawYInside) {
//
//                        drawValue(c, formatter, value, entry, 0, x, y + lineHeight / 2f, dataSet.getValueTextColor(j));
//                    }
//                }
//
//                if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {
//
//                    Drawable icon = entry.getIcon();
//
//                    float x = (labelRadius + iconsOffset.y) * sliceXBase + center.x;
//                    float y = (labelRadius + iconsOffset.y) * sliceYBase + center.y;
//                    y += iconsOffset.x;
//
//                    Utils.drawImage(
//                            c,
//                            icon,
//                            (int) x,
//                            (int) y,
//                            icon.getIntrinsicWidth(),
//                            icon.getIntrinsicHeight());
//                }
//
//                xIndex++;
//            }
//
//            //画左边
//            xIndex = entryCount-1;
//            for (int j = entryCount - 1; j >= 0; j--) {
//
//                PieEntry entry = dataSet.getEntryForIndex(j);
//
//                if (xIndex == 0)
//                    angle = 0.f;
//                else
//                    angle = absoluteAngles[xIndex - 1] * phaseX;
//
//                final float sliceAngle = drawAngles[xIndex];
//                final float sliceSpaceMiddleAngle = sliceSpace / (Utils.FDEG2RAD * labelRadius);
//
//                // offset needed to center the drawn text in the slice
//                final float angleOffset = (sliceAngle - sliceSpaceMiddleAngle / 2.f) / 2.f;
//
//                angle = angle + angleOffset;
//
//                final float transformedAngle = rotationAngle + angle * phaseY;
//
//                float value = mChart.isUsePercentValuesEnabled() ? entry.getY()
//                        / yValueSum * 100f : entry.getY();
//
//                final float sliceXBase = (float) Math.cos(transformedAngle * Utils.FDEG2RAD);
//                final float sliceYBase = (float) Math.sin(transformedAngle * Utils.FDEG2RAD);
//
//                final boolean drawXOutside = drawEntryLabels &&
//                        xValuePosition == PieDataSet.ValuePosition.OUTSIDE_SLICE;
//                final boolean drawYOutside = drawValues &&
//                        yValuePosition == PieDataSet.ValuePosition.OUTSIDE_SLICE;
//                final boolean drawXInside = drawEntryLabels &&
//                        xValuePosition == PieDataSet.ValuePosition.INSIDE_SLICE;
//                final boolean drawYInside = drawValues &&
//                        yValuePosition == PieDataSet.ValuePosition.INSIDE_SLICE;
//
//                if (drawXOutside || drawYOutside) {
//
//                    final float valueLineLength1 = dataSet.getValueLinePart1Length();
//                    final float valueLineLength2 = dataSet.getValueLinePart2Length();
//                    final float valueLinePart1OffsetPercentage = dataSet.getValueLinePart1OffsetPercentage() / 100.f;
//
//                    float pt2x, pt2y;
//                    float labelPtx, labelPty;
//
//                    float line1Radius;
//
//                    if (mChart.isDrawHoleEnabled())
//                        line1Radius = (radius - (radius * holeRadiusPercent))
//                                * valueLinePart1OffsetPercentage
//                                + (radius * holeRadiusPercent);
//                    else
//                        line1Radius = radius * valueLinePart1OffsetPercentage;
//
//                    final float polyline2Width = dataSet.isValueLineVariableLength()
//                            ? labelRadius * valueLineLength2 * (float) Math.abs(Math.sin(
//                            transformedAngle * Utils.FDEG2RAD))
//                            : labelRadius * valueLineLength2;
//
//                    final float pt0x = line1Radius * sliceXBase + center.x;
//                    final float pt0y = line1Radius * sliceYBase + center.y;
//
//                    final float pt1x = labelRadius * (1 + valueLineLength1) * sliceXBase + center.x;
//                    final float pt1y = labelRadius * (1 + valueLineLength1) * sliceYBase + center.y;
//
//                    //左右算法不一样,左边是从下往上排的，即你可以理解为饼图是顺时针方向，从零点排到12点的360度圆形，建议先看else里的，即右边的，方便理解
//                    if (transformedAngle % 360.0 >= 90.0 && transformedAngle % 360.0 <= 270.0) {//左边部分
//                        pt2x = center.x - radius - 5;
//                        if (lastPositionOfLeft == 0) {//第一个不用管
//                            pt2y = pt1y;
//                        } else {
//                            if (pt1y - lastPositionOfLeft < textHeight) {//如果上一个labley的位置减去计算出的pt1y的位置间距小于lable的高度，则需要在计算出的y基础上加上差的间距
//                                pt2y = pt1y + (textHeight - (pt1y - lastPositionOfLeft));//例如：lable高度5，计算出的pt1y位置10，上一个labley的位置12，则间距只有lastPositionOfRight-pt1y=2，还需要减去少的textHeight-(lastPositionOfRight-pt1y)=3才行
//                            } else {
//                                pt2y = pt1y;
//                            }
//                        }
//                        lastPositionOfLeft = pt2y;//记录上一个lableY方向的位置
//
//                        mValuePaint.setTextAlign(Paint.Align.RIGHT);
//
//                        if (drawXOutside)
//                            getPaintEntryLabels().setTextAlign(Paint.Align.RIGHT);
//
//                        labelPtx = pt2x - offset;
//                        labelPty = pt2y;
//
//                    } else {//右边部分
////                        pt2x = center.x + radius + 5;
////                        if (lastPositionOfRight == 0) {//第一个不用管
////                            pt2y = pt1y;
////                        } else {
////                            if (pt1y - lastPositionOfRight < textHeight) {//如果计算出的pt1y的位置减去上一个labley的位置间距小于lable的高度，则需要在计算出的y基础上加上差的间距
////                                pt2y = pt1y + (textHeight - (pt1y - lastPositionOfRight));//例如：lable高度5，计算出的pt1y位置10，上一个labley的位置8，则间距只有pt1y-lastPositionOfRight=2，还需要加上少的textHeight-(pt1y-lastPositionOfRight)=3才行
////                            } else {
////                                pt2y = pt1y;
////                            }
////                        }
////                        lastPositionOfRight = pt2y;//记录上一个lableY方向的位置
////                        mValuePaint.setTextAlign(Paint.Align.LEFT);
////
////                        if (drawXOutside)
////                            getPaintEntryLabels().setTextAlign(Paint.Align.LEFT);
////
////                        labelPtx = pt2x + offset;
////                        labelPty = pt2y;
//                        continue;
//                    }
//
//                    if (dataSet.getValueLineColor() != ColorTemplate.COLOR_NONE) {
//                        c.drawLine(pt0x, pt0y, pt1x, pt1y, mValueLinePaint);
//                        c.drawLine(pt1x, pt1y, pt2x, pt2y, mValueLinePaint);
//                    }
//
//                    // draw everything, depending on settings
//                    if (drawXOutside && drawYOutside) {
//
//                        drawValue(c,
//                                formatter,
//                                value,
//                                entry,
//                                0,
//                                labelPtx,
//                                labelPty,
//                                dataSet.getValueTextColor(j));
//
//                        if (j < data.getEntryCount() && entry.getLabel() != null) {
//                            drawEntryLabel(c, entry.getLabel(), labelPtx, labelPty + lineHeight);
//                        }
//
//                    } else if (drawXOutside) {
//                        if (j < data.getEntryCount() && entry.getLabel() != null) {
//                            drawEntryLabel(c, entry.getLabel(), labelPtx, labelPty + lineHeight / 2.f);
//                        }
//                    } else if (drawYOutside) {
//
//                        drawValue(c, formatter, value, entry, 0, labelPtx, labelPty + lineHeight / 2.f, dataSet
//                                .getValueTextColor(j));
//                    }
//                }
//
//                if (drawXInside || drawYInside) {
//                    // calculate the text position
//                    float x = labelRadius * sliceXBase + center.x;
//                    float y = labelRadius * sliceYBase + center.y;
//
//                    mValuePaint.setTextAlign(Paint.Align.CENTER);
//
//                    // draw everything, depending on settings
//                    if (drawXInside && drawYInside) {
//
//                        drawValue(c, formatter, value, entry, 0, x, y, dataSet.getValueTextColor(j));
//
//                        if (j < data.getEntryCount() && entry.getLabel() != null) {
//                            drawEntryLabel(c, entry.getLabel(), x, y + lineHeight);
//                        }
//
//                    } else if (drawXInside) {
//                        if (j < data.getEntryCount() && entry.getLabel() != null) {
//                            drawEntryLabel(c, entry.getLabel(), x, y + lineHeight / 2f);
//                        }
//                    } else if (drawYInside) {
//
//                        drawValue(c, formatter, value, entry, 0, x, y + lineHeight / 2f, dataSet.getValueTextColor(j));
//                    }
//                }
//
//                if (entry.getIcon() != null && dataSet.isDrawIconsEnabled()) {
//
//                    Drawable icon = entry.getIcon();
//
//                    float x = (labelRadius + iconsOffset.y) * sliceXBase + center.x;
//                    float y = (labelRadius + iconsOffset.y) * sliceYBase + center.y;
//                    y += iconsOffset.x;
//
//                    Utils.drawImage(
//                            c,
//                            icon,
//                            (int) x,
//                            (int) y,
//                            icon.getIntrinsicWidth(),
//                            icon.getIntrinsicHeight());
//                }
//
//                xIndex--;
//            }
//
//            MPPointF.recycleInstance(iconsOffset);
//        }
//        MPPointF.recycleInstance(center);
//        c.restore();
//    }
}
