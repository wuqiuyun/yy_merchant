package com.yiyue.store.module.im.addfriend.friendapply;

import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.result.AddFriendResult;

/**
 * Created by zhangzz on 2018/10/16.
 */
public interface FriendApplySendView extends IBaseView {
    /**
     * 添加好友成功
     * @param result
     */
    void onAddFriendSuccess(AddFriendResult result);
}
