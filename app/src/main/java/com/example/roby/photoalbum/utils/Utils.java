package com.example.roby.photoalbum.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

public class Utils {

    public static final int PHOTO_CARD_WIDTH = 1000; //recipe card has 1080dp width
    public static final String CHECK_INTERNET_CONNECTION = "Please check the internet connection";
    public static final String PREFERENCE_RECIPE_ID = "RECIPE_NAME";
    public static final String APP_WIDGET = "APP_WIDGET";
    public static final String APP_WIDGET_RECIPE_NAME_PREFERENCE = "APP_WIDGET_RECIPE_NAME";
    public static final String APP_WIDGET_ID = "APP_WIDGET_ID";
    public static final String APP_WIDGET_RECIPE_INGREDIENTS = "APP_WIDGET_ID";

    //compute number of columns based on screen size
    // https://stackoverflow.com/questions/1016896/get-screen-dimensions-in-pixels
    public static int calculateColumnNumber(Activity activity, int width) {
        DisplayMetrics dm = activity.getResources().getDisplayMetrics();
        float dpWidth = dm.density * dm.widthPixels;

        int columnNumber = dm.widthPixels / width;

        // we want to have at least one column
        return columnNumber < 1 ? 1: columnNumber;
    }
}
