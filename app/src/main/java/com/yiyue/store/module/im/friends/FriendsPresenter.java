package com.yiyue.store.module.im.friends;

import android.text.TextUtils;

import com.yiyue.store.api.ContactsApi;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.model.vo.result.UserFriendsResult;
import com.yl.core.component.net.exception.ApiException;

/**
 * Created by zhangzz on 2018/10/17.
 */
public class FriendsPresenter extends BasePresenter<FriendsView> {
    /**
     * 查询用户的所有好友
     *
     * @param userId
     */
    public void findAllContacts(String userId) {
        if (TextUtils.isEmpty(userId)) {
            getMvpView().showToast("userId不能为空");
            return;
        }

        requestFindAllContacts(userId);
    }

    /**
     * 查询用户的所有好友请求
     */
    private void requestFindAllContacts(String param) {
        new ContactsApi().requestFindContacts(param, new YLRxSubscriberHelper<UserFriendsResult>() {
            @Override
            public void _onNext(UserFriendsResult result) {
                getMvpView().onFindAllContactsSuccess(result.getData());
            }

            @Override
            public void _onError(ApiException error) {
                super._onError(error);
                getMvpView().onFindAllContactsFail();
            }
        });
    }
}
