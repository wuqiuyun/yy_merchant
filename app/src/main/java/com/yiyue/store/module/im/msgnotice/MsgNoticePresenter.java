package com.yiyue.store.module.im.msgnotice;

import com.yiyue.store.api.BulletinApi;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.model.vo.result.BulletinListResult;
import com.yl.core.component.net.exception.ApiException;

/**
 * Created by zm on 2018/9/19.
 */
public class MsgNoticePresenter extends BasePresenter<MsgNoticeView> {
    //获取公告列表
    public void getMsgList(int page, int size){
        new BulletinApi().findPageList(String.valueOf(page), String.valueOf(size), new YLRxSubscriberHelper<BulletinListResult>() {
            @Override
            public void _onNext(BulletinListResult bulletinListResult) {
                getMvpView().getMsgListSuc(bulletinListResult.getData());
            }

            @Override
            public void _onError(ApiException error) {
                getMvpView().getMsgListFail();
            }
        });
    }
}

