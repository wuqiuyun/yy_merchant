package com.yl.core.component.appupdate.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.yl.core.component.appupdate.service.DownloadService;
import com.yl.core.component.appupdate.utils.Constant;
import com.yl.core.component.appupdate.utils.PermissionUtil;
import com.yl.core.component.log.DLog;

/**
 * Create by zm on 2018/10/30
 */

public class PermissionActivity extends AppCompatActivity {

    private static final String TAG = "PermissionActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PermissionUtil.requestPermission(this, Constant.PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.PERMISSION_REQUEST_CODE) {
            if (grantResults.length == 0) {
                // do something...
                DLog.e(TAG, "权限请求回调：grantResults.length = 0");
            } else {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //授予了权限
                    download();
                } else {
                    //拒绝了
                    Toast.makeText(PermissionActivity.this, "请允许使用[存储空间]权限!", Toast.LENGTH_LONG).show();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            //用户勾选了不在询问
                            goToAppDetailPage(PermissionActivity.this);
                        }
                    }
                }
            }
            finish();
        }
    }

    /**
     * 开始下载
     */
    private void download() {
        startService(new Intent(this, DownloadService.class));
    }

    /**
     * 跳转至详情界面
     */
    private void goToAppDetailPage(Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        context.startActivity(intent);
    }
}
