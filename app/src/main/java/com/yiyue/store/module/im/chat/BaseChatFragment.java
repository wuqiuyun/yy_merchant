package com.yiyue.store.module.im.chat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.Type;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yiyue.store.R;
import com.yiyue.store.base.fragment.BaseMvpFragment;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.FragmentChatBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.ChatAdapterHolderbean;
import com.yiyue.store.model.vo.bean.ChatAdapterItemTypeBean;
import com.yiyue.store.model.vo.bean.EventBean;
import com.yiyue.store.model.vo.bean.SelfDefinedInfoBean;
import com.yiyue.store.model.vo.bean.daobean.GroupBean;
import com.yiyue.store.model.vo.bean.daobean.UserFriendsBean;
import com.yiyue.store.module.im.chat.chatview.EmojiKeyboard;
import com.yiyue.store.module.im.voicecall.VideoCallActivity;
import com.yiyue.store.module.im.voicecall.VoiceCallActivity;
import com.yiyue.store.util.FilePathUtil;
import com.yiyue.store.util.PhoneUtil;
import com.yiyue.store.util.easyutils.EaseNotifier;
import com.yiyue.store.util.easyutils.EasyUtil;
import com.yiyue.store.util.easyutils.emlisenter.MyEMMessageListener;
import com.yl.core.component.log.DLog;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangzz on 2018/10/9
 * 聊天fragment基类
 */
@CreatePresenter(ChatPresenter.class)
public abstract class BaseChatFragment extends BaseMvpFragment<ChatView, ChatPresenter> implements ChatView {
    private final String TAG = "BaseChatFragment";
    protected FragmentChatBinding mBinding;
    protected ChatAdapter2 mAdapter;
    private SmartRefreshLayout refreshLayout;

    protected MyEMMessageListener myEMMessageListener;//通过注册消息监听来接收消息。
    protected EaseNotifier easeNotifier;
    protected EMConversation mConversation; // 当前会话对象

    protected String mChatId;   // 当前聊天的 ID
    protected int mChatType;//当前聊天类型
    protected String mGroupName;//当前聊天的群名
    protected String friendId;//好友的userid
    protected GroupBean mGroup;//当前选择的群实体
    protected String groupId;//群列表bean中的id
    protected String currUsername;
    protected boolean isFriend = true;//单聊时  是否为好友 默认是好友 区别展示UI\
    protected ArrayList<ChatAdapterItemTypeBean> models = new ArrayList<>();
    protected UserFriendsBean friendUserBean;//保存好友头像使用的本地查询的bean
    protected GroupBean groupBean;

