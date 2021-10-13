package com.dale.utils;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import androidx.annotation.RequiresPermission;

import com.dale.constant.LibApplication;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static android.Manifest.permission.INTERNET;

/**
 * create by Dale
 * create on 2019/7/19
 * description: 网络状态相关
 */
public class NetworkUtils {
    /**
     * wifi
     */
    public static final int NETWORK_WIFI = 1;
    /**
     * 2G
     */
    public static final int NETWORK_2G = 2;
    /**
     * 3G
     */
    public static final int NETWORK_3G = 3;
    /**
     * 4G
     */
    public static final int NETWORK_4G = 4;
    /**
     * 无网
     */
    public static final int NETWORK_NO = -1;

    /**
     * 未知
     */
    public static final int NETWORK_UNKNOWN = 5;


    private NetworkUtils() {
        throw new UnsupportedOperationException("u can't fuck me...");
    }

    @RequiresPermission(ACCESS_NETWORK_STATE)
    private static NetworkInfo getActiveNetworkInfo() {
        return ((ConnectivityManager) LibApplication.getApp().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
    }

    /**
     * 获取网络类型 2G 3G 4G 等
     *
     * @return 网络类型
     */
    public static int getNetWorkType() {
        byte var2 = NETWORK_NO;
        NetworkInfo var3 = getActiveNetworkInfo();
        byte var1 = var2;
        if (var3 != null) {
            var1 = var2;
            if (var3.isAvailable()) {
                if (var3.getType() != 1) {
                    if (var3.getType() == 0) {
                        switch (var3.getSubtype()) {
                            case 1:
                            case 2:
                            case 4:
                            case 7:
                            case 11:
                            case 16:
                                return NETWORK_2G;
                            case 3:
                            case 5:
                            case 6:
                            case 8:
                            case 9:
                            case 10:
                            case 12:
                            case 14:
                            case 15:
                            case 17:
                                return NETWORK_3G;
                            case 13:
                            case 18:
                                return NETWORK_4G;
                            default:
                                String var4 = var3.getSubtypeName();
                                if (!var4.equalsIgnoreCase("TD-SCDMA") && !var4.equalsIgnoreCase("WCDMA") && !var4.equalsIgnoreCase("CDMA2000")) {
                                    return NETWORK_UNKNOWN;
                                }

                                return NETWORK_3G;
                        }
                    }

                    return NETWORK_UNKNOWN;
                } else {
                    return NETWORK_WIFI;
                }
            }
        }

        return var1;
    }

    /**
     * 网络类型名字
     *
     * @return 网络类型的名字
     */
    public static String getNetWorkTypeName() {
        switch (getNetWorkType()) {
            case NETWORK_NO:
                return "NETWORK_NO";
            case NETWORK_WIFI:
                return "NETWORK_WIFI";
            case NETWORK_2G:
                return "NETWORK_2G";
            case NETWORK_3G:
                return "NETWORK_3G";
            case NETWORK_4G:
                return "NETWORK_4G";
            case 0:
            default:
                return "NETWORK_UNKNOWN";
        }
    }

    /**
     * 网络运营商名称
     *
     * @return 网络运营商名称
     */
    public static String getNetworkOperatorName() {
        TelephonyManager var1 = (TelephonyManager) LibApplication.getApp().getSystemService(Context.TELEPHONY_SERVICE);
        return var1 != null ? var1.getNetworkOperatorName() : null;
    }

    /**
     * 电话类型
     *
     * @return 电话类型
     */
    public static int getPhoneType() {
        TelephonyManager var1 = (TelephonyManager) LibApplication.getApp().getSystemService(Context.TELEPHONY_SERVICE);
        return var1 != null ? var1.getPhoneType() : -1;
    }


    /**
     * 是否是4G
     *
     * @return true是
     */
    public static boolean is4G() {
        NetworkInfo var1 = getActiveNetworkInfo();
        return var1 != null && var1.isAvailable() && var1.getSubtype() == 13;
    }

    /**
     * 网络是否有网络
     *
     * @return true表示有
     */
    public static boolean isAvailable() {
        NetworkInfo var1 = getActiveNetworkInfo();
        return var1 != null && var1.isAvailable();
    }

    /**
     * 网络是否连接
     *
     * @return true表示已连接, 但未必有网
     */
    public static boolean isConnected() {
        NetworkInfo var1 = getActiveNetworkInfo();
        return var1 != null && var1.isConnected();
    }

    /**
     * 是否已经连接wifi
     *
     * @return true是
     */
    public static boolean isWifiConnected() {
        ConnectivityManager var1 = (ConnectivityManager) LibApplication.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        return var1 != null && var1.getActiveNetworkInfo().getType() == 1;
    }

    /**
     * 打开网络设置界面
     */
    public static void openWirelessSettings() {
        LibApplication.getApp().startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS)
                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    /**
     * 获取本地ip地址
     *
     * @return ip地址
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
        }
        return null;
    }


    @RequiresPermission(INTERNET)
    public static String getDomainAddress(final String domain) {
        InetAddress inetAddress;
        try {
            inetAddress = InetAddress.getByName(domain);
            return inetAddress.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "";
        }
    }
}
