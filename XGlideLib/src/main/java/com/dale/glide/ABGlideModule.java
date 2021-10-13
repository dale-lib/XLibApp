package com.dale.glide;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.module.AppGlideModule;
import com.dale.utils.FileUtils;
import com.dale.utils.StringUtils;

import java.io.File;
import java.io.InputStream;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

@GlideModule
public class ABGlideModule extends AppGlideModule {

    public static final int IMAGE_DISK_CACHE_MAX_SIZE = 100 * 1024 * 1024;//图片缓存文件最大值为100Mb

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        /**
         * 配置全局默认占位图等
         */
//        RequestOptions requestOptions = new RequestOptions()
//                .error(R.mipmap.x_state_failed)
//                .placeholder(R.mipmap.x_state_empty);
//        builder.setDefaultRequestOptions(requestOptions);


        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context).build();
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();

        int customMemoryCacheSize = (int) (1.2 * defaultMemoryCacheSize);
        int customBitmapPoolSize = (int) (1.2 * defaultBitmapPoolSize);

        builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));

        builder.setDefaultTransitionOptions(Drawable.class, DrawableTransitionOptions.withCrossFade());
        String path = FileUtils.getDir(FileUtils.PIC);
        if(!StringUtils.isEmpty(path)){
            builder.setDiskCache(() -> DiskLruCacheWrapper.create(new File(path), IMAGE_DISK_CACHE_MAX_SIZE));
        }else {
            super.applyOptions(context,builder);
        }

//        Glide.with(context)
//                .load("")
//                .apply(new RequestOptions().placeholder(R.drawable.ic_launcher))
//                .transition(DrawableTransitionOptions.with(drawableCrossFadeFactory))
//                .into(imageView);

    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(createHttpClient()));
    }


    private OkHttpClient createHttpClient() {
        return new OkHttpClient.Builder()
                .sslSocketFactory(createSSLSocketFactory(), new TrustAllManager())
                .hostnameVerifier(new TrustAllHostnameVerifier())
                .readTimeout(30_000, TimeUnit.MILLISECONDS)
                .connectTimeout(30_000, TimeUnit.MILLISECONDS)
                .writeTimeout(30_000, TimeUnit.MILLISECONDS)
                .build();
    }


    /**
     * trust manager
     */
    class TrustAllManager implements X509TrustManager {
        @SuppressLint("TrustAllX509TrustManager")
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        @SuppressLint("TrustAllX509TrustManager")
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }


    class TrustAllHostnameVerifier implements HostnameVerifier {
        @SuppressLint("BadHostnameVerifier")
        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }


    @SuppressLint("TrulyRandom")
    private static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory sSLSocketFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, null,
                    new SecureRandom());
            sSLSocketFactory = sc.getSocketFactory();
        } catch (Exception ignored) {
        }
        return sSLSocketFactory;
    }
}
