package com.yiyue.store.module.mine.wallet.withdraw.account;

import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.bean.CashAliBean;
import com.yiyue.store.model.vo.bean.StoreAuthBean;

import java.util.ArrayList;

/**
 * Created by lyj on 2018/10/30.
 */
public interface IAccountWithdrawView extends IBaseView {

    void onextractBankAccountSuccess(ArrayList<CashAliBean> cashAliBean);

    void getUserInfoSuccess(StoreAuthBean storeAuthBean);

    void getUserInfoFail();

}
