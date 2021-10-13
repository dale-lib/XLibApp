package com.dale.net.utils;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;

import com.dale.net.NetConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.ByteString;

/**
 * create by Dale
 * create on 2019/7/13
 * description:
 */
public class HttpLogger implements Interceptor {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;
        Connection connection = chain.connection();
        Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
        StringBuffer logBuffer = new StringBuffer();
        logBuffer.append("开始发送");
        logBuffer.append(request.method());
        logBuffer.append("请求:\n");
        logBuffer.append("请求路径:");
        logBuffer.append(request.url());
        logBuffer.append(" ");
        logBuffer.append(protocol);
        logBuffer.append("\n");
        if (hasRequestBody) {
            if (requestBody.contentType() != null) {
                logBuffer.append("Content-Type: ");
                logBuffer.append(requestBody.contentType());
                logBuffer.append("\n");
            }
            if (requestBody.contentLength() != -1) {
                logBuffer.append("Content-Length: ");
                logBuffer.append(requestBody.contentLength());
                logBuffer.append("\n");
            }
        }

        long t1 = System.nanoTime();//请求发起的时间
        Response response = chain.proceed(request);
        long t2 = System.nanoTime();//收到响应的时间

        Headers headers = response.request().headers();
        for (int i = 0, count = headers.size(); i < count; i++) {
            String name = headers.name(i);
            if (!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                logBuffer.append(name);
                logBuffer.append(": ");
                logBuffer.append(headers.value(i));
                logBuffer.append("\n");
            }
        }
        if (requestBody instanceof MultipartBody){
            hasRequestBody = false;
        }
        if (hasRequestBody && !bodyEncoded(request.headers())) {
            Buffer buffer = new Buffer();
            requestBody.writeTo(buffer);

            Charset charset = UTF8;
            MediaType contentType = requestBody.contentType();
            Buffer subBuffer = new Buffer();
            String boundary = "";
            long bufferSize = buffer.size();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
                if (TextUtils.equals(contentType.type(),"multipart")){
                    boundary = contentType.toString();
                    int indexBoundary = boundary.indexOf("boundary=");
                    if (indexBoundary > 0 && indexBoundary + 9 < boundary.length()){
                        boundary = boundary.substring(indexBoundary + 9);
                    }

                    long fromIndex = bufferSize - 2800 * 2 > 5 ? bufferSize - 2800 : 5;
                    long index = buffer.indexOf(ByteString.encodeUtf8(boundary),fromIndex);
                    if (index > 0){
                        buffer.copyTo(subBuffer,index,bufferSize - index);
                    }

                }
            }

            String requestBodyContent;
            if (subBuffer.size() > 0){
                requestBodyContent = String.format("%s\n文件\n%s",
                        boundary,getRequestParams(subBuffer,charset));
            }else {
                requestBodyContent = getRequestParams(buffer,charset);
            }

            logBuffer.append("请求参数: ");
            logBuffer.append(requestBodyContent);
            logBuffer.append("\n\n");
        }
        //这里不能直接使用response.body().string()的方式输出日志
        //因为response.body().string()之后，response中的流会被关闭，程序会报错，我们需要创建出一个新的response给应用层处理
        ResponseBody responseBody = response.peekBody(1024 * 1024);
        logBuffer.append("请求所花时间:");
        logBuffer.append((t2 - t1) / 1e6d);
        logBuffer.append("\n");
        Headers resHeaders = response.headers();
        boolean isUTF_8Body = false;
        for (int j = 0, rescount = resHeaders.size(); j < rescount; j++) {
            String name = resHeaders.name(j);
            String value = resHeaders.value(j);
            logBuffer.append(name);
            logBuffer.append(": ");
            logBuffer.append(value);
            logBuffer.append("\n");
            if (!TextUtils.isEmpty(value) &&
                    (value.contains("charset") || value.contains("application"))) {
                isUTF_8Body = true;
            }
        }
        logBuffer.append("响应状态码:");
        logBuffer.append(response.code());
        logBuffer.append("\n");
        logBuffer.append("返回数据:");
        String body = "";
        if (isUTF_8Body) {
            body = unicodeToUTF_8(responseBody.string());
        }
        body = parseJson(body);
        logBuffer.append(body.length() > 3200 ? body.substring(0, 3200) + ",未完..." : body);
        logBuffer.append("\n");
        logBuffer.append("** end **");
        String log = logBuffer.toString();
        Log.d(NetConstant.LOG_TAG,log);
        return response;
    }

    private String getRequestParams(Buffer buffer, Charset charset){
        if (isPlaintext(buffer) && charset != null) {
            return buffer.readString(charset);
        }
        return "";
    }

    /**
     * Returns true if the body in question probably contains human readable text. Uses a small sample
     * of code points to detect unicode control characters commonly used in binary file signatures.
     */
    private boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            // Truncated UTF-8 sequence.
            return false;
        }
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

    private static String unicodeToUTF_8(String src) {
        if (null == src) {
            return null;
        }
        StringBuilder out = new StringBuilder();
        try {
            for (int i = 0; i < src.length(); ) {
                char c = src.charAt(i);
                if (i + 6 < src.length() && c == '\\' && src.charAt(i + 1) == 'u') {
                    String hex = src.substring(i + 2, i + 6);
                    out.append((char) Integer.parseInt(hex, 16));
                    i = i + 6;
                } else {
                    out.append(src.charAt(i));
                    ++i;
                }
            }
        } catch (Exception e) {
        }
        return out.toString();
    }


    private static String parseJson(String msg) {
        String message;
        try {
            if (msg.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(msg);
                message = jsonObject.toString(4);//最重要的方法，就一行，返回格式化的json字符串，其中的数字4是缩进字符数
            } else if (msg.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(msg);
                message = jsonArray.toString(4);
            } else {
                message = msg;
            }
        } catch (JSONException e) {
            message = msg;
        }
        message = LINE_SEPARATOR + message;
        String[] lines = message.split(LINE_SEPARATOR);
        StringBuffer logBuffer = new StringBuffer();
        for (String line : lines) {
            logBuffer.append(line);
            logBuffer.append("\n");
        }
        return logBuffer.toString();
    }

    private static final String LINE_SEPARATOR = System.getProperty("line.separator");
}

