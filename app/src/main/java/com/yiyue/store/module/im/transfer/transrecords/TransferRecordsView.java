package com.yiyue.store.module.im.transfer.transrecords;

import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.result.RedBagSendResult;

/**
 * Created by zhangzz on 2018/11/6.
 */
public interface TransferRecordsView extends IBaseView {
    void requestSuccess(RedBagSendResult redBagSendResult);
}
