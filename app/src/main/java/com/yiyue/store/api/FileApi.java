package com.yiyue.store.api;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.yiyue.store.component.file.OnFileProgressListener;
import com.yiyue.store.component.file.UploadRequestBody;
import com.yiyue.store.component.net.YLRequestManager;
import com.yiyue.store.component.net.YLRxSubscriberHelper;
import com.yiyue.store.component.rx.RxSchedulers;
import com.yiyue.store.component.toast.ToastUtils;
import com.yiyue.store.model.vo.result.UploadImageResult;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * 文件上传和下载
 * <p>
 * Created by zm on 2018/9/25.
 */
public class FileApi {

    public interface Api {
        @Multipart
        @POST("/store-api/file/batchUpload")
        Observable<UploadImageResult> uploadFile(@Part List<MultipartBody.Part> params);

        @FormUrlEncoded
        @POST("/store-api/file/download")
        Observable<ResponseBody> downloadFile(@Field("param") String param);
    }

    private Api api;

    public FileApi() {
        this.api = YLRequestManager.getRequest(Api.class);
    }

    /**
     * 单文件上传
     * @param filePaths 文件路径
     */
    public void uploadFile(List<String> filePaths, YLRxSubscriberHelper<UploadImageResult> rxSubscriberHelper) {
        if (filePaths == null || filePaths.isEmpty()) return;

        List<MultipartBody.Part> params = new ArrayList<>();
        for (String filePath : filePaths) {
            File file = new File(filePath);
            if (!file.exists()) {
                ToastUtils.shortToast("文件不存在");
                return;
            }

            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part requestImgPart =
                    MultipartBody.Part.createFormData("files", file.getName(), requestFile);
            params.add(requestImgPart);
        }

        api.uploadFile(params)
                .compose(RxSchedulers.rxSchedulerHelper())
                .compose(RxSchedulers.handleResult())
                .subscribe(rxSubscriberHelper);
    }

    /**
     * 单文件上传
     * @param filePaths 文件路径
     * @param listener 文件上传进度监听
     */
    public void uploadFile(List<String> filePaths, OnFileProgressListener listener, YLRxSubscriberHelper<UploadImageResult> rxSubscriberHelper) {
        if (filePaths == null || filePaths.isEmpty()) return;

        List<MultipartBody.Part> params = new ArrayList<>();
        for (String filePath : filePaths) {
            File file = new File(filePath);
            if (!file.exists()) {
                ToastUtils.shortToast("文件不存在");
                return;
            }
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            UploadRequestBody requestFile = new UploadRequestBody(requestBody, listener);
            MultipartBody.Part requestImgPart =
                    MultipartBody.Part.createFormData("files", file.getName(), requestFile);
            params.add(requestImgPart);
        }


        api.uploadFile(params)
            .compose(RxSchedulers.rxSchedulerHelper())
            .compose(RxSchedulers.handleResult())
            .subscribe(rxSubscriberHelper);
    }

    /**
     * 下载文件
     * @param fileUrl
     * @param responseBodyConsumer
     */
    @SuppressLint("CheckResult")
    public void downloadFile(String fileUrl, Consumer<ResponseBody> responseBodyConsumer) {
        if (TextUtils.isEmpty(fileUrl)) {
            ToastUtils.shortToast("文件下载路径为空");
            return;
        }
        api.downloadFile(fileUrl)
                .doOnNext(responseBody -> {
                    // 保存文件
                })
                .compose(RxSchedulers.rxSchedulerHelper())
                .subscribe(responseBodyConsumer);
    }

    /**
     * 下载文件
     * @param fileUrl
     * @param listener 文件下载进度监听器
     * @param responseBodyConsumer
     */
    @SuppressLint("CheckResult")
    public void dowanloadFile(String fileUrl, OnFileProgressListener listener, Consumer<ResponseBody> responseBodyConsumer) {
        if (TextUtils.isEmpty(fileUrl)) {
            ToastUtils.shortToast("文件下载路径为空");
            return;
        }
        api.downloadFile(fileUrl)
                .doOnNext(responseBody -> {
                    // 保存文件
                })
                .compose(RxSchedulers.rxSchedulerHelper())
                .subscribe(responseBodyConsumer);
    }
}
