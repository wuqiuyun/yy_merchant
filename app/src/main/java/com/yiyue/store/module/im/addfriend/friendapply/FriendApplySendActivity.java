package com.yiyue.store.module.im.addfriend.friendapply;

import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivitySetRemakeBinding;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.AddFriendResBean;
import com.yiyue.store.model.vo.bean.EventBean;
import com.yiyue.store.model.vo.result.AddFriendResult;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by zhangzz on 2018/9/27
 * 加好友申请页面
 */
@CreatePresenter(FriendApplySendPresenter.class)
public class FriendApplySendActivity extends BaseMvpAppCompatActivity<FriendApplySendView, FriendApplySendPresenter> implements FriendApplySendView {
    private ActivitySetRemakeBinding mBinding;
    private String friendId;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_set_remake;
    }

    @Override
    protected void init() {
        StatusBarUtil.setLightMode(this);
        mBinding = (ActivitySetRemakeBinding) viewDataBinding;

        friendId = getIntent().getStringExtra(Constants.EXTRA_SERACH_USERID);

        mBinding.titleView.setLeftClickListener(v -> {
            finish();
        });

        initPage();

        initEvent();
    }

    private void initPage() {
        mBinding.titleView.setRightText(getString(R.string.ims_send));
        mBinding.titleView.setTitleText(getString(R.string.ims_friend_verifica));
        mBinding.afTvTablename.setText(getString(R.string.ims_friend_reason));
        mBinding.afEdit.setText("我是 " + AccountManager.getInstance().getNickName());//设置默认语句
    }

    private void initEvent() {
        mBinding.titleView.setRightTextClickListener(v -> {
            getMvpPresenter().addFriend(AccountManager.getInstance().getUserId() + "", AccountManager.getInstance().getAccount().getNickname(), friendId, mBinding.afEdit.getText().toString());
        });
    }

    @Override
    public void onAddFriendSuccess(AddFriendResult result) {
        if (result != null && result.getData() != null) {
            AddFriendResBean addFriendResBean = result.getData();
            if (addFriendResBean != null && 1 == addFriendResBean.getStatus()) {
                showToast(getString(R.string.add_friend_had_suc));
                EventBus.getDefault().post(new EventBean.FriendChangeEventBean(Constants.EVENT_FRIEND_CHANGE));
            } else {
                showToast(getString(R.string.add_friend_suc));
            }
        } else {
            showToast(getString(R.string.add_friend_suc));
        }

        finish();
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }
}
