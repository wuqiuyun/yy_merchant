package com.yiyue.store.module.im.groups.groupsedit;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.databind.ClickHandler;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityGroupsEditBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.EventBean;
import com.yiyue.store.model.vo.bean.daobean.GroupBean;
import com.yiyue.store.module.im.groups.groupintro.GroupIntroActivity;
import com.yiyue.store.module.im.groups.grouptransfer.GroupTransferActivity;
import com.yiyue.store.module.main.MainActivity;
import com.yiyue.store.util.easyutils.EasyUtil;
import com.yiyue.store.widget.dialog.BaseEasyDialog;
import com.yiyue.store.widget.dialog.EasyDialog;
import com.yiyue.store.widget.dialog.ViewConvertListener;
import com.yiyue.store.widget.dialog.ViewHolder;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 群资料编辑
 * Created by lizhuo on 2018/10/12.
 */

@CreatePresenter(GroupsEditPresenter.class)
public class GroupsEditActivity extends BaseMvpAppCompatActivity<GroupsEditView, GroupsEditPresenter> implements ClickHandler, GroupsEditView {

    private ActivityGroupsEditBinding mBinding;
    private String mUesrId; //当前用户的id
    private GroupBean mGroup;//当前群
    private long mGroupId;//当前群id
    private String mImGroupId;//当前群环信id