    protected EmojiKeyboard emojiKeyboard;
    private final MyHandler mHandler = new MyHandler(this);//Handler，主要用于刷新UI操作
    protected String imagePath;//拍照路径

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBean.VoiceCallEventBean event) {
        if (event != null) {
            if (Constants.REQUEST_CODE_VOICE_CALL == event.getTag()) {
                refreshList();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBean.RedPacketMsgUpdate event) {
        if (event != null) {
            if (0 == event.getTag() && event.getChatNoFriendBean() != null && event.getMessage() != null) {
                sendRedPacketMsg(event.getChatNoFriendBean(), event.getMessage(), event.getMsgType());
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBean.InviteMsgUpdate event) {
        if (event != null) {
            if (0 == event.getTag()) {
                refreshList();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        // 移除消息监听
        EasyUtil.getEmManager().removeMessageListener(myEMMessageListener);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chat;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        mBinding = (FragmentChatBinding) viewDataBinding;
        //表情包和键盘切换适配
        emojiKeyboard = new EmojiKeyboard(getActivity(), mBinding.includeBottomChat.chatEtSendmessage, mBinding.layoutBottom, mBinding.layoutBottomMojo, mBinding.includeBottomChat.chatBtnMore, mBinding.includeBottomChat.chatRlFace, mBinding.recylerView);

        currUsername = EasyUtil.getEmManager().getCurrentUser();//获取环信本地登录账号id
//        initRefreshLoadLayout();

    }


    protected abstract void initEvent();

    /**
     * 初始化会话对象，并且根据需要加载更多消息
     */
    public void initConversation() {
        mConversation = EasyUtil.getEmManager().getConversation(mChatId, null, true);//第三个表示如果会话不存在是否创建
        if (mConversation != null) {
            mConversation.markAllMessagesAsRead();        // 设置当前会话未读数为 0
            int count = mConversation.getAllMessages().size();
            if (count < mConversation.getAllMsgCount() && count < 20) {
                // 获取已经在列表中的最上边的一条消息id
                String msgId = mConversation.getAllMessages().get(0).getMsgId();
                // 分页加载更多消息，需要传递已经加载的消息的最上边一条消息的id，以及需要加载的消息的条数
                mConversation.loadMoreMsgFromDB(msgId, 20 - count);
            }
        }
        // 打开聊天界面获取最后一条消息内容并显示
//        if (mConversation.getAllMessages().size() > 0) {
//            List<EMMessage> emMessages = mConversation.getAllMessages();
//            makeData(emMessages);
//        }
    }

    /**
     * 语音通话页面后刷新list，默认不需要刷新
     */
    protected void refreshList() {
        if (mConversation != null) {
            List<EMMessage> var = mConversation.getAllMessages();
            mConversation.markAllMessagesAsRead();
            models.clear();
            makeData(var);
        }
    }

    private static class MyHandler extends Handler {

        private WeakReference<BaseChatFragment> mBaseChatFragment;//当内存已发生GC的时候就会回收

        public MyHandler(BaseChatFragment fragment) {
            this.mBaseChatFragment = new WeakReference<BaseChatFragment>(fragment);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BaseChatFragment baseChatFragment = mBaseChatFragment.get();
            if (baseChatFragment == null || baseChatFragment.isDetached()) {
                return;
            }
            switch (msg.what) {
                case 0:
                    EMMessage message = (EMMessage) msg.obj;
                    baseChatFragment.makeMessage(message);
                    baseChatFragment.mAdapter.setNewData(baseChatFragment.models);
                    baseChatFragment.mBinding.recylerView.scrollToPosition(baseChatFragment.models.size() - 1);
                    break;
            }
        }
    }

    /**
     * 照相机拍照发送图片
     */
    protected void selectPicFromCamera() {
        new RxPermissions(this)
                .request(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                )
                .subscribe(granted -> {
                    if (granted) {
                        imagePath = FilePathUtil.getCacheImagePick();
                        PhoneUtil.takePickByCamera(getActivity(), imagePath);
                    } else {
                        ToastUtils.shortToast(getString(R.string.permissions_phone_state));
                    }
                });
    }

    /**
     * 本地图片选择
     */
    protected void selectPicFromPhoto() {
        new RxPermissions(this)
                .request(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .subscribe(granted -> {
                    if (granted) { //相册选择照片的方法：
                        Matisse.from(getActivity())
                                .choose(MimeType.ofImage()) // 选择 mime 的类型
                                .countable(true)
//                                .capture(true)  //是否可以拍照
//                                .captureStrategy(//参数1 true表示拍照存储在共有目录，false表示存储在私有目录；参数2与 AndroidManifest中authorities值相同，用于适配7.0系统 必须设置
//                                        new CaptureStrategy(true, "com.yiyue.store.fileProvider"))
                                .maxSelectable(1) // 图片选择的最多数量
                                .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.dp_120)) //设置列宽
                                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                                .thumbnailScale(0.85f) // 缩略图的比例
                                .imageEngine(new GlideEngine()) // 使用的图片加载引擎
                                .forResult(Constants.REQUEST_CODE_PIC_FROM_PHOTO);
                    } else {
                        ToastUtils.shortToast(getString(R.string.permissions_phone_state));
                    }
                });
    }

    /**
     * 开启语音通话
     */
    protected void startVoiceCall() {
        new RxPermissions(this)
                .request(Manifest.permission.RECORD_AUDIO
                )
                .subscribe(granted -> {
                    if (granted) { //相册选择照片的方法：
                        if (!EMClient.getInstance().isConnected()) {
                            Toast.makeText(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(new Intent(getActivity(), VoiceCallActivity.class).putExtra("username", mChatId)
                                    .putExtra("isComingCall", false));
                        }
                    } else {
                        ToastUtils.shortToast(getString(R.string.permissions_phone_state));
                    }
                });

    }

    /**
     * 开启视频通话
     */
    protected void startVideoCall() {
        new RxPermissions(this)
                .request(Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA
                )
                .subscribe(granted -> {
                    if (granted) { //相册选择照片的方法：
                        if (!EMClient.getInstance().isConnected()) {
                            Toast.makeText(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(new Intent(getActivity(), VideoCallActivity.class).putExtra("username", mChatId)
                                    .putExtra("isComingCall", false));
                        }
                    } else {
                        ToastUtils.shortToast(getString(R.string.permissions_phone_state));
                    }
                });

    }

    /**
     * 发送语音消息 本地新增adapter中的一条数据
     */
    protected void sendVoiceMessage(String filePath, int length) {
        EMMessage message = EasyUtil.getEmManager().createVoiceSendMessage(filePath, length, mChatId);

        addItemModel(ChatAdapterItemTypeBean.CHAT_SEND_VOICE, length + "s", message);
        sendMessage(message);
    }

    /**
     * 发送普通消息 本地新增adapter中的一条数据
     */
    protected void sendNormalMessage() {
        String content = mBinding.includeBottomChat.chatEtSendmessage.getText().toString().trim();
        if (!TextUtils.isEmpty(content)) {
            mBinding.includeBottomChat.chatEtSendmessage.setText("");
            // 创建一条新消息，第一个参数为消息内容，第二个为接受者username:为对方用户或者群聊的id
            EMMessage message = EasyUtil.getEmManager().createTxtSendMessage(content, mChatId);

            addItemModel(ChatAdapterItemTypeBean.CHAT_SEND, content, message);
            message.setAttribute(Constants.MESSAGE_ATTR_IS_VOICE_CALL, false);
            sendMessage(message);
        } else {
            showToast("请输入发送内容");
        }
    }

    /**
     * 发送位置
     *
     * @param latitude
     * @param longitude
     * @param locationAddress
     */
    protected void sendLocationMessage(double latitude, double longitude, String locationAddress) {
        EMMessage message = EasyUtil.getEmManager().createLocationSendMessage(latitude, longitude, locationAddress, mChatId);
        message.setAttribute("locationUrl", "");

        addItemModel(ChatAdapterItemTypeBean.CHAT_SEND_LOCATION, "", message);
        sendMessage(message);
    }


    /**
     * 图库选择发送图片
     *
     * @param selectedImage
     */
    protected void sendPicByUri(Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (picturePath == null || picturePath.equals("null")) {
                Toast toast = Toast.makeText(getActivity(), R.string.cant_find_pictures, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            sendImageMessage(picturePath);
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                Toast toast = Toast.makeText(getActivity(), R.string.cant_find_pictures, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;

            }
            sendImageMessage(file.getAbsolutePath());
        }

    }

    /**
     * send file
     *
     * @param uri
     */
    protected void sendFileByUri(Uri uri) {
        String filePath = null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;

            try {
                cursor = getActivity().getContentResolver().query(uri, filePathColumn, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            filePath = uri.getPath();
        }
        if (filePath == null) {
            return;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            Toast.makeText(getActivity(), R.string.File_does_not_exist, Toast.LENGTH_SHORT).show();
            return;
        }
        sendFileMessage(filePath);
    }

    /**
     * 发送图片 本地新增adapter中的一条数据
     *
     * @param imagePath
     */
    protected void sendImageMessage(String imagePath) {
        EMMessage message = EasyUtil.getEmManager().createImageSendMessage(imagePath, false, mChatId);
        addItemModel(ChatAdapterItemTypeBean.CHAT_SEND_PIC, "", message);
        sendMessage(message);
    }

    protected void sendFileMessage(String filePath) {
        EMMessage message = EasyUtil.getEmManager().createFileSendMessage(filePath, mChatId);
        sendMessage(message);
    }


    /**
     * 发送红包消息 本地新增adapter中的一条数据
     *
     * @param
     */
    protected void sendRedPacketMsg(SelfDefinedInfoBean chatNoFriendBean, EMMessage message, int chatType) {
        ChatAdapterHolderbean model = new ChatAdapterHolderbean();
        model.setIcon(AccountManager.getInstance().getAccount() == null ? "" : AccountManager.getInstance().getAccount().getHeadImg());
        model.setChatNoFriendBean(chatNoFriendBean);
        model.setMessage(message);
        models.add(new ChatAdapterItemTypeBean(chatType, model));
        mAdapter.setNewData(models);

        mBinding.recylerView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    /**
     * 根据不同类型的消息UI整理adpater需要的类型model
     *
     * @param emMessages
     */
    private void makeData(List<EMMessage> emMessages) {
        if (emMessages != null && emMessages.size() > 0) {
            for (EMMessage message : emMessages) {
                if (message != null) {
                    makeMessage(message);
                }
            }
            mAdapter.setNewData(models);
            mBinding.recylerView.scrollToPosition(models.size() - 1);
        }
    }

    String selfMsg;
    SelfDefinedInfoBean noFriendBean;

    /**
     * 适配不同的消息展示UI
     *
     * @param message
     */
    private void makeMessage(EMMessage message) {
        selfMsg = message.getStringAttribute(Constants.IM_MESSAGE_ORDER_ADDMONEY_KEY, null);
        noFriendBean = new Gson().fromJson(selfMsg, SelfDefinedInfoBean.class);

        if (Type.TXT == message.getType()) {
            EMTextMessageBody body = (EMTextMessageBody) message.getBody();
            if (body != null) {
                //是否是语音聊天的消息
                if (message.getBooleanAttribute(Constants.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
                    if (!message.getFrom().equals(currUsername)) {
                        addModel(ChatAdapterItemTypeBean.CHAT_RECV_VOICE_CALL, body.getMessage(), message);
                    } else {
                        addModel(ChatAdapterItemTypeBean.CHAT_SEND_VOICE_CALL, body.getMessage(), message);
                    }
                } else if (message.getBooleanAttribute(Constants.MESSAGE_ATTR_IS_VIDEO_CALL, false)) {
                    if (!message.getFrom().equals(currUsername)) {
                        addModel(ChatAdapterItemTypeBean.CHAT_RECV_VIDEO_CALL, body.getMessage(), message);
                    } else {
                        addModel(ChatAdapterItemTypeBean.CHAT_SEND_VIDEO_CALL, body.getMessage(), message);
                    }
                } else {
                    try {
                        makeTextMessage(message, body);
                    } catch (Exception e) {
                        DLog.d("json数据异常了，有可能是IOS端发过来的自定义消息，String和int转换异常");
                    }
                }
            }
        } else if (Type.IMAGE == message.getType()) {
            EMImageMessageBody body = (EMImageMessageBody) message.getBody();
            if (body != null) {
                if (!message.getFrom().equals(currUsername)) {
                    addModel(ChatAdapterItemTypeBean.CHAT_RECV_PIC, body.getFileName(), message);
                } else {
                    addModel(ChatAdapterItemTypeBean.CHAT_SEND_PIC, body.getFileName(), message);
                }
            }
        } else if (Type.VOICE == message.getType()) {
            EMVoiceMessageBody body = (EMVoiceMessageBody) message.getBody();
            if (body != null) {
                if (!message.getFrom().equals(currUsername)) {
                    addModel(ChatAdapterItemTypeBean.CHAT_RECV_VOICE, body.getLength() + "s", message);
                } else {
                    addModel(ChatAdapterItemTypeBean.CHAT_SEND_VOICE, body.getLength() + "s", message);
                }
            }
        } else if (Type.LOCATION == message.getType()) {
            if (!message.getFrom().equals(currUsername)) {
                addModel(ChatAdapterItemTypeBean.CHAT_RECV_LOCATION, "", message);
            } else {
                addModel(ChatAdapterItemTypeBean.CHAT_SEND_LOCATION, "", message);
            }
        }
    }

    /**
     * 区别是否为自定义消息类型 展示TextMessage
     * * 1.洗剪吹 2.洗吹 3.烫发 4.染发 5.护理 6.接发 7.洗色 8.拉直 9.单项套餐 10.多项套餐
     * 100.正常消息(非特殊弹框) 101.分享门店 102.分享作品 103.分享美发师 110.推荐好友 111.申请加入门店
     *
     * @param message
     * @param body
     */
    private void makeTextMessage(EMMessage message, EMTextMessageBody body) {
        if (!message.getFrom().equals(currUsername)) {//不是本人的消息
            //此处的太容易try用来判断是否存在这个getStringAttribute自定义参数，没有会是异常返回，此时展示正常消息
            if (TextUtils.isEmpty(selfMsg)) {
                addModel(ChatAdapterItemTypeBean.CHAT_RECV, body.getMessage(), message);
            } else {
                if (noFriendBean != null) {
                    switch (noFriendBean.getDefinedMsgType()) {
                        case 401:
                            addModel(ChatAdapterItemTypeBean.CHAT_RECV_GROUP_JOIN, body.getMessage(), message, noFriendBean);
                            break;
                        case 201:
                            addModel(ChatAdapterItemTypeBean.CHAT_RECV_RED_PACKET, body.getMessage(), message, noFriendBean);
                            break;
                        case 202://红包 202.对方已领取红包 203.我已领取对方红包
                            addModel(ChatAdapterItemTypeBean.CHAT_RECV_RED_OPEN, body.getMessage(), message, noFriendBean);
                            break;
                        case 301://转账
                            addModel(ChatAdapterItemTypeBean.CHAT_RECV_TRANS_ACCOUNT, body.getMessage(), message, noFriendBean);
                            break;
                        case 302://转账
                            addModel(ChatAdapterItemTypeBean.CHAT_RECV_TRANS_OPEN, body.getMessage(), message, noFriendBean);
                            break;
                        case 101: //这几种是一个UI展示  101.分享门店 102.分享作品 103.分享美发师 110.推荐好友 111.邀请平台美发师加入 112.邀请门店美发师加入  113平台美发师加入申请 114门店美发师加入申请
                        case 102:
                        case 103:
                        case 110:
                        case 111:
                        case 112:
                        case 113:
                        case 114:
                            addModel(ChatAdapterItemTypeBean.CHAT_RECV_SHARE_MSG, body.getMessage(), message, noFriendBean);
                            break;
                        case 1://1.洗剪吹 2.洗吹 3.烫发 4.染发 5.护理 6.接发 7.洗色 8.拉直 9.单项套餐 10.多项套餐
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                        case 10:
                            addModel(ChatAdapterItemTypeBean.CHAT_RECV_ADD_MONEY, body.getMessage(), message, noFriendBean);
                            break;

                        default:
                            makeSelfDefinedMessage(message);
                    }
                } else {
                    addModel(ChatAdapterItemTypeBean.CHAT_RECV, body.getMessage(), message);
                }
            }
        } else {//是本人的消息
            String addMoneyMsg = message.getStringAttribute(Constants.IM_MESSAGE_ORDER_ADDMONEY_KEY, null);
            if (TextUtils.isEmpty(addMoneyMsg)) {
                addModel(ChatAdapterItemTypeBean.CHAT_SEND, body.getMessage(), message);
            } else {
                SelfDefinedInfoBean noFriendBean = new Gson().fromJson(addMoneyMsg, SelfDefinedInfoBean.class);
                if (noFriendBean != null) {
                    switch (noFriendBean.getDefinedMsgType()) {
                        case 401:
                            addModel(ChatAdapterItemTypeBean.CHAT_SEND_GROUP_JOIN, body.getMessage(), message, noFriendBean);
                            break;
                        case 201://红包
                            addModel(ChatAdapterItemTypeBean.CHAT_SEND_RED_PACKET, body.getMessage(), message, noFriendBean);
                            break;
                        case 202://红包 203.我已领取对方红包
                            addModel(ChatAdapterItemTypeBean.CHAT_SEND_RED_OPEN, body.getMessage(), message, noFriendBean);
                            break;
                        case 301://转账
                            addModel(ChatAdapterItemTypeBean.CHAT_SEND_TRANS_ACCOUNT, body.getMessage(), message, noFriendBean);
                            break;
                        case 302://转账
                            addModel(ChatAdapterItemTypeBean.CHAT_SEND_TRANS_OPEN, body.getMessage(), message, noFriendBean);
                            break;
                        case 101: //这几种是一个UI展示  101.分享门店 102.分享作品 103.分享美发师 110.推荐好友
                        case 102:
                        case 103:
                        case 110:
                        case 111:
                        case 112:
                        case 113:
                        case 114:
                            addModel(ChatAdapterItemTypeBean.CHAT_SEND_SHARE_MSG, body.getMessage(), message, noFriendBean);
                            break;
                        case 1://1.洗剪吹 2.洗吹 3.烫发 4.染发 5.护理 6.接发 7.洗色 8.拉直 9.单项套餐 10.多项套餐
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                        case 9:
                        case 10:
                            addModel(ChatAdapterItemTypeBean.CHAT_SEND_ADD_MONEY, body.getMessage(), message, noFriendBean);
                            break;
                        default:
                            makeSelfDefinedMessage(message);
                    }
                } else {
                    addModel(ChatAdapterItemTypeBean.CHAT_SEND, body.getMessage(), message);
                }
            }
        }
    }

    /**
     * 默认人消息展示
     *
     * @param message
     */
    private void makeSelfDefinedMessage(EMMessage message) {
        if (Type.TXT == message.getType()) {
            EMTextMessageBody body = (EMTextMessageBody) message.getBody();
            if (body != null) {
                //是否是语音聊天的消息
                if (message.getBooleanAttribute(Constants.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
                    if (!message.getFrom().equals(currUsername)) {
                        addModel(ChatAdapterItemTypeBean.CHAT_RECV_VOICE_CALL, body.getMessage(), message);
                    } else {
                        addModel(ChatAdapterItemTypeBean.CHAT_SEND_VOICE_CALL, body.getMessage(), message);
                    }
                } else if (message.getBooleanAttribute(Constants.MESSAGE_ATTR_IS_VIDEO_CALL, false)) {
                    if (!message.getFrom().equals(currUsername)) {
                        addModel(ChatAdapterItemTypeBean.CHAT_RECV_VIDEO_CALL, body.getMessage(), message);
                    } else {
                        addModel(ChatAdapterItemTypeBean.CHAT_SEND_VIDEO_CALL, body.getMessage(), message);
                    }
                } else {
                    if (!message.getFrom().equals(currUsername)) {//不是本人的消息
                        //此处的太容易try用来判断是否存在这个getStringAttribute自定义参数，没有会是异常返回，此时展示正常消息
                        addModel(ChatAdapterItemTypeBean.CHAT_RECV, body.getMessage(), message);
                    } else {
                        addModel(ChatAdapterItemTypeBean.CHAT_SEND, body.getMessage(), message);
                    }
                }
            }
        } else if (Type.IMAGE == message.getType()) {
            EMImageMessageBody body = (EMImageMessageBody) message.getBody();
            if (body != null) {
                if (!message.getFrom().equals(currUsername)) {
                    addModel(ChatAdapterItemTypeBean.CHAT_RECV_PIC, body.getFileName(), message);
                } else {
                    addModel(ChatAdapterItemTypeBean.CHAT_SEND_PIC, body.getFileName(), message);
                }
            }
        } else if (Type.VOICE == message.getType()) {
            EMVoiceMessageBody body = (EMVoiceMessageBody) message.getBody();
            if (body != null) {
                if (!message.getFrom().equals(currUsername)) {
                    addModel(ChatAdapterItemTypeBean.CHAT_RECV_VOICE, body.getLength() + "s", message);
                } else {
                    addModel(ChatAdapterItemTypeBean.CHAT_SEND_VOICE, body.getLength() + "s", message);
                }
            }
        } else if (Type.LOCATION == message.getType()) {
            if (!message.getFrom().equals(currUsername)) {
                addModel(ChatAdapterItemTypeBean.CHAT_RECV_LOCATION, "", message);
            } else {
                addModel(ChatAdapterItemTypeBean.CHAT_SEND_LOCATION, "", message);
            }
        }
    }


    /**
     * 添加聊天双方的聊天adpter的model
     *
     * @param adapterType
     * @param content
     * @param message
     */
    protected void addModel(int adapterType, String content, EMMessage message) {
        ChatAdapterHolderbean model = new ChatAdapterHolderbean();
        model.setContent(content);
        model.setMessage(message);
        if (Constants.CHATTYPE_GROUP == mChatType) {
            if (noFriendBean != null) {
                model.setIcon(noFriendBean.getPath());
            }
        } else {
            if (friendUserBean != null) {
                if (message.getFrom().equals(friendUserBean.getImusername())) {
                    model.setIcon(friendUserBean != null ? friendUserBean.getPath() : "");
                } else {
                    model.setIcon(AccountManager.getInstance().getAccount() == null ? "" : AccountManager.getInstance().getAccount().getHeadImg());
                }
            } else {
                model.setIcon("");
            }
        }
        models.add(new ChatAdapterItemTypeBean(adapterType, model));
    }

    /**
     * 自定义消息类型时：添加聊天双方的聊天adpter的model
     */
    protected void addModel(int adapterType, String content, EMMessage message, SelfDefinedInfoBean noFriendBean) {
        ChatAdapterHolderbean model = new ChatAdapterHolderbean();
        model.setContent(content);
        model.setMessage(message);
        model.setChatNoFriendBean(noFriendBean);

        if (Constants.CHATTYPE_GROUP == mChatType) {
            if (noFriendBean != null) {
                model.setIcon(noFriendBean.getPath());
            }
        } else {
            if (friendUserBean != null) {
                if (message.getFrom().equals(friendUserBean.getImusername())) {
                    model.setIcon(friendUserBean != null ? friendUserBean.getPath() : "");
                } else {
                    model.setIcon(AccountManager.getInstance().getAccount() == null ? "" : AccountManager.getInstance().getAccount().getHeadImg());
                }
            } else {
                model.setIcon("");
            }
        }
        models.add(new ChatAdapterItemTypeBean(adapterType, model));
    }

    /**
     * 本地发送消息添加adapter的item
     *
     * @param adpterType
     * @param content
     * @param message
     */
    protected void addItemModel(int adpterType, String content, EMMessage message) {
        ChatAdapterHolderbean model = new ChatAdapterHolderbean();

        model.setIcon(AccountManager.getInstance().getAccount() == null ? "" : AccountManager.getInstance().getAccount().getHeadImg());
        model.setContent(content);
        model.setMessage(message);
        models.add(new ChatAdapterItemTypeBean(adpterType, model));
        mAdapter.setNewData(models);

        mBinding.recylerView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    /**
     * 区分群聊还是单聊的message
     * friendUserBean.setImusername(mChatId);
     * friendUserBean.setPath(headPath);
     * friendUserBean.setNickname(chatName);
     * if (!TextUtils.isEmpty(friendId)) {
     * friendUserBean.setUserId(Long.parseLong(friendId));
     * }
     *
     * @param message
     */
    protected void sendMessage(EMMessage message) {
        SelfDefinedInfoBean selfBean = new SelfDefinedInfoBean();
        selfBean.setImusername(AccountManager.getInstance().getAccount().getImusername());
        selfBean.setPath(AccountManager.getInstance().getAccount().getHeadImg());
        selfBean.setNickname(AccountManager.getInstance().getAccount().getNickname());
        String usid = AccountManager.getInstance().getUserId();
        if (!TextUtils.isEmpty(usid)) {
            selfBean.setUserId(Long.parseLong(usid));
        }

        if (Constants.CHATTYPE_GROUP == mChatType) {
            message.setChatType(EMMessage.ChatType.GroupChat);    //如果是群聊，设置chattype，默认是单聊
            if (groupBean != null) {
                selfBean.setRecvImusername(groupBean.getImusername());
                selfBean.setRecvNickname(groupBean.getName());
                selfBean.setRecvPath(groupBean.getPath());
                selfBean.setRecvUserId(groupBean.getId());
            }
        } else {
            message.setChatType(EMMessage.ChatType.Chat);
            if (friendUserBean != null) {
                selfBean.setRecvImusername(friendUserBean.getImusername());
                selfBean.setRecvUserId(friendUserBean.getUserId());
                selfBean.setRecvPath(friendUserBean.getPath());
                selfBean.setRecvNickname(friendUserBean.getNickname());
            }
        }
        message.setAttribute(Constants.IM_MESSAGE_ORDER_ADDMONEY_KEY, new Gson().toJson(selfBean));//自定义消息类型  用这个key传值/ios端也能识别
        // 调用发送消息的方法
        EasyUtil.getEmManager().sendMessage(message);
        // 为消息设置回调
//        message.setMessageStatusCallback(new MyEMCallBack() {
//            @Override
//            public void onProgress(int i, String s) {
//                // 消息发送进度，一般只有在发送图片和文件等消息才会有回调，txt不回调
//            }
//        });
    }

    public void initLisenter() {
        myEMMessageListener = new MyEMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                super.onMessageReceived(messages);
                // 循环遍历当前收到的消息
                for (EMMessage message : messages) {
                    Log.i("yyl", "收到新消息:" + message);
                    if (message.getChatType() == EMMessage.ChatType.Chat && message.getFrom().equals(mChatId)) {
                        // 设置消息为已读
                        if (mConversation != null) {
                            mConversation.markMessageAsRead(message.getMsgId());
                        }
                        // 非ui线程，用handler去更新ui
                        Message msg = mHandler.obtainMessage();
                        msg.what = 0;
                        msg.obj = message;
                        mHandler.sendMessage(msg);
                    } else if (message.getChatType() == EMMessage.ChatType.GroupChat) {
                        if (mConversation != null) {
                            mConversation.markMessageAsRead(message.getMsgId());
                        }
                        // 因为消息监听回调这里是非ui线程，所以要用handler去更新ui
                        Message msg = mHandler.obtainMessage();
                        msg.what = 0;
                        msg.obj = message;
                        mHandler.sendMessage(msg);
                    }
                    //目的是不是本会话聊天的话 就顶部弹框提示新消息，目前看微信不需要该提示
//                    else {
//                        easeNotifier.notify(message);
//                    }
                }
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> messages) {
                super.onCmdMessageReceived(messages);
                for (int i = 0; i < messages.size(); i++) {
                    // 透传消息
                    EMMessage cmdMessage = messages.get(i);
                    EMCmdMessageBody body = (EMCmdMessageBody) cmdMessage.getBody();
                    Log.i("yyl", "收到 CMD 透传消息" + body.action());
                }
            }
        };
    }

    /**
     * 隐藏软键盘
     *
     * @param v
     */
    public void hideKeyBorad(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
        }
    }

    /**
     * EditText获取焦点显示软键盘
     */
    public void showSoftInputFromWindow(Activity activity, EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }
}
