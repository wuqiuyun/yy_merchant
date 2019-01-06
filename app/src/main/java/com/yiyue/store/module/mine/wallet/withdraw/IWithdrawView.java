package com.yiyue.store.module.mine.wallet.withdraw;

import com.yiyue.store.base.mvp.IBaseView;

/**
 * Created by zm on 2018/10/8.
 */
public interface IWithdrawView extends IBaseView {
    //微信提现成功
    void onWxCashSuccess();

    //支付宝提现成功
    void onALiCashSuccess();
    void checkPasswordSuccess();
    void onInitPayWord(String json);
    //获取账户支付密码信息成功
    void oninitPayWordInfoSuccess(String json);

    //获取账户支付密码信息失败
    void oninitPayWordInfoFail();
}
