package com.dale.net.callback;

import androidx.lifecycle.Observer;

import com.dale.net.bean.DataType;
import com.dale.net.bean.LiveResult;
import com.dale.net.exception.ErrorMessage;

public abstract class NetObserver<T> implements Observer<LiveResult<T>> {
    @Override
    public void onChanged(LiveResult<T> result) {
        if (result == null) {
            return;
        }
        switch (result.type) {
            case DataType.SUCCESS:
                onLoading(false);
                onSuccess(result.data);
                break;
            case DataType.LOADING:
                onLoading(true);
                break;
            case DataType.ERROR:
                onLoading(false);
                onError(result.errorMessage);
                break;
            default:
                throw new NullPointerException("事件类型找不到");
        }
    }

    protected abstract void onSuccess(T t);

    protected void onLoading(boolean show){
    }

    protected abstract void onError(ErrorMessage errorMessage);
}
