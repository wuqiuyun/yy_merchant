package com.yiyue.store.module.home.time;

import android.text.TextUtils;

import com.yiyue.store.api.StoreSettingApi;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.model.vo.bean.StoreSettingTimeBean;
import com.yiyue.store.model.vo.result.StoreSettingTimeResult;

/**
 * Created by lvlong on 2018/10/11.
 */
public class TimeManagePresenter extends BasePresenter<TimeManageView> {

    /**
     *  updateOrSave门店时间
     *
     * @param endtime 结束时间
     * @param open 开店，默认1 关店0
     * @param starttime 开始时间
     * @param station 工位
     * @param stationlock 工位锁定
     * @param storeId 门店ID
     * @param workday 营业日 1,2,3,4,5,6,7
     */
    public void updateOrSaveStoreTime(int open, String starttime,String endtime,int station,
                              int stationlock,String storeId,String workday,int id) {
        if (TextUtils.isEmpty(starttime)||TextUtils.isEmpty(endtime)){
            getMvpView().showToast("请选择营业时间");
            return;
        }
        if (workday==null||TextUtils.isEmpty(workday)){
            getMvpView().showToast("请选择营业周期");
            return;
        }
        updateOrSave(open, starttime, endtime, station, stationlock, storeId, workday , id);
    }


    /**
     * 根据店铺ID查询
     */
    public void getStoreSettingByStoreId(String storeId) {
        new StoreSettingApi().getStoreSettingByStoreId(storeId,new YLRxSubscriberHelper<StoreSettingTimeResult>() {
            @Override
            public void _onNext(StoreSettingTimeResult baseResponse) {
                StoreSettingTimeBean storeSettingTimeBean = baseResponse.getData();
                // callback
                getMvpView().getStoreTimeSuccess(storeSettingTimeBean);
            }
        });
    }

    /**
     *  updateOrSave门店时间
     *
     * @param endtime 结束时间
     * @param open 开店，默认1 关店0
     * @param starttime 开始时间
     * @param station 工位
     * @param stationlock 工位锁定
     * @param storeId 门店ID
     * @param workday 营业日 1,2,3,4,5,6,7
     */
    private void updateOrSave(int open, String starttime,String endtime,int station,
                              int stationlock,String storeId,String workday , int id) {
        new StoreSettingApi().updateOrSave(open,  starttime, endtime, station,
         stationlock, storeId, workday, id ,new YLRxSubscriberHelper<StoreSettingTimeResult>() {
            @Override
            public void _onNext(StoreSettingTimeResult baseResponse) {
                StoreSettingTimeBean storeSettingTimeBean = baseResponse.getData();
                // callback
                getMvpView().setStoreTimeSuccess(storeSettingTimeBean);
                getMvpView().showToast("保存成功");
            }
        });
    }

    @Override
    public void onDetachMvpView() {
        super.onDetachMvpView();
    }

}
