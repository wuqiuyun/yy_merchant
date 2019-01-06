package com.yiyue.store.module.im.groups.groupsedit;

import android.text.TextUtils;

import com.yl.core.component.net.exception.ApiException;
import com.yiyue.store.api.GroupApi;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;

/**
 * Created by Lizhuo on 2018/10/12.
 */
public class GroupsEditPresenter extends BasePresenter<GroupsEditView> {
    /**
     * 修改群组资料
     */
    public void updateGroupInfo(String id, String name, String describe, String membersonly) {
        if (TextUtils.isEmpty(id)) {
            getMvpView().showToast("群组id不能为空");
            return;
        }
        update(id, name, describe, membersonly);
    }

    private void update(String id, String name, String describe, String membersonly) {
        if (TextUtils.isEmpty(id)) {
            getMvpView().showToast("群组id不能为空");
            return;
        }

        new GroupApi().updateGroup(id, name, describe, membersonly, new YLRxSubscriberHelper<BaseResponse>() {
            @Override
            public void _onNext(BaseResponse baseResponse) {
                getMvpView().updateSuccess();
            }

            @Override
            public void _onError(ApiException error) {
                getMvpView().updateFail();
            }
        });
    }

    /**
     * 解散群
     */
    public void deleteGroup(String id, String imgroup) {
        if (TextUtils.isEmpty(id)) {
            getMvpView().showToast("群组id不能为空");
            return;
        }
        new GroupApi().deleteGroup(id, imgroup, new YLRxSubscriberHelper<BaseResponse>() {
            @Override
            public void _onNext(BaseResponse baseResponse) {
                getMvpView().deleteSuccess();
            }

            @Override
            public void _onError(ApiException error) {
                getMvpView().deleteFail();
            }
        });
    }
}
