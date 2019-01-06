package com.yiyue.store.widget.timedialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

/**
 * @author   LUZI
 * @version  创建时间：2016年5月10日 下午1:08:05
 * @说明
 */
public class CustomDialog extends Dialog
{
    public CustomDialog(Context context, int layout, int style) {
        this(context, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT, layout, style,
                Gravity.CENTER);
    }

    public CustomDialog(Context context, int width, int height, int layout,
                        int style, int gravity, int anim) {
        super(context, style);

        setContentView(layout);
        // set window params
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;// (int) (width * density);
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;// (int) (height * density);
        params.gravity = gravity;
        window.setAttributes(params);
        window.setWindowAnimations(anim);
        //window.setWindowAnimations(R.style.pop_anim_style);
    }

    public CustomDialog(Context context, int width, int height, int layout,
                        int style, int gravity) {
        this(context, width, height, layout, style, gravity, 0);
        // setCanceledOnTouchOutside(true);
    }
}
