package com.example.roby.photoalbum;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import com.example.roby.photoalbum.ui.PhotoViewActivity;
import com.example.roby.photoalbum.utils.Utils;
import com.example.roby.photoalbum.widget.PhotoWidgetService;

/**
 * Implementation of App Widget functionality.
 */
public class FavoritePhotoWidget extends AppWidgetProvider {

    public static final String NO_DESCRIPTION = "No description";

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                       int appWidgetId) {

        //get recipe name from shared preferences
        SharedPreferences sharedPreferences = context.getSharedPreferences(Utils.PREFERENCE_PHOTO_ALBUM_NAME, Context.MODE_PRIVATE);
        String widgetText = sharedPreferences.getString(Utils.APP_WIDGET_PHOTO_NAME_PREFERENCE + appWidgetId, "");
        String photoDescr = sharedPreferences.getString(Utils.PREFERENCE_PHOTO_DESCR + appWidgetId, NO_DESCRIPTION);
        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.favorite_photo_widget);
        views.setTextViewText(R.id.appwidget_photo_name, widgetText);
        views.setTextViewText(R.id.appwidget_photo_descr_tv, photoDescr);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

