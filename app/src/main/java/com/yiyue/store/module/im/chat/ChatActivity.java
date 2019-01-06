package com.yiyue.store.module.im.chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.yiyue.store.R;
import com.yiyue.store.base.BaseAppCompatActivity;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.AddrInfoBean;
import com.yiyue.store.model.vo.bean.ConversationBean;
import com.yiyue.store.model.vo.bean.EventBean;
import com.yiyue.store.model.vo.bean.daobean.GroupBean;
import com.yiyue.store.module.im.imlogin.ImLoginManager;
import com.yiyue.store.util.FragmentManagerUtil;
import com.yiyue.store.util.PhoneUtil;
import com.yiyue.store.util.easyutils.EasyUtil;
import com.yiyue.store.util.easyutils.emlisenter.MyCallBackImpl;
import com.yl.core.component.log.DLog;
import com.yl.core.util.StatusBarUtil;
import com.zhihu.matisse.Matisse;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.concurrent.Callable;

import bolts.Task;

/**
 * Created by zhangzz on 2018/10/9
 * 聊天页面
 */

public class ChatActivity extends BaseAppCompatActivity {
    private FragmentManagerUtil fragmentManagerUtil;

    private ChatFragment chatFragment;

    public static void startFromConversationActivity(Context context, ConversationBean conversationBean) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(Constants.EXTRA_USER_ID, conversationBean.getImusername());
        intent.putExtra(Constants.EXTRA_CHAT_IS_FROM_CONVER, true);
        //区分群聊还是单聊传值
        if (conversationBean.getChatType().equals(EMMessage.ChatType.GroupChat)) {
            intent.putExtra(Constants.EXTRA_CHAT_TYPE, Constants.CHATTYPE_GROUP);
            intent.putExtra(Constants.EXTRA_GROUPBEAN_ID, conversationBean.getFriendId() + "");
            intent.putExtra(Constants.EXTRA_GROUP_NAME, conversationBean.getNickname());
            intent.putExtra("groupPath", conversationBean.getPath());
        } else {
            intent.putExtra(Constants.EXTRA_CHAT_TYPE, Constants.CHATTYPE_SINGLE);
            intent.putExtra(Constants.EXTRA_FRIEND_ID, conversationBean.getFriendId() + "");
            intent.putExtra(Constants.EXTRA_FRIEND_NAME, TextUtils.isEmpty(conversationBean.getRemarks()) ? conversationBean.getNickname() : conversationBean.getRemarks());
            intent.putExtra(Constants.EXTRA_FRIEND_HEAD_PATH, conversationBean.getPath());
            intent.putExtra(Constants.EXTRA_FRIEND_IS_FRIEND, conversationBean.isFriend());
        }
        if (conversationBean != null) {
            if (!checkLoginStatus(context, conversationBean.getImusername())) {
                 loginIm(context, intent);
            } else {
                startConverActivity(context, intent);
            }
        } else {
            startConverActivity(context, intent);
        }
    }

    private static void loginIm(Context context, Intent intent) {
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
                        startConverActivity(context, intent);
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
                                errorMsg = "请检查网络";
                                break;
                            // 无效的用户名 101
                            case EMError.INVALID_USER_NAME:
                                // 无效的密码 102
                            case EMError.INVALID_PASSWORD:
                                // 用户认证失败，用户名或密码错误 202
                            case EMError.USER_AUTHENTICATION_FAILED:
                                // 用户不存在 204
                            case EMError.USER_NOT_FOUND:
                                // 无法访问到服务器 300
                            case EMError.SERVER_NOT_REACHABLE:
                                // 等待服务器响应超时 301
                            case EMError.SERVER_TIMEOUT:
                                // 服务器繁忙 302
                            case EMError.SERVER_BUSY:
                                // 未知 Server 异常 303 一般断网会出现这个错误
                            case EMError.SERVER_UNKNOWN_ERROR:
                            default:
                                errorMsg = "聊天账号异常";
                                break;
                        }
                        DLog.d("huanxin", errorMsg);
                        ToastUtils.shortToast(errorMsg);

                        return null;
                    }
                }, Task.UI_THREAD_EXECUTOR);
            }
        });
    }

    private static void startConverActivity(Context context, Intent intent) {
        context.startActivity(intent);
    }


    public static void startFromGroupActivity(Context context, GroupBean groupBean) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(Constants.EXTRA_USER_ID, groupBean.getImgroup());
        intent.putExtra(Constants.EXTRA_GROUP_NAME, groupBean.getName());
        intent.putExtra(Constants.EXTRA_GROUP, groupBean);
        intent.putExtra(Constants.EXTRA_CHAT_TYPE, Constants.CHATTYPE_GROUP);
        if (TextUtils.isEmpty(groupBean.getImgroup())) {
            ToastUtils.shortToast("群聊数据异常, 请刷新");
            return;
        }

        if (!EMClient.getInstance().isConnected()) {
            loginIm(context, intent);
        } else {
            startConverActivity(context, intent);
        }
    }

    public static void startFromFriendInfoActivity(Context context, String mChatId, String friendId, String name, String path) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(Constants.EXTRA_USER_ID, mChatId);
        intent.putExtra(Constants.EXTRA_CHAT_TYPE, Constants.CHATTYPE_SINGLE);
        intent.putExtra(Constants.EXTRA_FRIEND_ID, friendId);
        intent.putExtra(Constants.EXTRA_FRIEND_NAME, name);
        intent.putExtra(Constants.EXTRA_FRIEND_HEAD_PATH, path);
        if (!checkLoginStatus(context, mChatId)) {
            loginIm(context, intent);
        } else {
            startConverActivity(context, intent);
        }
    }

    /**
     * 咨询聊天入口
     * 需要传 咨询对象的环信id，用户id，昵称，头像路径
     *
     * @param context
     * @param mChatId 环信id
     * @param userid  用户id
     * @param name    昵称
     * @param path    头像路径
     */
    public static void startFromZIxunActivity(Context context, String mChatId, String userid, String name, String path) {
        Intent intent = new Intent(context, ChatActivity.class);

        intent.putExtra(Constants.EXTRA_USER_ID, mChatId);
        intent.putExtra(Constants.EXTRA_CHAT_TYPE, Constants.CHATTYPE_SINGLE);
        intent.putExtra(Constants.EXTRA_FRIEND_ID, userid);
        intent.putExtra(Constants.EXTRA_FRIEND_NAME, name);
        intent.putExtra(Constants.EXTRA_FRIEND_HEAD_PATH, path);
        intent.putExtra(Constants.EXTRA_FRIEND_IS_FRIEND, false);//默认是非好友，在聊天页面会进行好友判定，如果是好友会修改状态
        intent.putExtra(Constants.IM_MESSAGE_KEY, Constants.IM_MESSAGE_ORDER_DETAIL_TYPE);//资讯类聊天标志

        if (!checkLoginStatus(context, mChatId)) {
            loginIm(context, intent);
        } else {
            startConverActivity(context, intent);
        }


    }

    private static boolean checkLoginStatus(Context context, String imusername) {
        if (!EMClient.getInstance().isConnected()) {
            DLog.e(context.getString(R.string.im_outline));
            return false;
        }
        if (TextUtils.isEmpty(imusername)) {
            DLog.e("聊天ID不存在，请刷新好友数据");
            return false;
        }
        String currUsername = EasyUtil.getEmManager().getCurrentUser();//获取环信本地登录账号id

        if (TextUtils.isEmpty(currUsername)) {//对异常登录环信 以及和自己聊天的情况进行处理 2018.11.16
            DLog.e(context.getString(R.string.im_outline));
            return false;
        } else {
            if (currUsername.equals(imusername)) {
                DLog.e("不能和自己聊天");
                return false;
            }
        }

        return true;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void init() {
        StatusBarUtil.setLightMode(this);
        fragmentManagerUtil = new FragmentManagerUtil(this, R.id.layout_frame);
        chatFragment = new ChatFragment();
        chatFragment.setArguments(getIntent().getExtras());

        fragmentManagerUtil.chAddFrag(chatFragment, "", false);
    }

    @Override
    public void onBackPressed() {
        if (!chatFragment.emojiKeyboard.interceptBackPress()) {
            if (chatFragment.isChatFromConver) {
                EventBus.getDefault().post(new EventBean.ConversationRefreshEvent(1));
            } else {
                EventBus.getDefault().post(new EventBean.ConversationRefreshEvent(2));
            }
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constants.REQUEST_CODE_PIC_FROM_PHOTO://本地相册返回
                    if (data != null) {
                        List<Uri> mSelected = Matisse.obtainResult(data);

                        if (mSelected != null && mSelected.size() > 0) {
                            chatFragment.sendPicByUri(mSelected.get(0));
                        }
                    }
                    break;

                case Constants.REQUEST_CODE_MAP://地图
                    if (data != null) {
                        AddrInfoBean addrInfoBean = data.getParcelableExtra("data");
                        if (addrInfoBean != null) {
                            chatFragment.sendLocationMessage(addrInfoBean.getLat(), addrInfoBean.getLon(), addrInfoBean.getAddrDetail());
                        } else {
                            chatFragment.sendLocationMessage(39.884916, 116.34008, "北京");
                        }
                    }

                    break;
                case PhoneUtil.REQUESTCODE_SYS_CAMERA: // 相机
                    if (!TextUtils.isEmpty(chatFragment.imagePath)) {
                        chatFragment.sendImageMessage(chatFragment.imagePath);
                    }
                    break;

            }
        }
    }
}
