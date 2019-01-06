package com.yiyue.store.module.certification;

import android.content.Context;

import com.yiyue.store.api.FileApi;
import com.yiyue.store.api.StoreAuthApplyApi;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.model.vo.requestbody.StoreAuthApplyRequestBody;
import com.yiyue.store.model.vo.result.UploadImageResult;
import com.yl.core.component.net.exception.ApiException;

import java.util.List;

/**
 * Created by lvlong on 2018/9/20.
 */
public class CertificationPresenter extends BasePresenter<ICertificationView> {

    /**
     * 提交认证信息
     * @param requestBody 认证信息
     */
    public void submitCertiData(Context context, StoreAuthApplyRequestBody requestBody) {
        if (!requestBody.checkParams()) {
            return;
        }
        new StoreAuthApplyApi().save(requestBody, new YLRxSubscriberHelper<BaseResponse>(context, true) {
            @Override
            public void _onNext(BaseResponse baseResponse) {
                getMvpView().onSubmitCertiDataSuccess();
            }
        });
    }

    /**
     * 上传文件
     * @param filePaths 文件地址
     */
    public void uploadImage(Context context, List<String> filePaths) {
        if (filePaths == null || filePaths.isEmpty()) {
            ToastUtils.shortToast("文件不存在");
            return;
        }

        new FileApi().uploadFile(filePaths, new YLRxSubscriberHelper<UploadImageResult>(context, true) {
            @Override
            public void _onNext(UploadImageResult baseResponse) {
                getMvpView().onUploadFileSuccess(baseResponse.getData());
            }

            @Override
            public void _onError(ApiException error) {
                super._onError(error);

            }
        });
    }
}
