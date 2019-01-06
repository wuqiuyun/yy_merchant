package com.yiyue.store.util.easyutils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.EMLog;
import com.yiyue.store.R;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.EventBean;
import com.yiyue.store.model.vo.bean.InviteMessageBean;
import com.yiyue.store.module.im.chat.ChatActivity;
import com.yiyue.store.module.im.receiver.CallReceiver;
import com.yiyue.store.module.im.voicecall.VideoCallActivity;
import com.yiyue.store.module.im.voicecall.VoiceCallActivity;
import com.yiyue.store.util.DataCacheUtil;
import com.yiyue.store.util.NotificationUtils;
import com.yiyue.store.util.easyutils.emlisenter.MyContactListener;
import com.yiyue.store.util.easyutils.emlisenter.MyEMGroupChangeListener;
import com.yiyue.store.util.easyutils.emlisenter.MyEMMessageListener;
import com.yl.core.component.log.DLog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by zhangzz on 2018/9/17
 * 初始化环信的基本配置
 * 添加各种事件回调
 */
public class EasyHelper {
    private final static String TAG = "EasyHelper";
    public static EasyHelper instance;
    private EaseNotifier easeNotifier;
    private MyEMMessageListener mEMMessageListener;
    private Context mContext;
    private LocalBroadcastManager broadcastManager;

    public boolean isVoiceCalling;
    public boolean isVideoCalling;
    private CallReceiver callReceiver;
    private Ringtone rt;

    public synchronized static EasyHelper getInstance() {
        if (instance == null) {
            instance = new EasyHelper();
        }
        return instance;
    }

    public void init(Context context) {
        this.mContext = context;
        broadcastManager = LocalBroadcastManager.getInstance(mContext);
        initEMOptions();
        easeNotifier = new EaseNotifier(context);
        addNotfPrivoder();
        registerMessageListener();
        addCallRecv();
        addListeners();
    }

    private void addCallRecv() {
        IntentFilter callFilter = new IntentFilter(EMClient.getInstance().callManager().getIncomingCallBroadcastAction());
        if (callReceiver == null) {
            callReceiver = new CallReceiver();
        }

        //register incoming call receiver
        mContext.registerReceiver(callReceiver, callFilter);
    }

    private void addListeners() {
        EMClient.getInstance().groupManager().addGroupChangeListener(new MyEMGroupChangeListener() {
            @Override
            public void onUserRemoved(String groupId, String groupName) {
                //管理员将你踢出时的通知
                DLog.e("testGroup,onUserRemoved", groupName);
                String st3 = mContext.getString(R.string.Invite_you_to_remove_group_chat);
//                EMMessage msg = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
//                msg.setChatType(EMMessage.ChatType.GroupChat);
//                msg.setFrom(groupName);
//                msg.setTo(groupId);
//                msg.setMsgId(UUID.randomUUID().toString());
//                msg.addBody(new EMTextMessageBody(groupName + " " + st3));
//                msg.setStatus(EMMessage.Status.SUCCESS);
//                // save invitation as messages
//                EMClient.getInstance().chatManager().saveMessage(msg);
                // notify invitation message
//                easeNotifier.vibrateAndPlayTone(msg);
                EasyUtil.getEmManager().deleteConversation(groupId);

                NotificationUtils notificationUtils = new NotificationUtils(mContext);
                notificationUtils.sendNormalNotification("意约消息", groupName + " " + st3, groupId.hashCode());

                EventBus.getDefault().post(new EventBean.GroupChangeEvent(Constants.EVENT_GROUP_CHANGE));
                EventBus.getDefault().post(new EventBean.ConversationRefreshEvent(2));
            }
        });

        //监控app断线重连
//        EMClient.getInstance().addConnectionListener(new EMConnectionListener() {
//            @Override
//            public void onDisconnected(int error) {
//                EMLog.d(TAG, "onDisconnect" + error);
//
//                if (error == EMError.USER_REMOVED) {
//
//                } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
//
//                } else if (error == EMError.SERVER_SERVICE_RESTRICTED) {
//
//                } else if (error == EMError.USER_KICKED_BY_CHANGE_PASSWORD) {
//
//                } else if (error == EMError.USER_KICKED_BY_OTHER_DEVICE) {
//                    ImLoginManager.getInstance().loginIn(false);
//                }
//            }
//
//            @Override
//            public void onConnected() {
//
//            }
//        });
    }

