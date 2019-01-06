package com.yiyue.store.widget.piechart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;


import com.yiyue.store.R;
import com.yiyue.store.model.vo.bean.MonthSumBean;
import com.yiyue.store.util.ColorUtil;
import com.yiyue.store.util.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinyan on 2018/12/28.
 */
public class PieChart extends View {
    private Paint mPaint;//画笔
    private RectF rectF;//矩形
    private List<MonthSumBean.ClassifyInBean> mPieChartDatas=new ArrayList<>();
    private int w;
    private int h;
    private int lineMargin=25;
    private int lineWidht=200;//连接线长
    private float lineHean=5;//连接线圆点
    private float textX=0;//文字X坐标
    private float textY=0;//文字Y坐标
    private float mTextAngle;//上一个角度


    public PieChart(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
//        mPaint.setStrokeWidth(5);
        //设置画笔默认颜色
        mPaint.setColor(Color.WHITE);
        //设置画笔模式：填充
        mPaint.setStyle(Paint.Style.FILL);
        //文字大小
        mPaint.setTextSize(25);
        //初始化区域
        rectF = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(w / 2, h / 2);             //将画布坐标原点移到中心位置
        float currentStartAngle = 0;                //起始角度
        float r = (float) (Math.min(w, h) / 4);     //饼状图半径(取宽高里最小的值)
        rectF.set(-r, -r, r, r);                    //设置将要用来画扇形的矩形的轮廓
        if (mPieChartDatas.size()==0){
            mPaint.setTextSize(40);
            mPaint.setColor(Color.GRAY);
            canvas.drawText("暂无此月份统计", -100, 0, mPaint);    //绘制文字
            return;
        }
        mPaint.setTextSize(25);
        for (int i = 0; i < mPieChartDatas.size(); i++) {
            MonthSumBean.ClassifyInBean  pieChartData= mPieChartDatas.get(i);
            mPaint.setColor(pieChartData.getColor());
            //绘制扇形(通过绘制圆弧)
            canvas.drawArc(rectF, currentStartAngle, pieChartData.getAngle(), true, mPaint);
            //绘制连接线
            //计算连接线角度
            float textAngle = currentStartAngle + pieChartData.getAngle() / 2;
            float x = (float) ((r+15)  * Math.cos(textAngle * Math.PI / 180));    //计算文字位置坐标
            float y = (float) ((r+15)  * Math.sin(textAngle * Math.PI / 180));
            if (!TextUtils.isEmpty(pieChartData.getName())){
                drawLine(canvas,pieChartData.getName(),x,y, textAngle);
            }
            currentStartAngle += pieChartData.getAngle();     //改变起始角度
        }
        //绘制中间圆形
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(0,0,r/2,mPaint);
    }

    private void drawText(Canvas canvas) {
        //绘制扇形上文字
//            float textAngle = currentStartAngle + pieChartData.angle / 2;    //计算文字位置角度
//            mPaint.setColor(Color.BLACK);
//            float x = (float) (r / 2 * Math.cos(textAngle * Math.PI / 180));    //计算文字位置坐标
//            float y = (float) (r / 2 * Math.sin(textAngle * Math.PI / 180));
//            mPaint.setColor(Color.YELLOW);        //文字颜色
//            canvas.drawText(pieChartData.lable, x, y, mPaint);    //绘制文字
    }

