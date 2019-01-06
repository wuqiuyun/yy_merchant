package com.yiyue.store.module.home.orders.manager;

import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.bean.OrderManagerBean;

import java.util.ArrayList;

/**
 * Created by zm on 2018/12/17.
 */
public interface IOrderManagerView extends IBaseView {
    void setDatas(ArrayList<OrderManagerBean> datas);
}
