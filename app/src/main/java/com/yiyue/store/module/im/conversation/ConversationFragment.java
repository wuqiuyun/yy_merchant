package com.yiyue.store.module.im.conversation;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yiyue.store.R;
import com.yiyue.store.base.fragment.BaseMvpFragment;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.FragmentConversationBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.local.preferences.SharePreferencesUtils;
import com.yiyue.store.model.vo.bean.ConversationBean;
import com.yiyue.store.model.vo.bean.EventBean;
import com.yiyue.store.model.vo.bean.GroupJoinMemberBean;
import com.yiyue.store.model.vo.bean.SelfDefinedInfoBean;
import com.yiyue.store.model.vo.bean.daobean.ChatNoFriendBean;
import com.yiyue.store.model.vo.bean.daobean.GroupBean;
import com.yiyue.store.model.vo.bean.daobean.UserFriendsBean;
import com.yiyue.store.module.im.chat.ChatActivity;
import com.yiyue.store.module.im.daoutil.ChatNoFriendDaoUtils;
import com.yiyue.store.module.im.daoutil.GroupDaoUtils;
import com.yiyue.store.module.im.daoutil.UserFriendDaoUtils;
import com.yiyue.store.module.im.imsearch.ImSearchActivity;
import com.yiyue.store.module.im.msgnotice.MsgNoticeActivity;
import com.yiyue.store.module.im.sysnotice.SysNoticeActivity;
import com.yiyue.store.util.easyutils.EasyUtil;
import com.yiyue.store.util.easyutils.emlisenter.MyEMMessageListener;
import com.yiyue.store.widget.popwindow.PopupUtil;
import com.yiyue.store.widget.popwindow.XGravity;
import com.yiyue.store.widget.popwindow.YGravity;
import com.yl.core.component.mvp.factory.CreatePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by zhangzz on 2018/9/27
 * 消息fragment
 */
@CreatePresenter(ConversationPresenter.class)
public class ConversationFragment extends BaseMvpFragment<ConversationView, ConversationPresenter> implements ConversationView, OnRefreshListener {
    private FragmentConversationBinding mBinding;
    private boolean isShow;//有新系统消息时，显示红点


    private View headerView;
    private TextView mTvRedIcon;
    private RelativeLayout mLayoutSystemNotice;
    private RelativeLayout mLayoutSystemNoticeRednum;
    private RelativeLayout mLayoutNoticeRednum;

    private BaseQuickAdapter converAdapter;
    private MyEMMessageListener myEMMessageListener;
    private List<EMConversation> conversationList = new ArrayList<>();

    private UserFriendDaoUtils userFriendDaoUtils;//好友数据库操作类
    private GroupDaoUtils groupDaoUtils;//群组数据库操作类
    protected ChatNoFriendDaoUtils chatNoFriendDaoUtils;//非好友数据库操作类

    private List<ConversationBean> conBeans = new ArrayList<>();//好友会话列表展示的adapter的list

    protected String orderDataStr = "";//订单详情传过来的美发师bean的json
    protected SelfDefinedInfoBean selfDefinedInfoBean;
    ;//非好友聊天传来的bean
    private SmartRefreshLayout refreshLayout;

    private boolean isChatBack = false;//默认不是聊天页面返回的会话列表 此时刷新会话列表要做整体刷新 包括数据库查好友非好友信息 否则不做数据库查找操作

    private String currUsername;//当前登录人的聊天id

    private PopupUtil mDeletePop;//删除聊天弹框

    public static ConversationFragment getInstance() {
        return new ConversationFragment();
    }

