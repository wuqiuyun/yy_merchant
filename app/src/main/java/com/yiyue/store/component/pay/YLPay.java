package com.yiyue.store.component.pay;

import android.app.Activity;

import com.yl.core.component.pay.PayChannel;
import com.yl.core.component.pay.data.AliPayData;
import com.yl.core.component.pay.data.WeiXinPayData;
import com.yl.core.component.pay.inf.IPay;
import com.yl.core.component.pay.inf.IPayData;
import com.yiyue.store.component.pay.imp.AliPay;
import com.yiyue.store.component.pay.imp.WeiXinPay;

/**
 * 云易链支付
 * <p>
 * Created by zm on 2018/10/24.
 */
public class YLPay {

    private YLPayData ylPayData;
    private Activity activity;
    private IPay iPay;

    public YLPay(Activity activity, YLPayData ylPayData) {
        this.ylPayData = ylPayData;
        this.activity = activity;
    }

    public void toPay() {
        //1、获取支付数据
        IPayData payData= createPayData();
        //2、创建支付渠道
        iPay = createPay();
        //3、设置支付数据
        iPay.setPayData(payData);
        //4、发起支付
        iPay.pay();
    }

    private IPayData createPayData(){
        IPayData payData = null;
        if(ylPayData.mPayChannel == PayChannel.ALI){
            payData = new AliPayData(ylPayData.mPayData).getPayData();
        }else if(ylPayData.mPayChannel == PayChannel.WEIXIN){
            payData = new WeiXinPayData(ylPayData.mPayData).getPayData();
        }else{
            //...
        }
        return payData;
    }
    private IPay createPay(){
        IPay payData = null;
        if(ylPayData.mPayChannel== PayChannel.ALI){
            payData = new AliPay(activity);
            payData.setPayCallback(ylPayData.mPayCallback);
        }else if(ylPayData.mPayChannel== PayChannel.WEIXIN){
            payData = new WeiXinPay(activity);
            payData.setPayCallback(ylPayData.mPayCallback);
        }else{
            //...
        }
        return payData;
    }
    public IPay getPayStrategy() {
        return iPay;
    }
}
