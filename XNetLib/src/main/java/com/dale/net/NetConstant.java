package com.dale.net;

/**
 * create by Dale
 * create on 2019/7/12
 * description:
 */
public class NetConstant {

    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String DELETE = "DELETE";
    public static final String HEAD = "HEAD";
    public static final String PUT = "PUT";
    public static final String PATCH = "PATCH";

    public static final String LOG_TAG = "NetLog";

    /**
     * 网络异常
     */
    public static final int NET_ERROR_CODE = 11001;
    /**
     * 请求异常
     */
    public static final int REQUEST_ERROR_CODE = 11002;

    /**
     * 数据解析异常
     */
    public static final int DATA_PARSE_ERROR = 11003;

    /**
     * 下载创建文件失败
     */
    public static final int REQUEST_FILE_CODE = 11004;

    /**
     * response body 为空
     */
    public static final int RESPONSE_BODY_EMPTY = 11005;

    /**
     * 未知服务异常
     */
    public static final int UNKNOW_SERVICE_ERROR = 11006;

}