    private void addNotfPrivoder() {
        //set options
        easeNotifier.setSettingsProvider(new EaseNotifier.EaseSettingsProvider() {

            @Override
            public boolean isSpeakerOpened() {
                return true;
            }

            @Override
            public boolean isMsgVibrateAllowed(EMMessage message) {
                return true;
            }

            @Override
            public boolean isMsgSoundAllowed(EMMessage message) {
                return true;
            }

            @Override
            public boolean isMsgNotifyAllowed(EMMessage message) {
                return true;
            }
        });


        easeNotifier.setNotificationInfoProvider(new EaseNotifier.EaseNotificationInfoProvider() {

            @Override
            public String getTitle(EMMessage message) {
                //you can update title here
                return null;
            }

            @Override
            public int getSmallIcon(EMMessage message) {
                //you can update icon here
                return 0;
            }

            @Override
            public String getDisplayedText(EMMessage message) {
                // be used on notification bar, different text according the message type.
                return message.getFrom() + ": " + "tik";
            }

            @Override
            public String getLatestText(EMMessage message, int fromUsersNum, int messageNum) {
                // here you can customize the text.
                // return fromUsersNum + "contacts send " + messageNum + "messages to you";
                return null;
            }

            @Override
            public Intent getLaunchIntent(EMMessage message) {
                // you can set what activity you want display when user click the notification
                Intent intent = new Intent(mContext, ChatActivity.class);
                // open calling activity if there is call
                if (isVideoCalling) {
                    intent = new Intent(mContext, VideoCallActivity.class);
                } else if (isVoiceCalling) {
                    intent = new Intent(mContext, VoiceCallActivity.class);
                } else {
                    EMMessage.ChatType chatType = message.getChatType();
                    if (chatType == EMMessage.ChatType.Chat) { // single chat message
                        intent.putExtra(Constants.EXTRA_USER_ID, message.getFrom());
                        intent.putExtra(Constants.EXTRA_CHAT_TYPE, Constants.CHATTYPE_SINGLE);
                    } else if (chatType == EMMessage.ChatType.GroupChat) { // single chat message
                        intent.putExtra(Constants.EXTRA_USER_ID, message.getFrom());
                        intent.putExtra(Constants.EXTRA_CHAT_TYPE, Constants.CHATTYPE_GROUP);
                    }
                }
                return intent;
            }
        });
    }

    protected void registerMessageListener() {
        mEMMessageListener = new MyEMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                if (rt == null) {
                    Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                    rt = RingtoneManager.getRingtone(mContext, uri);
                }

                rt.play();

                for (EMMessage message : messages) {
                    EMLog.d(TAG, "onMessageReceived id : " + message.getMsgId());
                    easeNotifier.notify(message);
                    broadcastManager.sendBroadcast(new Intent(Constants.ACTION_CONVERSION_COMING));
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                for (EMMessage message : messages) {
                    EMLog.d(TAG, "receive command message");
                    //get message body
                    EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
                    final String action = cmdMsgBody.action();//获取自定义action
                    //获取扩展属性 此处省略
                    //maybe you need get extension of your message
                    //message.getStringAttribute("");
                    EMLog.d(TAG, String.format("Command：action:%s,message:%s", action, message.toString()));
                }
            }

            @Override
            public void onMessageRecalled(List<EMMessage> messages) {
                EMLog.d(TAG, "onMessageRecalled:");
            }

            @Override
            public void onMessageChanged(EMMessage message, Object change) {
                EMLog.d(TAG, "change:");
                EMLog.d(TAG, "change:" + change);
            }
        };

