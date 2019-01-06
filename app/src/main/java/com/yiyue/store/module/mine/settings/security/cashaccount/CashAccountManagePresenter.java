package com.yiyue.store.module.mine.settings.security.cashaccount;

import android.content.Context;
import android.text.TextUtils;

import com.yiyue.store.api.SettingsApi;
import com.yiyue.store.api.StoreAuthApplyApi;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.model.vo.bean.CashAliBean;
import com.yiyue.store.model.vo.bean.StoreAuthBean;
import com.yiyue.store.model.vo.result.CashAliResult;
import com.yiyue.store.model.vo.result.StoreAuthResult;
import com.yl.core.component.net.exception.ApiException;

import java.util.ArrayList;

/**
 * Created by lyj on 2018/11/8.
 */
public class CashAccountManagePresenter extends BasePresenter<CashAccountManageView>{


    //当前账户实名信息
    public void getStoreAuthDTO(Context context) {
        new StoreAuthApplyApi().getStoreAuthDTO(new YLRxSubscriberHelper<StoreAuthResult>(context,true) {
            @Override
            public void _onNext(StoreAuthResult result) {
                StoreAuthBean cashAliBeans = result.getData();
                getMvpView().getUserInfoSuccess(cashAliBeans);
            }

            @Override
            public void _onError(ApiException error) {
                super._onError(error);
                getMvpView().getUserInfoFail();
            }
        });
    }

    //当前支付宝绑定账户
    public void extractAccount(String bindType, Context context) {
        new SettingsApi().extractAccount(bindType, new YLRxSubscriberHelper<CashAliResult>(context,true) {
            @Override
            public void _onNext(CashAliResult result) {
                ArrayList<CashAliBean> cashAliBeans = result.getData();
                getMvpView().onextractaLIAccountSuccess(cashAliBeans);
            }

        });
    }

    //当前银行卡绑定账户
    public void extractBankAccount(String bindType, Context context) {
        new SettingsApi().extractAccount(bindType, new YLRxSubscriberHelper<CashAliResult>(context,true) {
            @Override
            public void _onNext(CashAliResult result) {
                ArrayList<CashAliBean> cashAliBeans = result.getData();
                getMvpView().onextractBankAccountSuccess(cashAliBeans);
            }

        });
    }

    //绑定账户
    public void bindAccount(String accountNo,String bindType,String branch,
                            String realName,String shortName, String stylistId,Context context) {
        if (TextUtils.isEmpty(realName)) {
            getMvpView().showToast("请输入真实姓名");
            return;
        }
        if (bindType.equals("ALI")){
            if (TextUtils.isEmpty(accountNo)) {
                getMvpView().showToast("请输入支付宝账户号");
                return;
            }
        }else {
            if (TextUtils.isEmpty(shortName)) {
                getMvpView().showToast("请选择发卡银行");
                return;
            }
            if (TextUtils.isEmpty(branch)) {
                getMvpView().showToast("请输入发卡支行");
                return;
            }
            if (TextUtils.isEmpty(accountNo)) {
                getMvpView().showToast("请输入银行卡号");
                return;
            }
        }

        new SettingsApi().bindAccount( accountNo, bindType, branch,
                realName, shortName,  stylistId,new YLRxSubscriberHelper<BaseResponse>(context,true) {
                    @Override
                    public void _onNext(BaseResponse result) {
                        getMvpView().showToast("支付宝绑定成功");
                        getMvpView().onUnBindSuccess();
                    }

                    @Override
                    public void _onError(ApiException error) {
                        super._onError(error);
                        getMvpView().showToast("支付宝绑定失败");
                    }
                });
    }

    //解除绑定账户
    public void unbindAccount(String bindId,Context context) {
        new SettingsApi().unBind( bindId, new YLRxSubscriberHelper<BaseResponse>(context,true) {
            @Override
            public void _onNext(BaseResponse result) {
                getMvpView().showToast("解除支付宝绑定成功");
                getMvpView().onUnBindSuccess();
            }

            @Override
            public void _onError(ApiException error) {
                super._onError(error);
                getMvpView().showToast("解除支付宝绑定失败");
            }

        });
    }

}
