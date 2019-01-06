package com.yl.core.component.appupdate.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

import com.yl.core.YLCore;
import com.yl.core.component.appupdate.base.BaseHttpDownloadManager;
import com.yl.core.component.appupdate.listener.OnDownloadListener;
import com.yl.core.component.appupdate.manager.DownloadManager;
import com.yl.core.component.appupdate.manager.HttpDownloadManager;
import com.yl.core.component.appupdate.utils.ApkUtil;
import com.yl.core.component.appupdate.utils.FileUtil;
import com.yl.core.component.appupdate.utils.NotificationUtil;
import com.yl.core.component.log.DLog;
import com.yl.core.util.ClientUtil;

import java.io.File;

/**
 * Create by zm on 2018/10/30
 */

public final class DownloadService extends Service implements OnDownloadListener {

    private static final String TAG = "DownloadService";
    private int smallIcon;
    private String apkUrl;
    private String apkName;
    private String downloadPath;
    private String authorities;
    private OnDownloadListener listener;
    private boolean showNotification;
    private boolean jumpInstallPage;
    private int lastProgress;
    /**
     * 是否正在下载，防止重复点击
     */
    private boolean downloading;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null == intent) {
            return START_STICKY;
        }
        init();
        return super.onStartCommand(intent, flags, startId);
    }


    private void init() {
        apkUrl = DownloadManager.getInstance().getApkUrl();
        apkName = DownloadManager.getInstance().getApkName();
        downloadPath = DownloadManager.getInstance().getDownloadPath();
        smallIcon = DownloadManager.getInstance().getSmallIcon();
        authorities = DownloadManager.getInstance().getAuthorities();
        //如果没有设置则为包名
        if (TextUtils.isEmpty(authorities)) {
            authorities = ClientUtil.getAppPackageName(YLCore.sContext) + ".fileProvider";
        }
        //创建apk文件存储文件夹
        FileUtil.createDirDirectory(downloadPath);

        listener = DownloadManager.getInstance().getConfiguration().getOnDownloadListener();
        showNotification = DownloadManager.getInstance().getConfiguration().isShowNotification();
        jumpInstallPage = DownloadManager.getInstance().getConfiguration().isJumpInstallPage();
        //获取app通知开关是否打开
        boolean enable = NotificationUtil.notificationEnable(this);
        DLog.e(TAG, enable ? "应用的通知栏开关状态：已打开" : "应用的通知栏开关状态：已关闭");
        download();
    }

    /**
     * 获取下载管理者
     */
    private synchronized void download() {
        if (downloading) {
            DLog.e(TAG, "download: 当前正在下载，请务重复下载！");
            return;
        }
        BaseHttpDownloadManager manager = DownloadManager.getInstance().getConfiguration().getHttpManager();
        //如果用户自己定义了下载过程
        if (manager != null) {
            manager.download(apkUrl, apkName, this);
        } else {
            //使用自己的下载
            new HttpDownloadManager(this, downloadPath).download(apkUrl, apkName, this);
        }
        downloading = true;
    }


    @Override
    public void start() {
        if (showNotification) {
            handler.sendEmptyMessage(0);
            NotificationUtil.showNotification(this, smallIcon, "开始下载", "可稍后查看下载进度");
        }
        if (listener != null) {
            listener.start();
        }
    }

    @Override
    public void downloading(int max, int progress) {
        DLog.e(TAG, "max: " + max + " --- progress: " + progress);
        if (showNotification) {
            //优化通知栏更新，减少通知栏更新次数
            int curr = (int) (progress / (double) max * 100.0);
            if (curr != lastProgress) {
                lastProgress = curr;
                NotificationUtil.showProgressNotification(this, smallIcon, "正在下载新版本", "", max, progress);
            }
        }
        if (listener != null) {
            listener.downloading(max, progress);
        }
    }

    @Override
    public void done(File apk) {
        downloading = false;
        if (showNotification) {
            NotificationUtil.showDoneNotification(this, smallIcon, "下载完成", "点击进行安装", authorities, apk);
        }
        //如果用户设置了回调 则先处理用户的事件 在执行自己的
        if (listener != null) {
            listener.done(apk);
        }
        if (jumpInstallPage) {
            ApkUtil.installApk(this, authorities, apk);
        }
        releaseResources();
    }

    @Override
    public void error(Exception e) {
        DLog.e(TAG, "error: " + e);
        downloading = false;
        if (showNotification) {
            NotificationUtil.showErrorNotification(this, smallIcon, "下载出错", "点击继续下载");
        }
        if (listener != null) {
            listener.error(e);
        }
    }

    /**
     * 下载完成释放资源
     */

    private void releaseResources() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
        stopSelf();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(DownloadService.this, "正在后台下载新版本...", Toast.LENGTH_SHORT).show();

        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
