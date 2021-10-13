package com.dale.net.bean;

import com.dale.net.DownloadRequest;
import com.dale.net.callback.DownCallBack;

/**
 * create by Dale
 * create on 2019/7/13
 * description:
 */
public class DownloadRequestBuilder {
    String url;
    String savePath;
    String fileName;
    private DownloadRequest mRequest;

    public DownloadRequestBuilder url(String url) {
        this.url = url;
        return this;
    }

    public DownloadRequestBuilder savePath(String savePath) {
        this.savePath = savePath;
        return this;
    }

    public DownloadRequestBuilder fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public void cancelDownload(){
        if (mRequest != null){
            mRequest.cancel();
        }
    }

    public DownloadRequest send(DownCallBack downCallBack){
        mRequest = new DownloadRequest(this);
        mRequest.send(downCallBack);
        return mRequest;
    }

    public String getUrl() {
        return url;
    }

    public String getSavePath() {
        return savePath;
    }

    public String getFileName() {
        return fileName;
    }

}
