package com.yiyue.store.widget.picker.imp;

import com.yiyue.store.widget.picker.BaseTimePickerView;

import java.util.Calendar;

/**
 * Created by zm on 2018/11/1.
 */
public class YMDHMTimePickerView extends BaseTimePickerView{

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
        return new boolean[]{true, true, true, true, true, false};
    }

    @Override
    protected String[] getLabel() {
        return new String[]{"年", "月", "日", "时", "分", ""};
    }
}
