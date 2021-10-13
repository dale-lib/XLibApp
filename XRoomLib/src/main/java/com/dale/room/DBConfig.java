package com.dale.room;

import android.content.Context;

import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.dale.utils.AppUtil;
import com.dale.utils.FileUtils;

import java.util.List;
import java.util.concurrent.Executor;

public class DBConfig implements IDBConfig {

    private DBManager dbManager;
    private Context context;
    private String dbName = AppUtil.getVersionName() + ".db";//名称
    private String dbPath = FileUtils.getDir("room");//路径
    private boolean allowMainThread = true;//是否允许在主线程访问数据库
    private boolean fallbackToUp = true;//当升级数据库时，允许数据库抛弃旧数据重新创建新的数据库表(设置迁移数据库如果发生错误，将会重新创建数据库，而不是发生崩溃)
    private  boolean fallbackToDown;//如果发生降级，是否会自动重新创建数据库
    private int[] fallbackToDestructive;//设置从某个版本开始迁移数据库如果发生错误，将会重新创建数据库，而不是发生崩溃
    private RoomDatabase.JournalMode journalMode = RoomDatabase.JournalMode.TRUNCATE;//设置数据库的日志模式
    private Executor executor;//设置自定义线程池
    private SupportSQLiteOpenHelper.Factory factory;//设置数据库工厂
    private List<RoomDatabase.Callback> callbacks;//数据库状态监听(监听数据库，创建和打开的操作)
    private Migration[] migrations;

    @Override
    public <T extends RoomDatabase> void initSDK(Context context, Class<T> databaseClass) {
        this.context = context;
        dbManager = new DBManager(this);
        dbManager.databaseBuilder(databaseClass);
    }

    @Override
    public DBManager with() {
        return dbManager;
    }

    @Override
    public IDBConfig setDBName(String dbName) {
        this.dbName = dbName;
        return this;
    }

    @Override
    public IDBConfig setDBPath(String dbPath) {
        this.dbPath = dbPath;
        return this;
    }

    @Override
    public IDBConfig setAllowMainThread(boolean allowMainThread) {
        this.allowMainThread = allowMainThread;
        return this;
    }

    @Override
    public IDBConfig setFallbackToUp(boolean fallbackToUp) {
        this.fallbackToUp = fallbackToUp;
        return this;
    }

    @Override
    public IDBConfig setFallbackToDown(boolean fallbackToDown) {
        this.fallbackToDown = fallbackToDown;
        return this;
    }

    @Override
    public IDBConfig setFallbackToDestructive(int[] fallbackToDestructive) {
        this.fallbackToDestructive = fallbackToDestructive;
        return this;
    }

    @Override
    public IDBConfig setJournalMode(RoomDatabase.JournalMode journalMode) {
        this.journalMode = journalMode;
        return this;
    }

    @Override
    public IDBConfig setExecutor(Executor executor) {
        this.executor = executor;
        return this;
    }

    @Override
    public IDBConfig setFactory(SupportSQLiteOpenHelper.Factory factory) {
        this.factory = factory;
        return this;
    }

    @Override
    public IDBConfig setRoomDatabaseCallback(List<RoomDatabase.Callback> callbacks) {
        this.callbacks = callbacks;
        return this;
    }

    @Override
    public IDBConfig setMigration(Migration... migrations) {
        this.migrations = migrations;
        return this;
    }

    public Context getContext() {
        return context;
    }

    public String getDbName() {
        return dbName;
    }

    public String getDbPath() {
        return dbPath;
    }

    public boolean isAllowMainThread() {
        return allowMainThread;
    }

    public boolean isFallbackToUp() {
        return fallbackToUp;
    }

    public boolean isFallbackToDown() {
        return fallbackToDown;
    }

    public int[] getFallbackToDestructive() {
        return fallbackToDestructive;
    }

    public RoomDatabase.JournalMode getJournalMode() {
        return journalMode;
    }

    public Executor getExecutor() {
        return executor;
    }

    public SupportSQLiteOpenHelper.Factory getFactory() {
        return factory;
    }

    public List<RoomDatabase.Callback> getCallbacks() {
        return callbacks;
    }

    public Migration[] getMigrations() {
        return migrations;
    }
}
