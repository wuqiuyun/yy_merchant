package com.yiyue.store.module.home.store;

import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.bean.ReCodeBean;
import com.yiyue.store.model.vo.bean.StoreManageNexusStyScroolBean;
import com.yiyue.store.model.vo.bean.StoreManageScopeBean;
import com.yiyue.store.model.vo.bean.StoreManageScopeInfoBean;

import java.util.ArrayList;

/**
 * Created by zm on 2018/10/12.
 */
public interface IStoreManagerView extends IBaseView {
    /**
     * 获取门店顾客评价成功
     */
    void getStoreScoreSucceed(StoreManageScopeBean storeManageScopeBean);

    /**
     * 获取门店服务范围成功
     */
    void getStoreServerScoreSucceed(StoreManageScopeBean storeManageScopeBean);

    /**
     * 获取入驻/签约理发师成功
     */
    void getNexusStyScroolSucceed(ArrayList<StoreManageNexusStyScroolBean> storeManageNexusStyScroolBean);

    /**
     * 获取门店位置信息成功
     */
    void getStoreInfoSucceed(StoreManageScopeInfoBean storeManageScopeInfoBean);

    /**
     * 修改门店收藏状态成功
     */
    void updateCollectionTypeSuc();

    void findReCodeSuc(ReCodeBean recode);

}
