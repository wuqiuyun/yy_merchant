package com.yiyue.store.module.mine.settings.feedback;

import android.content.Context;
import android.text.TextUtils;

import com.yiyue.store.model.vo.bean.FeedBackBean;
import com.yiyue.store.model.vo.result.FeedBackResult;
import com.yl.core.component.net.exception.ApiException;
import com.yiyue.store.api.SettingsApi;
import com.yiyue.store.base.data.BaseResponse;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;

/**
 * Created by lvlong on 2018/10/8.
 */
public class FeedbackPresenter extends BasePresenter<IFeedbackView> {
    //提交反馈
    public void submitFeedback(String subContent, String userId, Context context) {
        if (TextUtils.isEmpty(subContent)){
            getMvpView().showToast("请输入反馈内容");
        }
        new SettingsApi().submitFeedback(subContent, userId, new YLRxSubscriberHelper<BaseResponse>(context,true) {
            @Override
            public void _onNext(BaseResponse result) {
                    getMvpView().submitSuccess();
            }

            @Override
            public void _onError(ApiException error) {
                getMvpView().submitFail(error.code+"");
            }
        });
    }

    //反馈信息
    public void initAppFeedback(Context context) {
        new SettingsApi().initAppFeedback(new YLRxSubscriberHelper<FeedBackResult>(context,true) {
            @Override
            public void _onNext(FeedBackResult result) {
                FeedBackBean feedBackBean = result.getData();
                getMvpView().initSuccess(feedBackBean);
            }

            @Override
            public void _onError(ApiException error) {
            }
        });
    }
}
