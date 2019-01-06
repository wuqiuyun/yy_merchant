package com.yiyue.store.module.common.addimg;

import com.yiyue.store.base.mvp.IBaseView;

import java.util.List;

/**
 * Created by zm on 2018/11/14.
 */
public interface IAddImageView extends IBaseView{
    /**
     * 文件上传成功
     * @param filePath
     */
    void onUploadFileSuccess(List<String> filePath);
    /**
     * 文件上传失败
     */
    void onUploadFileFail();
}
