package com.yiyue.store.module.im.addfriend;

import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.bean.daobean.UserFriendsBean;

import java.util.List;

/**
 * Created by zhangzz on 2018/10/16.
 */
public interface AddFriendView extends IBaseView {
    void onSearchUserSuccess(List<UserFriendsBean> data);
}
