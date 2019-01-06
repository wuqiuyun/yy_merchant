package com.yiyue.store.module.im.chat;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.yiyue.store.R;
import com.yiyue.store.component.greendao.DaoCallBackInterface;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.ChatAdapterHolderbean;
import com.yiyue.store.model.vo.bean.ChatAdapterItemTypeBean;
import com.yiyue.store.model.vo.bean.ChatMoreChooseBean;
import com.yiyue.store.model.vo.bean.ConversationBean;
import com.yiyue.store.model.vo.bean.SelfDefinedInfoBean;
import com.yiyue.store.model.vo.bean.daobean.ChatNoFriendBean;
import com.yiyue.store.model.vo.bean.daobean.GroupBean;
import com.yiyue.store.model.vo.bean.daobean.UserFriendsBean;
import com.yiyue.store.model.vo.result.RedBagSendResult;
import com.yiyue.store.module.common.addr.AddressSelectActivity;
import com.yiyue.store.module.home.store.StoreManagerActivity;
import com.yiyue.store.module.im.chat.chatview.EmotiomComplateFragment;
import com.yiyue.store.module.im.chat.chatview.EmotionUtils;
import com.yiyue.store.module.im.chat.chatview.FragmentFactory;
import com.yiyue.store.module.im.chat.chatview.GlobalOnItemClickManagerUtils;
import com.yiyue.store.module.im.chat.chatview.ImVoiceRecorderView;
import com.yiyue.store.module.im.chat.chatview.NoHorizontalScrollerVPAdapter;
import com.yiyue.store.module.im.daoutil.ChatNoFriendDaoUtils;
import com.yiyue.store.module.im.daoutil.UserFriendDaoUtils;
import com.yiyue.store.module.im.friendinfo.FriendInfoActivity;
import com.yiyue.store.module.im.groupinfo.GroupInfoActivity;
import com.yiyue.store.module.im.recommend.RecommendFriendActivity;
import com.yiyue.store.module.im.redpacket.RedPacketSendActivity;
import com.yiyue.store.module.im.redpacket.reddetail.RedPacketDetailActivity;
import com.yiyue.store.module.im.transfer.TransferAccountsActivity;
import com.yiyue.store.module.im.transfer.transdetail.TransDetailActivity;
import com.yiyue.store.module.mine.settings.security.paypassword.PayPasswordActivity;
import com.yiyue.store.module.mine.stylist.details.StylistDetailsActivity;
import com.yiyue.store.module.mine.stylist.details.definedmsgdetail.StylistDetailsFromMsgActivity;
import com.yiyue.store.module.mine.works.details.WorksDetailsActivity;
import com.yiyue.store.util.easyutils.EaseNotifier;
import com.yiyue.store.util.easyutils.EasyUtil;
import com.yiyue.store.widget.dialog.BaseEasyDialog;
import com.yiyue.store.widget.dialog.EasyDialog;
import com.yiyue.store.widget.dialog.ViewConvertListener;
import com.yiyue.store.widget.dialog.ViewHolder;
import com.yl.core.component.image.ImageLoader;
import com.yl.core.component.image.ImageLoaderConfig;
import com.yl.core.component.log.DLog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by zhangzz on 2018/10/9
 * 聊天fragment
 */
public class ChatFragment extends BaseChatFragment implements View.OnClickListener {
    protected List<ChatMoreChooseBean> moreBeans = new ArrayList<>();
    protected ChatMoreChooseAdapter mMoreAdapter;
    private List<Fragment> emojiFragments = new ArrayList<>();//emoji表情fragment
    private UserFriendDaoUtils userFriendDaoUtils;//好友数据库操作类
    protected ChatNoFriendDaoUtils chatNoFriendDaoUtils;
    private ObjectAnimator rpOpenAnim;//打开红包动画
    protected BaseEasyDialog redOpenDialog;//开启红包dialog
    private String chatName;//Constants.EXTRA_FRIEND_NAME  聊天对象的昵称
    private SelfDefinedInfoBean redItemFriendBean;//保存当前点击的 红包的bean 用于网络请求后处理拆红包 和查看详情的操作
    private String headPath;//单聊时 对方的头像
    protected int ylMessageType = Constants.IM_MESSAGE_NORMOR_TYPE;//意约不同界面跳到聊天页时 需要不同的操作，比如订单详情页点击咨询进入！默认正常好友之间聊天
    private boolean isRedSend = false;//true 红包 false 转账
    protected boolean isChatFromConver = false;//是否是来自会话的聊天 默认false  如果是会话进来的聊天退出后不刷新会话列表整个环信的记录!

