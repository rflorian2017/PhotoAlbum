package com.example.roby.photoalbum.model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.roby.photoalbum.database.AppDatabase;

public class AddAlbumEntryViewModel extends ViewModel {

    private LiveData<AlbumEntry> albumEntryLiveData;

    // COMPLETED (8) Create a constructor where you call loadTaskById of the taskDao to initialize the tasks variable
    // Note: The constructor should receive the database and the taskId
    public AddAlbumEntryViewModel(AppDatabase database, String photoName) {
        albumEntryLiveData = database.albumEntryDAO().loadAlbumEntry(photoName);
    }

    public LiveData<AlbumEntry> getAlbumEntryLiveData() {
        return albumEntryLiveData;
    }
}
