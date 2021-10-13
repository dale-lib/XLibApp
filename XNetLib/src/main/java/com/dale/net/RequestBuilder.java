package com.dale.net;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.collection.ArrayMap;
import androidx.lifecycle.MutableLiveData;

import com.dale.net.bean.NetLiveData;

import java.io.File;

import okhttp3.Headers;
import okhttp3.MediaType;

public class RequestBuilder<T> implements NetCall<T> {
    public String method;
    public final ServiceMethod<T, ?> serviceMethod;
    public Object operas;
    public String url;
    public ArrayMap<String, File> fileMap = new ArrayMap<>();
    public ArrayMap<String, String> allStringParams = new ArrayMap<>();
    public Headers.Builder headers;
    public String baseUrl;
    public MutableLiveData<Integer> netLiveData;
    MediaType mMediaType;


    RequestBuilder(ServiceMethod<T, ?> serviceMethod) {
        this.serviceMethod = serviceMethod;
        headers = new Headers.Builder();
        this.url = serviceMethod.relativeUrl;
        this.method = serviceMethod.httpMethod;
        baseUrl = NetSdk.getConfig().getBaseUrl();
        mMediaType = MediaType.parse("application/x-www-form-urlencoded;charset=utf-8");
    }

    @Override
    public NetCall<T> url(String url) {
        if (!TextUtils.isEmpty(url)) {
            if (!TextUtils.isEmpty(this.url)) {
                this.url += url;
            } else {
                this.url = url;
            }
        }
        return this;
    }

    @Override
    public NetCall<T> params(Object params) {
        this.operas = params;
        return this;
    }

    @Override
    public NetCall<T> params(String key, File value) {
        fileMap.put(key, value);
        return this;
    }

    @Override
    public NetCall<T> params(String key, String value) {
        allStringParams.put(key, value);
        return this;
    }


    @Override
    public NetCall<T> baseUrl(String baseUrl) {
        if (TextUtils.isEmpty(baseUrl)) return this;
        this.baseUrl = baseUrl;
        return this;
    }

    @Override
    public NetCall<T> addHeader(String name, String value) {
        headers.add(name, value);
        return this;
    }

    @Override
    public NetCall<T> mediaType(MediaType mediaType) {
        this.mMediaType = mediaType;
        return this;
    }

    @Override
    public NetCall<T> send(NetLiveData<T> netLiveData) {
        netLiveData.postLoading();
        new Request<>(this).send(netLiveData);
        return this;
    }


    @Override
    public RequestBuilder<T> addUploadListener( @NonNull final MutableLiveData<Integer> netLiveData) {
        this.netLiveData = netLiveData;
        return this;
    }

}
