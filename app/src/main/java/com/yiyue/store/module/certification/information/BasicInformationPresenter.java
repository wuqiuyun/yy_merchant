package com.yiyue.store.module.certification.information;

import com.yiyue.store.api.StoreUserApi;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.vo.requestbody.DoStoreDataRequestBody;

/**
 * Created by lvlong on 2018/10/9.
 */
public class BasicInformationPresenter extends BasePresenter<BasicInformationView> {

    /**
     * 提交基本信息
     * @param requestBody
     */
    public void doStoreData(DoStoreDataRequestBody requestBody) {
        if (!requestBody.checkStoreParams()) {
            return;
        }
        new StoreUserApi().doStoreData(requestBody, new YLRxSubscriberHelper<BaseResponse>() {
            @Override
            public void _onNext(BaseResponse baseResponse) {
                // 设置店铺id
                AccountManager.getInstance().setStoreId((String) baseResponse.getData());
                getMvpView().onSubmitDataSuccess();
            }
        });
    }
}
