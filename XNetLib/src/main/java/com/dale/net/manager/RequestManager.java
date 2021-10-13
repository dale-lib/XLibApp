package com.dale.net.manager;

import android.annotation.SuppressLint;
import android.util.Log;

import com.dale.net.NetConstant;
import com.dale.net.NetConfigImpl;
import com.dale.net.utils.HttpLogger;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * create by Dale
 * create on 2019/7/12
 * description:
 */
public class RequestManager {
    private static Map<String, OkHttpClient> sClientMap = new ConcurrentHashMap<>();

    public static OkHttpClient getClient(NetConfigImpl config) {
        String clientKey = config.getBaseUrl();
        OkHttpClient client = sClientMap.get(clientKey);
        if (client != null) {
            if(config.isNeedLog()){
                Log.d(NetConstant.LOG_TAG,"OkHttpClient 缓存中获取");
            }
            return client;
        }
        client = createHttpClient(config);
        if(config.isNeedLog()){
            Log.d(NetConstant.LOG_TAG,"OkHttpClient 新创建");
        }
        sClientMap.put(clientKey, client);
        return client;
    }



    private static OkHttpClient createHttpClient(NetConfigImpl config) {
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
                //设置超时
                .writeTimeout(config.getWriteTimeout(), TimeUnit.MILLISECONDS);
        return httpBuilder.build();
    }


    /**
     * trust manager
     */
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
