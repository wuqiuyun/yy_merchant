package com.yiyue.store.module.im.recommend;

import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hyphenate.chat.EMMessage;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityRecommendFriendBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.ChatAdapterItemTypeBean;
import com.yiyue.store.model.vo.bean.EventBean;
import com.yiyue.store.model.vo.bean.SelfDefinedInfoBean;
import com.yiyue.store.model.vo.bean.daobean.UserFriendsBean;
import com.yiyue.store.module.im.friendinfo.PinyinComparator;
import com.yiyue.store.module.im.friends.UserFriendsAdapter;
import com.yiyue.store.util.StringUtil;
import com.yiyue.store.util.easyutils.EasyUtil;
import com.yiyue.store.widget.WaveSideBar;
import com.yiyue.store.widget.dialog.BaseEasyDialog;
import com.yiyue.store.widget.dialog.EasyDialog;
import com.yiyue.store.widget.dialog.ViewConvertListener;
import com.yiyue.store.widget.dialog.ViewHolder;
import com.yl.core.component.image.ImageLoader;
import com.yl.core.component.image.ImageLoaderConfig;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@CreatePresenter(RecommFriendPresenter.class)
public class RecommendFriendActivity extends BaseMvpAppCompatActivity<RecommFriendView, RecommFriendPresenter> implements RecommFriendView, OnRefreshListener {
    private ActivityRecommendFriendBinding mBinding;
    private BaseQuickAdapter friendAdapter;
    List<UserFriendsBean> frindLists = null;
    private SmartRefreshLayout refreshLayout;

    private String tofriendname;
    private String mChatId;
    private String toUserId;
    private String toHeadPath;

    @Override
    public void onRefresh(RefreshLayout refreshLayout) {
        requestListData();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_recommend_friend;
    }

    @Override
    protected void init() {
        StatusBarUtil.setLightMode(this);
        mBinding = (ActivityRecommendFriendBinding) viewDataBinding;
        tofriendname = getIntent().getStringExtra("tofriendname");
        mChatId = getIntent().getStringExtra(Constants.EXTRA_USER_ID);
        toUserId = getIntent().getStringExtra(Constants.EXTRA_FRIEND_ID);
        toHeadPath = getIntent().getStringExtra(Constants.EXTRA_FRIEND_HEAD_PATH);
        mBinding.titleView.setLeftClickListener(v -> {
            finish();
        });
        mBinding.rvFriendList.setHasFixedSize(true);
        mBinding.rvFriendList.setNestedScrollingEnabled(false);
        mBinding.rvFriendList.setLayoutManager(new LinearLayoutManager(this));

        friendAdapter = new UserFriendsAdapter(R.layout.adapter_user_friends, null);
        friendAdapter.openLoadAnimation();
        mBinding.rvFriendList.setAdapter(friendAdapter);

        frindLists = new ArrayList<>();

        mBinding.sideBar.setOnSelectIndexItemListener(new WaveSideBar.OnSelectIndexItemListener() {
            @Override
            public void onSelectIndexItem(String index) {
                for (int i = 0; i < frindLists.size(); i++) {
                    if (frindLists.get(i).getIndex().equals(index)) {
                        ((LinearLayoutManager) mBinding.rvFriendList.getLayoutManager()).scrollToPositionWithOffset(i, 0);
                        return;
                    }
                }
            }
        });

        initEvent();
        initRefreshLoadLayout();
        requestListData();
    }


    @Override
    public void onFindAllContactsSuccess(List<UserFriendsBean> data) {
        refreshLayout.finishRefresh();
        if (data != null && data.size() > 0) {
            makeAdapterData(data);
        } else {
            frindLists.clear();
            friendAdapter.setNewData(null);
        }
    }

    @Override
    public void onFindAllContactsFail() {
        refreshLayout.finishRefresh();
    }

    private void requestListData() {
        getMvpPresenter().findAllContacts(AccountManager.getInstance().getUserId(), this);
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }

