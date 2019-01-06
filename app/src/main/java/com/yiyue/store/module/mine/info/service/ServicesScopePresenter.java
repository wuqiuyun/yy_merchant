package com.yiyue.store.module.mine.info.service;

import com.yiyue.store.api.CategoryApi;
import com.yiyue.store.api.StoreCenterInfoApi;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.model.vo.result.GetCategoryResult;

import java.util.List;

/**
 * Created by lvlong on 2018/10/13.
 */
public class ServicesScopePresenter extends BasePresenter<ServicesScopeView> {

    /**
     * 获取平台类目
     */
    public void getCategoryAll() {
        new CategoryApi().getAll(new YLRxSubscriberHelper<GetCategoryResult>() {
            @Override
            public void _onNext(GetCategoryResult getCategoryResult) {
                getMvpView().onGetCategoryAllSuccess(getCategoryResult.getData());
            }
        });
    }

    /**
     * 更新服务范围
     * @param services
     */
    public void updateServices(List<Integer> services) {
        if (services == null || services.isEmpty()) {
            getMvpView().showToast("请至少选择一种服务类型.");
            return;
        }
        new StoreCenterInfoApi().updateServices(services, new YLRxSubscriberHelper<BaseResponse>() {
            @Override
            public void _onNext(BaseResponse baseResponse) {
                getMvpView().showToast("保存成功.");
                getMvpView().onUpdateServicesSuccess();
            }
        });
    }
}
