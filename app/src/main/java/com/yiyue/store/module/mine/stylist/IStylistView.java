package com.yiyue.store.module.mine.stylist;

import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.bean.AreaBean;
import com.yiyue.store.model.vo.bean.StylistBean;
import com.yiyue.store.model.vo.bean.StylistNumBean;

import java.util.List;

/**
 * Created by zm on 2018/10/10.
 */
public interface IStylistView extends IBaseView{
    void getStylistSuccess(List<StylistBean> stylistBeanList);
    void getAreaByStoreId(List<AreaBean> areaBeans);
    void getStylistFail();
    void onGetStoreStylistNumber(StylistNumBean bean);
}
