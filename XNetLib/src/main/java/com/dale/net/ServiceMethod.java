package com.dale.net;

import com.dale.net.http.DELETE;
import com.dale.net.http.GET;
import com.dale.net.http.HEAD;
import com.dale.net.http.PATCH;
import com.dale.net.http.POST;
import com.dale.net.http.PUT;
import com.dale.net.request.RequestAdapter;
import com.dale.net.utils.Utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * create by Dale
 * create on 2019/7/12
 * description:
 */
final class ServiceMethod<R, T> {
    final String httpMethod;
    final String relativeUrl;
    final RequestAdapter<R, T> requestAdapter;

    private ServiceMethod(Builder<R, T> builder) {
        httpMethod = builder.httpMethod;
        relativeUrl = builder.relativeUrl;
        requestAdapter = builder.requestAdapter;
    }


    static final class Builder<T, R> {
        final Annotation[] methodAnnotations;
        final Method method;
        Type responseType;
        String httpMethod;
        String relativeUrl;
        RequestAdapter<T, R> requestAdapter;
        public Builder(Method method) {
            this.method = method;
            this.methodAnnotations = method.getAnnotations();
        }

        public ServiceMethod build() {
            requestAdapter = createRequestAdapter();
            responseType = requestAdapter.responseType();

            for (Annotation annotation : methodAnnotations) {
                parseMethodAnnotation(annotation);
            }

            if (httpMethod == null) {
                throw methodError("请在http请求的方法加上@GET 或 @POST 或 @DELETE注解");
            }

            return new ServiceMethod<>(this);
        }

        private void parseMethodAnnotation(Annotation annotation) {
            if (annotation instanceof GET) {
                parseHttpMethodAndPath(NetConstant.GET,((GET) annotation).value());
            } else if (annotation instanceof POST) {
                parseHttpMethodAndPath(NetConstant.POST,((POST) annotation).value());
            }else if (annotation instanceof DELETE){
                parseHttpMethodAndPath(NetConstant.DELETE,((DELETE) annotation).value());
            }else if (annotation instanceof PUT){
                parseHttpMethodAndPath(NetConstant.PUT,((PUT) annotation).value());
            }else if (annotation instanceof HEAD){
                parseHttpMethodAndPath(NetConstant.HEAD,((HEAD) annotation).value());
            }else if (annotation instanceof PATCH){
                parseHttpMethodAndPath(NetConstant.PATCH,((PATCH) annotation).value());
            }
        }


        private void parseHttpMethodAndPath(String httpMethod, String value) {
            if (this.httpMethod != null) {
                throw methodError("Only one HTTP method is allowed. Found: %s and %s.",
                        this.httpMethod, httpMethod);
            }
            this.httpMethod = httpMethod;
            if (value.isEmpty()) {
                return;
            }
            this.relativeUrl = value;
        }

        RuntimeException methodError(String message, Object... args) {
            return methodError(null, message, args);
        }

        private RuntimeException methodError(Throwable cause, String message, Object... args) {
            message = String.format(message, args);
            return new IllegalArgumentException(message
                    + "\n    for method "
                    + method.getDeclaringClass().getSimpleName()
                    + "."
                    + method.getName(), cause);
        }


        private RequestAdapter<T, R> createRequestAdapter() {
            Type returnType = method.getGenericReturnType();
            if (Utils.hasUnresolvableType(returnType)) {
                throw methodError(
                        "Method return type must not include a type variable or wildcard: %s", returnType);
            }
            if (returnType == void.class) {
                throw methodError("Service methods cannot return void.");
            }
            Annotation[] annotations = method.getAnnotations();
            try {
                return (RequestAdapter<T, R>) RequestAdapterFactory.INSTANCE.get(returnType, annotations);
            } catch (RuntimeException e) { // Wide exception range because factories are user code.
                throw methodError(e, "Unable to create call adapter for %s", returnType);
            }
        }
    }

}

