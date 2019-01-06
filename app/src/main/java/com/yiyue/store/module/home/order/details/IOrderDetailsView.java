package com.yiyue.store.module.home.order.details;

import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.bean.OrderDetailsBean;

/**
 * Created by zm on 2018/9/27.
 */
public interface IOrderDetailsView extends IBaseView{
    /**
     * 订单详情
     */
    void onGetOrderDetailsSuccess(OrderDetailsBean data);
}
