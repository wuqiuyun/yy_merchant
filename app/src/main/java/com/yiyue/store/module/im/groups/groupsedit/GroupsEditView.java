package com.yiyue.store.module.im.groups.groupsedit;

import com.yiyue.store.base.mvp.IBaseView;

/**
 * Created by Lizhuo on 2018/10/12.
 */
public interface GroupsEditView extends IBaseView {
    void updateSuccess();

    void updateFail();

    void deleteSuccess();

    void deleteFail();
}
