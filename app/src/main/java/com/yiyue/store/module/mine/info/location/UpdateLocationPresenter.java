package com.yiyue.store.module.mine.info.location;

import com.yiyue.store.api.StoreCenterInfoApi;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.model.vo.requestbody.UpdateAddressRequestBody;


/**
 * Create by zm on 2018/10/22.
 */

public class UpdateLocationPresenter extends BasePresenter<IUpdateLocationView> {

    /**
     * 提交地址信息
     * @param requestBody
     */
    public void doStoreData(UpdateAddressRequestBody requestBody) {
        if (!requestBody.checkStoreParams()) {
            return;
        }
        new StoreCenterInfoApi().updateLocation(requestBody, new YLRxSubscriberHelper<BaseResponse>() {
            @Override
            public void _onNext(BaseResponse baseResponse) {
                getMvpView().showToast("地址修改成功.");
                getMvpView().onUpdateAddrSuccess();
            }
        });
    }
}
