package com.yiyue.store.component.file;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by zm on 2018/9/25.
 */
public class DowanlaodResponseBody extends ResponseBody {

    /**
     * 实际请求体
     */
    private ResponseBody responseBody;
    /**
     * 下载回调接口
     */
    private OnFileProgressListener fileListener;

    /**
     * BufferedSource
     */
    private BufferedSource bufferedSource;

    public DowanlaodResponseBody(ResponseBody responseBody, OnFileProgressListener fileListener) {
        this.responseBody = responseBody;
        this.fileListener = fileListener;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(source(responseBody.source()));
        }
        return bufferedSource;
    }

    private Source source(Source source) {
        return new ForwardingSource(source) {
            long totalBytesRead = 0L;
            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += bytesRead;
                // 回调
                if (fileListener != null)
                    fileListener.onLoading(responseBody.contentLength(), totalBytesRead);
                return byteCount;
            }
        };
    }
}
