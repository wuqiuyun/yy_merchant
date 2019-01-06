package com.yiyue.store.module.mine.wallet.recharge.pay;

import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.component.pay.imp.WeiXinPay;
import com.yiyue.store.model.vo.bean.WeiXinPayBean;

/**
 * Created by zm on 2018/10/9.
 */
public interface IPayView extends IBaseView {

    //微信充值成功
    void onWxRechargeCashSuccess();

    //支付宝充值成功
    void onALiRechargeCashSuccess(String json);

    //微信充值成功
    void onWeChatCashSuccess(WeiXinPayBean weiXinPay);

}
