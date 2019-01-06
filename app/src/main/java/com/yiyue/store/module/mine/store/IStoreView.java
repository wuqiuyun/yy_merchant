package com.yiyue.store.module.mine.store;

import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.bean.StoreBean;

import java.util.List;

/**
 * Created by zm on 2018/10/10.
 */
public interface IStoreView extends IBaseView {
    void getStoreListSuccess(List<StoreBean> list);
    
    void getStoreListFail();
}
