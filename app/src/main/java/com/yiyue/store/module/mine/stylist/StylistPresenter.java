package com.yiyue.store.module.mine.stylist;

import com.yiyue.store.api.AreaApi;
import com.yiyue.store.api.CollectionApi;
import com.yiyue.store.api.FootApi;
import com.yiyue.store.api.StoreApi;
import com.yiyue.store.api.StoreStylistApi;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.model.vo.result.GetAreaResult;
import com.yiyue.store.model.vo.result.StylistListResult;
import com.yiyue.store.model.vo.result.StylistNumResult;
import com.yl.core.component.net.exception.ApiException;

/**
 * Created by zm on 2018/10/10.
 */
public class StylistPresenter extends BasePresenter<IStylistView> {
    //我的收藏——美发师列表
    public void getStylistCollection(int page, int size, String userId) {
        new CollectionApi().getStylistCollection(page, size, userId, new YLRxSubscriberHelper<StylistListResult>() {
            @Override
            public void _onNext(StylistListResult result) {
                getMvpView().getStylistSuccess(result.getData());
            }

            @Override
            public void _onError(ApiException error) {
                super._onError(error);
                if(getMvpView()!=null)getMvpView().getStylistFail();
            }
        });
    }

    //我的足迹——美发师列表
    public void getStylistFoot(int page, int size, String userId) {
        new FootApi().getStylistFoot(page, size, userId, new YLRxSubscriberHelper<StylistListResult>() {
            @Override
            public void _onNext(StylistListResult result) {
                if (null != result.getData()) {
                    getMvpView().getStylistSuccess(result.getData());
                } else {
                    getMvpView().getStylistFail();
                }
            }

            @Override
            public void _onError(ApiException error) {
                super._onError(error);
                if(getMvpView()!=null)getMvpView().getStylistFail();
            }
        });
    }

    //我的美发师——美发师列表
    public void getMyStylist(int nexus, String storeId) {
        new StoreApi().getMyStylist(nexus, storeId, new YLRxSubscriberHelper<StylistListResult>() {
            @Override
            public void _onNext(StylistListResult result) {
                getMvpView().getStylistSuccess(result.getData());
            }

            @Override
            public void _onError(ApiException error) {
                super._onError(error);
                if(getMvpView()!=null)getMvpView().getStylistFail();
            }
        });
    }

    //邀请美发师——附近美发师列表
    public void getInviteNear(String cityId, String districtId, String distance, String storeId, int page) {
        new StoreStylistApi().inviteNear(cityId, districtId, distance, storeId, page, new YLRxSubscriberHelper<StylistListResult>() {
            @Override
            public void _onNext(StylistListResult result) {
                getMvpView().getStylistSuccess(result.getData());
            }

            @Override
            public void _onError(ApiException error) {
                super._onError(error);
                if(getMvpView()!=null)getMvpView().getStylistFail();
            }
        });
    }    //邀请美发师——综合排序列表

    public void getInviteSort(int sortType, int page, String storeId) {
        new StoreStylistApi().inviteSort(sortType, page, storeId, new YLRxSubscriberHelper<StylistListResult>() {
            @Override
            public void _onNext(StylistListResult result) {
                getMvpView().getStylistSuccess(result.getData());
            }

            @Override
            public void _onError(ApiException error) {
                super._onError(error);
                if(getMvpView()!=null)getMvpView().getStylistFail();
            }
        });
    }

    //邀请美发师——筛选序列表
    public void getInviteScreen(String coupon, String position, int page, String storeId) {
        new StoreStylistApi().inviteScreen(coupon, position, page, storeId, new YLRxSubscriberHelper<StylistListResult>() {
            @Override
            public void _onNext(StylistListResult result) {
                getMvpView().getStylistSuccess(result.getData());
            }

            @Override
            public void _onError(ApiException error) {
                super._onError(error);
                if(getMvpView()!=null)getMvpView().getStylistFail();
            }
        });
    }

    //通过门店ID获取区域
    public void getAreaByStoreId(String storeId) {
        new AreaApi().getAreaByStoreId(storeId, new YLRxSubscriberHelper<GetAreaResult>() {
            @Override
            public void _onNext(GetAreaResult result) {
                getMvpView().getAreaByStoreId(result.getData());
            }

            @Override
            public void _onError(ApiException error) {
                super._onError(error);
                if(getMvpView()!=null)getMvpView().getStylistFail();
            }
        });
    }

    //通过name搜索
    public void getInviteSearch(String nickName, int page, String storeId) {
        new StoreStylistApi().inviteSearch(nickName, page, storeId, new YLRxSubscriberHelper<StylistListResult>() {
            @Override
            public void _onNext(StylistListResult result) {
                getMvpView().getStylistSuccess(result.getData());
            }

            @Override
            public void _onError(ApiException error) {
                super._onError(error);
                if(getMvpView()!=null)getMvpView().getStylistFail();
            }
        });
    }

    //获取门店平台美发师和店内美发师数量
    public void getStoreStylistNumber() {
        new StoreApi().getStoreStylistNumber(new YLRxSubscriberHelper<StylistNumResult>() {
            @Override
            public void _onNext(StylistNumResult stylistNumResult) {
                getMvpView().onGetStoreStylistNumber(stylistNumResult.getData());
            }
        });
    }

    //搜索我的美发师
    public void storeStylistSearch(String nickName, int page, String storeId) {
        new StoreApi().storeStylistSearch(nickName, page, storeId, new YLRxSubscriberHelper<StylistListResult>() {
            @Override
            public void _onNext(StylistListResult result) {
                getMvpView().getStylistSuccess(result.getData());
            }

            @Override
            public void _onError(ApiException error) {
                super._onError(error);
                if(getMvpView()!=null)getMvpView().getStylistFail();
            }
        });
    }
}