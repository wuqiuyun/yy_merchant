package com.yiyue.store.module.mine.settings.security.cashaccount.addbankcard;

import android.content.Context;
import android.text.TextUtils;

import com.yiyue.store.api.SettingsApi;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.model.vo.bean.BankCardBean;
import com.yiyue.store.model.vo.result.BankCardResult;
import com.yl.core.component.net.exception.ApiException;

import java.util.ArrayList;

/**
 * Created by lyj on 2018/11/8.
 */
public class AddBankCardPresenter extends BasePresenter<AddBankCardView>{

    //绑定账户
    public void bindAccount(String accountNo, String bindType, String branch,
                            String realName, String shortName, String stylistId, Context context) {
        if (bindType.equals("ALI")){
            if (TextUtils.isEmpty(realName)) {
                getMvpView().showToast("请输入真实姓名");
                return;
            }

            if (TextUtils.isEmpty(accountNo)) {
                getMvpView().showToast("请输入支付宝账户号");
                return;
            }
        }else {
            if (TextUtils.isEmpty(shortName)) {
                getMvpView().showToast("请选择开户银行");
                return;
            }
            if (TextUtils.isEmpty(branch)) {
                getMvpView().showToast("请输入开户支行");
                return;
            }
            if (TextUtils.isEmpty(accountNo)) {
                getMvpView().showToast("请输入银行卡号");
                return;
            }
            if (TextUtils.isEmpty(realName)) {
                getMvpView().showToast("请输入真实姓名");
                return;
            }
        }

        new SettingsApi().bindAccount( accountNo, bindType, branch,
                realName, shortName,  stylistId, new YLRxSubscriberHelper<BaseResponse>(context,true) {
                    @Override
                    public void _onNext(BaseResponse result) {
                        getMvpView().onBindSuccess();
                    }

                    @Override
                    public void _onError(ApiException error) {
                        super._onError(error);
                    }
                });
    }

    //获取所有银行
    public void getAllBank(Context context) {
        new SettingsApi().getAllBank( new YLRxSubscriberHelper<BankCardResult>(context,true) {
            @Override
            public void _onNext(BankCardResult result) {
                ArrayList<BankCardBean> bankCardBeans = result.getData();
                 getMvpView().onBankSuccess(bankCardBeans);
            }
        });
    }

}
