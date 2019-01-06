package com.yiyue.store.widget.bottomview;

import android.content.Context;

import com.yiyue.store.widget.bottomview.base.BaseBottomView;

/**
 * Created by zm on 2018/10/13.
 */
public class BottomViewFactory {

    public enum Type{
        /**
         * 头像
         */
        Avatar,
        /**
         * 地图
         */
        Map
    }

    public static final BaseBottomView create(Context context, Type type) {
        switch (type) {
            case Avatar:
                return new SelectPhotoView(context);
            case Map:
                return new MapSelectView(context);
            default:
                break;
        }
        return null;
    }
}
