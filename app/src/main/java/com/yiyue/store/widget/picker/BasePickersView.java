package com.yiyue.store.widget.picker;

import android.content.Context;

import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;

/**
 * Created by zm on 2018/11/1.
 */
public abstract class BasePickersView {

    /**
     * 初始化时间选择器
     */
    public abstract TimePickerView init(Context context, OnTimeSelectListener onTimeSelectListener);


    /**
     * 显示
     */
    public abstract void show();
}
