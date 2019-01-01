package com.example.roby.photoalbum.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

//set up table via ROOM
@Entity(tableName = "album_entries")
public class AlbumEntry {
    //Vars & columns
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name="picture_name")
    @NonNull
    public String pictureName;

    @ColumnInfo(name="image_path")
    public String imagePath;
    @ColumnInfo(name="nearby_place")
    public String nearbyPlace;
    public String location;

    public AlbumEntry(String pictureName, String imagePath, String nearbyPlace, String location) {
        this.pictureName = pictureName;
        this.imagePath = imagePath;
        this.nearbyPlace = nearbyPlace;
        this.location = location;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getNearbyPlace() {
        return nearbyPlace;
    }

    public void setNearbyPlace(String nearbyPlace) {
        this.nearbyPlace = nearbyPlace;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
