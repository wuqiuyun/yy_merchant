package com.yiyue.store.module.im.groupinfo;

import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.bean.daobean.GroupUserBean;
import com.yiyue.store.model.vo.bean.daobean.GroupBean;

import java.util.List;

/**
 * Created by Lizhuo on 2018/10/17.
 */
public interface GroupInfoView extends IBaseView {
    void findGroupAllUserSuccess(List<GroupUserBean> list);

//    void getGroupAllUserFail();

    void findGroupSuccess(GroupBean group);

//    void findGroupFail();

    void addSingleToGroupSuccess();

    void removeSingleFromGroupSuccess();
}
