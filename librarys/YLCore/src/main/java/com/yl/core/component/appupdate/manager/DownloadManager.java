package com.yl.core.component.appupdate.manager;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import com.yl.core.component.appupdate.activity.PermissionActivity;
import com.yl.core.component.appupdate.config.UpdateConfiguration;
import com.yl.core.component.appupdate.dialog.UpdateDialog;
import com.yl.core.component.appupdate.service.DownloadService;
import com.yl.core.component.appupdate.utils.ApkUtil;
import com.yl.core.component.appupdate.utils.Constant;
import com.yl.core.component.appupdate.utils.PermissionUtil;
import com.yl.core.component.log.DLog;


/**
 * Create by zm on 2018/10/30
 */

public class DownloadManager {

    private static final String TAG = "DownloadManager";

    /**
     * 上下文
     */
    private Context context;
    /**
     * 要更新apk的下载地址
     */
    private String apkUrl = "";
    /**
     * apk下载好的名字 .apk 结尾
     */
    private String apkName = "";
    /**
     * apk 下载存放的位置
     */
    private String downloadPath;
    /**
     * 是否提示用户 "当前已是最新版本"
     * <p>
     * {@link #download()}
     */
    private boolean showNewerToast = false;
    /**
     * 通知栏的图标 资源路径
     */
    private int smallIcon = -1;
    /**
     * 整个库的一些配置属性，可以从这里配置
     */
    private UpdateConfiguration configuration;
    /**
     * 要更新apk的versionCode
     */
    private int apkVersionCode = 1;
    /**
     * 显示给用户的版本号
     */
    private String apkVersionName = "";
    /**
     * 更新描述
     */
    private String apkDescription = "";
    /**
     * 安装包大小 单位 M
     */
    private String apkSize = "";
    /**
     * 兼容Android N 添加uri权限 authorities
     */
    private String authorities = "";

    private static DownloadManager manager;

    public static DownloadManager getInstance() {
        if (manager == null) {
            synchronized (DownloadManager.class) {
                if (manager == null) {
                    manager = new DownloadManager();
                }
            }
        }
        return manager;
    }

    private DownloadManager() {
    }
    public DownloadManager init(Context context) {
        this.context = context;
        return this;
    }

    public String getApkUrl() {
        return apkUrl;
    }

    public DownloadManager setApkUrl(String apkUrl) {
        this.apkUrl = apkUrl;
        return this;
    }

    public int getApkVersionCode() {
        return apkVersionCode;
    }

    public DownloadManager setApkVersionCode(int apkVersionCode) {
        this.apkVersionCode = apkVersionCode;
        return this;
    }

    public String getApkName() {
        return apkName;
    }

    public DownloadManager setApkName(String apkName) {
        this.apkName = apkName;
        return this;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public DownloadManager setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
        return this;
    }

    public DownloadManager setShowNewerToast(boolean showNewerToast) {
        this.showNewerToast = showNewerToast;
        return this;
    }

    public boolean isShowNewerToast() {
        return showNewerToast;
    }

    public int getSmallIcon() {
        return smallIcon;
    }

    public DownloadManager setSmallIcon(int smallIcon) {
        this.smallIcon = smallIcon;
        return this;
    }

    public DownloadManager setConfiguration(UpdateConfiguration configuration) {
        this.configuration = configuration;
        return this;
    }

    public UpdateConfiguration getConfiguration() {
        return configuration;
    }

    public String getApkVersionName() {
        return apkVersionName;
    }

    public DownloadManager setApkVersionName(String apkVersionName) {
        this.apkVersionName = apkVersionName;
        return this;
    }

    public String getApkDescription() {
        return apkDescription;
    }

    public DownloadManager setApkDescription(String apkDescription) {
        this.apkDescription = apkDescription;
        return this;
    }

    public String getApkSize() {
        return apkSize;
    }

    public DownloadManager setApkSize(String apkSize) {
        this.apkSize = apkSize;
        return this;
    }

    public String getAuthorities() {
        return authorities;
    }

    public DownloadManager setAuthorities(String authorities) {
        this.authorities = authorities;
        return this;
    }

    /**
     * 开始下载
     */
    public void download() {
        if (checkParams()) {
            //检查权限
            if (!PermissionUtil.checkStoragePermission(context)) {
                //没有权限,去申请权限
                context.startActivity(new Intent(context, PermissionActivity.class));
                return;
            }
            context.startService(new Intent(context, DownloadService.class));
        } else {
            //对版本进行判断，是否显示升级对话框
            if (apkVersionCode > ApkUtil.getVersionCode(context)) {
                UpdateDialog dialog = new UpdateDialog(context);
                dialog.show();
            } else {
                if (showNewerToast) {
                    Toast.makeText(context, "当前已是最新版本!", Toast.LENGTH_SHORT).show();
                }
                DLog.e(TAG, "当前已是最新版本");
            }
        }
    }

    /**
     * 检查参数
     */
    private boolean checkParams() {
        if (TextUtils.isEmpty(apkUrl)) {
            throw new RuntimeException("apkUrl can not be empty!");
        }
        if (TextUtils.isEmpty(apkName)) {
            throw new RuntimeException("apkName can not be empty!");
        }
        if (!apkName.endsWith(Constant.APK_SUFFIX)) {
            throw new RuntimeException("apkName must endsWith .apk!");
        }
        if (TextUtils.isEmpty(downloadPath)) {
            throw new RuntimeException("downloadPath can not be empty!");
        }
        if (smallIcon == -1) {
            throw new RuntimeException("smallIcon can not be empty!");
        }
        //如果用户没有进行配置，则使用默认的配置
        if (configuration == null) {
            configuration = new UpdateConfiguration();
        }
        //设置了 VersionCode 则库中进行对话框逻辑处理
        if (apkVersionCode > 1) {
            if (TextUtils.isEmpty(apkDescription)) {
                throw new RuntimeException("apkDescription can not be empty!");
            }
            return false;
        }
        //如果设置了小于的versionCode 你不是在写bug就是脑子瓦塌拉
        if (apkVersionCode < 1) {
            throw new RuntimeException("apkVersionCode can not be < 1");
        }
        return true;
    }

    /**
     * 释放资源
     */
    public void release() {
        manager = null;
    }
}
