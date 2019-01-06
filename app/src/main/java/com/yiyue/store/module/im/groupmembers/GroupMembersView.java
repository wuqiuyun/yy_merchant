package com.yiyue.store.module.im.groupmembers;

import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.bean.daobean.GroupUserBean;

import java.util.List;

/**
 * Created by Lizhuo on 2018/10/18.
 */
public interface GroupMembersView extends IBaseView {
    void findGroupAllUserSuccess(List<GroupUserBean> list);

    void deleteMemberSuccess();
}
