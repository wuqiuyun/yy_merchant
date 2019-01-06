package com.yl.core.component.pay.inf;

/**
 * Created by zm on 2018/10/24.
 */
public interface IPayData<T> {

    /**
     * 获取数据
     * @return
     */
    T getPayData();

    /**
     * 检查数据合法性
     * @return
     */
    boolean checkPayData();
}
