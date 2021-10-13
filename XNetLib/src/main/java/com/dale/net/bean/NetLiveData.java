package com.dale.net.bean;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.dale.net.exception.ErrorMessage;

public class NetLiveData<T> extends LiveData<LiveResult<T>> {
    /**
     * 请求成功的设值
     * 子线程时调用 此方法
     */
    public void postNext(T value) {
        LiveResult<T> result = getValue();
        if (result == null) {
            result = new LiveResult<>();
        }
        result.setData(value);
        super.postValue(result);
    }


    /**
     * 请求成功的设值
     * 主线程时调用此方法
     */
    public void setNext(T value) {
        LiveResult<T> result = getValue();
        if (result == null) {
            result = new LiveResult<>();
        }
        result.setData(value);
        super.setValue(result);
    }


    /**
     * loading
     * 子线程时调用 此方法
     */
    public void postLoading() {
        LiveResult<T> result = getValue();
        if (result == null) {
            result = new LiveResult<>();
        }
        result.setLoading();
        super.postValue(result);
    }

    /**
     * 请求loading
     * 主线程时调用此方法
     */
    public void setLoading() {
        LiveResult<T> result = getValue();
        if (result == null) {
            result = new LiveResult<>();
        }
        result.setLoading();
        super.setValue(result);
    }


    /**
     * 请求异常的设值
     * 子线程时调用 此方法
     */
    public void postError(ErrorMessage errorMessage) {
        LiveResult<T> result = getValue();
        if (result == null) {
            result = new LiveResult<>();
        }
        result.setError(errorMessage);
        super.postValue(result);
    }



    /**
     * 请求异常的设值
     * 主线程时调用此方法
     */
    public void setError(ErrorMessage errorMessage) {
        LiveResult<T> result = getValue();
        if (result == null) {
            result = new LiveResult<>();
        }
        result.setError(errorMessage);
        super.setValue(result);
    }

    /**
     * 获取数据
     */
    @Nullable
    public T getData(){
        LiveResult<T> result = getValue();
        if (result != null && result.type == DataType.SUCCESS){
            return result.data;
        }
        return null;
    }

    /**
     * 是否是成功数据
     */
    public boolean isSuccess(){
        LiveResult<T> result = getValue();
        return result != null && result.type == DataType.SUCCESS;
    }

    /**
     * 获取错误信息
     */
    public LiveResult getErrorInfo(){
        LiveResult<T> result = getValue();
        if (result != null && result.type != DataType.SUCCESS){
            return result;
        }
        return null;
    }
}
