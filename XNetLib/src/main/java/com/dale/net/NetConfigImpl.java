package com.dale.net;

import androidx.collection.ArrayMap;

import com.dale.net.manager.NetConfig;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;

/**
 * create by Dale
 * create on 2019/7/12
 * description:
 */
public class NetConfigImpl implements NetConfig {

    private int connectTimeout = 10_000;
    private int readTimeout = 10_000;
    private int writeTimeout = 10_000;
    private ArrayMap<String,String> headers = new ArrayMap<>();
    private ArrayMap<String,String> paramsMap = new ArrayMap<>();
    private List<Interceptor> interceptors = new ArrayList<>();
    private String baseUrl;
    private boolean needLog = false;

    @Override
    public NetConfig needLog(boolean allowLog) {
        this.needLog = allowLog;
        return this;
    }

    @Override
    public NetConfig connectTimeout(int time) {
        connectTimeout = time;
        return this;
    }

    @Override
    public NetConfig readTimeout(int time) {
        readTimeout = time;
        return this;
    }

    @Override
    public NetConfig writeTimeout(int time) {
        writeTimeout = time;
        return this;
    }

    @Override
    public NetConfig addHeader(String name, String value) {
        this.headers.put(name,value);
        return this;
    }

    @Override
    public NetConfig params(String key, String value) {
        this.paramsMap.put(key,value);
        return this;
    }

    @Override
    public NetConfig baseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    @Override
    public NetConfig addInterceptor(Interceptor interceptor) {
        interceptors.add(interceptor);
        return this;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public int getWriteTimeout() {
        return writeTimeout;
    }

    public ArrayMap<String, String> getHeaders() {
        return headers;
    }

    public ArrayMap<String, String> getParamsMap() {
        return paramsMap;
    }

    public List<Interceptor> getInterceptors() {
        return interceptors;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public boolean isNeedLog() {
        return needLog;
    }

}
