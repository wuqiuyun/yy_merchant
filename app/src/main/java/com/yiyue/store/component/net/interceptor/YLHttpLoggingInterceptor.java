package com.yiyue.store.component.net.interceptor;


import com.yl.core.component.net.JHttpLoggingInterceptor;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 如果是上传文件格式的，不需要添加过滤
 * <p>
 * Created by zm on 2018/9/8.
 */

public class YLHttpLoggingInterceptor extends JHttpLoggingInterceptor {

    /**
     * 需要被过滤掉log的url地址
     */
    public static final String[] FILTER_URLS = {"/file/batchUpload", "/file/download"};

    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request request = chain.request();
        try {
            final String url = request.url().url().toString();
            for (String filter_url : FILTER_URLS) {
                if (url.contains(filter_url)) {
                    Response response = chain.proceed(request);
                    return response;
                }
            }
        } catch (Exception e) {
            return super.intercept(chain);
        }
        return super.intercept(chain);
    }
}
