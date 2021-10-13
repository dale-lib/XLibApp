package com.dale.net;

import android.content.Context;

import com.dale.net.bean.DownloadRequestBuilder;
import com.dale.net.manager.NetConfig;
import com.dale.net.utils.Utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class NetSdk {
    private static NetConfigImpl mConfig = null;
    private static Context mContext;
    private static final Map<Method, ServiceMethod<?, ?>> serviceMethodCache = new ConcurrentHashMap<>();


    public static NetConfig config(Context application) {
        mContext = application;
        if (mConfig == null) {
            mConfig = new NetConfigImpl();
        }
        return mConfig;
    }

    /**
     * 获取当前配置,重新设值
     */
    public static NetConfigImpl getConfig() {
        if (mConfig == null) {
            throw new RuntimeException("请先在Application中调用NetSdk.config()初始化");
        }
        return mConfig;
    }


    /**
     * 动态设置baseUrl
     * @param baseUrl
     */
    public static void setBaseUrl(String baseUrl){
        getConfig().baseUrl(baseUrl);
    }

    public static DownloadRequestBuilder download(){
        return new DownloadRequestBuilder();
    }

    /**
     * 动态代理获取NetCall的实现类
     */
    @SuppressWarnings("unchecked")
    public static <T> T create(final Class<T> service) {
        Utils.validateServiceInterface(service);
        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class<?>[]{service},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args){
                        ServiceMethod<Object, Object> serviceMethod = (ServiceMethod<Object, Object>) loadServiceMethod(method);
                        RequestBuilder<Object> builder = new RequestBuilder<>(serviceMethod);
                        return serviceMethod.requestAdapter.adapt(builder);
                    }
                });
    }


    private static ServiceMethod<?, ?> loadServiceMethod(Method method) {
        ServiceMethod<?, ?> result = serviceMethodCache.get(method);
        if (result != null) return result;

        synchronized (serviceMethodCache) {
            result = serviceMethodCache.get(method);
            if (result == null) {
                result = new ServiceMethod.Builder<>(method)
                        .build();
                serviceMethodCache.put(method, result);
            }
        }
        return result;
    }

    public static Context getContext() {
        return mContext;
    }

}
