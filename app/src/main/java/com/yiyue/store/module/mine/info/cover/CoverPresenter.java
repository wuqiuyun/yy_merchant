package com.yiyue.store.module.mine.info.cover;

import android.content.Context;
import android.text.TextUtils;

import com.yiyue.store.api.FileApi;
import com.yiyue.store.api.StoreCenterInfoApi;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.helper.AccountManager;
import com.yiyue.store.model.vo.bean.UserBean;
import com.yiyue.store.model.vo.result.GetStoreCenterInfoResult;
import com.yiyue.store.model.vo.result.UploadImageResult;
import com.yiyue.store.module.mine.info.IUserInfoView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zm on 2018/9/29.
 */
public class CoverPresenter extends BasePresenter<ICoverView>{
    private StoreCenterInfoApi mInfoApi;

    private StoreCenterInfoApi getInfoApi() {
        return mInfoApi == null ? new StoreCenterInfoApi() : mInfoApi;
    }

    /**
     * 上传文件
     * @param filePath 文件地址
     */
    public void uploadImage(String filePath,Context context) {
        if (TextUtils.isEmpty(filePath)) {
            getMvpView().showToast("文件不存在.");
            return;
        }
        List<String> filePaths = new ArrayList<>();
        filePaths.add(filePath);
        new FileApi().uploadFile(filePaths, new YLRxSubscriberHelper<UploadImageResult>(context,true) {
            @Override
            public void _onNext(UploadImageResult baseResponse) {
                getMvpView().onUploadFileSuccess(baseResponse.getData().get(0));
            }
        });
    }

    /**
     * 上传文件
     * @param filePaths 文件地址
     */
    public void uploadStorephotos(Context context, List<String> filePaths) {
        if (filePaths == null || filePaths.isEmpty()) {
            ToastUtils.shortToast("文件不存在");
            return;
        }

        new FileApi().uploadFile(filePaths, new YLRxSubscriberHelper<UploadImageResult>(context, true) {
            @Override
            public void _onNext(UploadImageResult baseResponse) {
                getMvpView().onUploadStorePhotosSuccess(baseResponse.getData());
            }
        });
    }

    /**
     * 查询个人资料
     */
    public void getStoreCenterInfo() {
        getInfoApi().getStoreCenterInfo(new YLRxSubscriberHelper<GetStoreCenterInfoResult>() {
            @Override
            public void _onNext(GetStoreCenterInfoResult baseResponse) {
                getMvpView().onGetStoreCenterInfoSuccess(baseResponse.getData());
            }
        });
    }

    /**
     * 修改封面照片
     * @param bannerPath
     */
    public void updateCoverImg(String bannerPath) {
        if (TextUtils.isEmpty(bannerPath)) {
            getMvpView().showToast("上传失败，请稍后再试");
            return;
        }
        List<String> paths = new ArrayList<>();
        paths.add(bannerPath);
        getInfoApi().updateCoverImg(paths, new YLRxSubscriberHelper<BaseResponse>() {
            @Override
            public void _onNext(BaseResponse baseResponse) {
                getMvpView().showToast("封面照更新成功.");
                getMvpView().onUpdateCoverImgSuccess();
            }
        });
    }
}
