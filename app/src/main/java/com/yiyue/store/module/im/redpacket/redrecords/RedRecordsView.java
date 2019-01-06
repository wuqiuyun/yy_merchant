package com.yiyue.store.module.im.redpacket.redrecords;

import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.result.RedRecordResult;

/**
 * Created by zhangzz on 2018/11/6.
 */
public interface RedRecordsView extends IBaseView {
    void requestSuccess(RedRecordResult redRecordResult, boolean isRefresh);
    void requestFail();
}
