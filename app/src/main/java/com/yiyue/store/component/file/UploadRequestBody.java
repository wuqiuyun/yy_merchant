package com.yiyue.store.component.file;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Created by zm on 2018/9/25.
 */
public class UploadRequestBody extends RequestBody{

    /**
     * 实体请求体
     */
    private RequestBody requestBody;
    /**
     * 包装完成的BufferedSink
     */
    private BufferedSink bufferedSink;

    private OnFileProgressListener fileListener;

    public UploadRequestBody(RequestBody requestBody, OnFileProgressListener fileListener) {
        this.requestBody = requestBody;
        this.fileListener = fileListener;
    }

    @Override
    public MediaType contentType() {
        return requestBody.contentType();
    }

    @Override
    public long contentLength() throws IOException {
        return requestBody.contentLength();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        bufferedSink = Okio.buffer(sink(sink));

        // 写入
        requestBody.writeTo(bufferedSink);
        //必须调用flush，否则最后一部分数据可能不会被写入
        bufferedSink.flush();
    }

    private Sink sink(Sink sink) {
        return new ForwardingSink(sink) {

            //当前写入字节数
            long bytesWritten = 0L;
            //总字节长度，避免多次调用contentLength()方法
            long contentLength = 0L;
            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength == 0) {
                    //获得contentLength的值，后续不再调用
                    contentLength = contentLength();
                }
                // 增加当前写入的字节数
                bytesWritten += byteCount;
                // 回调
                if (fileListener != null)
                    fileListener.onLoading(contentLength, bytesWritten);
            }
        };
    }
}
