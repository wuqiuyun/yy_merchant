package com.yiyue.store.module.im.groups.creategroup;

import android.content.Context;
import android.text.TextUtils;

import com.yiyue.store.api.FileApi;
import com.yiyue.store.api.GroupApi;
import com.yiyue.store.base.mvp.BasePresenter;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.model.vo.bean.daobean.GroupBean;
import com.yiyue.store.model.vo.result.GroupResult;
import com.yiyue.store.model.vo.result.UploadImageResult;

import java.util.List;

/**
 * Created by Lizhuo on 2018/10/16.
 */
public class CreateGroupPresenter extends BasePresenter<CreateGroupView> {
    public void createGroup(String name, String describe, String path, String imusername, String userId) {
        if (TextUtils.isEmpty(name)) {
            getMvpView().showToast("群名不能为空");
            return;
        }


        if (TextUtils.isEmpty(imusername)) {
            getMvpView().showToast("imusername不能为空");
            return;
        }

        if (TextUtils.isEmpty(userId)) {
            getMvpView().showToast("userId不能为空");
            return;
        }

        create(name, describe, path, imusername, userId);
    }

    private void create(String name, String describe, String path, String imusername, String userId) {
        new GroupApi().createGroup(name, describe, path, imusername, userId, new YLRxSubscriberHelper<GroupResult>() {
            @Override
            public void _onNext(GroupResult groupResult) {
                GroupBean group = groupResult.getData();

                if (null != group) getMvpView().onSuccess(group);
            }
        });
    }

    /**
     * 上传文件
     *
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
        });
    }
}
