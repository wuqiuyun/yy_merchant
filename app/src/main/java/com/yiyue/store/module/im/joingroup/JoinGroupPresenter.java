package com.yiyue.store.module.im.joingroup;

import android.text.TextUtils;

import com.yiyue.store.api.GroupApi;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;

/**
 * Created by zm on 2018/9/19.
 */
public class JoinGroupPresenter extends BasePresenter<JoinGroupView> {

    /**
     * 申请加入群组
     *
     * @param userId     申请人userid
     * @param nickname   申请人 昵称
     * @param path       申请人 头像路径
     * @param imusername 申请人 im帐号
     * @param groupId    群组id
     * @param friendId   群主userid
     * @param remarks    备注
     * @param imgroup    群组im编号
     */
    public void requestAddGroup(String userId, String nickname, String path, String imusername, String groupId, String friendId, String remarks, String imgroup) {
        if (TextUtils.isEmpty(userId)) {
            getMvpView().showToast("用户id不能为空");
            return;
        }

        if (TextUtils.isEmpty(imusername)) {
            getMvpView().showToast("用户imusername不能为空");
            return;
        }

        if (TextUtils.isEmpty(groupId)) {
            getMvpView().showToast("用户groupId不能为空");
            return;
        }
        new GroupApi().requestAddGroup(userId, nickname, path, imusername, groupId, friendId, remarks, imgroup, new YLRxSubscriberHelper<BaseResponse>() {
            @Override
            public void _onNext(BaseResponse baseResponse) {
                getMvpView().requestSuccess();
            }
        });
    }
}
