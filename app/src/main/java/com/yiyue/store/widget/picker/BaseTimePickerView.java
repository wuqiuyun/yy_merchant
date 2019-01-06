package com.yiyue.store.widget.picker;

import android.content.Context;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.yiyue.store.R;
import com.yiyue.store.util.ColorUtil;

import java.util.Calendar;

/**
 * Created by zm on 2018/11/1.
 */
public abstract class BaseTimePickerView extends BasePickersView{
    private TimePickerView mTimePickerView;

    @Override
    public TimePickerView init(Context context, OnTimeSelectListener onTimeSelectListener) {
        mTimePickerView = new TimePickerBuilder(context, onTimeSelectListener)
                //年月日时分秒 的显示与否，不设置则默认全部显示
                .setType(getType())
                .setLabel(getLabel()[0], getLabel()[1],getLabel()[2],getLabel()[3],getLabel()[4],getLabel()[5])
                .isCenterLabel(false)
                // 分割线的颜色
                .setDividerColor(ColorUtil.getColor(R.color.color_CCCCCC))
                // 字体大小
                .setContentTextSize(16)
                // 起始、结束时间
                .setRangDate(getStartData(), getEndData())
                .setLineSpacingMultiplier(2.0f)
                // 设置取消、确定按钮大小
                .setSubCalSize(16)
                .setBackgroundId(0x00FFFFFF)
                .setDecorView(null)
                .build();
        return mTimePickerView;
    }

    @Override
    public void show() {
        if (mTimePickerView != null) {
            mTimePickerView.show();
        }
    }

    /**
     * 结束时间
     * @return
     */
    protected abstract Calendar getEndData();

    /**
     * 起始时间
     * @return
     */
    protected abstract Calendar getStartData();

    /**
     * 注意 数组大小为6
     * @return
     */
    protected abstract boolean[] getType();

    /**
     * 注意 数组大小为6
     * @return
     */
    protected abstract String[] getLabel();
}
