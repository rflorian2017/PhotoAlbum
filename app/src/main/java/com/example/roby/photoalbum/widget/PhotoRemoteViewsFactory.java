package com.example.roby.photoalbum.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.roby.photoalbum.R;
import com.example.roby.photoalbum.database.AppDatabase;
import com.example.roby.photoalbum.model.AlbumEntry;
import com.example.roby.photoalbum.utils.Utils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PhotoRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private int mAppWidgetId;
    private String photoName;
    private List<AlbumEntry> albumEntries = new ArrayList<>();

    public PhotoRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(Utils.APP_WIDGET,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    // Initialize the data set.
    public void onCreate() {
        // In onCreate() you set up any connections / cursors to your data source. Heavy lifting,
        // for example downloading or creating content etc, should be deferred to onDataSetChanged()
        // or getViewAt(). Taking more than 20 seconds in this call will result in an ANR.
    }

    @Override
    public void onDataSetChanged() {

        AppDatabase mDb;
        mDb = AppDatabase.getInstance(mContext);
        final long identityToken = Binder.clearCallingIdentity();

        try {
            albumEntries = mDb.albumEntryDAO().loadAlbumEntryList();

        } catch (Exception e) {
            e.printStackTrace();
            e.getMessage();
        }

        //get photo name from shared prefs
        SharedPreferences sharedPreferences = mContext.getSharedPreferences(Utils.PREFERENCE_PHOTO_ALBUM_NAME + mAppWidgetId, Context.MODE_PRIVATE);
        photoName = sharedPreferences.getString(Utils.PREFERENCE_PHOTO_ALBUM_NAME + mAppWidgetId, "");

        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return 0;
    }


    // Given the position (index) of a WidgetItem in the array, use the item's text value in
    // combination with the app widget item XML file to construct a RemoteViews object.
    @Override
    public RemoteViews getViewAt(int position) {
        // position will always range from 0 to getCount() - 1.

        // Construct a RemoteViews item based on the app widget item XML file, and set the
        // text based on the position.
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.favorite_photo_widget);

        // Return the RemoteViews object.
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

}