package com.yiyue.store.module.im.msgnotice.msgdetail;

import com.yiyue.store.api.BulletinApi;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.model.vo.result.BulletinDetailResult;

/**
 * Created by Lizhuo on 2018/11/20.
 */
public class MsgDetailPresenter extends BasePresenter<IMsgDetailView> {
    public void getDetail(long id){
        if (id == 0){
            ToastUtils.shortToast("公告获取有误");
            return;
        }
        
        new BulletinApi().findBulletin(String.valueOf(id), new YLRxSubscriberHelper<BulletinDetailResult>() {
            @Override
            public void _onNext(BulletinDetailResult result) {
                if (result.getData() != null){
                    getMvpView().getDetailSuc(result.getData());
                }
            }
        });
    }
}
