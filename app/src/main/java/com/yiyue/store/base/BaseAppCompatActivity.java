package com.yiyue.store.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.yl.core.util.StatusBarUtil;
import com.yiyue.store.R;
import com.yiyue.store.helper.AppManager;
import com.yiyue.store.util.ColorUtil;

/**
 * Created by zm on 2018/9/27.
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity{

    protected Bundle savedInstanceState;
    protected ViewDataBinding viewDataBinding;

    @LayoutRes
    protected abstract int getLayoutResId();
    protected abstract void init();

    /**
     * start Activity
     * @param context
     * @param targetClass
     */
    public static void startActivity(Context context, Class targetClass) {
        startActivity(context, targetClass, null);
    }

    /**
     * start Activity
     * @param context
     * @param targetClass
     */
    public static void startActivity(Context context, Class targetClass, Bundle bundle) {
        Intent intent = new Intent();
        if (bundle != null) intent.putExtras(bundle);
        intent.setClass(context, targetClass);
        if (context instanceof Activity) {
            //do nothing
        } else {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        this.savedInstanceState = savedInstanceState;
        viewDataBinding = DataBindingUtil.setContentView(this, getLayoutResId());
        setStatusBar();
        init();
    }

    protected void setStatusBar() {
        StatusBarUtil.setColorNoTranslucent(this, ColorUtil.getColor(R.color.colorPrimary));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
        if (viewDataBinding != null) {
            viewDataBinding.unbind();
        }
    }
}
