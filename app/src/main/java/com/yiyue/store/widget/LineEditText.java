package com.yiyue.store.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.widget.TextView;

import com.yiyue.store.R;

import java.lang.reflect.Field;

/**
 * Created by zhangzz on 2018/10/8
 * 下划线粗细及距离的Edit
 */
public class LineEditText extends AppCompatEditText {
    private Paint mPaint; // 画笔
    private int cursor; // 光标

    // 分割线
    private int lineColor_click, lineColor_unclick, textColor;// 点击时 & 未点击颜色 字体颜色
    private int lineColor;
    private int linePosition;//越大离文字越近

    private boolean haveLine = true;

    public LineEditText(Context context) {
        super(context);
    }

    public LineEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public LineEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.LineEditText);
        haveLine = typedArray.getBoolean(R.styleable.LineEditText_have_line, true);

        /**
         * 初始化光标（颜色 & 粗细）
         */
        cursor = typedArray.getResourceId(R.styleable.LineEditText_cursor, R.drawable.cursor);
        try {
            // 获取光标属性
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            // 传入资源ID
            f.set(this, cursor);

        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         * 初始化分割线（颜色、粗细、位置）
         */
        mPaint = new Paint();
        mPaint.setStrokeWidth(2.0f); // 分割线粗细

        // 设置分割线颜色
        int lineColorClickDefault = context.getResources().getColor(R.color.color_28C8B5);
        int lineColorunClickDefault = context.getResources().getColor(R.color.color_888888);
        int textColorDefault = context.getResources().getColor(R.color.color_666666);
        lineColor_click = typedArray.getColor(R.styleable.LineEditText_lineColor_click, lineColorClickDefault);
        lineColor_unclick = typedArray.getColor(R.styleable.LineEditText_lineColor_unclick, lineColorunClickDefault);
        textColor = typedArray.getColor(R.styleable.LineEditText_lineColor_unclick, textColorDefault);
        lineColor = lineColor_unclick;

        mPaint.setColor(lineColor_unclick);

        // 分割线位置 越大离文字越近
        linePosition = typedArray.getInteger(R.styleable.LineEditText_linePosition, 20);
        setBackground(null);
    }

    /**
     * 绘制分割线
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(lineColor);
        setTextColor(textColor);
        if (haveLine) {
            int x = this.getScrollX(); // 获取延伸后的长度
            int w = this.getMeasuredWidth(); // 获取控件长度

            canvas.drawLine(0, this.getMeasuredHeight() - linePosition, w + x,
                    this.getMeasuredHeight() - linePosition, mPaint);
        }
    }
}