        EasyUtil.getEmManager().addMessageListener(mEMMessageListener);
    }

    public void initEMOptions() {
        // 调用初始化方法初始化sdk
        EMClient.getInstance().init(mContext, initOptions());
        // 设置开启debug模式
        EMClient.getInstance().setDebugMode(true);

        EMClient.getInstance().contactManager().setContactListener(new contactListener(mContext));
    }

    /**
     * SDK初始化的一些配置
     * 关于 EMOptions 可以参考官方的 API 文档
     * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1chat_1_1_e_m_options.html
     */
    private EMOptions initOptions() {
        EMOptions options = new EMOptions();
        // 设置Appkey，如果配置文件已经配置，这里可以不用设置
        // options.setAppKey("lzan13#hxsdkdemo");
        // 设置自动登录
        options.setAutoLogin(true);
        // 设置是否需要发送已读回执
        options.setRequireAck(true);
        // 设置是否需要发送回执，
        options.setRequireDeliveryAck(true);
        // 设置是否根据服务器时间排序，默认是true
        options.setSortMessageByServerTime(false);
        // 收到好友申请是否自动同意，如果是自动同意就不会收到好友请求的回调，因为sdk会自动处理，默认为true
        options.setAcceptInvitationAlways(false);
        // 设置是否自动接收加群邀请，如果设置了当收到群邀请会自动同意加入
        options.setAutoAcceptGroupInvitation(true);
        // 设置（主动或被动）退出群组时，是否删除群聊聊天记录
        options.setDeleteMessagesAsExitGroup(false);
        // 设置是否允许聊天室的Owner 离开并删除聊天室的会话
        options.allowChatroomOwnerLeave(true);

        // 设置google GCM推送id，国内可以不用设置
        // options.setGCMNumber(MLConstants.ML_GCM_NUMBER);
        // 设置集成小米推送的appid和appkey
        // options.setMipushConfig(MLConstants.ML_MI_APP_ID, MLConstants.ML_MI_APP_KEY);

        return options;
    }

    public class contactListener extends MyContactListener {
        private Context mContext;

        public contactListener(Context context) {
            this.mContext = context;
        }

        @Override
        public void onContactAdded(String username) {
            //增加了联系人时回调此方法
            username.trim();
        }

        @Override
        public void onContactDeleted(String username) {
            //被删除时回调此方法
            username.trim();
        }

        @Override
        public void onContactInvited(String username, String reason) {
            //收到好友邀请
            List<InviteMessageBean> msgs = DataCacheUtil.getInstance(mContext).getInviteMsgList();
            if (msgs == null || msgs.size() < 1) {
                msgs = new ArrayList<>();
            } else {
                for (InviteMessageBean inviteMessageBean : msgs) {
                    if (inviteMessageBean.getGroupId() == null && inviteMessageBean.getFrom().equals(username)) {
                        //已添加为好友 此时提示好友存在
                    }
                }
            }
            // save invitation as message
            InviteMessageBean msg = new InviteMessageBean();
            msg.setFrom(username);
            msg.setTime(System.currentTimeMillis());
            msg.setReason(reason);
            // set invitation status
            msg.setStatus(InviteMessageBean.InviteMessageStatus.BEINVITEED);
            msgs.add(msg);
            DataCacheUtil.getInstance(mContext).saveInviteMsgList(msgs);

            broadcastManager.sendBroadcast(new Intent(Constants.ACTION_CONTACT_CHANAGED));
        }

        @Override
        public void onFriendRequestAccepted(String username) {
            //好友请求被同意
            username.trim();
        }

        @Override
        public void onFriendRequestDeclined(String username) {
            //好友请求被拒绝
            username.trim();
        }
    }

    /**
     * if ever logged in
     *
     * @return
     */
    public boolean isLoggedIn() {
        return EMClient.getInstance().isLoggedInBefore();
    }

    /**
     * logout
     *
     * @param unbindDeviceToken whether you need unbind your device token
     * @param callback          callback
     */
    public void logout(boolean unbindDeviceToken, final EMCallBack callback) {
        endCall();
        Log.d(TAG, "logout: " + unbindDeviceToken);

        EMClient.getInstance().logout(unbindDeviceToken, new EMCallBack() {

            @Override
            public void onSuccess() {
                Log.d(TAG, "logout: onSuccess");
                reset();
                if (callback != null) {
                    callback.onSuccess();
                }

            }

            @Override
            public void onProgress(int progress, String status) {
                if (callback != null) {
                    callback.onProgress(progress, status);
                }
            }

            @Override
            public void onError(int code, String error) {
                Log.d(TAG, "logout: onSuccess");
                reset();
                if (callback != null) {
                    callback.onError(code, error);
                }
            }
        });
    }

    void endCall() {
        try {
            EMClient.getInstance().callManager().endCall();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    synchronized void reset() {

    }
}
