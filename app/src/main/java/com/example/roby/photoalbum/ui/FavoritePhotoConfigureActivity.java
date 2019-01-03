package com.example.roby.photoalbum.ui;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.roby.photoalbum.FavoritePhotoWidget;
import com.example.roby.photoalbum.R;
import com.example.roby.photoalbum.adapters.AlbumEntryAdapter;
import com.example.roby.photoalbum.model.AlbumEntry;
import com.example.roby.photoalbum.model.AlbumEntryViewModel;
import com.example.roby.photoalbum.utils.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//https://medium.com/android-bits/android-widgets-ad3d166458d3

public class FavoritePhotoConfigureActivity extends AppCompatActivity implements AlbumEntryAdapter.AlbumEntryAdapterOnClickHandler {

    RecyclerView mRecyclerViewPhotoList;

    AlbumEntryAdapter mAlbumEntryAdapter;

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setResult(RESULT_CANCELED);
        // activity stuffs
        setContentView(R.layout.activity_widget_configure);

        mRecyclerViewPhotoList = findViewById(R.id.widget_favorite_photo_rv);
        //set the layout manager to grid
        GridLayoutManager layoutManager = new GridLayoutManager(this, Utils.calculateColumnNumber(this, Utils.PHOTO_CARD_WIDTH));//, GridLayoutManager.VERTICAL, false);
        mRecyclerViewPhotoList.setLayoutManager(layoutManager);

        mRecyclerViewPhotoList.setHasFixedSize(true);
        mAlbumEntryAdapter = new AlbumEntryAdapter(this);
        mRecyclerViewPhotoList.setAdapter(mAlbumEntryAdapter);

        AlbumEntryViewModel viewModel = ViewModelProviders.of(this).get(AlbumEntryViewModel.class);

        // COMPLETED (12) Observe the LiveData object in the ViewModel. Use it also when removing the observer
        viewModel.getAlbumEntries().observe(this, albumEntries -> {
            List<String> photoPaths = new ArrayList<>();
            HashMap<String, String> descriptionsPhotos = new HashMap<>();

            for (AlbumEntry albumEntry : albumEntries) {
                photoPaths.add(albumEntry.getImagePath());
                descriptionsPhotos.put(albumEntry.getPictureName(), albumEntry.getDescription());
            }
            mAlbumEntryAdapter.setAlbumData(photoPaths);
            mAlbumEntryAdapter.setDescriptions(descriptionsPhotos);
        });

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
    }

    @Override
    public void onClick(String param) {
        final Context context = FavoritePhotoConfigureActivity.this;

        SharedPreferences.Editor sharedPreferences = context.getSharedPreferences(Utils.PREFERENCE_PHOTO_ALBUM_NAME, 0).edit();
        String photoName = new File(param).getName();
        sharedPreferences.putString(Utils.PREFERENCE_PHOTO_ALBUM_NAME + mAppWidgetId, photoName);
        sharedPreferences.putString(Utils.APP_WIDGET_PHOTO_NAME_PREFERENCE + mAppWidgetId, photoName);


        sharedPreferences.putString(Utils.PREFERENCE_PHOTO_DESCR + mAppWidgetId, mAlbumEntryAdapter.getDescriptionsPhotos().get(photoName));

        sharedPreferences.apply();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        FavoritePhotoWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

        Intent resultValue = new Intent();
        // Set the results as expected from a 'configure activity'.
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
        setResult(RESULT_OK, resultValue);
        finish();
    }

    private void showErrorMessage() {
        Toast.makeText(this, Utils.CHECK_DATABASE_CONNECTION, Toast.LENGTH_LONG).show();
    }
}