    private String mGroupName, mGroupNameNew; // 群名
    private int mMembersonly, mMembersonlyNew; //  加群是否需要群主验证 0是 1否
    private String mDescribe, mDescribeNew; //  群描述

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_groups_edit;
    }

    @Override
    protected void init() {
        StatusBarUtil.setLightMode(this);
        EventBus.getDefault().register(this);
        mBinding = (ActivityGroupsEditBinding) viewDataBinding;
        mBinding.titleView.setLeftClickListener(view -> finish());
        mBinding.setClick(this);

        initData();
    }

    private void initData() {
        mUesrId = AccountManager.getInstance().getAccount().getId();
        mGroup = (GroupBean) getIntent().getSerializableExtra(Constants.EXTRA_GROUP);
        if (null != mGroup) {
            mGroupName = mGroupNameNew = mGroup.getName();
            mMembersonly = mMembersonlyNew = mGroup.getMembersonly();
            mDescribe = mDescribeNew = mGroup.getDescribe();
            mGroupId = mGroup.getId();
            mImGroupId = mGroup.getImgroup();
        }

        initView();
    }

    private void initView() {
        mBinding.tvGroupEditName.setText(mGroupName);

        switch (mMembersonly) {
            case 0:
                mBinding.joinSwitch.setChecked(true);
                break;
            case 1:
                mBinding.joinSwitch.setChecked(false);
                break;
        }

        if (!TextUtils.isEmpty(mDescribe)) {
            mBinding.tvGroupEditIntroduce.setText(mDescribe);
        } else {
            mBinding.tvGroupEditIntroduce.setText(getString(R.string.group_edit_introduce_hint));
        }

        initEvent();
    }

    private void initEvent() {
        mBinding.titleView.setRightTextClickListener(view -> {
            if (checkModify()) {//有过修改
                getMvpPresenter().updateGroupInfo(String.valueOf(mGroup.getId()), mGroupNameNew, mDescribeNew, String.valueOf(mMembersonlyNew));
            } else {//没有修改
                showToast("请修改后再做保存");
            }
        });

        mBinding.joinSwitch.setOnCheckedChangeListener((compoundButton, checked) ->
        {
            if (checked) {
                mMembersonlyNew = 0;
            } else {
                mMembersonlyNew = 1;
            }
        });
    }


    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_group_edit_name://修改群名
                showNameDialog(mGroupNameNew);
                break;
            case R.id.rl_group_edit_introduce://修改群介绍
                startActivity(this, GroupIntroActivity.class);
                break;
            case R.id.rl_group_edit_transfer://群转让
                Bundle bundle = new Bundle();
                bundle.putString(Constants.EXTRA_GROUPBEAN_ID, String.valueOf(mGroupId));
                bundle.putString(Constants.EXTRA_GROUP_IM_ID, mImGroupId);
                startActivity(this, GroupTransferActivity.class, bundle);
                break;
            case R.id.rl_group_edit_dissolve://群解散
                showDissolveDialog(mGroupName);
                break;
        }
    }

    //检查是否有做修改
    private boolean checkModify() {
        if (!TextUtils.isEmpty(mGroupNameNew)||!TextUtils.isEmpty(mDescribeNew)){
            return true;
        }
        return false;
    }

    //修改群名对话框
    private void showNameDialog(String groupName) {
        EasyDialog.init()
                .setLayoutId(R.layout.dialog_group_name)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseEasyDialog dialog) {
                        holder.setText(R.id.et_name, groupName);
                        holder.getView(R.id.et_name).requestFocus();
                        Timer timer = new Timer();
                        timer.schedule(new TimerTask() {
                                           public void run() {
                                               InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                               imm.showSoftInput(holder.getView(R.id.et_name), InputMethodManager.SHOW_IMPLICIT);
                                           }
                                       },
                                200);
                        holder.getView(R.id.tv_name_ok).setOnClickListener(v -> {
                            String newName = ((EditText) holder.getView(R.id.et_name)).getText().toString();
                            if (TextUtils.isEmpty(newName) || newName.equals(groupName)) {
                                //无事发生
                                dialog.dismiss();
                            } else {
                                mGroupNameNew = newName;
                                mBinding.tvGroupEditName.setText(mGroupNameNew);
                                dialog.dismiss();
                            }

                        });
                        holder.getView(R.id.tv_name_cancel).setOnClickListener(v -> {
                            dialog.dismiss();
                        });
                    }
                })
                .setPosition(Gravity.CENTER)
                .setMargin(45)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
    }


    //解散对话框
    private void showDissolveDialog(String groupName) {
        EasyDialog.init()
                .setLayoutId(R.layout.dialog_group_dissolve)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseEasyDialog dialog) {
                        String target = groupName + "(" + mImGroupId + ")";
                        String content = String.format(getResources().getString(R.string.group_dialog_dissolve_content), target);
                        holder.setText(R.id.tv_dissolve_content, content);
                        holder.getView(R.id.tv_dissolve_ok).setOnClickListener(v -> {
                            getMvpPresenter().deleteGroup(String.valueOf(mGroupId), mImGroupId);
                            dialog.dismiss();
                        });
                        holder.getView(R.id.tv_dissolve_cancel).setOnClickListener(v -> {
                            dialog.dismiss();
                        });
                    }
                })
                .setPosition(Gravity.CENTER)
                .setMargin(45)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
    }

    //接收群组介绍
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBean.GroupIntroduce event) {
        if (null != event) {
            mDescribeNew = event.getIntroduce();
            if (!TextUtils.isEmpty(mDescribeNew)) {
                mBinding.tvGroupEditIntroduce.setText(mDescribeNew);
            } else {
                mBinding.tvGroupEditIntroduce.setText(getString(R.string.group_edit_introduce_hint));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void updateSuccess() {
        showToast("修改群组信息成功");
        mGroupName = mGroupNameNew;
        mMembersonly = mMembersonlyNew;
        mDescribe = mDescribeNew;
        EventBus.getDefault().post(new EventBean.GroupUpdate(0));
        EventBus.getDefault().post(new EventBean.GroupChangeEvent(Constants.EVENT_GROUP_CHANGE));
        finish();
    }

    @Override
    public void updateFail() {
        showToast("修改群组信息失败");
    }

    @Override
    public void deleteSuccess() {
        showToast("群组 " + mGroupName + " 已解散");
        EasyUtil.getEmManager().deleteConversation(mImGroupId);
        EventBus.getDefault().post(new EventBean.GroupChangeEvent(Constants.EVENT_GROUP_CHANGE));
        EventBus.getDefault().post(new EventBean.ConversationRefreshEvent(2));
        finish();
    }

    @Override
    public void deleteFail() {
        showToast("解散群组失败");
    }
}
