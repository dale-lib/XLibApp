package com.dale.net.request;


import com.dale.net.callback.ProgressListener;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;


public class UploadRequestBody extends RequestBody {
    private RequestBody requestBody;
    private ProgressListener mListener;
    private BufferedSink bufferedSink;

    public UploadRequestBody(RequestBody body, ProgressListener listener) {
        requestBody = body;
        mListener = listener;
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

        if (bufferedSink==null){
            bufferedSink = Okio.buffer(sink(sink));
        }
        //写入
        requestBody.writeTo(bufferedSink);
        //刷新
        bufferedSink.flush();
    }

    private Sink sink(BufferedSink sink) {

        return new ForwardingSink(sink) {
            long bytesWritten = 0L;
            long contentLength = 0L;
            @Override
            public void write(Buffer source, long byteCount) throws IOException {
                super.write(source, byteCount);
                if (contentLength==0){
                    contentLength = contentLength();
                }
                bytesWritten += byteCount;
                //回调
                if (mListener != null){
                    mListener.progress((int) (bytesWritten * 100 /contentLength));
                }
            }
        };
    }

}
