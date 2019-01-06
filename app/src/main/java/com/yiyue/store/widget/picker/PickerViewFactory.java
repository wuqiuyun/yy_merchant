package com.yiyue.store.widget.picker;

/**
 * Created by zm on 2018/11/1.
 */
public class PickerViewFactory {

    public static <T extends BasePickersView> T createPickersView(Class<T> clz){
        BasePickersView waves = null;
        try {
            waves = (BasePickersView) Class.forName(clz.getName()).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (T)waves;
    }
}
