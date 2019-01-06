package com.yiyue.store.module.im.imlogin;

import android.text.TextUtils;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.util.easyutils.EasyHelper;
import com.yiyue.store.util.easyutils.EasyUtil;
import com.yiyue.store.util.easyutils.emlisenter.MyCallBackImpl;
import com.yl.core.component.log.DLog;

import java.util.concurrent.Callable;

import bolts.Task;

/**
 * Created by zhangzz on 2018/10/16
 */
public class ImLoginManager {
    private static ImLoginManager imInstance;

    private ImLoginManager() {
    }

    public synchronized static ImLoginManager getInstance() {
        if (imInstance == null) {
            imInstance = new ImLoginManager();
        }
        return imInstance;
    }

    /**
     * 登录方法
     */
    public void loginIn(boolean isShowToast) {
        if (AccountManager.getInstance() == null || AccountManager.getInstance().getAccount() == null)
            return;
        String username = AccountManager.getInstance().getAccount().getImusername();
        String password = AccountManager.getInstance().getAccount().getImpassword();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            DLog.e("MBLogLogin", "环信的账号、密码获取为null");
            return;
        }
        EasyUtil.getEmManager().login(username, password, new MyCallBackImpl() {
            /**
             * 登陆成功的回调
             */
            @Override
            public void onSuccess() {
                Task.call(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        // 加载所有会话到内存
                        EMClient.getInstance().chatManager().loadAllConversations();
                        // 加载所有群组到内存，如果使用了群组的话
                        EMClient.getInstance().groupManager().loadAllGroups();

                        if (isShowToast) {
                            ToastUtils.shortToast("账号已重新登录成功");
                        }
                        DLog.d("huanxin", "环信登录成功");
                        return null;
                    }
                }, Task.UI_THREAD_EXECUTOR);
            }

            /**
             * 登陆错误的回调
             * @param i
             * @param s
             */
            @Override
            public void onError(final int i, final String s) {
                Task.call(new Callable<Void>() {
                    @Override
                    public Void call() throws Exception {
                        /**
                         * 关于错误码可以参考官方api详细说明
                         * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1_e_m_error.html
                         */

                        String errorMsg;
                        switch (i) {
                            // 网络异常 2
                            case EMError.NETWORK_ERROR:
                                errorMsg = "网络错误 code: " + i + ", message:" + s;
                                break;
                            // 无效的用户名 101
                            case EMError.INVALID_USER_NAME:
                                errorMsg = "无效的用户名 code: : " + i + ", message:" + s;
                                break;
                            // 无效的密码 102
                            case EMError.INVALID_PASSWORD:
                                errorMsg = "无效的密码 code: : " + i + ", message:" + s;
                                break;
                            // 用户认证失败，用户名或密码错误 202
                            case EMError.USER_AUTHENTICATION_FAILED:
                                errorMsg = "用户认证失败 code: : " + i + ", message:" + s;
                                break;
                            // 用户不存在 204
                            case EMError.USER_NOT_FOUND:
                                errorMsg = "用户不存在 code: : " + i + ", message:" + s;
                                break;
                            // 无法访问到服务器 300
                            case EMError.SERVER_NOT_REACHABLE:
                                errorMsg = "无法访问到服务器 code: : " + i + ", message:" + s;
                                break;
                            // 等待服务器响应超时 301
                            case EMError.SERVER_TIMEOUT:
                                errorMsg = "等待服务器响应超时 code: : " + i + ", message:" + s;
                                break;
                            // 服务器繁忙 302
                            case EMError.SERVER_BUSY:
                                errorMsg = "服务器繁忙 code: : " + i + ", message:" + s;
                                break;
                            // 未知 Server 异常 303 一般断网会出现这个错误
                            case EMError.SERVER_UNKNOWN_ERROR:
                                errorMsg = "未知的服务器异常 code: : " + i + ", message:" + s;
                                break;
                            default:
                                errorMsg = "ml_sign_in_failed code: : " + i + ", message:" + s;
                                break;
                        }
                        DLog.d("huanxin", errorMsg);
                        if (isShowToast){
                            ToastUtils.shortToast(errorMsg);
                        }
                        return null;
                    }
                }, Task.UI_THREAD_EXECUTOR);
            }
        });
    }

    /**
     * 登录环信 登录前判断是否退出之前账号 如没有  则先做登出环信操作 再登录环信
     */
    public void loginIM() {
        if (EMClient.getInstance().isConnected()) {
            EasyHelper.getInstance().logout(true, new EMCallBack() {

                @Override
                public void onSuccess() {
                    ImLoginManager.getInstance().loginIn(false);
                }

                @Override
                public void onProgress(int progress, String status) {

                }

                @Override
                public void onError(int code, String message) {
                    DLog.d("聊天登录失败!");//
                }
            });
        } else {
            ImLoginManager.getInstance().loginIn(false);
        }
    }


    public void isLoginIM() {
        if (!EMClient.getInstance().isConnected()) {
            ImLoginManager.getInstance().loginIn(false);
        }
    }
}
