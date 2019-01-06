package com.yiyue.store.module.im.friendinfo;

import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.yiyue.store.R;
import com.yiyue.store.YLApplication;
import com.yiyue.store.base.fragment.BaseMvpFragment;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.greendao.DaoCallBackInterface;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.FragmentFriendInfoBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.EventBean;
import com.yiyue.store.model.vo.bean.daobean.UserFriendsBean;
import com.yiyue.store.module.im.addfriend.friendapply.FriendApplySendActivity;
import com.yiyue.store.module.im.chat.ChatActivity;
import com.yiyue.store.module.im.daoutil.UserFriendDaoUtils;
import com.yiyue.store.module.im.friendcheck.SetRemakeActivity;
import com.yiyue.store.util.Utils;
import com.yiyue.store.widget.dialog.BaseEasyDialog;
import com.yiyue.store.widget.dialog.EasyDialog;
import com.yiyue.store.widget.dialog.ViewConvertListener;
import com.yiyue.store.widget.dialog.ViewHolder;
import com.yiyue.store.widget.popwindow.PopupUtil;
import com.yiyue.store.widget.popwindow.TriangleDrawable;
import com.yiyue.store.widget.popwindow.XGravity;
import com.yiyue.store.widget.popwindow.YGravity;
import com.yl.core.component.image.ImageLoader;
import com.yl.core.component.image.ImageLoaderConfig;
import com.yl.core.component.log.DLog;
import com.yl.core.component.mvp.factory.CreatePresenter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by zhangzz on 2018/10/8
 * 好友资料页面fragment
 */
