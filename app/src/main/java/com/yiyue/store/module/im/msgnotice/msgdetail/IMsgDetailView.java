package com.yiyue.store.module.im.msgnotice.msgdetail;

import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.bean.BulletinDetailBean;

/**
 * Created by Lizhuo on 2018/11/20.
 */
public interface IMsgDetailView extends IBaseView {
    void getDetailSuc(BulletinDetailBean detail);
    
    void getDetailFail();
}
