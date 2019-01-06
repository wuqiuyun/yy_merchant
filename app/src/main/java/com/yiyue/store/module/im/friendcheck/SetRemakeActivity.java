package com.yiyue.store.module.im.friendcheck;

import android.text.TextUtils;

import com.yiyue.store.R;
import com.yiyue.store.base.BaseMvpAppCompatActivity;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.databinding.ActivitySetRemakeBinding;
import com.yiyue.store.model.constant.Constants;
import com.yiyue.store.model.vo.bean.EventBean;
import com.yl.core.component.mvp.factory.CreatePresenter;
import com.yl.core.util.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by zhangzz on 2018/9/27
 * 设置备注页面
 */
@CreatePresenter(SetRemakePresenter.class)
public class SetRemakeActivity extends BaseMvpAppCompatActivity<SetRemakeView, SetRemakePresenter> implements SetRemakeView {
    private ActivitySetRemakeBinding mBinding;
    private String friendRelationId = "";//好友关系id

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_set_remake;
    }

    @Override
    protected void init() {
        StatusBarUtil.setLightMode(this);
        mBinding = (ActivitySetRemakeBinding) viewDataBinding;
        friendRelationId = getIntent().getStringExtra(Constants.EXTRA_FRIEND_RELATION_ID);

        mBinding.titleView.setLeftClickListener(v -> {
            finish();
        });

        initEvent();
    }

    private void initEvent() {
        mBinding.titleView.setRightTextClickListener(v -> {
            String toStr = null;
            if (TextUtils.isEmpty(mBinding.afEdit.getText().toString())) {
                toStr = getString(R.string.ims_remake);
                ToastUtils.shortToast(toStr);
                return;
            }

            getMvpPresenter().updateNickName(friendRelationId, mBinding.afEdit.getText().toString());
        });
    }

    @Override
    public void onUpdateNickNameSuccess() {
        showToast(getString(R.string.ims_remake_sucs));
        EventBus.getDefault().post(new EventBean.FriendInfoUpdate(0));
        EventBus.getDefault().post(new EventBean.FriendChangeEventBean(Constants.EVENT_FRIEND_CHANGE));
        finish();
    }

    @Override
    public void showToast(String message) {
        ToastUtils.shortToast(message);
    }
}
