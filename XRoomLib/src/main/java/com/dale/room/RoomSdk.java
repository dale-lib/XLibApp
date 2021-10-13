package com.dale.room;

public class RoomSdk {

    private static class RoomHolder {
        private static IDBConfig dbConfig = new DBConfig();
    }

    public static IDBConfig ins() {
        return RoomHolder.dbConfig;
    }
}
