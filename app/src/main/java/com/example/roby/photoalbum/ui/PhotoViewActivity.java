package com.example.roby.photoalbum.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.roby.photoalbum.R;
import com.example.roby.photoalbum.fragments.AlbumEntryFragment;
import com.example.roby.photoalbum.fragments.EditAlbumEntryFragment;
import com.example.roby.photoalbum.utils.Constants;

public class PhotoViewActivity extends AppCompatActivity implements AlbumEntryFragment.OnEditClickListener, EditAlbumEntryFragment.OnSaveClickListener{
    private String mTmpImagePath;
    private Bundle bundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(null == savedInstanceState) {
            // get all the date from the bundle of the intent
            bundle = getIntent().getBundleExtra(Constants.PHOTO_FRAGMENT_VIEW_BUNDLE);
        }
        else {
            bundle = savedInstanceState.getBundle(Constants.PHOTO_FRAGMENT_VIEW_BUNDLE);
        }
        mTmpImagePath = bundle.getString(Constants.EXTRA_PHOTO_PATH);
        setContentView(R.layout.activity_photo_album_entry);

        AlbumEntryFragment viewAlbumEntryFragment = new AlbumEntryFragment();

        //set bundle for the passed data
        viewAlbumEntryFragment.setArguments(bundle);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.photo_entry_container, viewAlbumEntryFragment)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        bundle.putString(Constants.EXTRA_PHOTO_PATH, mTmpImagePath);
        outState.putBundle(Constants.PHOTO_FRAGMENT_VIEW_BUNDLE, bundle);
    }

    @Override
    public void clickEditBtn(String image) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.EXTRA_PHOTO_PATH, image);
        FragmentManager fm = getSupportFragmentManager();
        EditAlbumEntryFragment editAlbumEntryFragment = new EditAlbumEntryFragment();
        editAlbumEntryFragment.setArguments(bundle);

        fm.beginTransaction()
                .replace(R.id.photo_entry_container, editAlbumEntryFragment)
                .commit();
    }

    @Override
    public String clickSaveBtn(String image, Bitmap mFinalBitmap) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.EXTRA_PHOTO_PATH, image);
        FragmentManager fm = getSupportFragmentManager();
        AlbumEntryFragment albumEntryFragment = new AlbumEntryFragment();
        albumEntryFragment.setArguments(bundle);

        fm.beginTransaction()
                .replace(R.id.photo_entry_container, albumEntryFragment)
                .commit();
        return image;
    }
}
