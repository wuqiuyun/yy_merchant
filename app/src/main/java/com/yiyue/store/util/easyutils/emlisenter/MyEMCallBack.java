package com.yiyue.store.util.easyutils.emlisenter;

import com.hyphenate.EMCallBack;

/**
 * Created by zhangzz on 2018/9/21
 * 为消息设置回调
 */
public class MyEMCallBack implements EMCallBack {
    @Override
    public void onSuccess() {
        // 消息发送成功，打印下日志，正常操作应该去刷新ui
    }

    @Override
    public void onError(int code, String error) {
        // 消息发送失败，打印下失败的信息，正常操作应该去刷新ui
    }

    @Override
    public void onProgress(int progress, String status) {
        // 消息发送进度，一般只有在发送图片和文件等消息才会有回调，txt不回调
    }
}
