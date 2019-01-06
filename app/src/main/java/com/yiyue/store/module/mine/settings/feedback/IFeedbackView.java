package com.yiyue.store.module.mine.settings.feedback;

import com.yiyue.store.base.mvp.IBaseView;
import com.yiyue.store.model.vo.bean.FeedBackBean;

/**
 * Created by lvlong on 2018/10/8.
 */
public interface IFeedbackView extends IBaseView {
    void submitSuccess();
    void submitFail(String s);

    void initSuccess(FeedBackBean feedBackBean);

}