    /**
     *
     * @param canvas 画布
     * @param startX X开始位置
     * @param startY Y开始位置
     * @param angle   角度，决定连接线折向
     */
    private void drawLine(Canvas canvas,String lable,float startX,float startY,float angle) {
        //绘制连接线
        float stopX,stopY;
        //绘制线初始圆点
        canvas.drawCircle(startX,startY,lineHean,mPaint);
        if (angle<=45||angle>315){
            stopX=startX+lineWidht;
            stopY=startY;
            canvas.drawLine(startX,startY,stopX,stopY, mPaint);
            textX=startX+lineMargin;
            textY=startY+lineMargin;
            mPaint.setColor(Color.GRAY);
            canvas.drawText(lable,textX, textY, mPaint);    //绘制文字
        }else if(45<angle&&angle<=90){
            if (angle>=72){
                stopX=startX+lineMargin;
                stopY=startY+lineMargin*2;
            }else {
                stopX=startX+lineMargin;
                stopY=startY+lineMargin;
            }
            canvas.drawLine(startX,startY,stopX,stopY, mPaint);
            //第二段
            canvas.drawLine(stopX,stopY,stopX+lineWidht,stopY, mPaint);
            textX=stopX;
            textY=stopY+lineMargin;
            mPaint.setColor(Color.GRAY);

            canvas.drawText(lable,textX, textY, mPaint);    //绘制文字
        }else if(90<angle&&angle<=126){
            if (angle<=108){
                stopX=startX-lineMargin;
                stopY=startY+lineMargin*2;
            }else {
                stopX=startX-lineMargin;
                stopY=startY+lineMargin;
            }
            canvas.drawLine(startX,startY,stopX,stopY, mPaint);
            //第二段
            canvas.drawLine(stopX,stopY,stopX-lineWidht,stopY, mPaint);
            textX=stopX-lineWidht+lineMargin;
            textY=stopY+lineMargin;
            mPaint.setColor(Color.GRAY);
            canvas.drawText(lable,textX, textY, mPaint);    //绘制文字
        }else if(126<angle&&angle<=225){
            //绘制线初始圆点
            canvas.drawCircle(startX,startY,lineHean,mPaint);
            stopX=startX-lineWidht;
            stopY=startY;
            canvas.drawLine(startX,startY,stopX,stopY, mPaint);
            mPaint.setColor(Color.GRAY);
            canvas.drawText(lable,stopX, stopY+lineMargin, mPaint);    //绘制文字
        }else if(225<angle&&angle<=270){
            //绘制线初始圆点
            canvas.drawCircle(startX,startY,lineHean,mPaint);
            stopX=startX-lineMargin;
            stopY=startY-lineMargin;
            canvas.drawLine(startX,startY,stopX,stopY, mPaint);
            //第二段
            canvas.drawLine(stopX,stopY,stopX-lineWidht,stopY, mPaint);
            mPaint.setColor(Color.GRAY);
            canvas.drawText(lable,stopX-lineWidht, stopY+lineMargin, mPaint);    //绘制文字
        }else if(270<angle&&angle<=315){
            //绘制线初始圆点
            canvas.drawCircle(startX,startY,lineHean,mPaint);
            stopX=startX+lineMargin;
            stopY=startY-lineMargin;
            canvas.drawLine(startX,startY,stopX,stopY, mPaint);
            //第二段
            canvas.drawLine(stopX,stopY,stopX+lineWidht,stopY, mPaint);
            mPaint.setColor(Color.GRAY);
            canvas.drawText(lable,stopX+lineMargin, stopY+lineMargin, mPaint);    //绘制文字
        }
        mTextAngle=angle;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w=w;
        this.h=h;
        lineWidht=w/5;
        if (lineWidht>150)lineWidht=150;
    }

    public void upData(List<MonthSumBean.ClassifyInBean> pieChartDatas) {
        if (pieChartDatas==null)return;
        double temp=0;//记录不够0.05比例
        int num=0;//记录大于0.05个数
        for (int i = 0; i < pieChartDatas.size(); i++) {
            MonthSumBean.ClassifyInBean classifyInBean=pieChartDatas.get(i);
            //为了美观，最小5%
            if (classifyInBean.getPercent()<0.05){
                temp+=0.05-classifyInBean.getPercent();
                classifyInBean.setPercent(0.05);
            }else {
                num++;
            }
            switch (classifyInBean.getInType()){
                case 1:
                    classifyInBean.setColor(ColorUtil.getColor(R.color.color_43BBD1));//注册奖金
                    break;
                case 2:
                    classifyInBean.setColor(ColorUtil.getColor(R.color.color_4AB35C));//推广佣金
                    break;
                case 3:
                    classifyInBean.setColor(ColorUtil.getColor(R.color.color_E5342B));//红包
                    break;
                case 4:
                    classifyInBean.setColor(ColorUtil.getColor(R.color.color_F1B34E));//服务收入
                    break;
                case 5:
                    classifyInBean.setColor(ColorUtil.getColor(R.color.color_F3427D));//转账
                    break;
                case 6:
                    classifyInBean.setColor(ColorUtil.getColor(R.color.color_FE7902));//退款
                    break;
                default:
                    classifyInBean.setColor(ColorUtil.getColor(R.color.color_BCD15A));//其他
                    break;
            }
        }
        //重新计算比例
        for (MonthSumBean.ClassifyInBean classifyInBean :pieChartDatas) {
            float angle;
            if (classifyInBean.getPercent()==0.05){
                 angle = (float) (0.05* 360); //对应的角度
            }else {
                if (classifyInBean.getPercent()-temp/num>0.05){
                    classifyInBean.setPercent(classifyInBean.getPercent()-temp/num);
                }
                 angle = (float) (classifyInBean.getPercent() * 360); //对应的角度
            }
            classifyInBean.setAngle(angle);
        }
        mPieChartDatas=pieChartDatas;
        invalidate();
    }

    public void clear() {
        mPieChartDatas.clear();
        invalidate();
    }
}
