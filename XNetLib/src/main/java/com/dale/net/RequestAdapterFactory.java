package com.dale.net;

import com.dale.net.request.RequestAdapter;
import com.dale.net.utils.Utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;


final class RequestAdapterFactory extends RequestAdapter.Factory{
    static final RequestAdapter.Factory INSTANCE = new RequestAdapterFactory();

    @Override
    public RequestAdapter<?, ?> get(Type returnType, Annotation[] annotations) {
        if (getRawType(returnType) != NetCall.class) {
            throw new IllegalArgumentException("返回类型必须是NetCall");
        }

        final Type responseType = Utils.getCallResponseType(returnType);
        return new RequestAdapter<Object, NetCall<?>>() {
            @Override
            public Type responseType() {
                return responseType;
            }

            @Override
            public NetCall<Object> adapt(NetCall<Object> call) {
                return call;
            }
        };
    }
}
