package com.yiyue.store.module.mine.works;

import com.yl.core.component.net.exception.ApiException;
import com.yiyue.store.api.CollectionApi;
import com.yiyue.store.api.FootApi;
import com.yiyue.store.api.StoreStylistApi;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.model.vo.result.OpusListResult;

/**
 * Created by zm on 2018/10/10.
 */
public class WorksPresenter extends BasePresenter<IWorksView> {
    //我的收藏——作品列表
    public void getOpusCollection(int page, int size, String userId) {
        new CollectionApi().getOpusCollection(page, size, userId, new YLRxSubscriberHelper<OpusListResult>() {
            @Override
            public void _onNext(OpusListResult result) {
                getMvpView().getOpusListSuccess(result.getData());
            }

            @Override
            public void _onError(ApiException error) {
                getMvpView().getOpusListFail();
            }
        });


    }

    //我的足迹——作品列表
    public void getOpusFoot(int page, int size, String userId) {
        new FootApi().getOpusFoot(page, size, userId, new YLRxSubscriberHelper<OpusListResult>() {
            @Override
            public void _onNext(OpusListResult result) {
                if (null != result.getData() && result.getData().size() > 0) {
                    getMvpView().getOpusListSuccess(result.getData());
                } else {
                    getMvpView().getOpusListFail();
                }
            }

            @Override
            public void _onError(ApiException error) {
                getMvpView().getOpusListFail();
            }
        });
    }
    
    //点击则增加查看数
    public void opusCount(long opusId, int type){
        new StoreStylistApi().oupsCount(String.valueOf(opusId), String.valueOf(type), new YLRxSubscriberHelper<BaseResponse>() {
            @Override
            public void _onNext(BaseResponse baseResponse) {

            }
        });
    }
}
