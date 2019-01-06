package com.yiyue.store.model.constant;

import android.support.annotation.IntDef;

/**
 * 订单状态
 * <p>
 * Created by zm on 2018/9/27.
 */
public  class OrderStatus {
    public static final int ORDER_CONFIRM = 0; // 待确认
    public static final int ORDER_SERVICE = 1; // 待核销
    public static final int ORDER_COMPLETE = 2; // 已完成
    public static final int ORDER_REFUND = 3; // 已退款

    @IntDef({ORDER_CONFIRM, ORDER_SERVICE, ORDER_COMPLETE, ORDER_REFUND})
    public @interface OrderType {}
}
