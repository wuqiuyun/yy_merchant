package com.yiyue.store.module.mine.info;

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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zm on 2018/9/29.
 */
public class UserInfoPresenter extends BasePresenter<IUserInfoView>{
    private StoreCenterInfoApi mInfoApi;

    private StoreCenterInfoApi getInfoApi() {
        return mInfoApi == null ? new StoreCenterInfoApi() : mInfoApi;
    }

    /**
     * 上传文件
     * @param filePath 文件地址
     */
    public void uploadImage(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            getMvpView().showToast("文件不存在.");
            return;
        }
        List<String> filePaths = new ArrayList<>();
        filePaths.add(filePath);
        new FileApi().uploadFile(filePaths, new YLRxSubscriberHelper<UploadImageResult>() {
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

    /**
     * 修改门店环境照片
     * @param imgs
     */
    public void updateEnvironmentImg(List<String> imgs) {
        if (imgs == null || imgs.isEmpty()) {
            getMvpView().showToast("上传失败，请稍后再试");
            return;
        }
        getInfoApi().updateEnvironmentImg(imgs, new YLRxSubscriberHelper<BaseResponse>() {
            @Override
            public void _onNext(BaseResponse baseResponse) {
                getMvpView().showToast("环境照修改成功.");
                getMvpView().onUpdateEnvironmentImgSuccess();
            }
        });
    }

    /**
     * 更新门店头像照片
     * @param imageUrl
     */
    public void updateHeadImg(String imageUrl) {
        if (TextUtils.isEmpty(imageUrl)) {
            getMvpView().showToast("更新头像失败，请稍后再试");
            return;
        }
        getInfoApi().updateHeadImg(imageUrl, new YLRxSubscriberHelper<BaseResponse>() {
            @Override
            public void _onNext(BaseResponse baseResponse) {
                getMvpView().showToast("头像更新成功.");
                UserBean userBean = AccountManager.getInstance().getAccount();
                userBean.setHeadImg(imageUrl);
                AccountManager.getInstance().updateAccount(userBean, true);
                getMvpView().onUpdateHeadImgSuccess();
            }
        });
    }

    /**
     * 更新工位
     * @param storeStation
     */
    public void updateStation(String storeStation) {
        if (TextUtils.isEmpty(storeStation)) {
            getMvpView().showToast("工位数不能少于1");
            return;
        }
        getInfoApi().updateStation(storeStation, new YLRxSubscriberHelper<BaseResponse>() {
            @Override
            public void _onNext(BaseResponse baseResponse) {
                getMvpView().onUpdateStationSuccess();
            }
        });
    }

    /**
     * 更新门店名
     * @param storeName
     */
    public void updateStoreName(String storeName) {
        if (TextUtils.isEmpty(storeName)) {
            getMvpView().showToast("名店名不能为空");
            return;
        }
        getInfoApi().updateStoreName(storeName, new YLRxSubscriberHelper<BaseResponse>() {
            @Override
            public void _onNext(BaseResponse baseResponse) {
                AccountManager.getInstance().setStoreName(storeName);
                getMvpView().onUpdateStoreNameSuccess();
            }
        });
    }
}
