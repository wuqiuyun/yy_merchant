package com.yiyue.store.module.certification;


import com.yiyue.store.base.mvp.IBaseView;

import java.util.List;

/**
 * Created by lvlong on 2018/9/20.
 */
public interface ICertificationView extends IBaseView {

    /**
     * 提交认证信息成功
     */
    void onSubmitCertiDataSuccess();

    /**
     * 文件上传成功
     * @param filePaths
     */
    void onUploadFileSuccess(List<String> filePaths);
}
