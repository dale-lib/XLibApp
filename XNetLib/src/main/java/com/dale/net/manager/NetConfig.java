package com.dale.net.manager;


import okhttp3.Interceptor;


public interface NetConfig {
    /**
     * desc: 是否打印日志
     */
    NetConfig needLog(boolean allowLog);

    /**
     * desc: 设置连接超时时间
     */
    NetConfig connectTimeout(int time);


    /**
     * desc: 设置读取数据超时时间
     */
    NetConfig readTimeout(int time);

    /**
     * desc: 设置发送数据超时时间
     */
    NetConfig writeTimeout(int time);

    /**
     * desc: 添加header
     */
    NetConfig addHeader(String name, String value);

    /**
     * desc: 添加公共参数
     */
    NetConfig params(String key, String value);

    /**
     * desc: 全局默认使用base url
     */
    NetConfig baseUrl(String baseUrl);

    /**
     * desc: 拦截器
     */
    NetConfig addInterceptor(Interceptor interceptor);

}
