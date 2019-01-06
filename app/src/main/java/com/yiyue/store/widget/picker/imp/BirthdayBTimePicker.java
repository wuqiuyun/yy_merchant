package com.yiyue.store.widget.picker.imp;

import com.yiyue.store.widget.picker.BaseTimePickerView;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by zm on 2018/11/1.
 */
public class BirthdayBTimePicker extends BaseTimePickerView{
    @Override
    protected Calendar getEndData() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        return c;
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
