package com.example.roby.photoalbum.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.example.roby.photoalbum.R;
import com.example.roby.photoalbum.fragments.AlbumEntryFragment;
import com.example.roby.photoalbum.fragments.EditAlbumEntryFragment;
import com.example.roby.photoalbum.utils.BitmapUtils;
import com.example.roby.photoalbum.utils.Constants;

public class PhotoEditActivity extends AppCompatActivity implements EditAlbumEntryFragment.OnSaveClickListener, AlbumEntryFragment.OnEditClickListener {
    private String mTmpImagePath;
    private String mFinalImagePath;
    private Bundle bundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null == savedInstanceState) {
            // get all the date from the bundle of the intent
            bundle = getIntent().getBundleExtra(Constants.PHOTO_FRAGMENT_EDIT_BUNDLE);
        } else {
            bundle = savedInstanceState.getBundle(Constants.PHOTO_FRAGMENT_EDIT_BUNDLE);
        }

        if (bundle.getString(Constants.EXTRA_TEMP_PHOTO_PATH) != null) {
            mTmpImagePath = bundle.getString(Constants.EXTRA_TEMP_PHOTO_PATH);
        }

        if (bundle.getString(Constants.EXTRA_PHOTO_PATH) != null) {
            mFinalImagePath = bundle.getString(Constants.EXTRA_PHOTO_PATH);
        }


        setContentView(R.layout.activity_edit_photo_album_entry);
        // getSupportActionBar().setTitle(selectedRecipe.getRecipeName());

        EditAlbumEntryFragment editAlbumEntryFragment = new EditAlbumEntryFragment();

        //set bundle for the passed data
        editAlbumEntryFragment.setArguments(bundle);

        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.photo_edit_entry_container, editAlbumEntryFragment)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        bundle.putString(Constants.EXTRA_TEMP_PHOTO_PATH, mTmpImagePath);
        bundle.putString(Constants.EXTRA_PHOTO_PATH, mFinalImagePath);
        outState.putBundle(Constants.PHOTO_FRAGMENT_EDIT_BUNDLE, bundle);
    }

    @Override
    public String clickSaveBtn(String image, Bitmap mResultsBitmap) {
        Bundle bundle = new Bundle();
        if (mTmpImagePath != null) {
            // Delete the temporary image file
            BitmapUtils.deleteImageFile(this, mTmpImagePath);

            // Save the image
            mFinalImagePath = BitmapUtils.saveImage(this, mResultsBitmap);
            bundle.putString(Constants.EXTRA_PHOTO_PATH, mFinalImagePath);
        }
        else {
            bundle.putString(Constants.EXTRA_PHOTO_PATH, image);
        }
        FragmentManager fm = getSupportFragmentManager();
        AlbumEntryFragment albumEntryFragment = new AlbumEntryFragment();
        albumEntryFragment.setArguments(bundle);

        fm.beginTransaction()
                .replace(R.id.photo_edit_entry_container, albumEntryFragment)
                .commit();
        return mFinalImagePath;
    }

    @Override
    public void clickEditBtn(String image) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.EXTRA_PHOTO_PATH, image);
        FragmentManager fm = getSupportFragmentManager();
        EditAlbumEntryFragment editAlbumEntryFragment = new EditAlbumEntryFragment();
        editAlbumEntryFragment.setArguments(bundle);

        fm.beginTransaction()
                .replace(R.id.photo_edit_entry_container, editAlbumEntryFragment)
                .commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
