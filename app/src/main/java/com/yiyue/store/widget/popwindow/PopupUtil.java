package com.yiyue.store.widget.popwindow;

import android.content.Context;
import android.view.View;

public class PopupUtil extends BasePopup<PopupUtil> {

    private OnViewListener mOnViewListener;

    public static PopupUtil create() {
        return new PopupUtil();
    }

    public static PopupUtil create(Context context) {
        return new PopupUtil(context);
    }

    public PopupUtil() {

    }

    public PopupUtil(Context context) {
        setContext(context);
    }

    @Override
    protected void initAttributes() {

    }

    @Override
    protected void initViews(View view, PopupUtil popup) {
        if (mOnViewListener != null) {
            mOnViewListener.initViews(view, popup);
        }
    }

    public PopupUtil setOnViewListener(OnViewListener listener) {
        this.mOnViewListener = listener;
        return this;
    }

    public interface OnViewListener {
        void initViews(View view, PopupUtil popup);
    }
}
