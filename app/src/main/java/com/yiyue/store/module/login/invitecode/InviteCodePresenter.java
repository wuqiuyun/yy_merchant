package com.yiyue.store.module.login.invitecode;

import android.text.TextUtils;

import com.yiyue.store.api.StoreUserApi;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.helper.AccountManager;

/**
 * Created by lvlong on 2018/9/27.
 */
public class InviteCodePresenter extends BasePresenter<InviteCodeView> {


    /**
     * 提交邀请码
     * @param inviteCode 邀请码
     */
    public void submitInviteCode(String inviteCode) {
        if (TextUtils.isEmpty(inviteCode)) {
            getMvpView().showToast("邀请码不能为空");
            return;
        }

        new StoreUserApi().inviteFriends(AccountManager.getInstance().getAccount().getId(),
                inviteCode, new YLRxSubscriberHelper<BaseResponse>() {
            @Override
            public void _onNext(BaseResponse baseResponse) {

                getMvpView().onInviteCodeSuccess();
            }
        });
    }
}
