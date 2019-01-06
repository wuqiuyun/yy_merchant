package com.yiyue.store.util.easyutils;

/**
 * Created by zhangzz on 2018/9/14
 * 不同第三方库的实例获取 目前仅有环信的api代理类
 */
public class EasyUtil {
    /* 使用入口：获取EasyUI操作管理类实例
     * @return
     */
    public static EMInterface getEmManager() {
        if (EasyManagerSet.umManager == null) {
            EMInterfaceImpl.initEMInstance();
        }
        return EasyManagerSet.umManager;
    }

    public final static class EasyManagerSet {
        private static EMInterface umManager;

        public static void setUmManager(EMInterface manager) {
            EasyManagerSet.umManager = manager;
        }
    }
}
