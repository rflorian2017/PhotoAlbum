package com.example.roby.photoalbum.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class PhotoWidgetService extends RemoteViewsService {
    @Override
    public PhotoRemoteViewsFactory onGetViewFactory(Intent intent) {
        return new PhotoRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}
