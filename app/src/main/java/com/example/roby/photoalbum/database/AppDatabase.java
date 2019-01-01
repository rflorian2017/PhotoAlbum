package com.example.roby.photoalbum.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.example.roby.photoalbum.model.AlbumEntry;

@Database(entities = {AlbumEntry.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final Object LOCK = new Object();
    private static final String DB_NAME = "album_entries";

    private static AppDatabase sInstance;

    public static AppDatabase getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                //build DB
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class,
                        DB_NAME)
                        .build();
            }
        }
        return sInstance;
    }

    //call for DAO
    public abstract AlbumEntryDAO albumEntryDAO();
}
