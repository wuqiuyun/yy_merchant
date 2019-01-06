package com.yiyue.store.module.mine.wallet.bill.card;


import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.bean.BankDetailsBean;

/**
 * Created by wqy on 2018/12/18.
 */

public interface ICardDetailsView extends IBaseView {
    void showBanksSuccess(BankDetailsBean bean);

    void showBanksFail();

    void showBanksSumSuccess(BankDetailsBean bean);

    void showBanksSumFail();
}
