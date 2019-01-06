package com.yiyue.store.module.im.transfer.transrecords;

import android.app.Activity;
import android.text.TextUtils;

import com.yiyue.store.api.RedBagApi;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.model.vo.result.RedBagSendResult;
import com.yiyue.store.module.im.transfer.TransferAccountsView;

/**
 * Created by zhangzz on 2018/11/6.
 */
public class TransferRecordsPresenter extends BasePresenter<TransferRecordsView> {
    public void sendTransferReq(String receiveUserId, String remark, String sendUserId, String sendamount, Activity activity) {
        if (TextUtils.isEmpty(receiveUserId)) {
            getMvpView().showToast("receiveUserId不能为空");
            return;
        }

        if (TextUtils.isEmpty(sendUserId)) {
            getMvpView().showToast("sendUserId不能为空");
            return;
        }

        if (TextUtils.isEmpty(sendamount)) {
            getMvpView().showToast("金额不能为空");
            return;
        }
        new RedBagApi().sendTransfer(receiveUserId, remark, sendUserId, sendamount, new YLRxSubscriberHelper<RedBagSendResult>(activity, true) {
            @Override
            public void _onNext(RedBagSendResult baseResponse) {
                getMvpView().requestSuccess(baseResponse);
            }
        });
    }
}
