package com.dale.net.request;

import com.dale.net.NetCall;
import com.dale.net.utils.Utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;


public interface RequestAdapter<R, T> {

    Type responseType();

    T adapt(NetCall<R> call);

    abstract class Factory {

        public abstract RequestAdapter<?, ?> get(Type returnType, Annotation[] annotations);

        protected static Class<?> getRawType(Type type) {
            return Utils.getRawType(type);
        }
    }


}

