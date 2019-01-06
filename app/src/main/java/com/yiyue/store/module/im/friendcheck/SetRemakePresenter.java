package com.yiyue.store.module.im.friendcheck;

import android.text.TextUtils;

import com.yiyue.store.api.ContactsApi;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;

/**
 * Created by zm on 2018/9/19.
 */
public class SetRemakePresenter extends BasePresenter<SetRemakeView> {
    /**
     * 设置好友备注
     *
     * @param id 好友关系id
     */
    public void updateNickName(String id, String nickname) {
        if (TextUtils.isEmpty(id)) {
            getMvpView().showToast("id不能为空");
            return;
        }

        if (TextUtils.isEmpty(nickname)) {
            getMvpView().showToast("nickname不能为空");
            return;
        }

        requestUpdateNickName(id, nickname);
    }

    /**
     * 设置好友备注请求
     */
    private void requestUpdateNickName(String id, String nickname) {
        new ContactsApi().requestUpdateNickName(id, nickname, new YLRxSubscriberHelper<BaseResponse>() {
            @Override
            public void _onNext(BaseResponse result) {
                getMvpView().onUpdateNickNameSuccess();
            }
        });
    }
}
