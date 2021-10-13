package com.dale.net.callback;

import com.dale.net.exception.ErrorMessage;

import java.io.File;

/**
 * create by Dale
 * create on 2019/7/14
 * description: 文件下载回调
 */
public interface DownCallBack{

     void onSuccess(File file);

     void onProgress(Integer integer);

     void onError(ErrorMessage errorMessage);
}
