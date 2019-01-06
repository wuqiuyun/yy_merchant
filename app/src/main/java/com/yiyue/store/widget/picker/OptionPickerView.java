package com.yiyue.store.widget.picker;

import android.content.Context;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.yiyue.store.R;
import com.yiyue.store.util.ColorUtil;

import java.util.ArrayList;

/**
 * Created by zm on 2018/11/1.
 */
public class OptionPickerView {
    private OptionsPickerView mOptionsPickerView;

    public OptionPickerView(Context context, OnOptionsSelectListener onOptionsSelectListener) {
        mOptionsPickerView = new OptionsPickerBuilder(context, onOptionsSelectListener)
                // 字体大小
                .setContentTextSize(16)
                // 设置分割线颜色
                .setDividerColor(ColorUtil.getColor(R.color.color_CCCCCC))
                .setContentTextSize(16)
                .setLineSpacingMultiplier(2.0f)
                .setBackgroundId(0x00FFFFFF)
                .build();
    }


    public <P>void set(ArrayList<P> arrayList1) {
        mOptionsPickerView.setPicker(arrayList1);
    }


    public <P,C>void set(ArrayList<P> arrayList1, ArrayList<ArrayList<C>> arrayLists2) {
        mOptionsPickerView.setPicker(arrayList1, arrayLists2);
    }

    public <P, C, A> void set(ArrayList<P> arrayList1, ArrayList<ArrayList<C>> arrayLists2, ArrayList<ArrayList<ArrayList<A>>> arrayLists3) {
        mOptionsPickerView.setPicker(arrayList1, arrayLists2, arrayLists3);
    }

    public void show() {
        if (mOptionsPickerView != null) {
            mOptionsPickerView.show();
        }
    }
}
