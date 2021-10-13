package com.dale.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import androidx.annotation.RequiresApi;
import androidx.annotation.RequiresPermission;

import com.dale.constant.LibApplication;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * 设备信息工具类
 */
public final class DeviceUtils {

    /**
     * IMEI 通俗来讲就是标识你当前设备(手机)全世界唯一，类似于个人身份证
     *
     * @param ctx
     * @return
     */
    @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    public static String getIMEI(Context ctx) {
        String imei;
        try {
            imei = ((TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
            if (StringUtils.isEmpty(imei)) {
                imei = getIMSI(ctx);
            }
        } catch (Exception e) {
            imei = getIMSI(ctx);
        }
        return imei;
    }

    /**
     * IMSI通俗来讲就是标识你当前SIM卡(手机卡)唯一，同样类似于个人身份证，肯定唯一啦~
     *
     * @param ctx
     * @return
     */
    @RequiresPermission(android.Manifest.permission.READ_PHONE_STATE)
    public static String getIMSI(Context ctx) {
        String imsi;
        try {
            TelephonyManager tm = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
            imsi = tm.getSubscriberId();
        } catch (Exception e) {
            imsi = "";
        }
        return imsi;
    }


    /**
     * 获取设备系统版本号
     *
     * @return 设备系统版本号
     */
    public static String getSDKVersionName() {
        return Build.VERSION.RELEASE;
    }

    /**
     * 获取当前SDK 版本号
     *
     * @return
     */
    public static int getSDKVersion() {
        return Build.VERSION.SDK_INT;
    }


    /**
     * 获取Android id
     * <pre>
     *     在设备首次启动时, 系统会随机生成一个64位的数字, 并把这个数字以十六进制字符串的形式保存下来, 这个十六进制的字符串就是ANDROID_ID, 当设备被wipe后该值会被重置
     * </pre>
     *
     * @return
     */
    public static String getAndroidId() {
        // Android id 默认为 null
        String androidId = "";
        try {
            androidId = Settings.Secure.getString(LibApplication.getApp().getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {

        }
        return androidId;
    }


    /**
     * 判断设备是否 root
     *
     * @return
     */
    public static boolean isDeviceRooted() {
        String su = "su";
        String[] locations = {"/system/bin/", "/system/xbin/", "/sbin/", "/system/sd/xbin/",
                "/system/bin/failsafe/", "/data/local/xbin/", "/data/local/bin/", "/data/local/"};
        for (String location : locations) {
            if (new File(location + su).exists()) {
                return true;
            }
        }
        return false;
    }


    /**
     * 返回是否启用了 ADB
     *
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static boolean isAdbEnabled() {
        try {
            return Settings.Secure.getInt(LibApplication.getApp().getContentResolver(), Settings.Global.ADB_ENABLED, 0) > 0;
        } catch (Exception e) {
        }
        return false;
    }

    // 特殊mac地址用于判断是否获取失败
    private static final String CUSTOM_MAC = "02:00:00:00:00:00";

    /**
     * 获取设备 MAC 地址
     * <uses-permission android:name="android.permission.INTERNET" />
     * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
     *
     * @return
     */
    @RequiresPermission(allOf = {android.Manifest.permission.INTERNET, android.Manifest.permission.ACCESS_WIFI_STATE})
    public static String getMacAddress() {
        String macAddress = getMacAddressByWifiInfo();
        if (!CUSTOM_MAC.equals(macAddress)) {
            return macAddress;
        }
        macAddress = getMacAddressByNetworkInterface();
        if (!CUSTOM_MAC.equals(macAddress)) {
            return macAddress;
        }
        macAddress = getMacAddressByInetAddress();
        if (!CUSTOM_MAC.equals(macAddress)) {
            return macAddress;
        }

        if (!CUSTOM_MAC.equals(macAddress)) {
            return macAddress;
        }
        // 没有打开wifi, 获取 WLAN MAC 地址失败
        return null;
    }

    /**
     * 获取设备 MAC 地址
     * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
     *
     * @return
     */
    @RequiresPermission(android.Manifest.permission.ACCESS_WIFI_STATE)
    private static String getMacAddressByWifiInfo() {
        try {
            @SuppressLint("WifiManagerLeak")
            WifiManager wifiManager = (WifiManager) LibApplication.getApp().getSystemService(Context.WIFI_SERVICE);
            if (wifiManager != null) {
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                if (wifiInfo != null) return wifiInfo.getMacAddress();
            }
        } catch (Exception e) {
        }
        return CUSTOM_MAC;
    }

    /**
     * 获取设备 MAC 地址
     * <uses-permission android:name="android.permission.INTERNET" />
     *
     * @return
     */
    @RequiresPermission(android.Manifest.permission.INTERNET)
    private static String getMacAddressByNetworkInterface() {
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                if (ni == null || !ni.getName().equalsIgnoreCase("wlan0")) continue;
                byte[] macBytes = ni.getHardwareAddress();
                if (macBytes != null && macBytes.length > 0) {
                    StringBuilder builder = new StringBuilder();
                    for (byte b : macBytes) {
                        builder.append(String.format("%02x:", b));
                    }
                    return builder.substring(0, builder.length() - 1);
                }
            }
        } catch (Exception e) {
        }
        return CUSTOM_MAC;
    }

    /**
     * 通过 InetAddress 获取 Mac 地址
     *
     * @return Mac 地址
     */
    private static String getMacAddressByInetAddress() {
        try {
            InetAddress inetAddress = getInetAddress();
            if (inetAddress != null) {
                NetworkInterface ni = NetworkInterface.getByInetAddress(inetAddress);
                if (ni != null) {
                    byte[] macBytes = ni.getHardwareAddress();
                    if (macBytes != null && macBytes.length > 0) {
                        StringBuilder builder = new StringBuilder();
                        for (byte b : macBytes) {
                            builder.append(String.format("%02x:", b));
                        }
                        return builder.substring(0, builder.length() - 1);
                    }
                }
            }
        } catch (Exception e) {
        }
        return "02:00:00:00:00:00";
    }

    /**
     * 获取 InetAddress
     *
     * @return
     */
    private static InetAddress getInetAddress() {
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                if (!ni.isUp()) continue;
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress inetAddress = addresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String hostAddress = inetAddress.getHostAddress();
                        if (hostAddress.indexOf(':') < 0) return inetAddress;
                    }
                }
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 获取设备厂商 如 Xiaomi
     *
     * @return 设备厂商
     */
    public static String getManufacturer() {
        return Build.MANUFACTURER;
    }

    /**
     * 获取设备型号 如 RedmiNote4X
     *
     * @return 设备型号
     */
    public static String getModel() {
        String model = Build.MODEL;
        if (model != null) {
            model = model.trim().replaceAll("\\s*", "");
        } else {
            model = "";
        }
        return model;
    }

}
