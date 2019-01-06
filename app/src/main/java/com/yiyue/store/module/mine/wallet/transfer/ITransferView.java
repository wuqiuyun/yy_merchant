package com.yiyue.store.module.mine.wallet.transfer;

import com.yiyue.store.base.mvp.IBaseView;

/**
 * Created by zm on 2018/10/9.
 */
public interface ITransferView extends IBaseView {
    void onSuccess();

    void checkPasswordSuccess();

    //获取账户支付密码信息成功
    void oninitPayWordInfoSuccess(String json);

    //获取账户支付密码信息失败
    void oninitPayWordInfoFail();

}