@CreatePresenter(FriendInfoPresenter.class)
public class FriendInfoFragment extends BaseMvpFragment<FriendInfoView, FriendInfoPresenter> implements FriendInfoView, ClickHandler {
    private FragmentFriendInfoBinding mBinding;
    private PopupUtil mPopWindow;
    private String mChatId;   // 当前聊天的 ID
    private String friendId;
    private UserFriendDaoUtils userFriendDaoUtils;//好友数据库操作类
    private UserFriendsBean userFriendsBeanLocal;//本地数据库查询的好友bean对象
    private String status = "0";//0正常 1禁言 好友屏蔽与否   接口获取
    private UserFriendsBean currFriendInfoBean;//当前页面获取到的好友详情bean
    private boolean isFriend = true;//是否是陌生人 true是好友 false非好友
    private boolean isFromFriends = false;//是否是好友列表进入的详情页 只有还有列表进入详情可以点击发消息按钮
    private boolean isFromRecommendFriends = false;//是否是推荐好友点击进入的详情页 此时如果是非好友并且不是自己， 需要展示加好友和发信息两个底部弹框
    private String relationId;//好友关系id


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_friend_info;
    }

    /**
     * 群资料变动后 更新本页面
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBean.FriendInfoUpdate event) {
        if (event.getTag() == 0) {
            requestData();
        }
    }

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
        mBinding = (FragmentFriendInfoBinding) viewDataBinding;
        mBinding.setClick(this);
        // 获取当前会话的username(如果是群聊就是群id)
        mChatId = getArguments().getString(Constants.EXTRA_USER_ID);
        friendId = getArguments().getString(Constants.EXTRA_FRIEND_ID);
        isFriend = getArguments().getBoolean(Constants.EXTRA_FRIEND_IS_FRIEND, true);
        isFromFriends = getArguments().getBoolean(Constants.EXTRA_FRIEND_IS_FROM_FRIEND, false);
        isFromRecommendFriends = getArguments().getBoolean(Constants.EXTRA_FRIEND_IS_FROM_RECOMMEND_FRIEND, false);

        if (!isFromFriends) {
            mBinding.fiBtnSendmsg.setVisibility(View.GONE);
        }

        mBinding.titleView.setLeftClickListener(v -> {
            getActivity().finish();
        });

        mBinding.titleView.setRightTextClickListener(v -> {
            if (mPopWindow != null) {
                showPop(mBinding.titleView.getRightText());
            } else {
                DLog.d("好友信息获取为null，弹框未初始化");
            }
        });

        userFriendDaoUtils = new UserFriendDaoUtils(getActivity());
        userFriendDaoUtils.setOnQuerySingleBackInterface(new DaoCallBackInterface.OnQuerySingleBackInterface<UserFriendsBean>() {
            @Override
            public void onQuerySingleBackInterface(UserFriendsBean entry, String id) {
                if (entry != null) {
                    userFriendsBeanLocal = entry;
                } else { //如果本地查询失败 表示非好友详情查看  需要展示加好友的UI
                    if (isFromFriends && isFriend) {
                        //好友数据库表查询失败，说明数据库有问题
                        userFriendDaoUtils.deleteAll();
                        EventBus.getDefault().post(new EventBean.FriendChangeEventBean(Constants.EVENT_FRIEND_CHANGE));
                    }
                }
            }
        });

        userFriendDaoUtils.setOnUpdateInterface(new DaoCallBackInterface.OnUpdateInterface() {
            @Override
            public void onUpdateInterface(boolean type) {
                DLog.d("单个更新成功");
            }
        });

        userFriendDaoUtils.queryWhereUser(mChatId);

        requestData();
    }

    private void requestData() {
        getMvpPresenter().getFriendInfo(getActivity(), AccountManager.getInstance().getUserId(), mChatId);
    }

    @Override
    protected void loadData() {

    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fi_set_remake:
                Intent remakeIntent = new Intent(getActivity(), SetRemakeActivity.class);
                remakeIntent.putExtra(Constants.EXTRA_FRIEND_RELATION_ID, relationId);
                startActivity(remakeIntent);
                break;
            case R.id.fi_sex:
                break;
            case R.id.fi_hobby:
                break;
            case R.id.fi_profession:
                break;
            case R.id.fi_btn_sendmsg:
                if (currFriendInfoBean != null) {
                    ChatActivity.startFromFriendInfoActivity(getActivity(), mChatId, friendId, TextUtils.isEmpty(currFriendInfoBean.getRemarks()) ? currFriendInfoBean.getNickname() : currFriendInfoBean.getRemarks(), currFriendInfoBean.getPath());
                }
                break;
            case R.id.fi_btn_addfriend:
                Intent intent = new Intent(getActivity(), FriendApplySendActivity.class);
                if (currFriendInfoBean != null) {
                    intent.putExtra(Constants.EXTRA_SERACH_USERID, currFriendInfoBean.getUserId() + "");//好友userId
                    startActivity(intent);
                } else {
                    ToastUtils.shortToast("好友数据获取异常");
                }
                break;

        }
    }

    private void initPop() {
        mPopWindow = PopupUtil.create()
                .setContext(getActivity())
                .setContentView(R.layout.popwin_friendinfo_layout)
                .setAnimationStyle(R.style.AnimImPopwindow)
                .setOnViewListener(new PopupUtil.OnViewListener() {
                    @Override
                    public void initViews(View view, PopupUtil basePopup) {
                        View arrowView = view.findViewById(R.id.v_arrow);
                        TextView fi_tv_shield = view.findViewById(R.id.fi_tv_shield);
                        if ("0".equals(status)) {
                            fi_tv_shield.setText(getString(R.string.fi_shield));
                        } else {
                            fi_tv_shield.setText(getString(R.string.fi_cancel_shield));
                        }
                        arrowView.setBackground(new TriangleDrawable(TriangleDrawable.TOP, Color.WHITE));

                        view.findViewById(R.id.fi_layout_shield).setOnClickListener(v -> {
                            ToastUtils.shortToast("屏蔽好友");//屏蔽好友
                            if ("0".equals(status)) {
                                getMvpPresenter().addToBlackList(getActivity(), relationId);
                            } else {
                                getMvpPresenter().removeFromBlackList(getActivity(), relationId);
                            }
                            mPopWindow.dismiss();
                        });
                        view.findViewById(R.id.fi_layout_delete).setOnClickListener(v -> {
                            showDeleteDialog();//删除好友\
                            mPopWindow.dismiss();
                        });
                    }
                })
                .setFocusAndOutsideEnable(true)
                .setBackgroundDimEnable(true)
                .setDimValue(0.4f)
                .apply();
    }

    /**
     * 右上角弹框
     *
     * @param view
     */
    private void showPop(View view) {
        int offsetX = Utils.dp2px(YLApplication.getContext(), 20) - view.getWidth() / 2;
        int offsetY = (mBinding.titleView.getHeight() - view.getHeight()) / 2;
        mPopWindow.showAtAnchorView(view, YGravity.BELOW, XGravity.ALIGN_RIGHT, offsetX, offsetY - 10);
    }

    /**
     * 删除好友底部弹框
     */
    public void showDeleteDialog() {
        EasyDialog.init()
                .setLayoutId(R.layout.dialog_delete_friend)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseEasyDialog dialog) {
                        holder.getView(R.id.fi_delete_sure).setOnClickListener(v -> {
                            ToastUtils.shortToast("确认删除好友");
                            getMvpPresenter().requestDeleteFriendSingle(getActivity(), relationId);
                            dialog.dismiss();
                        });
                        holder.getView(R.id.fi_delete_cancel).setOnClickListener(v -> {
                            ToastUtils.shortToast("取消删除好友");
                            dialog.dismiss();
                        });
                    }
                })
                .setPosition(Gravity.BOTTOM)
                .setOutCancel(true)
                .show(getActivity().getSupportFragmentManager());
    }

    @Override
    public void onDeleteFriendSingleSuccess() {
        showToast("删除好友成功");
        userFriendDaoUtils.deleteByIdSingleUser(currFriendInfoBean.getId());
        EventBus.getDefault().post(new EventBean.FriendChangeEventBean(Constants.EVENT_FRIEND_CHANGE));
        getActivity().finish();
    }

    @Override
    public void onAddToBlackListSuccess() {
        showToast("屏蔽好友成功");
    }

    @Override
    public void onRemoveFromBlackListSuccess() {
        showToast("取消屏蔽好友成功");
    }

    @Override
    public void onGetFriendsSuccess(UserFriendsBean friendInfoBean) {
        if (friendInfoBean != null) {
            currFriendInfoBean = friendInfoBean;
            relationId = currFriendInfoBean.getId() + "";
            if (TextUtils.isEmpty(currFriendInfoBean.getIdno())) {
                mBinding.tvFiServer.setVisibility(View.GONE);
            } else {
                mBinding.tvFiServer.setVisibility(View.VISIBLE);
                mBinding.tvFiServer.setText("ID: " + currFriendInfoBean.getIdno());
            }

            if (TextUtils.isEmpty(friendInfoBean.getId() + "") || friendInfoBean.getId() == 0) {
                isFriend = false;
            } else {
                isFriend = true;
            }

            if (!isFriend) {
                strangerUi();
            }

            mBinding.tvFiName.setText(TextUtils.isEmpty(friendInfoBean.getRemarks()) ? friendInfoBean.getNickname() : friendInfoBean.getRemarks());

            ImageLoaderConfig config = null;
            if (config == null) {
                config = new ImageLoaderConfig.Builder().
                        setAsBitmap(true).
                        setPlaceHolderResId(R.drawable.im_avatar).
                        setErrorResId(R.drawable.im_avatar).
                        setDiskCacheStrategy(ImageLoaderConfig.DiskCache.SOURCE).
                        setPrioriy(ImageLoaderConfig.LoadPriority.HIGH).build();
            }
            ImageLoader.loadImage(mBinding.ivFiPhoto, friendInfoBean.getPath(), config, null);

            String sex = "无";
            if (friendInfoBean.getGender() == 1) {
                sex = "男";
            } else if (friendInfoBean.getGender() == 2) {
                sex = "女";
            } else if (friendInfoBean.getGender() == 3) {
                sex = "人妖";
            }
            mBinding.fiSex.setRightText(sex);
            mBinding.fiHobby.setRightText(TextUtils.isEmpty(friendInfoBean.getHobby()) ? "无" : friendInfoBean.getHobby());
            if (friendInfoBean.getRole() == 1) {//1 普通用户  2 美发师 3 门店
                mBinding.fiProfession.setVisibility(View.VISIBLE);
                mBinding.fiProfession.setRightText(TextUtils.isEmpty(friendInfoBean.getJob()) ? "无" : friendInfoBean.getJob());
            } else {
                mBinding.fiProfession.setVisibility(View.GONE);
            }
            String bir = friendInfoBean.getBirthday() + "";

            //20181123
            if (TextUtils.isEmpty(bir)) {
                mBinding.fiBirthday.setVisibility(View.GONE);
            } else {
                if (bir.length() == 8) {
                    StringBuffer birth = new StringBuffer();
                    birth.append(bir.substring(0, 4));
                    birth.append("/");
                    birth.append(bir.substring(4, 6));
                    birth.append("/");
                    birth.append(bir.substring(6, 8));

                    mBinding.fiBirthday.setRightText(TextUtils.isEmpty(birth.toString()) ? "无" : birth.toString());
                } else {
                    mBinding.fiBirthday.setVisibility(View.GONE);
                }
            }

            mBinding.fiFaceture.setRightText(getFace(friendInfoBean.getFaceture()));
            mBinding.fiHairstyle.setRightText(getHair(friendInfoBean.getHairstyle()));

            status = friendInfoBean.getStatus() + "";

            initPop();

            if (userFriendsBeanLocal != null) {
                friendInfoBean.set_id(userFriendsBeanLocal.get_id());
                if (!userFriendsBeanLocal.equals(friendInfoBean)) {//如果和本地数据库中信息不一致则更新数据库中的信息
                    userFriendDaoUtils.updateSingleUser(friendInfoBean);
                }
            }
        } else {
            ToastUtils.shortToast("用户信息获取失败");
            defaultUI();
            mBinding.fiBtnAddfriend.setVisibility(View.GONE);
        }
    }

    /**
     * 陌生人和自己展示UI
     */
    private void strangerUi() {
        if (currFriendInfoBean != null) {
            mBinding.fiSetRemake.setVisibility(View.GONE);
            mBinding.lineRemake.setVisibility(View.GONE);
            mBinding.titleView.getRightText().setVisibility(View.INVISIBLE);
            if (AccountManager.getInstance().getUserId().equals(currFriendInfoBean.getUserId() + "")) {//是不是自己的详情
                mBinding.fiBtnAddfriend.setVisibility(View.GONE);
                mBinding.fiLine.setVisibility(View.GONE);
            } else {
                if (isFromRecommendFriends) {
                    mBinding.fiBtnAddfriend.setVisibility(View.VISIBLE);
                    mBinding.fiLine.setVisibility(View.VISIBLE);
                    mBinding.fiBtnSendmsg.setVisibility(View.VISIBLE);
                } else {
                    mBinding.fiBtnAddfriend.setVisibility(View.VISIBLE);
                    mBinding.fiLine.setVisibility(View.GONE);
                    mBinding.fiBtnSendmsg.setVisibility(View.GONE);
                }
            }
        } else {
            defaultUI();
        }
    }

    /**
     * 网络错误时默认UI
     */
    private void defaultUI() {
        mBinding.fiSetRemake.setVisibility(View.GONE);
        mBinding.lineRemake.setVisibility(View.GONE);
        mBinding.fiBtnAddfriend.setVisibility(View.VISIBLE);
        mBinding.fiLine.setVisibility(View.GONE);
        mBinding.titleView.getRightText().setVisibility(View.INVISIBLE);
        mBinding.fiBtnSendmsg.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (userFriendDaoUtils != null) {
            userFriendDaoUtils.closeConnection();
        }
    }

    //获取脸型
    private String getFace(int faceType) {
        String face;
        switch (faceType) {
            case 1:
                face = "方脸";
                break;
            case 2:
                face = "圆脸";
                break;
            case 3:
                face = "尖脸";
                break;
            case 4:
                face = "瓜子脸";
                break;
            case 5:
                face = "鹅蛋脸";
                break;
            case 6:
                face = "包子脸";
                break;
            default:
                face = "未设置";
                break;
        }
        return face;
    }

    //获取发长
    private String getHair(int HairType) {
        String hair;
        switch (HairType) {
            case 1:
                hair = "长发";
                break;
            case 2:
                hair = "短发";
                break;
            case 3:
                hair = "中发";
                break;
            default:
                hair = "未设置";
                break;
        }
        return hair;
    }

}
