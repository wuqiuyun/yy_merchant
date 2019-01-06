package com.yiyue.store.module.im.groupinfo;


import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import com.yiyue.store.R;
import com.yiyue.store.YLApplication;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.recycleview.GridSpacingItemDecoration;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityGroupInfoBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.EventBean;
import com.yiyue.store.model.vo.bean.daobean.GroupBean;
import com.yiyue.store.model.vo.bean.daobean.GroupUserBean;
import com.yiyue.store.module.im.friendinfo.FriendInfoActivity;
import com.yiyue.store.module.im.groupmembers.GroupMembersActivity;
import com.yiyue.store.module.im.groups.groupaddfriends.GroupAddFriendsActivity;
import com.yiyue.store.module.im.groups.groupsedit.GroupsEditActivity;
import com.yiyue.store.module.im.joingroup.JoinGroupActivity;
import com.yiyue.store.util.Utils;
import com.yiyue.store.widget.dialog.BaseEasyDialog;
import com.yiyue.store.widget.dialog.EasyDialog;
import com.yiyue.store.widget.dialog.ViewConvertListener;
import com.yiyue.store.widget.dialog.ViewHolder;
import com.yiyue.store.widget.popwindow.PopupUtil;
import com.yiyue.store.widget.popwindow.TriangleDrawable;
import com.yiyue.store.widget.popwindow.XGravity;
import com.yiyue.store.widget.popwindow.YGravity;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * 群详情资料页面 群好友列表数据库在这个页面后台load与本地对比更新
 * Created by zzz on 2018/10/12.
 */

@CreatePresenter(GroupInfoPresenter.class)
public class GroupInfoActivity extends BaseMvpAppCompatActivity<GroupInfoView, GroupInfoPresenter> implements GroupInfoView, ClickHandler {
    private ActivityGroupInfoBinding mBinding;
    private PopupUtil mPopWindow;

    protected String mGroupName;//当前聊天的群名
    private GroupInfoMemberAdapter adapter;

    private String mUesrId; //当前用户的id
    private String mImusername;//当前用户的环信用户名

    private String mGroupId; //当前群的id
    private String mImgroup;//当前群组的编号
    private GroupBean mGroup;//当前群
    private String num;//当前群人数

    private boolean isMember = false;//是否是该群成员
    private boolean isMaster;//是否是群主

    private long mUserBeanid;// 群组成员关系id 消息免打扰用
    private int mStatus;// 是否接收群消息 0 正常 1 禁止

    private int mMembersonly; //加群是否需要狗群主/狗管理审批  0是 1否
    private int mAllowinvites; //是否允许群成员邀请他人入群    0允许 1只允许群主

