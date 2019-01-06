package com.yiyue.store.module.im.sharetofriend;

import android.app.Activity;
import android.text.TextUtils;

import com.yiyue.store.api.ContactsApi;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.model.vo.result.UserFriendsResult;
import com.yl.core.component.net.exception.ApiException;

/**
 * Created by zhangzz on 2018/10/17.
 */
public class ShareToFriendPresenter extends BasePresenter<ShareToFriendView> {
    /**
     * 查询用户的所有好友
     *
     * @param userId
     */
    public void findAllContacts(String userId, Activity activity) {
        if (TextUtils.isEmpty(userId)) {
            getMvpView().showToast("userId不能为空");
            return;
        }

        requestFindAllContacts(userId, activity);
    }

    /**
     * 查询用户的所有好友请求
     */
    private void requestFindAllContacts(String param, Activity activity) {
        new ContactsApi().requestFindContacts(param, new YLRxSubscriberHelper<UserFriendsResult>(activity, true) {
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
