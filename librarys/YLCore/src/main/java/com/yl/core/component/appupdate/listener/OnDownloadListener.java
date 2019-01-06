package com.yl.core.component.appupdate.listener;

import java.io.File;

/**
 * Create by zm on 2018/10/30
 */

public interface OnDownloadListener {

    /**
     * 开始下载
     */
    void start();

    /**
     * 下载中
     *
     * @param max      总进度
     * @param progress 当前进度
     */
    void downloading(int max, int progress);

    /**
     * 下载完成
     *
     * @param apk 下载好的apk
     */
    void done(File apk);

    /**
     * 下载出错
     *
     * @param e 错误信息
     */
    void error(Exception e);
}
