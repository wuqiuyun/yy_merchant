package com.yl.core.component.appupdate.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.yl.core.R;
import com.yl.core.component.appupdate.activity.PermissionActivity;
import com.yl.core.component.appupdate.listener.OnButtonClickListener;
import com.yl.core.component.appupdate.manager.DownloadManager;
import com.yl.core.component.appupdate.service.DownloadService;
import com.yl.core.component.appupdate.utils.PermissionUtil;
import com.yl.core.component.appupdate.utils.ScreenUtil;


/**
 * Create by zm on 2018/10/30
 */

public class UpdateDialog extends Dialog implements View.OnClickListener {

    private Context context;
    private DownloadManager manager;
    private boolean forcedUpgrade;
    private Button update;
    private OnButtonClickListener buttonClickListener;
    private View mBtnCancleUpdate;

    public UpdateDialog(@NonNull Context context) {
        super(context, R.style.UpdateDialog);
        init(context);
    }

    /**
     * 初始化布局
     */
    private void init(Context context) {
        this.context = context;
        manager = DownloadManager.getInstance();
        forcedUpgrade = manager.getConfiguration().isForcedUpgrade();
        buttonClickListener = manager.getConfiguration().getOnButtonClickListener();
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_update, null);
        setContentView(view);
        setWindowSize(context);
        initView(view);
    }

    private void initView(View view) {
        View ibClose = view.findViewById(R.id.ib_close);
        mBtnCancleUpdate = view.findViewById(R.id.btn_cancle_update);
        View ibClose1 = view.findViewById(R.id.ib_close1);
        TextView title = view.findViewById(R.id.tv_title);
        TextView size = view.findViewById(R.id.tv_size);
        TextView description = view.findViewById(R.id.tv_description);
        update = view.findViewById(R.id.btn_update);
        View line = view.findViewById(R.id.line);
        update.setOnClickListener(this);
        ibClose.setOnClickListener(this);
        ibClose1.setOnClickListener(this);
        mBtnCancleUpdate.setOnClickListener(this);
        //强制升级
        if (forcedUpgrade) {
            ibClose1.setVisibility(View.GONE);
            setOnKeyListener(new OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    //屏蔽返回键
                    return keyCode == KeyEvent.KEYCODE_BACK;
                }
            });
            mBtnCancleUpdate.setVisibility(View.GONE);
        }
        //设置界面数据
        if (!TextUtils.isEmpty(manager.getApkVersionName())) {
            title.setText(String.format("发现新版v%s可以下载啦！", manager.getApkVersionName()));
        }
        if (!TextUtils.isEmpty(manager.getApkSize())) {
            size.setText(String.format("新版本大小：%sM", manager.getApkSize()));
            size.setVisibility(View.VISIBLE);
        }
        description.setText(manager.getApkDescription());
    }

    private void setWindowSize(Context context) {
        Window dialogWindow = this.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = (int) (ScreenUtil.getWith(context) * 0.7f);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.CENTER;
        dialogWindow.setAttributes(lp);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ib_close1) {
            if (!forcedUpgrade) {
                dismiss();
            }
            //回调点击事件
            if (buttonClickListener != null) {
                buttonClickListener.onButtonClick(OnButtonClickListener.CANCEL);
            }
        } else if (id == R.id.btn_update) {
            if (forcedUpgrade) {
                update.setEnabled(false);
                update.setText("正在后台下载新版本...");
            } else {
                dismiss();
            }
            //回调点击事件
            if (buttonClickListener != null) {
                buttonClickListener.onButtonClick(OnButtonClickListener.UPDATE);
            }
            //检查权限
            if (!PermissionUtil.checkStoragePermission(context)) {
                //没有权限,去申请权限
                context.startActivity(new Intent(context, PermissionActivity.class));
                return;
            }
            context.startService(new Intent(context, DownloadService.class));
        }else if (id == R.id.btn_cancle_update) {
            dismiss();
        }
    }
}