    protected void initEvent() {
        friendAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                showRecommDialog(frindLists.get(position));
            }
        });
    }

    protected void initRefreshLoadLayout() {
        refreshLayout = mBinding.refreshLayout;
        if (refreshLayout != null) {
            refreshLayout.setRefreshHeader(new ClassicsHeader(this));
            refreshLayout.setOnRefreshListener(this);
        }
    }

    private void makeAdapterData(List<UserFriendsBean> data) {
        if (data != null && data.size() > 0) {
            for (int i = 0; i < data.size(); ++i) {
                String name = null;
                if (!TextUtils.isEmpty(data.get(i).getRemarks())) {
                    name = data.get(i).getRemarks();
                } else if (!TextUtils.isEmpty(data.get(i).getNickname())) {
                    name = data.get(i).getNickname();
                } else {
                    data.get(i).setIndex("#");
                }
                if (!TextUtils.isEmpty(name)) {
                    String pinyin = StringUtil.getPinYin(name);
                    String Fpinyin = pinyin.substring(0, 1).toUpperCase();
                    // 正则表达式，判断首字母是否是英文字母
                    if (Fpinyin.matches("[A-Z]")) {
                        data.get(i).setIndex(Fpinyin);
                    } else {
                        data.get(i).setIndex("#");
                    }
                }
            }
            PinyinComparator pinyinComparator = new PinyinComparator();
            Collections.sort(data, pinyinComparator);
            frindLists = data;
            friendAdapter.setNewData(frindLists);
        }
    }

    //弹出推荐好友确认框
    private void showRecommDialog(UserFriendsBean friendUserBean) {
        EasyDialog.init()
                .setLayoutId(R.layout.dialog_recomm_friend)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseEasyDialog dialog) {
                        holder.setText(R.id.tv_content_name, TextUtils.isEmpty(friendUserBean.getRemarks()) ? friendUserBean.getNickname() : friendUserBean.getRemarks());
                        holder.setText(R.id.tv_content, TextUtils.isEmpty(friendUserBean.getLabel()) ? "暂无标签" : friendUserBean.getLabel());
                        holder.setText(R.id.tv_to_nick_name, tofriendname);

                        ImageLoaderConfig config = null;
                        if (config == null) {
                            config = new ImageLoaderConfig.Builder().
                                    setAsBitmap(true).
                                    setPlaceHolderResId(R.drawable.im_avatar).
                                    setErrorResId(R.drawable.im_avatar).
                                    setDiskCacheStrategy(ImageLoaderConfig.DiskCache.SOURCE).
                                    setPrioriy(ImageLoaderConfig.LoadPriority.HIGH).build();
                        }
                        ImageLoader.loadImage(holder.getView(R.id.iv_head), friendUserBean.getPath(), config, null);

                        holder.setOnClickListener(R.id.tv_recomm_sure, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                SelfDefinedInfoBean chatNoFriendBean = new SelfDefinedInfoBean();
                                chatNoFriendBean.setImusername(AccountManager.getInstance().getAccount().getImusername());
                                chatNoFriendBean.setNickname(TextUtils.isEmpty(AccountManager.getInstance().getAccount().getNickname()) ? AccountManager.getInstance().getUsername() : AccountManager.getInstance().getAccount().getNickname());
                                chatNoFriendBean.setPath(AccountManager.getInstance().getAccount().getHeadImg());

                                String userId = AccountManager.getInstance().getUserId();
                                if (!TextUtils.isEmpty(userId)) {
                                    chatNoFriendBean.setUserId(Long.parseLong(userId));
                                }

                                chatNoFriendBean.setToImusername(friendUserBean.getImusername());
                                chatNoFriendBean.setToUserId(friendUserBean.getId());
                                chatNoFriendBean.setToGender(friendUserBean.getGender());
                                chatNoFriendBean.setToLabel(friendUserBean.getLabel());
                                chatNoFriendBean.setToPath(friendUserBean.getPath());
                                chatNoFriendBean.setToNickname(friendUserBean.getNickname());

                                chatNoFriendBean.setRecvImusername(mChatId);
                                if (!TextUtils.isEmpty(toUserId)) {
                                    chatNoFriendBean.setRecvUserId(Long.parseLong(toUserId));
                                }
                                chatNoFriendBean.setRecvPath(toHeadPath);
                                chatNoFriendBean.setRecvNickname(tofriendname);

                                chatNoFriendBean.setDefinedMsgType(110);//红包 110.推荐好友
                                chatNoFriendBean.setContent("好友推荐");

                                EMMessage message = EasyUtil.getEmManager().sendOrderAddMoneyMes(chatNoFriendBean, mChatId);
                                EventBus.getDefault().post(new EventBean.RedPacketMsgUpdate(0, chatNoFriendBean, message, ChatAdapterItemTypeBean.CHAT_SEND_SHARE_MSG));
                                dialog.dismiss();
                            }
                        });

                        holder.setOnClickListener(R.id.tv_recomm_cancel, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .setPosition(Gravity.CENTER)
                .setMargin(45)
                .setOutCancel(true)
                .setWidth(300)
                .show(this.getSupportFragmentManager());

    }

}
