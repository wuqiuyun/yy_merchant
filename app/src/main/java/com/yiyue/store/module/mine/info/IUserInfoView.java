package com.yiyue.store.module.mine.info;

import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.result.GetStoreCenterInfoResult;

import java.util.List;

/**
 * Created by zm on 2018/9/29.
 */
public interface IUserInfoView extends IBaseView{
    /**
     * 文件上传成功
     * @param filePath
     */
    void onUploadFileSuccess(String filePath);

    /**
     * 获取门店个人资料成功回调
     */
    void onGetStoreCenterInfoSuccess(GetStoreCenterInfoResult.Data data);

    /**
     * 文件上传成功
     * @param filePaths
     */
    void onUploadStorePhotosSuccess(List<String> filePaths);

    /**
     * 更新封面照片成功回调
     */
    void onUpdateCoverImgSuccess();

    /**
     * 上传门店环境照片
     */
    void onUpdateEnvironmentImgSuccess();

    /**
     * 更新头像成功回调
     */
    void onUpdateHeadImgSuccess();

    /**
     * 更新服务范围成功回调
     */
    void onUpdateServicesSuccess();

    /**
     * 修改工位成功回调
     */
    void onUpdateStationSuccess();

    /**
     * 更新门店名成功回调
     */
    void onUpdateStoreNameSuccess();
}
