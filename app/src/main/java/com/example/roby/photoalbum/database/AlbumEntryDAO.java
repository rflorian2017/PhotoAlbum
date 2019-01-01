package com.example.roby.photoalbum.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.roby.photoalbum.model.AlbumEntry;

import java.util.List;

@Dao
public interface AlbumEntryDAO {
    @Query("SELECT * FROM album_entries ORDER BY picture_name")
    LiveData<List<AlbumEntry>> loadAllEntries();

    @Query("SELECT * FROM album_entries ORDER BY picture_name")
    List<AlbumEntry> loadAlbumEntryList();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertAlbumEntry(AlbumEntry albumEntry);

    @Query("SELECT * FROM album_entries WHERE picture_name = :picture_name")
    LiveData<AlbumEntry> loadAlbumEntry(String picture_name);

}