    @Override
    protected void initView() {
        super.initView();

        mChatId = getArguments().getString(Constants.EXTRA_USER_ID);// 获取当前会话的username(如果是群聊就是群id)
        mChatType = getArguments().getInt(Constants.EXTRA_CHAT_TYPE, Constants.CHATTYPE_SINGLE);
        isChatFromConver = getArguments().getBoolean(Constants.EXTRA_CHAT_IS_FROM_CONVER, false);

        initConversation();//初始化会话数据

        initRecycleView();//设置用到的两个recycleview

        //判断是否群聊
        if (Constants.CHATTYPE_GROUP == mChatType) {
            mGroup = (GroupBean) getArguments().getSerializable(Constants.EXTRA_GROUP);
            groupId = getArguments().getString(Constants.EXTRA_GROUPBEAN_ID);
            mGroupName = getArguments().getString(Constants.EXTRA_GROUP_NAME);
            groupBean = new GroupBean();
            if (!isChatFromConver && mGroup != null) {
                groupId = mGroup.getId() + "";
                groupBean.setPath(mGroup.getPath());
            } else {
                String groupPath = getArguments().getString("groupPath");
                groupBean.setPath(groupPath);
            }
            groupBean.setImusername(mChatId);
            groupBean.setName(mGroupName);
            if (!TextUtils.isEmpty(mChatId)) {
                groupBean.setId(Long.parseLong(mChatId));
            }

            refreshList();
        } else {
            friendId = getArguments().getString(Constants.EXTRA_FRIEND_ID);
            chatName = getArguments().getString(Constants.EXTRA_FRIEND_NAME);
            headPath = getArguments().getString(Constants.EXTRA_FRIEND_HEAD_PATH);
            isFriend = getArguments().getBoolean(Constants.EXTRA_FRIEND_IS_FRIEND, true);
            ylMessageType = getArguments().getInt(Constants.IM_MESSAGE_KEY, Constants.IM_MESSAGE_NORMOR_TYPE);

            friendUserBean = new UserFriendsBean();
            friendUserBean.setImusername(mChatId);
            friendUserBean.setPath(headPath);
            friendUserBean.setNickname(chatName);
            if (!TextUtils.isEmpty(friendId)) {
                friendUserBean.setUserId(Long.parseLong(friendId));
            }

            switch (ylMessageType) {
                case Constants.IM_MESSAGE_ORDER_DETAIL_TYPE:
                    initFriendDao();//咨询进入消息页面 会进行好友数据库查询 如果不存在则在非好友表中插入该咨询对象信息

                    break;
                default:
            }
            refreshList();
        }


        initData();//初始化顶部布局展示

        initLisenter();//消息监听初始化
        easeNotifier = new EaseNotifier(getActivity());
        EasyUtil.getEmManager().addMessageListener(myEMMessageListener);
        initEvent();//点击事件设置
    }

