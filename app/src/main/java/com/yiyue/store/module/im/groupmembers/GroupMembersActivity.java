package com.yiyue.store.module.im.groupmembers;

import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivityGroupMembersBinding;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.EventBean;
import com.yiyue.store.model.vo.bean.daobean.GroupUserBean;
import com.yiyue.store.util.StringUtil;
import com.yiyue.store.widget.dialog.BaseEasyDialog;
import com.yiyue.store.widget.dialog.EasyDialog;
import com.yiyue.store.widget.dialog.ViewConvertListener;
import com.yiyue.store.widget.dialog.ViewHolder;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * 群成员页面
 * Created by zzz on 2018/10/12.
 */

@CreatePresenter(GroupMembersPresenter.class)
public class GroupMembersActivity extends BaseMvpAppCompatActivity<GroupMembersView, GroupMembersPresenter> implements GroupMembersView {
    private ActivityGroupMembersBinding mBinding;
    private GroupMemberAdapter adapter;
    protected String mGroupId;   // 当前聊天的 ID
    protected String mImGroup;//当前聊天的群组编号

    private boolean isMaster; //是否是群主

    List<GroupUserBean> frindLists = null;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_group_members;
    }


    @Override
    protected void init() {
        mBinding = (ActivityGroupMembersBinding) viewDataBinding;
        initData();
        initView();
    }

    private void initData() {
        mGroupId = getIntent().getStringExtra(Constants.EXTRA_GROUPBEAN_ID);
        mImGroup = getIntent().getStringExtra(Constants.EXTRA_GROUP_IM_ID);
        isMaster = getIntent().getBooleanExtra(Constants.EXTRA_GROUP_ROLE, false);
        frindLists = new ArrayList<>();
        getMvpPresenter().findGroupAllUser(mGroupId, this);
    }

    private void initView() {
        StatusBarUtil.setLightMode(this);
        mBinding.titleView.setLeftClickListener(view -> finish());
        mBinding.titleView.setTitleText(getString(R.string.gm_title_name));

        mBinding.rvGroupMembers.setHasFixedSize(true);
        mBinding.rvGroupMembers.setNestedScrollingEnabled(false);
        mBinding.rvGroupMembers.setLayoutManager(new LinearLayoutManager(this));

        adapter = new GroupMemberAdapter(R.layout.adapter_group_members, isMaster);
        adapter.openLoadAnimation();

        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.tv_remove && adapter.getData() != null && adapter.getData().size() > 0) {
                    GroupUserBean bean = (GroupUserBean) adapter.getData().get(position);
                    showRemoveDialog(bean);
                }
            }
        });
        mBinding.rvGroupMembers.setAdapter(adapter);
    }

    //移除群员对话框
    private void showRemoveDialog(GroupUserBean member) {
        EasyDialog.init()
                .setLayoutId(R.layout.dialog_group_remove_member)
                .setConvertListener(new ViewConvertListener() {
                    @Override
                    public void convertView(ViewHolder holder, final BaseEasyDialog dialog) {
                        String target = member.getNickname();
                        String content = String.format(getResources().getString(R.string.gm_dialog_remove_content), target);
                        holder.setText(R.id.tv_remove_content, content);
                        holder.getView(R.id.tv_remove_ok).setOnClickListener(v -> {
                            getMvpPresenter().removeSingleFromGroup(String.valueOf(mGroupId), mImGroup, member.getImusername(), String.valueOf(member.getId()), GroupMembersActivity.this);
                            dialog.dismiss();
                        });
                        holder.getView(R.id.tv_remove_cancel).setOnClickListener(v -> {
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
    public void findGroupAllUserSuccess(List<GroupUserBean> list) {
        frindLists.clear();
        if (null != list && list.size() > 0) {
            for (GroupUserBean bean : list) {
                String name = bean.getNickname();
                String pinyin = StringUtil.getPinYin(name);
                String Fpinyin = pinyin.substring(0, 1).toUpperCase();
                // 正则表达式，判断首字母是否是英文字母
                if (Fpinyin.matches("[A-Z]")) {
                    bean.setIndex(Fpinyin);
                } else {
                    bean.setIndex("#");
                }
                frindLists.add(bean);
            }
        }
        adapter.setNewData(frindLists);
    }

    @Override
    public void deleteMemberSuccess() {
        showToast("移出成功");
        getMvpPresenter().findGroupAllUser(mGroupId, this);
        EventBus.getDefault().post(new EventBean.GroupUpdate(0));//刷新群信息页面
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }
}