    /**
     * 有会话刷新会话列表数据的事件
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBean.ConversationRefreshEvent event) {
        if (event != null) {
            //1只刷新现有会话列表的对话信息  2刷新 新会话
            if (1 == event.getTag()) {
                isChatBack = true;
            } else {
                isChatBack = false;
            }

            loadData();
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_conversation;
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        mBinding = (FragmentConversationBinding) viewDataBinding;
        //view初始化
        headerView = getLayoutInflater().inflate(R.layout.header_view__conversation, null);
        headerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mTvRedIcon = headerView.findViewById(R.id.tv_notice_icon);
        mLayoutSystemNotice = headerView.findViewById(R.id.layout_system_notice);
        mLayoutSystemNoticeRednum = headerView.findViewById(R.id.layout_system_notice_red_num);
        mLayoutNoticeRednum = headerView.findViewById(R.id.layout_notice_red_num);
        isShowRedPoint();


        //设置listview
        mBinding.rvConversation.setHasFixedSize(true);
        mBinding.rvConversation.setNestedScrollingEnabled(false);
        mBinding.rvConversation.setLayoutManager(new LinearLayoutManager(getActivity()));
        converAdapter = new ConverAdapter(R.layout.adapter_conversation_item1, conBeans);
        converAdapter.openLoadAnimation();
        converAdapter.addHeaderView(headerView);
        mBinding.rvConversation.setAdapter(converAdapter);
        currUsername = EasyUtil.getEmManager().getCurrentUser();//获取环信本地登录账号id
        //数据库操作类初始化
        userFriendDaoUtils = new UserFriendDaoUtils(getActivity());
        groupDaoUtils = new GroupDaoUtils(getActivity());
        chatNoFriendDaoUtils = new ChatNoFriendDaoUtils(getActivity());

        initRefreshLoadLayout();
        initEvent();//初始化监听、点击事件
        EasyUtil.getEmManager().addMessageListener(myEMMessageListener);
    }

    protected void initRefreshLoadLayout() {
        refreshLayout = mBinding.refreshLayout;
        if (refreshLayout != null) {
            refreshLayout.setRefreshHeader(new ClassicsHeader(getContext()));
            refreshLayout.setOnRefreshListener(this);
        }
    }

    /**
     * 初始化各种监听、点击事件
     */
    protected void initEvent() {
        myEMMessageListener = new MyEMMessageListener() {
            @Override
            public void onMessageReceived(List<EMMessage> messages) {
                super.onMessageReceived(messages);
                // 监听到有消息进来刷新会话列表
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        loadData();
                    }
                });
            }
        };

        converAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (conBeans != null && conBeans.size() > 0) {
                    ChatActivity.startFromConversationActivity(getActivity(), conBeans.get(position));
                }
            }
        });


        converAdapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                if (conBeans.size() > 0) {
                    showDeletePop(view, conBeans.get(position).getImusername(), position);
                } else {
                    loadData();
                }
                return false;
            }
        });


        headerView.findViewById(R.id.layout_notice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MsgNoticeActivity.class));
            }
        });

        mLayoutSystemNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SysNoticeActivity.class));
                SharePreferencesUtils.getSharePreferencesUtils().setSysNoticeShow(false);
                isShowRedPoint();

            }
        });

        rootView.findViewById(R.id.layout_search).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ImSearchActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void loadConversationSuccess(List<EMConversation> emConversationList) {
        refreshLayout.finishRefresh();
        if (emConversationList != null && emConversationList.size() > 0) {
            if (isChatBack) {
                for (EMConversation emConversation : emConversationList) {
                    if (conBeans != null && conBeans.size() > 0) {
                        for (ConversationBean vbean : conBeans) {
                            if (emConversation != null && !TextUtils.isEmpty(emConversation.conversationId()) && emConversation.conversationId().equals(vbean.getImusername())) {
                                EMMessage lastMessage = emConversation.getLastMessage();
                                vbean.setCreatetime(lastMessage.getMsgTime());
                                vbean.setUnreadNum(emConversation.getUnreadMsgCount());
                                selfDefinedInfoBean = groupJoinMsg(lastMessage);
                                if (selfDefinedInfoBean != null && selfDefinedInfoBean.getDefinedMsgType() == 401) {
                                    setConverBean(emConversation, selfDefinedInfoBean, vbean, 1);
                                } else {
                                    vbean.setContent(setConverBeanMsgType(lastMessage, vbean).getContent());
                                }
                            }
                            break;
                        }
                    }
                }

                if (conBeans.size() > 0) {
                    converAdapter.setNewData(conBeans);
                }
            } else {
                conversationList.clear();
                conversationList = emConversationList;
                makeConverDatas(conversationList);
            }
        } else {
            converAdapter.setNewData(null);
        }

    }

    //取出自定义消息
    private SelfDefinedInfoBean groupJoinMsg(EMMessage lastMessage) {
        try {
            orderDataStr = lastMessage.getStringAttribute(Constants.IM_MESSAGE_ORDER_ADDMONEY_KEY);
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        if (!TextUtils.isEmpty(orderDataStr)) {
            SelfDefinedInfoBean selfDefinedInfoBean = new Gson().fromJson(orderDataStr, SelfDefinedInfoBean.class);
            if (selfDefinedInfoBean != null) {
                return selfDefinedInfoBean;
            }
        }

        return null;
    }

    @Override
    public void loadConversationFail() {
        refreshLayout.finishRefresh();
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }

    /**
     * 收到im的主页面的刷新通知
     */
    public void refresh() {
        isChatBack = false;
        loadData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        EasyUtil.getEmManager().removeMessageListener(myEMMessageListener);
        if (userFriendDaoUtils != null) {
            userFriendDaoUtils.closeConnection();
        }
        if (groupDaoUtils != null) {
            groupDaoUtils.closeConnection();
        }
        if (chatNoFriendDaoUtils != null) {
            chatNoFriendDaoUtils.closeConnection();
        }
    }

    /**
     * 非好友 点击订单详情进入咨询进来的会话  这里做会话列表数据整理展示
     *
     * @param conversation
     * @param lastMessage
     * @param entry
     */
    private void noFriendDataMake(EMConversation conversation, EMMessage lastMessage, SelfDefinedInfoBean entry) {
        if (entry != null) {//111.邀请平台美发师加入 112.邀请门店美发师加入  113平台美发师加入申请 114门店美发师加入申请
            if (conversation.isGroup() || isStartWithNumber(conversation.conversationId())) {
                ConversationBean conversationBean = new ConversationBean();
                conversationBean.setNickname(entry.getRecvNickname());
                conversationBean.setCreatetime(lastMessage.getMsgTime());
                conversationBean.setFriendId(entry.getRecvUserId());
                conversationBean.setPath(entry.getRecvPath());
                conversationBean.setUnreadNum(conversation.getUnreadMsgCount());
                conversationBean.setImusername(conversation.conversationId());
                conversationBean.setChatType(EMMessage.ChatType.GroupChat);
                if (entry.getDefinedMsgType() == 401) {
                    setConverBean(conversation, entry, conversationBean, 3);
                } else {
                    setConverBean(lastMessage, conversationBean);
                }
            } else {
                ConversationBean conversationBean = new ConversationBean();

                //为这几个数据时 表示本地发送申请入驻时 本地数据库数据保存失败 此时删除非好友表，然后做重新创建插入数据操作
                if (entry.getDefinedMsgType() == 111 || entry.getDefinedMsgType() == 112) {
                    chatNoFriendDaoUtils.deleteAll();
                }
                if (lastMessage.getFrom().equals(currUsername)) {
                    conversationBean.setNickname(entry.getRecvNickname());
                    conversationBean.setCreatetime(lastMessage.getMsgTime());
                    conversationBean.setFriendId(entry.getRecvUserId());
                    conversationBean.setPath(entry.getRecvPath());
                    conversationBean.setUnreadNum(conversation.getUnreadMsgCount());
                    conversationBean.setImusername(entry.getRecvImusername());
                    conversationBean.setChatType(EMMessage.ChatType.Chat);
                    conversationBean.setFriend(false);
                } else {
                    conversationBean.setNickname(entry.getNickname());
                    conversationBean.setCreatetime(lastMessage.getMsgTime());
                    conversationBean.setFriendId(entry.getUserId());
                    conversationBean.setUnreadNum(conversation.getUnreadMsgCount());
                    conversationBean.setPath(entry.getPath());
                    conversationBean.setImusername(entry.getImusername());
                    conversationBean.setChatType(EMMessage.ChatType.Chat);
                    conversationBean.setFriend(false);

                }

                setConverBean(lastMessage, conversationBean);

                ChatNoFriendBean chatNoFriendBean = new ChatNoFriendBean();
                chatNoFriendBean.setUserId(conversationBean.getFriendId());
                chatNoFriendBean.setPath(conversationBean.getPath());
                chatNoFriendBean.setNickname(conversationBean.getNickname());
                chatNoFriendBean.setImusername(conversationBean.getImusername());

                ChatNoFriendBean chatNoFriendSearch = null;
                try {
                    chatNoFriendSearch = chatNoFriendDaoUtils.querySynch(conversationBean.getImusername());
                } catch (Exception e){
                    chatNoFriendDaoUtils.deleteAll();
                }

                if (chatNoFriendSearch == null) {
                    chatNoFriendDaoUtils.insertUser(chatNoFriendBean);
                }
            }
        }
    }


    //判断字符串是不是以数字开头
    public static boolean isStartWithNumber(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str.charAt(0) + "");
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 根据不同的消息类型设置ConversationBean的content和类型type字段，给到adpter分辨展示
     */
    private void setConverBean(EMMessage lastMessage, ConversationBean conversationBean) {
        conversationBean = setConverBeanMsgType(lastMessage, conversationBean);

        if (conBeans != null && !conBeans.contains(conversationBean)) {
            conBeans.add(conversationBean);
        }
    }

    /**
     * @param conversation
     * @param entry
     * @param conversationBean
     * @param fromWhere
     */
    private void setConverBean(EMConversation conversation, SelfDefinedInfoBean entry, ConversationBean conversationBean, int fromWhere) {
        StringBuffer groupJoinContent = new StringBuffer();
        if (entry.getGroupJoinMembers() != null && entry.getGroupJoinMembers().size() > 0) {
            String myIm = AccountManager.getInstance().getAccount().getImusername();
            GroupJoinMemberBean groupJoinMemberBean = new GroupJoinMemberBean(AccountManager.getInstance().getNickName(), myIm);
            if (entry.getGroupJoinMembers().contains(groupJoinMemberBean)) {
                groupJoinContent.append(entry.getNickname() + " 邀请你加入群聊");
            } else {
                boolean isMySelf = false;

                if (!TextUtils.isEmpty(entry.getImusername()) && !TextUtils.isEmpty(myIm)) {
                    if (entry.getImusername().equals(myIm)) {//如果该红包发送对象是 我自己
                        isMySelf = true;
                    } else {
                        isMySelf = false;
                    }
                }

                if (entry.getGroupJoinMembers().size() == 1) {
                    groupJoinContent.append((isMySelf ? "" : entry.getNickname()) + " 邀请 " + entry.getGroupJoinMembers().get(0).getNickName() + " 加入群聊");
                } else {
                    groupJoinContent.append((isMySelf ? "" : entry.getNickname()) + " 邀请 " + entry.getGroupJoinMembers().get(0).getNickName() + " ... 加入群聊");
                }
            }
        }

        conversationBean.setContent(groupJoinContent.toString());

        if (fromWhere != 1) {
            if (conBeans != null && !conBeans.contains(conversationBean)) {
                conBeans.add(conversationBean);
            }

            if (fromWhere == 3) {
                GroupBean groupBean = groupDaoUtils.querySynch(conversation.conversationId());
                if (groupBean == null) {
                    EventBus.getDefault().post(new EventBean.GroupChangeEvent(Constants.EVENT_GROUP_CHANGE));
                }
            }
        }
    }

    private ConversationBean setConverBeanMsgType(EMMessage lastMessage, ConversationBean conversationBean) {
        int type = 0;
        if (lastMessage != null) {
            if (EMMessage.Type.TXT == lastMessage.getType()) {
                EMTextMessageBody body = (EMTextMessageBody) lastMessage.getBody();
                if (lastMessage.getBooleanAttribute(Constants.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
                    type = 4;
                    conversationBean.setContent("[语音通话]" + body.getMessage());
                } else if (lastMessage.getBooleanAttribute(Constants.MESSAGE_ATTR_IS_VIDEO_CALL, false)) {
                    type = 5;
                    conversationBean.setContent("[视频通话]" + body.getMessage());
                } else {
                    type = 0;
                    conversationBean.setContent(body.getMessage());
                }
            } else if (EMMessage.Type.IMAGE == lastMessage.getType()) {//图片发送信息
                type = 1;
                conversationBean.setContent("[图片]");
            } else if (EMMessage.Type.VOICE == lastMessage.getType()) { //语音发送信息
                type = 3;
                conversationBean.setContent("[语音]");
            } else if (EMMessage.Type.LOCATION == lastMessage.getType()) {
                type = 2;
                conversationBean.setContent("[位置]");
            }
        }
        conversationBean.setType(type);
        return conversationBean;
    }

    /**
     * 整理环信会话消息 结合本地数据库好友信息进行展示会话
     * 这里做异步处理 防止卡顿
     *
     * @param lists
     */
    private void makeConverDatas(List<EMConversation> lists) {
        Task.callInBackground(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                conBeans.clear();
                if (lists != null && lists.size() > 0) {
                    for (EMConversation conversation : lists) {
                        UserFriendsBean userFriendsBean = userFriendDaoUtils.querySynch(conversation.conversationId());
                        if (userFriendsBean != null) {
                            ConversationBean conversationBean = new ConversationBean();
                            conversationBean.setNickname(userFriendsBean.getNickname());
                            EMMessage lastMessage = conversation.getLastMessage();
                            conversationBean.setCreatetime(lastMessage.getMsgTime());
                            conversationBean.setFriendId(userFriendsBean.getFriendId());
                            conversationBean.setId(userFriendsBean.getId());
                            conversationBean.setImusername(userFriendsBean.getImusername());
                            conversationBean.setPath(userFriendsBean.getPath());
                            conversationBean.setRemarks(userFriendsBean.getRemarks());
                            conversationBean.setStatus(userFriendsBean.getStatus());
                            conversationBean.setUnreadNum(conversation.getUnreadMsgCount());
                            conversationBean.setChatType(EMMessage.ChatType.Chat);
                            conversationBean.setFriend(true);

                            setConverBean(lastMessage, conversationBean);
                        } else {
                            GroupBean groupBean = groupDaoUtils.querySynch(conversation.conversationId());
                            if (groupBean != null) {
                                ConversationBean conversationBean = new ConversationBean();
                                conversationBean.setNickname(groupBean.getName());
                                EMMessage lastMessage = conversation.getLastMessage();
                                conversationBean.setCreatetime(lastMessage.getMsgTime());
                                conversationBean.setId(groupBean.getId());
                                conversationBean.setImusername(groupBean.getImgroup());
                                conversationBean.setPath(groupBean.getPath());
                                conversationBean.setStatus(groupBean.getGroupContactsStatus());//群消息免打扰与否
                                conversationBean.setUnreadNum(conversation.getUnreadMsgCount());
                                conversationBean.setChatType(EMMessage.ChatType.GroupChat);
                                selfDefinedInfoBean = groupJoinMsg(lastMessage);
                                if (selfDefinedInfoBean != null && selfDefinedInfoBean.getDefinedMsgType() == 401) {
                                    setConverBean(conversation, selfDefinedInfoBean, conversationBean, 2);
                                } else {
                                    setConverBean(lastMessage, conversationBean);
                                }

                            } else {
                                ChatNoFriendBean entry = null;
                                try {
                                    entry = chatNoFriendDaoUtils.querySynch(conversation.conversationId());
                                } catch (Exception e){
                                    chatNoFriendDaoUtils.deleteAll();
                                }

                                EMMessage lastMessage = conversation.getLastMessage();
                                if (entry != null) {//如果好友数据库表中和非好友数据库表中都没有这个用户，则添加该用户到非好友中
                                    ConversationBean conversationBean = new ConversationBean();
                                    conversationBean.setNickname(entry.getNickname());
                                    conversationBean.setCreatetime(lastMessage.getMsgTime());
                                    conversationBean.setFriendId(entry.getUserId());
                                    conversationBean.setPath(entry.getPath());
                                    conversationBean.setUnreadNum(conversation.getUnreadMsgCount());
                                    conversationBean.setImusername(entry.getImusername());
                                    conversationBean.setChatType(EMMessage.ChatType.Chat);
                                    conversationBean.setFriend(false);
                                    setConverBean(lastMessage, conversationBean);
                                } else {
                                    selfDefinedInfoBean = groupJoinMsg(lastMessage);
                                    if (selfDefinedInfoBean != null) {
                                        noFriendDataMake(conversation, lastMessage, selfDefinedInfoBean);
                                    }
                                }
                            }
                        }
                    }
                }
                return null;
            }
        }).continueWith(new Continuation<Void, Object>() {
            @Override
            public Object then(Task<Void> task) throws Exception {
                if (conBeans.size() > 0) {
                    converAdapter.setNewData(conBeans);
                }

                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);
    }

    @Override
    protected void loadData() {
        getMvpPresenter().loadConversationList();//加载环信会话数据
    }

    /**
     * 收到新的推送 显示红点
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBean.NewMessage event) {
        if (event != null) {
            isShowRedPoint();
        }
    }

    private void isShowRedPoint() {
        isShow = SharePreferencesUtils.getSharePreferencesUtils().isSysNoticeShow();
        if (isShow) {
            mLayoutSystemNoticeRednum.setVisibility(View.VISIBLE);
        } else {
            mLayoutSystemNoticeRednum.setVisibility(View.GONE);
        }
    }


    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        isChatBack = false;
        loadData();
    }

    /**
     * 单条聊天记录的删除
     *
     * @param view
     * @param imId
     * @param pos
     */
    private void showDeletePop(View view, String imId, int pos) {
        mDeletePop = PopupUtil.create()
                .setContentView(getActivity(), R.layout.popwin_conversation_oper)
                .setFocusAndOutsideEnable(true)
                .setBackgroundDimEnable(true)
                .setAnimationStyle(R.style.AnimImPopwindow)
                .setOnViewListener(new PopupUtil.OnViewListener() {
                    @Override
                    public void initViews(View view, PopupUtil basePopup) {
                        view.findViewById(R.id.tv_delete_chat).setOnClickListener(v -> {
                            EasyUtil.getEmManager().deleteConversation(imId);
                            refresh();

                            mDeletePop.dismiss();
                        });

                        view.findViewById(R.id.tv_has_read).setOnClickListener(v -> {
                            //将本条消息置为全部已读
                            if (conBeans!=null && conBeans.get(pos)!=null && conBeans.get(pos).getUnreadNum() > 0 && conversationList != null && conversationList.size() > 0) {
                                conversationList.get(pos).markAllMessagesAsRead();
                                conBeans.get(pos).setUnreadNum(0);
                                converAdapter.setNewData(conBeans);
                            }
                            mDeletePop.dismiss();
                        });
                    }
                })
                .setDimValue(0.4f)
                .apply();

        mDeletePop.showAtAnchorView(view, YGravity.CENTER, XGravity.CENTER);
    }
}
