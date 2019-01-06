package com.yiyue.store.module.mine.info.cover;

import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.result.GetStoreCenterInfoResult;

import java.util.List;

/**
 * Created by zm on 2018/9/29.
 */
public interface ICoverView extends IBaseView{
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
}
