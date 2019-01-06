package com.yiyue.store.module.mine.settings.security.cashaccount;

import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.bean.CashAliBean;
import com.yiyue.store.model.vo.bean.ServerItemBean;
import com.yiyue.store.model.vo.bean.StoreAuthBean;

import java.util.ArrayList;

/**
 * Created by lyj on 2018/11/8.
 */
public interface CashAccountManageView extends IBaseView{
    void onextractaLIAccountSuccess(ArrayList<CashAliBean> cashAliBean);

    void onextractBankAccountSuccess(ArrayList<CashAliBean> cashAliBean);

    void onUnBindSuccess();

    void onSuccess(ArrayList<ServerItemBean> serverItemBeans);
    void onFail(int type);


    void getUserInfoSuccess(StoreAuthBean storeAuthBean);

    void getUserInfoFail();

}
