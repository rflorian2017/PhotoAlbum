package com.example.roby.photoalbum.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

//set up table via ROOM
@Entity(tableName = "album_entries")
public class AlbumEntry {
    //Vars & columns
    @PrimaryKey(autoGenerate = false)
    public String pictureName;

    public String imagePath;
}
