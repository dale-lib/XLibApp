package com.dale.room;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import java.util.List;


/**
 * 数据库配置
 * <p>
 * 使用 2017 Google IO 大会 Architecture Components 架构组件 Room
 * <p>
 * 请在外部添加自己的数据库并继承 {@link RoomDatabase}}
 * 方法获取实例，{@link RoomDatabase} 已经实现单例化，外部无需再处理
 *
 * @see <a href="https://developer.android.google.cn/training/data-storage/room"></a>
 * @see <a href="https://github.com/googlesamples/android-architecture-components"></a>
 */
public class DBManager {

    private RoomDatabase database;

    private DBConfig config;

    public DBManager(DBConfig config) {
        this.config = config;
    }

    public RoomDatabase database() {
        return database;
    }

    public <T extends RoomDatabase> T databaseBuilder(@NonNull Class<T> databaseClass) {
        RoomDatabase.Builder<T> builder = Room.databaseBuilder(config.getContext(), databaseClass, config.getDbPath() + config.getDbName());
        if (config.isAllowMainThread()) {
            builder.allowMainThreadQueries();
        }

        if (config.isFallbackToUp()) {
            builder.fallbackToDestructiveMigration();
        }

        int[] startVersions = config.getFallbackToDestructive();
        if (startVersions != null && startVersions.length != 0) {
            builder.fallbackToDestructiveMigrationFrom(startVersions);
        }

        if (config.getJournalMode() != null) {
            builder.setJournalMode(config.getJournalMode());
        }

        if (config.isFallbackToDown()) {
            builder.fallbackToDestructiveMigrationOnDowngrade();
        }

        if (config.getExecutor() != null) {
            builder.setQueryExecutor(config.getExecutor());
        }

        if (config.getFactory() != null) {
            builder.openHelperFactory(config.getFactory());
        }

        List<RoomDatabase.Callback> callbacks = config.getCallbacks();
        if (callbacks != null) {
            for (RoomDatabase.Callback callback : callbacks) {
                builder.addCallback(callback);
            }
        }

        if (config.getMigrations() != null) {
            builder.addMigrations(config.getMigrations());
        }
        database = builder.build();
        return (T) database;
    }
}