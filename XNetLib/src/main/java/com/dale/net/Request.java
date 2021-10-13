package com.dale.net;


import android.text.TextUtils;

import com.dale.net.bean.NetLiveData;
import com.dale.net.callback.ProgressListener;
import com.dale.net.exception.ErrorMessage;
import com.dale.net.manager.RequestManager;
import com.dale.net.request.UploadRequestBody;
import com.dale.net.utils.JsonUtils;
import com.dale.net.utils.Utils;

import java.io.File;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

/**
 * create by Dale
 * create on 2019/7/12
 * description:
 */
public class Request<T> {

    public transient okhttp3.Request mRequest;
    public transient RequestBuilder<T> requestBuilder;

    Request(RequestBuilder<T> requestBuilder) {
        this.requestBuilder = requestBuilder;
    }

    public void send(NetLiveData<T> netLiveData) {
        if (!Utils.isAvailable(NetSdk.getContext())) {
            ErrorMessage errorMessage = new ErrorMessage(NetConstant.NET_ERROR_CODE, "网络异常");
            netLiveData.postError(errorMessage);
            return;
        }

        String baseUrl = getBaseUrl(netLiveData);
        if (TextUtils.isEmpty(baseUrl)) {
            return;
        }
        switch (requestBuilder.method) {
            case NetConstant.GET:
            case NetConstant.HEAD:
            case NetConstant.DELETE:
            case NetConstant.PATCH:
                String realParams = Utils.convertGetParams(requestBuilder.allStringParams);
                String requestUrl;
                if (TextUtils.isEmpty(realParams)) {
                    requestUrl = String.format("%s%s",baseUrl,getUrl());
                } else {
                    requestUrl = String.format("%s%s?%s",baseUrl,getUrl(),realParams);
                }
                mRequest = new okhttp3.Request.Builder()
                        .headers(requestBuilder.headers.build())
                        .url(requestUrl)
                        .method(requestBuilder.method, null)
                        .build();
                execute(mRequest,netLiveData);
                break;
            case NetConstant.PUT:
            case NetConstant.POST:
                post(netLiveData);
                break;
            default:
                break;
        }

    }

    private String getUrl() {
        if (requestBuilder.url == null) {
            requestBuilder.url = "";
        }
        return requestBuilder.url.startsWith("/") ? requestBuilder.url.substring(1) : requestBuilder.url;
    }


    private String getBaseUrl(NetLiveData<T> netLiveData) {
        String baseUrl = requestBuilder.baseUrl;
        ErrorMessage errorMessage;
        if (TextUtils.isEmpty(baseUrl)) {
            errorMessage = new ErrorMessage(NetConstant.REQUEST_ERROR_CODE, "baseUrl为空");
            netLiveData.postError(errorMessage);
            return "";
        }

        if (TextUtils.isEmpty(baseUrl) || !baseUrl.startsWith("http")) {
            errorMessage = new ErrorMessage(NetConstant.REQUEST_ERROR_CODE, "不支持非http协议请求");
            netLiveData.postError(errorMessage);
            return "";
        }
        return baseUrl.endsWith("/") ? baseUrl : baseUrl + "/";
    }


    /**
     * 把map转化成请求参数
     */
    private String createParams() {
        if (requestBuilder.allStringParams == null || requestBuilder.allStringParams.size() == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        Set<Map.Entry<String, String>> allStringParamsEntities = requestBuilder.allStringParams.entrySet();

        for (Map.Entry<String, String> entity : allStringParamsEntities) {
            String key = entity.getKey();
            builder.append(key).append("=").append(entity.getValue()).append("&");
        }
        String params = builder.toString();
        return params.substring(0, params.length() - 1);
    }

    protected void post(NetLiveData<T> netLiveData) {
        if (requestBuilder.fileMap.size() != 0) {
            executeHasFileRequest(netLiveData);
        } else {
            executeJsonRequest(netLiveData);
        }
    }

    /**
     * desc: 包含文件的请求
     */
    private void executeHasFileRequest(NetLiveData<T> netLiveData) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        Set<Map.Entry<String, File>> fileEntities = requestBuilder.fileMap.entrySet();

        for (Map.Entry<String, File> entity : fileEntities) {
            String key = entity.getKey();
            File file = entity.getValue();
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("application/octet-stream"), file);

            UploadRequestBody uploadRequestBody = new UploadRequestBody(requestFile, new ProgressListener() {
                @Override
                public void progress(int progress) {
                    if (requestBuilder.netLiveData != null) {
                        requestBuilder.netLiveData.postValue(progress);
                    }
                }
            });
            builder.addFormDataPart(key, file.getName(), uploadRequestBody);
        }

        if (requestBuilder.allStringParams != null && requestBuilder.allStringParams.size() != 0) {
            Set<Map.Entry<String, String>> allStringParamsEntities = requestBuilder.allStringParams.entrySet();

            for (Map.Entry<String, String> entity : allStringParamsEntities) {
                builder.addFormDataPart(entity.getKey(), entity.getValue());
            }
        }
        RequestBody requestBodies = builder.build();
        requestBuilder.headers.removeAll("Content-Type");
        mRequest = new okhttp3.Request.Builder()
                .headers(requestBuilder.headers.build())
                .url(String.format("%s%s",getBaseUrl(netLiveData),getUrl()))
                .method(requestBuilder.method, requestBodies)
                .build();
        execute(mRequest,netLiveData);
    }


    private void executeJsonRequest(NetLiveData<T> netLiveData) {

        RequestBody request;
        String mediaTypeName = requestBuilder.mMediaType.toString();
        //根据传的参数,判断使用哪种mediaType请求
        if (requestBuilder.operas != null) {
            request = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JsonUtils.toJson(requestBuilder.operas));
            requestBuilder.headers.set("Content-Type", "application/json");
        } else if (mediaTypeName.contains("application/json")) {
            request = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JsonUtils.toJson(requestBuilder.allStringParams));
            requestBuilder.headers.set("Content-Type", "application/json");
        } else {
            request = RequestBody.create(requestBuilder.mMediaType, createParams());
            requestBuilder.headers.set("Content-Type", mediaTypeName);
        }

        mRequest = new okhttp3.Request.Builder()
                .headers(requestBuilder.headers.build())
                .url(String.format("%s%s",getBaseUrl(netLiveData),getUrl()))
                .method(requestBuilder.method, request)
                .build();
        execute(mRequest,netLiveData);
    }

    protected void execute(okhttp3.Request mRequest, NetLiveData<T> netLiveData) {
        OkHttpClient okHttpClient = RequestManager.getClient(NetSdk.getConfig());
        Call call = okHttpClient.newCall(mRequest);
        call.enqueue(new RequestCallback(this,netLiveData));
    }

}
