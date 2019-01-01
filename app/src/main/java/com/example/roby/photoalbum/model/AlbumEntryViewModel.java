package com.example.roby.photoalbum.model;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.annotation.NonNull;

import com.example.roby.photoalbum.database.AppDatabase;

import java.util.ArrayList;
import java.util.List;

public class AlbumEntryViewModel extends AndroidViewModel {
    private ImagesLiveData imageData;
    // Simple in-memory cache. Details omitted for brevity.

    private LiveData<List<AlbumEntry>> albumEntries;

    public LiveData<List<AlbumEntry>> getAlbumEntries() {
        return albumEntries;
    }

    public AlbumEntryViewModel(@NonNull Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        imageData = new ImagesLiveData(application);
        albumEntries = database.albumEntryDAO().loadAllEntries();
    }

    public LiveData<List<String>> getImageData() {
        return imageData;
    }
}



class ImagesLiveData extends LiveData<List<String>> {
    private final Context context;

    /**
     * Getting All Images Path.
     *            the activity
     * @return ArrayList with images Path
     */
    private ArrayList<String> getAllShownImagesPath() {
        Uri uri;
        Cursor cursor;
        int column_index_data;
        ArrayList<String> listOfAllImages = new ArrayList<>();
        String absolutePathOfImage ;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

        cursor = this.context.getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);

            listOfAllImages.add(absolutePathOfImage);
        }
        return listOfAllImages;
    }

    public ImagesLiveData(Context context) {
        this.context = context;
        loadData();
    }
    private void loadData() {
        new AsyncTask<Activity,Void,List<String>>() {
            @Override
            protected List<String> doInBackground(Activity... activities) {

                try {
                    return getAllShownImagesPath();
                } catch (Exception e) {
                    e.printStackTrace();
                    e.getMessage();
                }
                return null;
            }
            @Override
            protected void onPostExecute(List<String> data) {
                setValue(data);
            }
        }.execute();
    }
}
