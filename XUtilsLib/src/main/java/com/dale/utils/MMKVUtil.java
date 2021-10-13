package com.dale.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.tencent.mmkv.MMKV;

import java.lang.reflect.Type;
import java.util.Set;

public final class MMKVUtil {

    private static MMKV INSTANCE;

    private MMKVUtil() {
    }


    public static void init(MMKV mmkv) {
        INSTANCE = mmkv;
    }

    public static void init(Context context) {
        init(1, context, null);
    }

    public static void init(Context context, String cryptKey) {
        init(1, context, cryptKey);
    }

    public static void init(int mode, Context context, String cryptKey) {
        MMKV.initialize(context);
        INSTANCE = MMKV.defaultMMKV(mode, cryptKey);
    }

    public static void initWithMmap(Context context, String mmapID) {
        initWithMmap(context, mmapID, 1);
    }

    public static void initWithMmap(Context context, String mmapID, int mode) {
        initWithMmap(context, mmapID, mode, (String) null);
    }

    public static void initWithMmap(Context context, String mmapID, int mode, String cryptKey) {
        MMKV.initialize(context);
        INSTANCE = MMKV.mmkvWithID(mmapID, mode, cryptKey);
    }

    public static MMKV mmkv() {
        if (INSTANCE == null) {
            throw new RuntimeException("请调用MMKVUtil.init()方法初始化MMKV");
        }
        return INSTANCE;
    }

    public static void put(String key, boolean value) {
        mmkv().encode(key, value);
    }

    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return mmkv().decodeBool(key, defaultValue);
    }

    public static void put(String key, int value) {
        mmkv().encode(key, value);
    }

    public static int getInt(String key) {
        return getInt(key, 0);
    }

    public static int getInt(String key, int defaultValue) {
        return mmkv().decodeInt(key, defaultValue);
    }

    public static void put(String key, long value) {
        mmkv().encode(key, value);
    }

    public static long getLong(String key) {
        return getLong(key, 0L);
    }

    public static long getLong(String key, long defaultValue) {
        return mmkv().decodeLong(key, defaultValue);
    }

    public static void put(String key, float value) {
        mmkv().encode(key, value);
    }

    public static float getFloat(String key) {
        return getFloat(key, 0.0F);
    }

    public static float getFloat(String key, float defaultValue) {
        return mmkv().decodeFloat(key, defaultValue);
    }

    public static void put(String key, double value) {
        mmkv().encode(key, value);
    }

    public static double getDouble(String key) {
        return getDouble(key, 0.0D);
    }

    public static double getDouble(String key, double defaultValue) {
        return mmkv().decodeDouble(key, defaultValue);
    }

    public static void put(String key, String value) {
        mmkv().encode(key, value);
    }

    public static void putObject(String key, Object obj) {
        if (obj == null) {
            return;
        }
        put(key, new Gson().toJson(obj));
    }

    @Nullable
    public static String getString(String key) {
        return getString(key, (String) null);
    }

    @Nullable
    public static <T> T getObject(String key, Type type) {
        String cache = getString(key, null);
        if (cache == null) return null;
        return new Gson().fromJson(cache, type);
    }

    @Nullable
    public static String getString(String key, String defaultValue) {
        return mmkv().decodeString(key, defaultValue);
    }

    public static void put(String key, Set<String> value) {
        mmkv().encode(key, value);
    }

    @Nullable
    public static Set<String> getStringSet(String key) {
        return getStringSet(key, (Set) null);
    }

    @Nullable
    public static Set<String> getStringSet(String key, Set<String> defaultValue) {
        return mmkv().decodeStringSet(key, defaultValue);
    }

    public static void put(String key, byte[] value) {
        mmkv().encode(key, value);
    }

    @Nullable
    public static byte[] decodeBytes(String key) {
        return mmkv().decodeBytes(key);
    }

    public static String[] getAllKeys() {
        return mmkv().allKeys();
    }

    public static long getCount() {
        return mmkv().count();
    }

    public static long getTotalSize() {
        return mmkv().totalSize();
    }

    public static void clearAll() {
        mmkv().clearAll();
    }

    public static void sync() {
        mmkv().sync();
    }

    public static boolean containsKey(String key) {
        return mmkv().containsKey(key);
    }

    public static void removeValueForKey(String key) {
        mmkv().removeValueForKey(key);
    }

    public static void removeValuesForKeys(String[] keys) {
        mmkv().removeValuesForKeys(keys);
    }

    public static void importFromSP(SharedPreferences sharedPreferences) {
        mmkv().importFromSharedPreferences(sharedPreferences);
    }


}

