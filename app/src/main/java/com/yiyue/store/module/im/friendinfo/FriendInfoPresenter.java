package com.yiyue.store.module.im.friendinfo;

import android.app.Activity;
import android.text.TextUtils;

import com.yiyue.store.api.ContactsApi;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.model.vo.result.FriendInfoResult;

/**
 * Created by zm on 2018/9/19.
 */
public class FriendInfoPresenter extends BasePresenter<FriendInfoView> {

    /**
     * 删除好友 解除IM用户的好友关系
     * * @param id 好友关系id
     */
    public void requestDeleteFriendSingle(Activity activity, String id) {
        if (TextUtils.isEmpty(id)) {
            getMvpView().showToast("id不能为空");
            return;
        }

        new ContactsApi().requestDeleteFriendSingle(id, new YLRxSubscriberHelper<BaseResponse>(activity, true) {
            @Override
            public void _onNext(BaseResponse result) {
//                result.getData();
                getMvpView().onDeleteFriendSingleSuccess();
            }
        });
    }

    /**
     * 屏蔽好友
     *
     * @param id 好友关系id
     */
    public void addToBlackList(Activity activity, String id) {
        if (TextUtils.isEmpty(id)) {
            getMvpView().showToast("id不能为空");
            return;
        }

        requestAddToBlackList(activity, id);
    }

    /**
     * 屏蔽好友
     */
    private void requestAddToBlackList(Activity activity, String id) {
        new ContactsApi().requestAddToBlackList(id, new YLRxSubscriberHelper<BaseResponse>(activity, true) {
            @Override
            public void _onNext(BaseResponse result) {
//                result.getData();
                getMvpView().onAddToBlackListSuccess();
            }
        });
    }


    /**
     * 解除屏蔽好友
     *
     * @param id 好友关系id
     */
    public void removeFromBlackList(Activity activity, String id) {
        if (TextUtils.isEmpty(id)) {
            getMvpView().showToast("id不能为空");
            return;
        }

        requestRemoveFromBlackList(activity, id);
    }

    /**
     * 解除屏蔽好友
     */
    private void requestRemoveFromBlackList(Activity activity, String id) {
        new ContactsApi().requestRemoveFromBlackList(id, new YLRxSubscriberHelper<BaseResponse>(activity, true) {
            @Override
            public void _onNext(BaseResponse result) {
//                result.getData();
                getMvpView().onRemoveFromBlackListSuccess();
            }
        });
    }

    /**
     * 查看好友用户信息
     *
     * @param userId   用戶ID
     * @param imusername 朋友环信id
     */
    public void getFriendInfo(Activity activity, String userId, String imusername) {
        if (TextUtils.isEmpty(userId)) {
            getMvpView().showToast("userId不能为空");
            return;
        }

        if (TextUtils.isEmpty(imusername)) {
            getMvpView().showToast("聊天id不能为空");
            return;
        }

        requestGetFriendInfo(activity, userId, imusername);
    }

    /**
     * 查看好友用户信息
     */
    private void requestGetFriendInfo(Activity activity, String userId, String imusername) {
        new ContactsApi().requestGetFriend(userId, imusername, new YLRxSubscriberHelper<FriendInfoResult>(activity, true) {
            @Override
            public void _onNext(FriendInfoResult result) {
//                result.getData();
                getMvpView().onGetFriendsSuccess(result.getData());
            }
        });
    }
}
