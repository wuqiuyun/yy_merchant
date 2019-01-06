package com.yiyue.store.component.file;

/**
 * 文件上传、下载监听器
 * <p>
 * Created by zm on 2018/9/25.
 */
public interface OnFileProgressListener {
    void onLoading(long total, long progress);
}
