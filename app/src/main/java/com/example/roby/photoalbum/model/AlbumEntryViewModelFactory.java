package com.example.roby.photoalbum.model;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;

import com.example.roby.photoalbum.database.AppDatabase;

public class AlbumEntryViewModelFactory extends ViewModelProvider.NewInstanceFactory{
    private final AppDatabase mDb;
    private final String mPhotoName;

    public AlbumEntryViewModelFactory(AppDatabase mDb, String mPhotoName) {
        this.mDb = mDb;
        this.mPhotoName = mPhotoName;
    }

    @Override
    public <T extends ViewModel> T create(Class<T> modelClass) {
        //noinspection unchecked
        return (T) new AddAlbumEntryViewModel(mDb, mPhotoName);
    }
}
