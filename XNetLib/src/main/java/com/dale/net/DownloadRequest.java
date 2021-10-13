package com.dale.net;

import static com.dale.net.NetConstant.REQUEST_ERROR_CODE;
import static com.dale.net.NetConstant.REQUEST_FILE_CODE;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.dale.net.bean.DownloadRequestBuilder;
import com.dale.net.callback.DownCallBack;
import com.dale.net.exception.ErrorMessage;
import com.dale.net.utils.HttpLogger;
import com.dale.net.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSource;
import okio.Okio;
import okio.Sink;
import okio.Source;

/**
 * create by Dale
 * create on 2019/7/13
 * description: 下载文件
 */
public class DownloadRequest {
    private String url;
    private String savePath;
    private String fileName;
    private Call mCall;
    private okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
    public DownloadRequest(DownloadRequestBuilder builder) {
        url = builder.getUrl();
        savePath = builder.getSavePath();
        fileName = builder.getFileName();
    }

    public void send(final DownCallBack downCallBack) {
        final MutableLiveData<File> succLiveData = new MutableLiveData<>();
        final MutableLiveData<Integer> progressLiveData = new MutableLiveData<>();
        final MutableLiveData<ErrorMessage> errLiveData = new MutableLiveData<>();
        Observer<File> succObserver = new Observer<File>() {
            @Override
            public void onChanged(File t) {
                downCallBack.onSuccess(t);
            }
        };
        Observer<Integer> progressObserver = new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                downCallBack.onProgress(integer);
            }
        };
        Observer<ErrorMessage> errObserver = new Observer<ErrorMessage>() {
            @Override
            public void onChanged(ErrorMessage errorMessage) {
                downCallBack.onError(errorMessage);
            }
        };
        succLiveData.observeForever(succObserver);
        progressLiveData.observeForever(progressObserver);
        errLiveData.observeForever(errObserver);
        if (!Utils.isAvailable(NetSdk.getContext())) {
            ErrorMessage errorMessage = new ErrorMessage(NetConstant.NET_ERROR_CODE, "网络异常");
            errLiveData.postValue(errorMessage);
            return;
        }
        if (TextUtils.isEmpty(savePath)) {
            throw new NullPointerException("请调用savePath()方法设置文件存储路径");
        }
        if (TextUtils.isEmpty(fileName)) {
            throw new NullPointerException("请调用fileName()方法设置文件存储名字");
        }
        NetConfigImpl config = NetSdk.getConfig();
        OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();
        if (config.isNeedLog()) {
            httpBuilder.addInterceptor(new HttpLogger());
        }
        for (Interceptor interceptor : config.getInterceptors()) {
            httpBuilder.addInterceptor(interceptor);
        }
        httpBuilder
                .sslSocketFactory(createSSLSocketFactory(), new TrustAllManager())
                .hostnameVerifier(new TrustAllHostnameVerifier())
                .readTimeout(config.getReadTimeout(), TimeUnit.MILLISECONDS)
                .connectTimeout(config.getConnectTimeout(), TimeUnit.MILLISECONDS)
                .writeTimeout(config.getWriteTimeout(), TimeUnit.MILLISECONDS);
        OkHttpClient client = httpBuilder.build();
        builder.url(url);
        builder.addHeader("Accept-Encoding","identity");
        final Request request = builder.get().build();
        mCall = client.newCall(request);

        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                errLiveData.postValue(new ErrorMessage(REQUEST_ERROR_CODE, e.getMessage()));
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
               if(response == null){
                   errLiveData.postValue(new ErrorMessage(REQUEST_ERROR_CODE, "请求异常"));
               }else{
                   if(!response.isSuccessful()){
                       errLiveData.postValue(new ErrorMessage(response.code(), "请求异常"));
                   }else {
                       File dir = new File(savePath);
                       if (!dir.exists()) {
                           boolean mkdirs = dir.mkdirs();
                           if (!mkdirs) {
                               String errMsg = String.format("创建%s文件失败", savePath);
                               errLiveData.postValue(new ErrorMessage(REQUEST_FILE_CODE, errMsg));
                           }
                       }
                       File file = new File(dir, fileName);
                       Sink sink = Okio.sink(file);
                       ResponseBody responseBody = response.body();
                       if (responseBody == null) {
                           okhttp3.internal.Util.closeQuietly(sink);
                           throw new IOException("response body is null");
                       }
                       Source source = Okio.source(responseBody.byteStream());
                       final long totalSize = responseBody.contentLength();
                       BufferedSink bufferedSink = Okio.buffer(sink);
                       bufferedSink.writeAll(new ForwardingSource(source) {
                           long sum = 0;
                           int oldRate = 0;
                           @Override
                           public long read(@NonNull Buffer sink, long byteCount) throws IOException {
                               long readSize = super.read(sink, byteCount);
                               if (readSize != -1L) {
                                   sum += readSize;

                                   final int rate = Math.round(sum * 1F / totalSize * 100F);
                                   if (oldRate != rate) {
                                       progressLiveData.postValue(rate);
                                       oldRate = rate;
                                   }

                               }
                               return readSize;
                           }
                       });
                       bufferedSink.flush();
                       okhttp3.internal.Util.closeQuietly(sink);
                       okhttp3.internal.Util.closeQuietly(source);
                       succLiveData.postValue(file);
                   }
               }
            }
        });

    }

    public void cancel() {
        if (mCall != null) {
            mCall.cancel();
        }
    }


    static class TrustAllManager implements X509TrustManager {
        @SuppressLint("TrustAllX509TrustManager")
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        @SuppressLint("TrustAllX509TrustManager")
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }

    /**
     * hostname verifier
     */
    static class TrustAllHostnameVerifier implements HostnameVerifier {
        @SuppressLint("BadHostnameVerifier")
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }


    @SuppressLint("TrulyRandom")
    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory sSLSocketFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, null,
                    new SecureRandom());
            sSLSocketFactory = sc.getSocketFactory();
        } catch (Exception ignored) {
        }
        return sSLSocketFactory;
    }



}
