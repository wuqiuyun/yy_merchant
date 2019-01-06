package com.yiyue.store.util.easyutils.emlisenter;

import com.hyphenate.EMCallBack;

/**
 * Created by zhangzz on 2018/9/14
 * EM 通用的回调函数接口 这里做抽象使用
 */
public abstract class MyCallBackImpl implements EMCallBack {
    @Override
    public void onSuccess() {
         //程序执行成功时执行回调函数。
    }

    @Override
    public void onError(int code, String error) {
         //发生错误时调用的回调函数
    }

    @Override
    public void onProgress(int progress, String status) {
         //刷新进度的回调函数
    }
}
