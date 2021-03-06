package com.yiyue.store.module.im.sharetofriend;

import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.bean.daobean.UserFriendsBean;

import java.util.List;

/**
 * Created by zm on 2018/9/19.
 */
public interface ShareToFriendView extends IBaseView {
    void onFindAllContactsSuccess(List<UserFriendsBean> data);
    void onFindAllContactsFail();
}
