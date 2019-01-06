package com.yiyue.store.module.common.addimg;

import android.content.Context;
import android.text.TextUtils;

import com.yiyue.store.api.FileApi;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.model.vo.result.UploadImageResult;
import com.yl.core.component.log.DLog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zm on 2018/11/14.
 */
public class AddImagePresenter extends BasePresenter<IAddImageView>{

    /**
     * 上传文件
     * @param filePaths 文件地址集合
     */
    public void uploadImage(List<String> filePaths,Context context) {
        if (filePaths.size()==0) {
            ToastUtils.shortToast("文件不存在");
            return;
        }
        new FileApi().uploadFile(filePaths, new YLRxSubscriberHelper<UploadImageResult>(context,true) {
            @Override
            public void _onNext(UploadImageResult baseResponse) {
                getMvpView().onUploadFileSuccess(baseResponse.getData());
            }
        });
    }
}
