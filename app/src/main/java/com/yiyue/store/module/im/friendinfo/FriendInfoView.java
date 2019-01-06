package com.yiyue.store.module.im.friendinfo;

import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.bean.daobean.UserFriendsBean;

/**
 * Created by zm on 2018/9/19.
 */
public interface FriendInfoView extends IBaseView {
    void onDeleteFriendSingleSuccess();

    void onAddToBlackListSuccess();

    void onRemoveFromBlackListSuccess();

    void onGetFriendsSuccess(UserFriendsBean friendInfoBean);
}