    protected List<GroupUserBean> groupBeanReq = new ArrayList<>();//网络请求bean的列表

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_group_info;
    }

    @Override
    protected void init() {
        EventBus.getDefault().register(this);
        mBinding = (ActivityGroupInfoBinding) viewDataBinding;
        mBinding.setClick(this);
        initData();
        initView();
        initPop();
    }

    private void initData() {
        mUesrId = AccountManager.getInstance().getAccount().getId();
        mImusername = AccountManager.getInstance().getAccount().getImusername();
        GroupBean groupBean = (GroupBean) getIntent().getSerializableExtra(Constants.EXTRA_GROUP);
        if (groupBean == null) {
            mGroupId = getIntent().getStringExtra(Constants.EXTRA_GROUPBEAN_ID);
        } else {
            mGroupId = groupBean.getId() + "";
        }

        loadData();
    }

    private void loadData() {
        getMvpPresenter().findGroup(mGroupId + "", AccountManager.getInstance().getUserId(), this);
        getMvpPresenter().findGroupAllUser(mGroupId + "");
    }

    private void initView() {
        StatusBarUtil.setLightMode(this);
        mBinding.titleView.setLeftClickListener(view -> finish());
        mBinding.titleView.setRightText("•••");
        mBinding.titleView.setRightTextClickListener(view -> showPop(mBinding.titleView.getRightText()));

        mBinding.giRvMembers.setHasFixedSize(true);
        GridLayoutManager gridLayoutManagerEm = new GridLayoutManager(this, 5);
        mBinding.giRvMembers.setLayoutManager(gridLayoutManagerEm);
        mBinding.giRvMembers.addItemDecoration(new GridSpacingItemDecoration(5, 30, true));
        adapter = new GroupInfoMemberAdapter(R.layout.adapter_item_group_member_head, this);

        adapter.setOnItemClickListener((adapter, view, position) ->
        {
            if (adapter.getData() != null && adapter.getData().size() > 0) {
                GroupUserBean groupUser = (GroupUserBean) adapter.getData().get(position);
                if (groupUser.isAdd()) {
                    Intent intent = new Intent(this, GroupAddFriendsActivity.class);
                    if (mGroup != null) {
                        intent.putExtra("groupId", mGroup.getId() + "");//群组id
                        intent.putExtra("imgroup", mGroup.getImgroup());//群组编号
                        intent.putExtra("groupName", mGroup.getName());//群组名称
                        intent.putExtra("groupPath", mGroup.getPath());//群组头像
                        ArrayList arrayList = new ArrayList();
                        arrayList.addAll(groupBeanReq);
                        intent.putParcelableArrayListExtra("groupBeanReq", arrayList);
                        startActivity(intent);
                    } else {
                        showToast("群数据异常");
                    }
                } else {
                    if (groupUser != null) {
                        Intent intent = new Intent(this, FriendInfoActivity.class);
                        intent.putExtra(Constants.EXTRA_CHAT_TYPE, Constants.CHATTYPE_SINGLE);
                        intent.putExtra(Constants.EXTRA_USER_ID, groupUser.getImusername());
                        intent.putExtra("friendId", groupUser.getUserId() + "");//朋友的用户id
                        startActivity(intent);
                    } else {
                        showToast("群数据异常");
                    }
                }
            } else {
                showToast("群数据异常");
            }
        });

        mBinding.giRvMembers.setAdapter(adapter);


        mBinding.disturbSwitch.setOnCheckedChangeListener((compoundButton, flag) ->
        {
            if (flag) {//免打扰
                getMvpPresenter().refuseMessage(mUserBeanid);
            } else {
                getMvpPresenter().receiveMessage(mUserBeanid);
            }
        });
    }

    private void initPop() {
        mPopWindow = PopupUtil.create()
                .setContext(this)
                .setContentView(R.layout.popwin_groupinfo_layout)

                .setAnimationStyle(R.style.AnimImPopwindow)
                .setOnViewListener(new PopupUtil.OnViewListener() {
                    @Override
                    public void initViews(View view, PopupUtil basePopup) {
                        View arrowView = view.findViewById(R.id.v_arrow);
                        arrowView.setBackground(new TriangleDrawable(TriangleDrawable.TOP, Color.WHITE));

                        view.findViewById(R.id.group_exit).setOnClickListener(v -> {
                            if (isMember) {
                                showRafDialog(mGroup.getName());
                            }
                            mPopWindow.dismiss();
                        });
                    }
                })
                .setFocusAndOutsideEnable(true)
                .setBackgroundDimEnable(true)
                .setDimValue(0.4f)
                .apply();
    }

    private void showPop(View view) {
        int offsetX = Utils.dp2px(YLApplication.getContext(), 20) - view.getWidth() / 2;
        int offsetY = (mBinding.titleView.getHeight() - view.getHeight()) / 2;
        mPopWindow.showAtAnchorView(view, YGravity.BELOW, XGravity.ALIGN_RIGHT, offsetX, offsetY - 10);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.gi_show_allmembers:
                Intent intentMember = new Intent(this, GroupMembersActivity.class);
                intentMember.putExtra(Constants.EXTRA_GROUPBEAN_ID, mGroupId);
                intentMember.putExtra(Constants.EXTRA_GROUP_IM_ID, mImgroup);
                intentMember.putExtra(Constants.EXTRA_GROUP_ROLE, isMaster);
                startActivity(intentMember);
                break;
            case R.id.gi_tv_apply_jion:
                if (mMembersonly == 0) {
                    Intent intent = new Intent(this, JoinGroupActivity.class);
                    intent.putExtra(Constants.EXTRA_GROUP, mGroup);
                    startActivity(intent);
                } else {
                    getMvpPresenter().addSingleToGroup(mGroupId, mImgroup, mImusername, mUesrId);
                }

                break;
        }
    }

    private void resetGroupInfo(GroupBean newGroup) {

        mGroup = newGroup;
        mImgroup = mGroup.getImgroup();
        mGroupName = mGroup.getName();
        mMembersonly = mGroup.getMembersonly();
        mAllowinvites = mGroup.getAllowinvites();
        mUserBeanid = newGroup.getGroupContactsId();
        mStatus = newGroup.getGroupContactsStatus();

        if (newGroup != null && newGroup.getIsmember() == 1) {
            isMember = true;
        } else {
            isMember = false;
        }

        if (!TextUtils.isEmpty(mUesrId) && mUesrId.equals(String.valueOf(mGroup.getUserId()))) { //用户是群主 显示群主相关操作区域
            mBinding.titleView.setRightIcon(getResources().getDrawable(R.drawable.im_pen));
            mBinding.titleView.getRightText().setVisibility(View.GONE);

            mBinding.titleView.setRightImgClickListener(view -> {
                //查看群资料
                Intent intent = new Intent(this, GroupsEditActivity.class);
                intent.putExtra(Constants.EXTRA_GROUP, mGroup);
                startActivity(intent);
            });
            mBinding.giCoupons.setVisibility(View.GONE);

            mBinding.giTvApplyJion.setVisibility(View.GONE);
            mBinding.rlGiDisturb.setVisibility(View.VISIBLE);
            if (mStatus == 0) {
                mBinding.disturbSwitch.setChecked(false);
            } else {
                mBinding.disturbSwitch.setChecked(true);
            }

            isMaster = true;
        } else if (isMember) { //用户不是群主，是普通群成員
            mBinding.titleView.getRightText().setVisibility(View.VISIBLE);

            mBinding.giTvApplyJion.setVisibility(View.GONE);
            mBinding.rlGiDisturb.setVisibility(View.VISIBLE);
            if (mStatus == 0) {
                mBinding.disturbSwitch.setChecked(false);
            } else {
                mBinding.disturbSwitch.setChecked(true);
            }
            mBinding.titleView.getRightText().setVisibility(View.VISIBLE);
        } else { //用户非該群成員
            mBinding.titleView.getRightText().setVisibility(View.VISIBLE);

            mBinding.giCoupons.setVisibility(View.GONE);
            mBinding.giTvApplyJion.setVisibility(View.VISIBLE);
            mBinding.rlGiDisturb.setVisibility(View.GONE);

            isMaster = false;
        }

        mBinding.titleView.setTitleText(TextUtils.isEmpty(mGroupName) ? getString(R.string.gi_name) : mGroupName);
        mBinding.giGroupIntroduce.setText(mGroup.getDescribe());
    }

    @Override
    public void findGroupSuccess(GroupBean newGroup) {
        mGroup = null;
        if (newGroup != null) {
            resetGroupInfo(newGroup);
        } else {
            showToast("当前数据获取为null");
        }
    }

    @Override
    public void findGroupAllUserSuccess(List<GroupUserBean> list) {
        if (list != null && list.size() > 0) {
            groupBeanReq = list;
            showGroupMembers(list);
        }

    }

    private void showGroupMembers(List<GroupUserBean> list) {
        //群人数
        num = String.format(getResources().getString(R.string.gi_member_num), list.size());
        mBinding.giShowAllmembers.setSubContentText(num);

        if (list != null && list.size() > 7) {
            list = list.subList(0, 7);
        }

        GroupUserBean bean = new GroupUserBean();
        bean.setAdd(true);
        list.add(bean);

        adapter.setNewData(list);
    }

    /**
     * 申请入群成功
     */
    @Override
    public void addSingleToGroupSuccess() {
        showToast("申请入群成功");
        EventBus.getDefault().post(new EventBean.GroupJoinOrOut(0, mGroup.getId()));
    }

    @Override
    public void removeSingleFromGroupSuccess() {
        showToast("退出群成功");
        EventBus.getDefault().post(new EventBean.GroupJoinOrOut(1, mGroup.getId()));
    }

    /**
     * 群资料变动后 更新本页面
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBean.GroupUpdate event) {
        if (event.getTag() == 1) {
            finish();
        } else {
            loadData();
        }
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //退出群对话框
    private void showRafDialog(String name) {
        EasyDialog.init()
                .setLayoutId(R.layout.dialog_receive_addfriend)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseEasyDialog dialog) {
                        String content = String.format(getResources().getString(R.string.ag_dialog_quit_group), name);
                        holder.setText(R.id.tv_raf_content, content);
                        holder.setText(R.id.tv_dialog_title, getResources().getString(R.string.ag_dialog_quit_title));
                        holder.setText(R.id.tv_raf_ok, getResources().getString(R.string.group_dialog_ok));
                        holder.setText(R.id.tv_raf_cancel, getResources().getString(R.string.group_dialog_cancel));

                        holder.getView(R.id.tv_raf_ok).setOnClickListener(v -> {
                            getMvpPresenter().removeSingleFromGroup(mGroup.getId() + "", mGroup.getImgroup(), AccountManager.getInstance().getAccount().getImusername(), mGroup.getGroupContactsId() + "");
                            dialog.dismiss();
                        });
                        holder.getView(R.id.tv_raf_cancel).setOnClickListener(v -> {
                            dialog.dismiss();
                        });
                    }
                })
                .setPosition(Gravity.CENTER)
                .setMargin(45)
                .setOutCancel(true)
                .show(getSupportFragmentManager());
    }
}
