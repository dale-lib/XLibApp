package com.dale.room;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Update;
import androidx.sqlite.db.SimpleSQLiteQuery;

import java.util.Collection;
import java.util.List;

@Dao
public interface BaseDao<T> {

    //    onConflict = OnConflictStrategy.REPLACE指定主键冲突时候的解决方式。直接替换。

    /**
     * 添加单个对象
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(T item);//插入单条数据

    /**
     * 插入对象集合
     * @param items
     * @return
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    List<Long> insert(Collection<T> items);

    /**
     * 根据对象中的主键删除（主键是自动增长的，无需手动赋值）
     */
    @Delete
    void delete(T item);

    /**
     * 删除对象集合
     * @param items
     */
    @Delete
    void delete(Collection<T> items);

//    default int deleteAll(){
//        String query = new SimpleSQLiteQuery("delete from $tableName");
//    }

    /**
     * 根据对象中的主键更新（主键是自动增长的，无需手动赋值）
     */
    @Update
    int update(T item);

    /**
     * 根据对象中的主键更新集合（主键是自动增长的，无需手动赋值）
     */
    @Update
    int update(Collection<T> items);


}
