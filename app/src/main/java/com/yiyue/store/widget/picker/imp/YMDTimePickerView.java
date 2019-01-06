package com.yiyue.store.widget.picker.imp;

import com.yiyue.store.widget.picker.BaseTimePickerView;

import java.util.Calendar;

/**
 * 年月日时间选择器
 * Created by zm on 2018/11/1.
 */
public class YMDTimePickerView extends BaseTimePickerView {


    @Override
    protected Calendar getEndData() {
        return null;
    }

    @Override
    protected Calendar getStartData() {
        return null;
    }

    @Override
    protected boolean[] getType() {
        return new boolean[]{true, true, true, false, false, false};
    }

    @Override
    protected String[] getLabel() {
        return new String[]{"年", "月", "日", "", "", ""};
    }
}
