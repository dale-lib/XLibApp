package com.dale.room;


import android.content.Context;

import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import java.util.List;
import java.util.concurrent.Executor;


public interface IDBConfig {

    <T extends RoomDatabase> void initSDK(Context context,Class<T> databaseClass);

    /**
     * 名称
     */
    IDBConfig setDBName(String dbName);

    /**
     * 路径
     */
    IDBConfig setDBPath(String dbPath);

    /**
     * 是否允许在主线程访问数据库
     */
    IDBConfig setAllowMainThread(boolean allowMainThread);

    /**
     *  当升级数据库时，允许数据库抛弃旧数据重新创建新的数据库表
     *  (设置迁移数据库如果发生错误，将会重新创建数据库，而不是发生崩溃)
     */
    IDBConfig setFallbackToUp(boolean fallbackToUp);

    /**
     * 如果发生降级，是否会自动重新创建数据库
     */
    IDBConfig setFallbackToDown(boolean fallbackToDown);

    /**
     * 通知数据库，允许从特定的版本中抛弃旧数据重新创建新的数据库表
     * (设置从某个版本开始迁移数据库如果发生错误，将会重新创建数据库，而不是发生崩溃)
     */
    IDBConfig setFallbackToDestructive(int[] fallbackToDestructive);

    /**
     * 设置数据库的日志模式
     */
    IDBConfig setJournalMode(RoomDatabase.JournalMode journalMode);

    /**
     * 设置自定义线程池
     */
    IDBConfig setExecutor(Executor executor);

    /**
     * 设置数据库工厂
     */
    IDBConfig setFactory(SupportSQLiteOpenHelper.Factory factory);

    /**
     * 数据库状态监听
     * (监听数据库，创建和打开的操作)
     */
    IDBConfig setRoomDatabaseCallback(List<RoomDatabase.Callback> callbacks);

    /**
     * (设置数据库升级(迁移)的逻辑)
     *
     * 版本迁移构建器
     * 每个迁移都有一个开始和结束版本，Room运行这些迁移以将数据库带到最新版本。
     * 如果当前版本和最新版本之间缺少迁移项目，Room将清除数据库并重新创建，即使两个版本之间没有更改，您仍应向构建器提供迁移对象。
     * 迁移可以处理多个版本（例如，如果在没有转到版本4的情况下，在执行版本3到5时有更快的路径可供选择）。如果Room打开版本3的数据库且最新版本> = 5，
     * 则Room将使用可以从3迁移到5而不是3到4和4到5的迁移对象。
     */
    IDBConfig setMigration(Migration... migrations);

    DBManager with();
}
