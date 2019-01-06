package com.yiyue.store.module.im.sysnotice;

import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.bean.SysMsgBean;

import java.util.List;

/**
 * Created by zm on 2018/9/19.
 */
public interface SysMsgView extends IBaseView {
//    void onFindAddFriendSuccess(List<SysMsgBean> data);

    void onReceiveAddFriendSuccess();

    void onReceiveAddGroupSuccess();
}
