package com.yl.core.component.appupdate.base;

import com.yl.core.component.appupdate.listener.OnDownloadListener;

/**
 * Create by zm on 2018/10/30
 */

public abstract class BaseHttpDownloadManager {

    /**
     * 下载apk
     *
     * @param apkUrl   apk下载地址
     * @param apkName  apk名字
     * @param listener 回调
     */
    public abstract void download(String apkUrl, String apkName, OnDownloadListener listener);
}
