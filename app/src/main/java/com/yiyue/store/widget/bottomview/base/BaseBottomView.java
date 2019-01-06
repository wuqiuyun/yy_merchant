package com.yiyue.store.widget.bottomview.base;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.yiyue.store.R;

/**
 * 底部弹出框
 */
public class BaseBottomView {

    protected Context mContext;
    protected View rootView;
    private int theme;

    private Dialog bv;
    private int animationStyle;
    private boolean isTop = false;

    public BaseBottomView(Context context, int theme, int resource) {
        this.mContext = context;
        this.theme = theme;
        this.rootView = View.inflate(context, resource, null);
        setAnimation(R.style.BottomToTopAnim);
    }

    public BaseBottomView(Context context, int theme, View view) {
        this.mContext = context;
        this.theme = theme;
        this.rootView = view;
        setAnimation(R.style.BottomToTopAnim);
    }

    public void showBottomView(boolean CanceledOnTouchOutside) {
        if (isShow()) {
            return;
        }
        if (this.bv == null) {
            if (this.theme == 0) {
                this.bv = new Dialog(this.mContext);
            } else {
                this.bv = new Dialog(this.mContext, this.theme);
            }

            this.bv.setCanceledOnTouchOutside(CanceledOnTouchOutside);
            this.bv.getWindow().requestFeature(1);
            this.bv.setContentView(this.rootView);
            Window wm = this.bv.getWindow();
            WindowManager m = wm.getWindowManager();
            Display d = m.getDefaultDisplay();
            WindowManager.LayoutParams p = wm.getAttributes();
            p.width = d.getWidth() * 1;
            if (this.isTop) {
                p.gravity = 48;
            } else {
                p.gravity = 80;
            }

            if (this.animationStyle != 0) {
                wm.setWindowAnimations(this.animationStyle);
            }

            wm.setAttributes(p);
        }
        this.bv.show();
    }

    public void setTopIfNecessary() {
        this.isTop = true;
    }

    public void setAnimation(int animationStyle) {
        this.animationStyle = animationStyle;
    }

    public View getView() {
        return this.rootView;
    }

    private boolean isShow() {
        if (this.bv == null) {
            return false;
        }
        return this.bv.isShowing();
    }

    public void dismissBottomView() {
        if (this.bv != null) {
            this.bv.dismiss();
        }

    }
}
