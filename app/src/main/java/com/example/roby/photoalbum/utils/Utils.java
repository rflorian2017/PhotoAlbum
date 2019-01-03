package com.example.roby.photoalbum.utils;

import android.app.Activity;
import android.media.ExifInterface;
import android.util.DisplayMetrics;

import com.google.android.gms.common.api.ApiException;
import com.google.photos.library.v1.internal.InternalPhotosLibraryClient;
import com.google.photos.library.v1.proto.MediaItem;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Utils {

    public static final int PHOTO_CARD_WIDTH = 1000; //recipe card has 1080dp width
    public static final String TAG_DATETIME = "datetime";
    public static final String CHECK_INTERNET_CONNECTION = "Please check the internet connection";
    public static final String PREFERENCE_PHOTO_ALBUM_NAME = "PHOTO_NAME";
    public static final String APP_WIDGET = "APP_WIDGET";
    public static final String APP_WIDGET_PHOTO_NAME_PREFERENCE = "APP_WIDGET_PHOTO_NAME_PREFERENCE";
    public static final String APP_WIDGET_ID = "APP_WIDGET_ID";
    public static final String APP_WIDGET_RECIPE_INGREDIENTS = "APP_WIDGET_ID";
    public static final String CHECK_DATABASE_CONNECTION = "Database access error";
    public static final String PREFERENCE_PHOTO_DESCR = "Photo_decription";

    //compute number of columns based on screen size
    // https://stackoverflow.com/questions/1016896/get-screen-dimensions-in-pixels
    public static int calculateColumnNumber(Activity activity, int width) {
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        float dpWidth = dm.density * dm.widthPixels;

        int columnNumber = (int)dpWidth / width;

        // we want to have at least one column
        return columnNumber < 1 ? 1: columnNumber;
    }

    public static Map<String,String> ReadExif(String file){
        Map<String, String> imageData = new HashMap<>() ;
        try {
            ExifInterface exifInterface = new ExifInterface(file);

            imageData.put(TAG_DATETIME, exifInterface.getAttribute(ExifInterface.TAG_DATETIME));

        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageData;
    }

    public void test( ) {
//        try {
//            // Make a request to list all media items in the user's library
//            // Iterate over all the retrieved media items
//            // Pagination is handled automatically
//            InternalPhotosLibraryClient.ListMediaItemsPagedResponse response = photosLibraryClient.listMediaItems();
//            for (MediaItem item : response.iterateAll()) {
//                // Get some properties of a media item
//                String id = item.getId();
//                String description = item.getDescription();
//                String mimeType = item.getMimeType();
//                String productUrl = item.getProductUrl();
//                String filename = item.getFilename();
//            }
//        } catch (ApiException e) {
//            // Handle error
//        }
    }
}
