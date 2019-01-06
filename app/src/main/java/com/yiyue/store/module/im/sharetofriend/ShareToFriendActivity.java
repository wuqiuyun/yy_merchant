package com.yiyue.store.module.im.sharetofriend;

import android.content.Context;
import android.content.Intent;
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
import com.yl.core.component.image.glide.GlideImageLoaderStrategy;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 分享店铺、美发师给好友的方法调用， 只支持推荐给好友，不支持群
 * by zhangzz 2018.11.17
 */
@CreatePresenter(ShareToFriendPresenter.class)
public class ShareToFriendActivity extends BaseMvpAppCompatActivity<ShareToFriendView, ShareToFriendPresenter> implements ShareToFriendView, OnRefreshListener {
    private ActivityRecommendFriendBinding mBinding;
    private BaseQuickAdapter friendAdapter;
    List<UserFriendsBean> frindLists = null;
    private SmartRefreshLayout refreshLayout;

    private String shareImusername;//分享店铺的，美发师的环信id ,分享作品时传null
    private String shareName;//分享店铺的，名称
    private String detailId;//分享店铺的，storeid
    private String shareUserId;//分享店铺的，用户id
    private String sharePath;//分享店铺的，头像地址
    private String shareLabelOrAddress;//分享店铺的地址 或者美发师的介绍label
    private int shareType;//* 101.分享门店 102.分享作品 103.分享美发师

    public static void startShareToFriendActivity(Context context, int shareType, String shareImusername, String shareName, String detailId, String shareUserId, String sharePath, String shareLabelOrAddress) {
        Intent intent = new Intent(context, ShareToFriendActivity.class);
        if (TextUtils.isEmpty(detailId)) {
            ToastUtils.shortToast("详情ID能为空");
            return;
        }
        intent.putExtra("shareType", shareType);
        intent.putExtra("shareImusername", shareImusername);
        intent.putExtra("shareName", shareName);
        intent.putExtra("detailId", detailId);
        intent.putExtra("shareUserId", shareUserId);
        intent.putExtra("sharePath", sharePath);
        intent.putExtra("shareLabelOrAddress", shareLabelOrAddress);
        context.startActivity(intent);
    }


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
        mBinding.titleView.setTitleText("分享好友");
        mBinding.titleView.setLeftClickListener(v->{
            finish();
        });

        shareType = getIntent().getIntExtra("shareType", 101);
        shareImusername = getIntent().getStringExtra("shareImusername");
        shareName = getIntent().getStringExtra("shareName");
        detailId = getIntent().getStringExtra("detailId");
        shareUserId = getIntent().getStringExtra("shareUserId");
        sharePath = getIntent().getStringExtra("sharePath");
        shareLabelOrAddress = getIntent().getStringExtra("shareLabelOrAddress");

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
                        holder.setText(R.id.tv_content_name, shareName);
                        holder.setText(R.id.tv_content, shareLabelOrAddress);
                        holder.setText(R.id.tv_to_nick_name, TextUtils.isEmpty(friendUserBean.getRemarks()) ? friendUserBean.getNickname() : friendUserBean.getRemarks());

                        ImageLoaderConfig config = null;
                        if (config == null) {
                            config = new ImageLoaderConfig.Builder().
                                    setAsBitmap(true).
                                    setPlaceHolderResId(R.drawable.im_avatar).
                                    setErrorResId(R.drawable.im_avatar).
                                    setDiskCacheStrategy(ImageLoaderConfig.DiskCache.SOURCE).
                                    setPrioriy(ImageLoaderConfig.LoadPriority.HIGH).build();
                        }
                        ImageLoader.loadImage(holder.getView(R.id.iv_head), sharePath);
//                        ImageLoader.loadImage(holder.getView(R.id.iv_head), sharePath, config, null);

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

                                //自定义消息 内容区域传值
                                chatNoFriendBean.setToImusername(shareImusername);
                                if (!TextUtils.isEmpty(shareUserId)) {
                                    chatNoFriendBean.setToUserId(Long.parseLong(shareUserId));
                                }
                                chatNoFriendBean.setToLabel(shareLabelOrAddress);
                                chatNoFriendBean.setToPath(sharePath);
                                chatNoFriendBean.setToNickname(shareName);

                                chatNoFriendBean.setRecvImusername(friendUserBean.getImusername());
                                chatNoFriendBean.setRecvUserId(friendUserBean.getFriendId());
                                chatNoFriendBean.setRecvPath(friendUserBean.getPath());
                                chatNoFriendBean.setRecvNickname( friendUserBean.getNickname());

                                chatNoFriendBean.setDefinedMsgType(shareType);//* 101.分享门店 102.分享作品 103.分享美发师
                                if (101 == shareType) {
                                    chatNoFriendBean.setContent("分享门店");
                                } else if (103 == shareType) {
                                    chatNoFriendBean.setContent("分享美发师");
                                } else {
                                    chatNoFriendBean.setContent("分享作品");
                                }

                                chatNoFriendBean.setDetailId(detailId);

                                EMMessage message = EasyUtil.getEmManager().sendOrderAddMoneyMes(chatNoFriendBean, friendUserBean.getImusername());
                                EventBus.getDefault().post(new EventBean.ConversationRefreshEvent(2));
                                dialog.dismiss();
                                finish();
                                ToastUtils.shortToast("分享成功");
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
