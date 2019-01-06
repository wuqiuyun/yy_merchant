package com.yiyue.store.module.im.groupinfo;

import android.content.Context;
import android.text.TextUtils;

import com.yl.core.component.log.DLog;
import com.yiyue.store.api.GroupApi;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.model.vo.result.GroupResult;
import com.yiyue.store.model.vo.result.findGroupAllUserResult;

/**
 * Created by Lizhuo on 2018/10/17.
 */
public class GroupInfoPresenter extends BasePresenter<GroupInfoView> {
    /**
     * 获取该群中所有用户
     */
    public void findGroupAllUser(String groupId) {
        if (TextUtils.isEmpty(groupId)) {
            getMvpView().showToast("groupId不能为空");
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
     * 根据id获取群详情
     */
    public void findGroup(String groupId, String userId, Context context) {
        if (TextUtils.isEmpty(groupId)) {
            getMvpView().showToast("groupId不能为空");
            return;
        }
        if (TextUtils.isEmpty(userId)) {
            getMvpView().showToast("userId不能为空");
            return;
        }
        new GroupApi().findGroup(groupId, userId, new YLRxSubscriberHelper<GroupResult>(context, true) {
            @Override
            public void _onNext(GroupResult result) {
                getMvpView().findGroupSuccess(result.getData());
            }
        });
    }

    /**
     * 接收群消息
     */
    public void receiveMessage(long id) {
        if (TextUtils.isEmpty(id + "")) {
            getMvpView().showToast("id不能为空");
            return;
        }
        new GroupApi().receiveMessage(String.valueOf(id), new YLRxSubscriberHelper<BaseResponse>() {
            @Override
            public void _onNext(BaseResponse baseResponse) {
                getMvpView().showToast("群消息可接收");
                DLog.d("群消息可接收");
            }
        });
    }

    /**
     * 群消息免打扰
     */
    public void refuseMessage(long id) {
        if (TextUtils.isEmpty(id + "")) {
            getMvpView().showToast("id不能为空");
            return;
        }
        new GroupApi().refuseMessage(String.valueOf(id), new YLRxSubscriberHelper<BaseResponse>() {
            @Override
            public void _onNext(BaseResponse baseResponse) {
                getMvpView().showToast("群消息已屏蔽");
                DLog.d("群消息已屏蔽");
            }
        });
    }

    /**
     * 不需要验证时 直接申请入群
     */
    public void addSingleToGroup(String groupId, String imgroup, String imusername, String userId) {
        if (TextUtils.isEmpty(groupId)) {
            getMvpView().showToast("groupId不能为空");
            return;
        }
        if (TextUtils.isEmpty(imgroup)) {
            getMvpView().showToast("imgroup不能为空");
            return;
        }
        if (TextUtils.isEmpty(imusername)) {
            getMvpView().showToast("imusername不能为空");
            return;
        }
        if (TextUtils.isEmpty(userId)) {
            getMvpView().showToast("userId不能为空");
            return;
        }
        new GroupApi().addSingleToGroup(groupId, imgroup, imusername, userId, new YLRxSubscriberHelper<BaseResponse>() {
            @Override
            public void _onNext(BaseResponse baseResponse) {
                getMvpView().addSingleToGroupSuccess();
            }
        });
    }

    /**
     * 退出群组调用的是群组减人
     *
     * @param groupId    群组id
     * @param imgroup    群组编号
     * @param imusername 要移除群的用户对于的imusername
     * @param id         群组与好友关系id
     */
    public void removeSingleFromGroup(String groupId, String imgroup, String imusername, String id) {
        if (TextUtils.isEmpty(groupId)) {
            ToastUtils.shortToast("群组id不能为空");
            return;
        }
        if (TextUtils.isEmpty(imgroup)) {
            ToastUtils.shortToast("imgroup不能为空");
            return;
        }
        if (TextUtils.isEmpty(imusername)) {
            ToastUtils.shortToast("imusername不能为空");
            return;
        }

        if (TextUtils.isEmpty(id)) {
            ToastUtils.shortToast("群组与好友关系id不能为空");
            return;
        }

        new GroupApi().removeSingleFromGroup(groupId, imgroup, imusername, id, new YLRxSubscriberHelper<BaseResponse>() {
            @Override
            public void _onNext(BaseResponse baseResponse) {
                getMvpView().removeSingleFromGroupSuccess();
            }
        });
    }
}
