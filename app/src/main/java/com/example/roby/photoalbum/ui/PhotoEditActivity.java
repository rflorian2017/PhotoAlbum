package com.example.roby.photoalbum.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.example.roby.photoalbum.R;
import com.example.roby.photoalbum.fragments.EditAlbumEntryFragment;
import com.example.roby.photoalbum.utils.BitmapUtils;
import com.example.roby.photoalbum.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoEditActivity extends AppCompatActivity {
    private String mTmpImagePath;
    private Bundle bundle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);

        if(null == savedInstanceState) {
            // get all the date from the bundle of the intent
           bundle = getIntent().getBundleExtra(Constants.PHOTO_FRAGMENT_EDIT_BUNDLE);
        }
        else {
            bundle = savedInstanceState.getBundle(Constants.PHOTO_FRAGMENT_EDIT_BUNDLE);
        }

        mTmpImagePath = bundle.getString(Constants.EXTRA_TEMP_PHOTO_PATH);

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
        outState.putBundle(Constants.PHOTO_FRAGMENT_EDIT_BUNDLE, bundle);
    }
}
