package com.yiyue.store.module.home.store;

import com.yiyue.store.api.RecomUserApi;
import com.yiyue.store.api.StoreManageApi;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.model.vo.bean.StoreManageNexusStyScroolBean;
import com.yiyue.store.model.vo.bean.StoreManageScopeBean;
import com.yiyue.store.model.vo.bean.StoreManageScopeInfoBean;
import com.yiyue.store.model.vo.result.FindReCodeResult;
import com.yiyue.store.model.vo.result.StoreManageNexusStyScroolResult;
import com.yiyue.store.model.vo.result.StoreManageScopeInfoResult;
import com.yiyue.store.model.vo.result.StoreManageScopeResult;
import com.yl.core.component.net.exception.ApiException;

import java.util.ArrayList;

/**
 * Created by zm on 2018/10/12.
 */
public class StoreManagerPresenter extends BasePresenter<IStoreManagerView> {

    /**
     * 获取我的推荐码
     */
    public void findReCode(){
        new RecomUserApi().findReCode(new YLRxSubscriberHelper<FindReCodeResult>() {
            @Override
            public void _onNext(FindReCodeResult findReCodeResult) {
                if (null != findReCodeResult.getData()) getMvpView().findReCodeSuc(findReCodeResult.getData());
            }

            @Override
            public void _onError(ApiException error) {
                super._onError(error);
            }
        });
    }
    
    /**
     * 我的管理门店入驻/签约理发师（名称、图片）
     *
     * nexus (string, optional): 入驻/签约，0入驻，1签约 ,3全部
     * storeId (integer, optional): 门店id（非本人门店必传） ,
     * userId (integer, optional): 用户id
     * */
    public void getNexusStyScrool(int nexus,String storeId) {
        new StoreManageApi().getNexusStyScrool(nexus,storeId,new YLRxSubscriberHelper<StoreManageNexusStyScroolResult>() {
            @Override
            public void _onNext(StoreManageNexusStyScroolResult baseResponse) {
                ArrayList<StoreManageNexusStyScroolBean> storeManageNexusStyScroolBean = baseResponse.getData();
                // callback
                getMvpView().getNexusStyScroolSucceed(storeManageNexusStyScroolBean);
            }
        });
    }

    /**
     * 门店位置信息
     *
     * lat (number, optional): 用户定位纬度 ,
     * lng (number, optional): 用户定位经度 ,
     * storeId (integer, optional): 门店id ,
     * userId (integer, optional): 用户id
     * */
    public void getStoreInfo(String lat, String lng,String storeId) {
        new StoreManageApi().getStoreInfo(lat,lng,storeId,new YLRxSubscriberHelper<StoreManageScopeInfoResult>() {
            @Override
            public void _onNext(StoreManageScopeInfoResult baseResponse) {
                StoreManageScopeInfoBean storeManageScopeInfoBean = baseResponse.getData();
                // callback
                getMvpView().getStoreInfoSucceed(storeManageScopeInfoBean);
            }
        });
    }

    /**
     * 门店顾客评价
     *
     * storeId (integer, optional): 门店ID，非本人门店必传 ,
     * userId (integer, optional): 用户id
     * */
    public void getStoreScore(String storeId) {

        new StoreManageApi().getStoreScore(storeId,new YLRxSubscriberHelper<StoreManageScopeResult>() {
            @Override
            public void _onNext(StoreManageScopeResult baseResponse) {
                StoreManageScopeBean storeManageScopeBean = baseResponse.getData();
                // callback
                getMvpView().getStoreScoreSucceed(storeManageScopeBean);
            }
        });
    }

    /**
     * 门店服务范围
     *
     * storeId (integer, optional): 门店ID，非本人门店必传 ,
     * userId (integer, optional): 用户id
     * */
    public void getStoreServerScope(String storeId) {

        new StoreManageApi().getStoreServerScope(storeId,new YLRxSubscriberHelper<StoreManageScopeResult>() {
            @Override
            public void _onNext(StoreManageScopeResult baseResponse) {
                StoreManageScopeBean storeManageScopeBean = baseResponse.getData();
                // callback
                getMvpView().getStoreServerScoreSucceed(storeManageScopeBean);
            }
        });
    }
    
    public void updateCollectionType(String isCollection, String storeId,String userId){
        new StoreManageApi().updateCollectionType(isCollection, storeId, userId, new YLRxSubscriberHelper<BaseResponse>() {
            @Override
            public void _onNext(BaseResponse baseResponse) {
                getMvpView().updateCollectionTypeSuc();
            }
        });
    }

    @Override
    public void onDetachMvpView() {
        super.onDetachMvpView();
    }

}
