package com.yiyue.store.module.im.groups.grouptransfer;

import android.text.TextUtils;

import com.yl.core.component.net.exception.ApiException;
import com.yiyue.store.api.GroupApi;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.model.vo.result.findGroupAllUserResult;

/**
 * Created by Lizhuo on 2018/10/15.
 */
public class GroupTransferPresenter extends BasePresenter<GroupTransferView> {
    /**
     * 获取该群中所有用户
     */
    public void findGroupAllUser(String groupId) {
        if (TextUtils.isEmpty(groupId)) {
            getMvpView().showToast("群组groupId不能为空");
            return;
        }

        new GroupApi().findGroupAllUser(groupId, new YLRxSubscriberHelper<findGroupAllUserResult>() {
            @Override
            public void _onNext(findGroupAllUserResult result) {
                getMvpView().findGroupAllUserSuccess(result.getData());
            }
        });
    }

    /**
     * 转让群给指定人
     *
     * @param id         群组id
     * @param imgroup    群组环信id
     * @param imusername 指定人环信id
     * @param userId     指定人id
     */
    public void transferGroupOwner(String id, String imgroup, String imusername, String userId) {
        if (TextUtils.isEmpty(id)) {
            getMvpView().showToast("群组id不能为空");
            return;
        }

        new GroupApi().transferGroupOwner(id, imgroup, imusername, userId, new YLRxSubscriberHelper<BaseResponse>() {
            @Override
            public void _onNext(BaseResponse baseResponse) {
                getMvpView().transferSuccess();
            }


            @Override
            public void _onError(ApiException error) {
                getMvpView().transferFail();
            }
        });
    }
}