    /**
     * 好友和非好友数据库查询初始化
     */
    private void initFriendDao() {
        chatNoFriendDaoUtils = new ChatNoFriendDaoUtils(getActivity());
        userFriendDaoUtils = new UserFriendDaoUtils(getActivity());
        Task.callInBackground(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                UserFriendsBean userFriendsBeanSech =  userFriendDaoUtils.querySynch(mChatId);
                if (userFriendsBeanSech == null) {//如果不在好友数据库中，则聊天对象可能在非好友数据库中，此时需要查询非好友数据库
                    ChatNoFriendBean chatNoFriendBeanSech = chatNoFriendDaoUtils.querySynch(mChatId);

                    if (chatNoFriendBeanSech == null) {//如果好友数据库表中和非好友数据库表中都没有这个用户，则添加该用户到非好友中
                        if (friendUserBean != null) {
                            ChatNoFriendBean chatNoFriend = new ChatNoFriendBean();
                            chatNoFriend.setImusername(friendUserBean.getImusername());
                            chatNoFriend.setNickname(friendUserBean.getNickname());
                            chatNoFriend.setPath(friendUserBean.getPath());
                            chatNoFriend.setUserId(friendUserBean.getUserId());

                            chatNoFriendDaoUtils.insertUser(chatNoFriend);
                        }
                    }
                } else {
                    isFriend = true;
                }
                return null;
            }
        }).continueWith(new Continuation<Void, Object>() {
            @Override
            public Object then(Task<Void> task) throws Exception {
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);


        chatNoFriendDaoUtils.setOnIsertInterface(new DaoCallBackInterface.OnIsertInterface() {
            @Override
            public void onIsertInterface(boolean type) {
                if (type) {
                    DLog.d("发送保存成功");
                } else {
                    DLog.d("发送保存失败");
                }
            }
        });
    }

    private void hideRed() {
        if (!isFriend) {
            List<ChatMoreChooseBean> moreBeans2 = new ArrayList<>();
            for (int i = 0; i < 5; ++i) {
                moreBeans2.add(new ChatMoreChooseBean(Constants.resouseIds[i], Constants.itemNames[i]));
            }

            mMoreAdapter.setNewData(moreBeans2);
        }
    }

    private void initData() {
        //判断是群聊还是单人聊天UI展示
        if (Constants.CHATTYPE_GROUP == mChatType) {
            mBinding.titleView.setTitleText(mGroupName);
            mBinding.titleView.setRightIcon(getResources().getDrawable(R.drawable.icon_group_settings));
            mBinding.titleView.setRightImgClickListener(v -> {
                //查看群资料
                Intent intent = new Intent(getActivity(), GroupInfoActivity.class);
                if (mGroup != null) {//从会话列表点击进到群资料页面时没有GroupBean 此时用groupId去传递
                    intent.putExtra(Constants.EXTRA_GROUP, mGroup);
                } else {
                    intent.putExtra(Constants.EXTRA_GROUPBEAN_ID, groupId);
                }
                startActivity(intent);
            });
        } else {
            mBinding.titleView.setRightIcon(getResources().getDrawable(R.drawable.icon_chatsettings_nor));
            mBinding.titleView.setTitleText(chatName);
            mBinding.titleView.setRightImgClickListener(v -> {
                Intent intent = new Intent(getActivity(), FriendInfoActivity.class);
                intent.putExtra(Constants.EXTRA_CHAT_TYPE, Constants.CHATTYPE_SINGLE);
                intent.putExtra(Constants.EXTRA_USER_ID, mChatId);
                intent.putExtra(Constants.EXTRA_FRIEND_ID, friendId);//朋友的用户id
                intent.putExtra(Constants.EXTRA_FRIEND_IS_FRIEND, isFriend);
                startActivity(intent);
            });
        }
    }

    private void initRecycleView() {
        //设置主对话recylerView
        mAdapter = new ChatAdapter2(models);
        mBinding.recylerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        layoutManager.setStackFromEnd(true);
        mBinding.recylerView.setLayoutManager(layoutManager);
        mBinding.recylerView.setAdapter(mAdapter);

        //设置下方点击加号展示更多选择recylerView
        mBinding.chatRvMoreChoose.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        mBinding.chatRvMoreChoose.setLayoutManager(gridLayoutManager);
        mMoreAdapter = new ChatMoreChooseAdapter(R.layout.im_chat_menu_item);
        mBinding.chatRvMoreChoose.setAdapter(mMoreAdapter);

        if (Constants.CHATTYPE_GROUP == mChatType) {
            for (int i = 0; i < 4; ++i) {
                moreBeans.add(new ChatMoreChooseBean(Constants.resouseIds[i], Constants.itemNames[i]));
            }
        } else {
            for (int i = 0; i < Constants.resouseIds.length; ++i) {
                moreBeans.add(new ChatMoreChooseBean(Constants.resouseIds[i], Constants.itemNames[i]));
            }
        }
        mMoreAdapter.setNewData(moreBeans);

        //设置Emojojd表情包viewpager
        GlobalOnItemClickManagerUtils globalOnItemClickManager = GlobalOnItemClickManagerUtils.getInstance(getActivity());
        //绑定当前Bar的编辑框
        globalOnItemClickManager.attachToEditText(mBinding.includeBottomChat.chatEtSendmessage);
        initViewPager();
    }

    private void initViewPager() {
        //创建fragment的工厂类
        FragmentFactory factory = FragmentFactory.getSingleFactoryInstance();
        //创建修改实例
        EmotiomComplateFragment complateFragment = (EmotiomComplateFragment) factory.getFragment(EmotionUtils.EMOTION_CLASSIC_TYPE);
        emojiFragments.add(complateFragment);

        NoHorizontalScrollerVPAdapter adapter = new NoHorizontalScrollerVPAdapter(getActivity().getSupportFragmentManager(), emojiFragments);
        mBinding.vpEmotionviewLayout.setAdapter(adapter);
    }

    @Override
    protected void initEvent() {
        mBinding.includeBottomChat.chatBtnSend.setOnClickListener(this);
        mBinding.includeBottomChat.chatBtnSetModeVoice.setOnClickListener(this);
        mBinding.includeBottomChat.chatBtnSetModeKeyboard.setOnClickListener(this);
        mBinding.includeBottomChat.chatEtSendmessage.setOnClickListener(this);

        mBinding.titleView.setLeftClickListener(v -> {
            getActivity().onBackPressed();
        });

        mBinding.includeBottomChat.chatEtSendmessage.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    mBinding.includeBottomChat.chatBtnMore.setVisibility(View.GONE);
                    mBinding.includeBottomChat.chatBtnSend.setVisibility(View.VISIBLE);
                } else {
                    mBinding.includeBottomChat.chatBtnMore.setVisibility(View.VISIBLE);
                    mBinding.includeBottomChat.chatBtnSend.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //输入语音操作
        mBinding.includeBottomChat.chatBtnPressToSpeak.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mBinding.voiceRecorder.onPressToSpeakBtnTouch(v, event, new ImVoiceRecorderView.EaseVoiceRecorderCallback() {

                    @Override
                    public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
                        sendVoiceMessage(voiceFilePath, voiceTimeLength);
                    }
                });
            }
        });

        //点击空白处收起键盘
        mBinding.recylerView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideBottomLayout(true);
                return false;
            }
        });

        mMoreAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                switch (position) {
                    case 0://拍照
                        selectPicFromCamera();
                        break;
                    case 1://相册
                        selectPicFromPhoto();
                        break;
                    case 2://位置
                        AddressSelectActivity.startActivity(getActivity(), Constants.REQUEST_CODE_MAP);
                        break;
                    case 3://推荐好友
//                        selectFileFromLocal();
                        if (friendUserBean != null) {
                            Intent intent = new Intent(getActivity(), RecommendFriendActivity.class);
                            intent.putExtra("tofriendname", TextUtils.isEmpty(friendUserBean.getRemarks()) ? friendUserBean.getNickname() : friendUserBean.getRemarks());
                            intent.putExtra(Constants.EXTRA_USER_ID, mChatId);
                            intent.putExtra(Constants.EXTRA_FRIEND_HEAD_PATH, headPath);
                            intent.putExtra(Constants.EXTRA_FRIEND_ID, friendId);
                            getActivity().startActivity(intent);
                        }
                        break;
//                    case 4:
//                        startVoiceCall();
//                        break;
                    case 4:
                        //红包
                        if (friendUserBean == null) {
                            showToast("好友数据不完整");
                            return;
                        }
                        isRedSend = true;
                        getMvpPresenter().initPayWord(getContext());
                        break;
                    case 5:
                        //转账
                        if (friendUserBean == null) {
                            showToast("好友数据不完整");
                            return;
                        }
                        isRedSend = false;
                        getMvpPresenter().initPayWord(getContext());
                        break;
                    case 6:
                        startVoiceCall();
                        break;
                    case 7:
                        startVideoCall();
                        break;
                }
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (models != null && models.size() > 0) {
                    ChatAdapterItemTypeBean chatAdapterItemTypeBean = models.get(position);
                    if (chatAdapterItemTypeBean != null && chatAdapterItemTypeBean.object != null) {
                        ChatAdapterHolderbean holderbean = (ChatAdapterHolderbean) chatAdapterItemTypeBean.object;
                        if (holderbean != null && holderbean.getChatNoFriendBean() != null) {
                            redItemFriendBean = holderbean.getChatNoFriendBean();
                            switch (view.getId()) {
                                case R.id.layout_red_packet://点击红包item

                                    //在此处调用后台接口判断这个红包是否已经被打开，在确定展示开红包dialog还是红包详情页面
                                    getMvpPresenter().findRedBagReq(holderbean.getChatNoFriendBean().getTradeId() + "", getActivity());

                                    break;
                                case R.id.layout_transfer: //点击转账item
                                    //在此处调用后台接口判断这个红包是否已经被打开，在确定展示开红包dialog还是红包详情页面
                                    getMvpPresenter().findTransferReq(holderbean.getChatNoFriendBean().getTradeId() + "", getActivity());

                                    break;
                                case R.id.layout_share_content://101.分享门店 102.分享作品 103.分享美发师 110.推荐好友 111.邀请平台美发师加入 112.邀请门店美发师加入  113平台美发师加入申请 114门店美发师加入申请
                                    if (redItemFriendBean != null) {
                                        switch (redItemFriendBean.getDefinedMsgType()) {
                                            case 101:
                                                Bundle bundleShare = new Bundle();
                                                bundleShare.putInt(Constants.STORE_TYPE, Constants.STORE_MANAGE);
                                                bundleShare.putString(Constants.STORE_ID, redItemFriendBean.getDetailId());
                                                StoreManagerActivity.startActivity(getContext(), StoreManagerActivity.class, bundleShare);
                                                break;
                                            case 102:
                                                Bundle opusBundle = new Bundle();
                                                opusBundle.putString(Constants.OPUS_ID, redItemFriendBean.getDetailId());
                                                opusBundle.putString(Constants.HEAD_PORTRAIT, redItemFriendBean.getToPath());
                                                opusBundle.putString(Constants.NICK_NAME, redItemFriendBean.getToNickname());
                                                WorksDetailsActivity.startActivity(getActivity(), WorksDetailsActivity.class, opusBundle);
                                                break;
                                            case 103:
                                                Bundle bundleShareSty = new Bundle();
                                                bundleShareSty.putString(Constants.STYLIST_ID, redItemFriendBean.getDetailId());
                                                StylistDetailsActivity.startActivity(getContext(), StylistDetailsActivity.class, bundleShareSty);
                                                break;
                                            case 110://110.推荐好友
                                                Intent intent = new Intent(getActivity(), FriendInfoActivity.class);
                                                intent.putExtra(Constants.EXTRA_CHAT_TYPE, Constants.CHATTYPE_SINGLE);
                                                intent.putExtra(Constants.EXTRA_USER_ID, redItemFriendBean.getToImusername());
                                                intent.putExtra(Constants.EXTRA_FRIEND_IS_FROM_RECOMMEND_FRIEND, true);
                                                startActivity(intent);
                                                break;
                                            case 111:
                                            case 112:
                                                Bundle bundle = new Bundle();
                                                bundle.putInt(Constants.STORE_TYPE, Constants.STORE_MANAGE);
                                                bundle.putString(Constants.STORE_ID, AccountManager.getInstance().getStoreId());
                                                StoreManagerActivity.startActivity(getContext(), StoreManagerActivity.class, bundle);
                                                break;
                                            case 113:
                                            case 114:
                                                Intent intentIv = new Intent(getActivity(), StylistDetailsFromMsgActivity.class);
                                                intentIv.putExtra(Constants.STYLIST_ID, redItemFriendBean.getDetailId());
                                                intentIv.putExtra(Constants.IM_SELF_BEAN, redItemFriendBean);
                                                startActivity(intentIv);
                                                //跳转推荐好友详情、邀请加入平台详情、美发师详情
                                                break;
                                        }
                                    }
                                    break;

                                case R.id.layout_addmoney_content:
                                    //跳转加价申请的订单详情
                                    showToast("跳转-》加价申请的订单详情");
                                    break;
                                case R.id.ic_user://点击头像进入好友详情
                                    toFriendInfoAct(holderbean.getMessage().getFrom());
                                    break;
                                case R.id.layout_voice_call:
                                    reVoiceOrVideo(holderbean);
                                    break;

                            }
                        } else {
                            switch (view.getId()) {
                                case R.id.ic_user://点击头像进入好友详情
                                    toFriendInfoAct(holderbean.getMessage().getFrom());
                                    break;
                                case R.id.layout_voice_call:
                                    reVoiceOrVideo(holderbean);//重新进入语音或者视频电话
                                    break;
                            }
                        }
                    }
                }
            }
        });

    }

    /**
     * 重新进入语音或者视频电话
     * @param holderbean
     */
    private void reVoiceOrVideo(ChatAdapterHolderbean holderbean) {
        if (holderbean.getMessage()!=null) {
            if (holderbean.getMessage().getBooleanAttribute(Constants.MESSAGE_ATTR_IS_VOICE_CALL, false)){
                startVoiceCall();
            } else {
                startVideoCall();
            }
        }
    }

    /**
     * 点击头像进入好友详情
     * @param from
     */
    private void toFriendInfoAct(String from) {
        Intent intent = new Intent(getActivity(), FriendInfoActivity.class);
        intent.putExtra(Constants.EXTRA_CHAT_TYPE, Constants.CHATTYPE_SINGLE);
        intent.putExtra(Constants.EXTRA_USER_ID, from);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.chat_btn_send:
                sendNormalMessage();
                break;
            case R.id.chat_btn_set_mode_voice:
                new RxPermissions(this)
                        .request(Manifest.permission.READ_PHONE_STATE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.RECORD_AUDIO
                        )
                        .subscribe(granted -> {
                            if (granted) {
                                voiceOrKeyBoard(true);
                                hideBottomLayout(true);
                            } else {
                                ToastUtils.shortToast(getString(R.string.permissions_phone_state));
                            }
                        });

                break;
            case R.id.chat_btn_set_mode_keyboard:
                hideBottomLayout(false);
                voiceOrKeyBoard(false);
                showSoftInputFromWindow(getActivity(), mBinding.includeBottomChat.chatEtSendmessage);
                break;
            case R.id.chat_et_sendmessage:
                hideBottomLayout(false);
                break;
        }
    }

    /**
     * 表情包列表显示或隐藏
     *
     * @param isShow
     */
    private void emojiconShowOrHide(boolean isShow) {
        mBinding.layoutBottomMojo.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    /**
     * 隐藏键盘、多选菜单、表情包布局
     * isShowKeyBoard true为隐藏软键盘 false不隐藏
     */
    private void hideBottomLayout(boolean isHideKeyBoard) {
        if (isHideKeyBoard) {
            hideKeyBorad(mBinding.includeBottomChat.chatEtSendmessage);
        }
        emojiconShowOrHide(false);
        mBinding.layoutBottom.setVisibility(View.GONE);
    }

    /**
     * 点击语音和键盘切换布局
     *
     * @param isVoice
     */
    private void voiceOrKeyBoard(boolean isVoice) {
        mBinding.includeBottomChat.chatBtnSetModeVoice.setVisibility(isVoice ? View.GONE : View.VISIBLE);
        mBinding.includeBottomChat.chatBtnSetModeKeyboard.setVisibility(isVoice ? View.VISIBLE : View.GONE);
        mBinding.includeBottomChat.chatBtnPressToSpeak.setVisibility(isVoice ? View.VISIBLE : View.GONE);
        mBinding.includeBottomChat.chatEdittextLayout.setVisibility(isVoice ? View.GONE : View.VISIBLE);
        mBinding.includeBottomChat.chatRlFace.setVisibility(isVoice ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void loadData() {
    }

    @Override
    public void findRedBagSuccess(RedBagSendResult redBagSendResult) {
        if (redBagSendResult != null && redBagSendResult.getData() != null) {
            if (!TextUtils.isEmpty(redItemFriendBean.getImusername()) && !TextUtils.isEmpty(currUsername) &&
                    redItemFriendBean.getImusername().equals(currUsername)) {//表示这个红包是自己发出的 直接查看的是详情
                Intent intent = new Intent(getActivity(), RedPacketDetailActivity.class);
                redItemFriendBean.setRedPacketStatus(redBagSendResult.getData().getStatus());
                intent.putExtra(Constants.EXTRA_RED_ITEM_BEAN, redItemFriendBean);
                getActivity().startActivity(intent);
            } else {
                //0 失效 1 发送中 2 已接收
                if (1 == redBagSendResult.getData().getStatus()) {
                    showRafDialog(redItemFriendBean, 201);
                } else if (2 == redBagSendResult.getData().getStatus()) {
                    Intent intent = new Intent(getActivity(), RedPacketDetailActivity.class);
                    redItemFriendBean.setRedPacketStatus(redBagSendResult.getData().getStatus());
                    intent.putExtra(Constants.EXTRA_RED_ITEM_BEAN, redItemFriendBean);
                    getActivity().startActivity(intent);
                }
            }
        }

    }

    @Override
    public void receiveRedBagSuc(RedBagSendResult redBagSendResult) {
        if (redBagSendResult != null && redBagSendResult.getData() != null) {
            redItemFriendBean.setRedPacketStatus(redBagSendResult.getData().getStatus());
            redOpenSuc(redItemFriendBean, 202);
        }
    }

    @Override
    public void findTransferSuccess(RedBagSendResult redBagSendResult) {
//                            getActivity().startActivity(new Intent(getActivity(), RedPacketDetailActivity.class));

        if (redBagSendResult != null && redBagSendResult.getData() != null) {
            if (!TextUtils.isEmpty(redItemFriendBean.getImusername()) && !TextUtils.isEmpty(currUsername) &&
                    redItemFriendBean.getImusername().equals(currUsername)) {//表示这个转账是自己发出的 直接查看的是详情
                Intent intent = new Intent(getActivity(), TransDetailActivity.class);
                redItemFriendBean.setRedPacketStatus(redBagSendResult.getData().getStatus());
                intent.putExtra(Constants.EXTRA_RED_ITEM_BEAN, redItemFriendBean);
                getActivity().startActivity(intent);
            } else {
                //0 失效 1 发送中 2 已接收
                if (1 == redBagSendResult.getData().getStatus()) {
                    showRafDialog(redItemFriendBean, 301);
                } else if (2 == redBagSendResult.getData().getStatus()) {
                    Intent intent = new Intent(getActivity(), TransDetailActivity.class);
                    redItemFriendBean.setRedPacketStatus(redBagSendResult.getData().getStatus());
                    intent.putExtra(Constants.EXTRA_RED_ITEM_BEAN, redItemFriendBean);
                    getActivity().startActivity(intent);
                }
            }
        }
    }

    @Override
    public void receiveTransferSuc(RedBagSendResult redBagSendResult) {
        if (redBagSendResult != null && redBagSendResult.getData() != null) {
            redItemFriendBean.setRedPacketStatus(redBagSendResult.getData().getStatus());
            redOpenSuc(redItemFriendBean, 302);
        }
    }

    @Override
    public void oninitPayWordInfoSuccess(String json) {
        //data=0(未设置)，data=1(已设置)。若未设置支付密码，弹窗跳转到设置支付密码页面
        Log.e("---------", "-----" + json);
        if (json.equals("0") || json.equals("0.0")) {
            Bundle bundle = new Bundle();
            bundle.putBoolean("isrepin", false);
            PayPasswordActivity.startActivity(getActivity(), PayPasswordActivity.class, bundle);

        } else {
            if (isRedSend) {
                //红包
                Intent intent = new Intent(getActivity(), RedPacketSendActivity.class);
                intent.putExtra(Constants.EXTRA_FRIEND_USER_BEAN, friendUserBean);
                startActivity(intent);
            } else {
                //转账
                Intent intentAcc = new Intent(getActivity(), TransferAccountsActivity.class);
                intentAcc.putExtra(Constants.EXTRA_FRIEND_USER_BEAN, friendUserBean);
                startActivity(intentAcc);
            }
        }

    }


    /**
     * //开红包dialog
     * 201 红包
     * 301 转账
     *
     * @param clickRedFriendBean
     * @param moneyType
     */
    private void showRafDialog(SelfDefinedInfoBean clickRedFriendBean, int moneyType) {
        redOpenDialog = EasyDialog.init()
                .setLayoutId(moneyType == 201 ? R.layout.dialog_red_packet_open : R.layout.dialog_transfer_open)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseEasyDialog dialog) {
                        ImageView iv_open_rp = holder.getView(R.id.iv_open_rp);
                        iv_open_rp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (rpOpenAnim == null) {
                                    rpOpenAnim = ObjectAnimator.ofFloat(iv_open_rp, "RotationY", new float[]{0.0F, 180.0F});
                                    rpOpenAnim.setDuration(500L);
                                    rpOpenAnim.setRepeatCount(-1);
                                }
                                rpOpenAnim.start();
                                // 2018/11/10  调用后台拆红包接口 通知后台此红包已打开
                                if (moneyType == 201) {
                                    getMvpPresenter().receiveRedBagReq(clickRedFriendBean.getTradeId() + "", getActivity());
                                } else {
                                    getMvpPresenter().receiveTransferReq(clickRedFriendBean.getTradeId() + "", getActivity());
                                }
                                dialog.dismiss();
                            }
                        });

                        holder.getView(R.id.iv_close).setOnClickListener(v -> {
                            dialog.dismiss();
                        });

                        ImageLoaderConfig config = null;
                        if (config == null) {
                            config = new ImageLoaderConfig.Builder().
                                    setAsBitmap(true).
                                    setPlaceHolderResId(R.drawable.im_avatar).
                                    setErrorResId(R.drawable.im_avatar).
                                    setDiskCacheStrategy(ImageLoaderConfig.DiskCache.SOURCE).
                                    setPrioriy(ImageLoaderConfig.LoadPriority.HIGH).build();
                        }
                        ImageLoader.loadImage(holder.getView(R.id.iv_header), clickRedFriendBean.getPath(), config, null);

                        holder.setText(R.id.tv_tip, clickRedFriendBean.getContent());
                        holder.setText(R.id.tv_name, clickRedFriendBean.getNickname());
                    }
                })
                .setPosition(Gravity.CENTER)
                .setMargin(30)
                .setOutCancel(true);
        redOpenDialog.show(getActivity().getSupportFragmentManager());
    }


    /**
     * 模拟接口返回 拆红包请求成功
     * moneyType 202 红包已开打  302 转账一打开
     *
     * @param clickRedFriendBean
     */
    private void redOpenSuc(SelfDefinedInfoBean clickRedFriendBean, int moneyType) {
        if (!clickRedFriendBean.getImusername().equals(currUsername)) {
            clickRedFriendBean.setImusername(AccountManager.getInstance().getAccount().getImusername());
            clickRedFriendBean.setPath(AccountManager.getInstance().getAccount().getHeadImg());
            clickRedFriendBean.setNickname(AccountManager.getInstance().getAccount().getNickname());
            String usid = AccountManager.getInstance().getUserId();
            if (!TextUtils.isEmpty(usid)) {
                clickRedFriendBean.setUserId(Long.parseLong(usid));
            }

            if (friendUserBean != null) {
                clickRedFriendBean.setRecvImusername(friendUserBean.getImusername());
                clickRedFriendBean.setRecvUserId(friendUserBean.getUserId());
                clickRedFriendBean.setRecvPath(friendUserBean.getPath());
                clickRedFriendBean.setRecvNickname(friendUserBean.getNickname());
            }
        }

        rpOpenAnim.end();
        redOpenDialog.dismiss();
        clickRedFriendBean.setRedPacketStatus(2);//发送未领取
        clickRedFriendBean.setDefinedMsgType(moneyType); //20+

        if (!clickRedFriendBean.getImusername().equals(currUsername)) {
            EMMessage message = EasyUtil.getEmManager().sendOrderAddMoneyMes(clickRedFriendBean, clickRedFriendBean.getRecvImusername());
        } else {
            EMMessage message = EasyUtil.getEmManager().sendOrderAddMoneyMes(clickRedFriendBean, mChatId);
        }
        refreshList();
//        sendRedPacketMsg(clickRedFriendBean, message, ChatAdapterItemTypeBean.CHAT_SEND_RED_OPEN);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (userFriendDaoUtils != null) {
            userFriendDaoUtils.closeConnection();
        }
        if (chatNoFriendDaoUtils != null) {
            chatNoFriendDaoUtils.closeConnection();
        }
    }

}
