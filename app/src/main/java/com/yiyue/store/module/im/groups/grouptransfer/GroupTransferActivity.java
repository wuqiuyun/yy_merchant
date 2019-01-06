package com.yiyue.store.module.im.groups.grouptransfer;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;

import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityGroupTransferBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.daobean.GroupUserBean;
import com.yiyue.store.module.im.groupmembers.GroupMemberAdapter;
import com.yiyue.store.module.main.MainActivity;
import com.yiyue.store.widget.dialog.BaseEasyDialog;
import com.yiyue.store.widget.dialog.EasyDialog;
import com.yiyue.store.widget.dialog.ViewConvertListener;
import com.yiyue.store.widget.dialog.ViewHolder;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 转让群
 * Created by Lizhuo on 2018/10/15.
 */
@CreatePresenter(GroupTransferPresenter.class)
public class GroupTransferActivity extends BaseMvpAppCompatActivity<GroupTransferView, GroupTransferPresenter> implements GroupTransferView {
    private ActivityGroupTransferBinding mBinding;
    private GroupMemberAdapter mFriendsAdapter;
    private String mGroupId;
    private String mImGroupId;
    private String mImUsername;
    private String mImUserId;
    private List<GroupUserBean> membersList;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_group_transfer;
    }

    @Override
    protected void init() {
        StatusBarUtil.setLightMode(this);
        mBinding = (ActivityGroupTransferBinding) viewDataBinding;
        mBinding.titleView.setLeftClickListener(view -> finish());

        initView();
        initData();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        mGroupId = bundle.getString(Constants.EXTRA_GROUPBEAN_ID);
        mImGroupId = bundle.getString(Constants.EXTRA_GROUP_IM_ID);
        getMvpPresenter().findGroupAllUser(mGroupId);
    }

    private void initView() {
        mBinding.rvGroupTransfer.setHasFixedSize(true);
        mBinding.rvGroupTransfer.setNestedScrollingEnabled(false);
        mBinding.rvGroupTransfer.setLayoutManager(new LinearLayoutManager(this));

        mFriendsAdapter = new GroupMemberAdapter(R.layout.adapter_group_members, false);
        mFriendsAdapter.openLoadAnimation();
        mFriendsAdapter.setOnItemClickListener((adapter, view, position) ->
        {
            String userId = AccountManager.getInstance().getUserId();
            if (!TextUtils.isEmpty(userId) && userId.equals(String.valueOf(membersList.get(position).getUserId()))) { //用户是群主 显示群主相关操作区域
                showToast(getString(R.string.gi_cannot_tranself));
            } else {
                mImUsername = membersList.get(position).getImusername();
                mImUserId = String.valueOf(membersList.get(position).getUserId());

                String name = membersList.get(position).getNickname();
                showTransferDialog(name);
            }
        });
        mBinding.rvGroupTransfer.setAdapter(mFriendsAdapter);
    }

    //转让对话框
    private void showTransferDialog(String name) {
        EasyDialog.init()
                .setLayoutId(R.layout.dialog_group_transfer)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseEasyDialog dialog) {
                        String content = String.format(getResources().getString(R.string.group_dialog_transfer_content), name);
                        holder.setText(R.id.tv_transfer_content, content);
                        holder.getView(R.id.tv_transfer_ok).setOnClickListener(v -> {
                            getMvpPresenter().transferGroupOwner(mGroupId, mImGroupId, mImUsername, mImUserId);
                            dialog.dismiss();
                        });
                        holder.getView(R.id.tv_transfer_cancel).setOnClickListener(v -> {
                            showToast("取消转让");
                            dialog.dismiss();
                        });
                    }
                })
                .setPosition(Gravity.CENTER)
                .setMargin(45)
                .setOutCancel(false)
                .show(getSupportFragmentManager());
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }

    @Override
    public void findGroupAllUserSuccess(List<GroupUserBean> list) {
        membersList = new ArrayList<>();
        membersList.addAll(list);
        mFriendsAdapter.setNewData(membersList);
    }

    @Override
    public void transferSuccess() {
        showToast("群组转让成功!");
        //返回首页
        MainActivity.startActivity(this, MainActivity.class);
        finish();
    }

    @Override
    public void transferFail() {
        showToast("群组转让失败");
    }
}
