package com.example.roby.photoalbum;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.roby.photoalbum.fragments.PhotoAlbumMasterListFragment;
import com.example.roby.photoalbum.ui.PhotoEditActivity;
import com.example.roby.photoalbum.ui.PhotoViewActivity;
import com.example.roby.photoalbum.utils.BitmapUtils;
import com.example.roby.photoalbum.utils.Constants;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements PhotoAlbumMasterListFragment.OnImageClickListener,
        SharedPreferences.OnSharedPreferenceChangeListener {

    @BindView(R.id.rv_photos)
    RecyclerView mRecyclerView;

    @BindView(R.id.fab)
    FloatingActionButton fab;

    private boolean mTwoPane;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_STORAGE_PERMISSION = 1;
    private String mTempPhotoPath;
    public static String displayCriteria;

    private static final String FILE_PROVIDER_AUTHORITY = "com.example.android.fileprovider";

    private static boolean PREFERENCES_HAVE_BEEN_UPDATED = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        fab.setOnClickListener(view -> {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            launchCamera();
        });

        //preferences are used to switch the DISPLAY criteria
        setupSharedPreferences();
    }

    //this is used to change the pref of the sorting criteria
    private void setupSharedPreferences() {
        //by default I am showing photos from internal storage
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
//        mMovieAdapter.setSortingCriteria(sharedPrefs.getString(getString(R.string.pref_display_crit),
//                getString(R.string.pref_sort_crit_top_rated_key)));
        displayCriteria = sharedPrefs.getString(getString(R.string.pref_display_crit), getString(R.string.pref_display_crit_int_storage_key));

        sharedPrefs.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If the image capture activity was called and was successful
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Process the image and set it to the TextView
            processAndSetImage();
        } else {

            // Otherwise, delete the temporary image file
            BitmapUtils.deleteImageFile(this, mTempPhotoPath);
        }
    }

    /**
     * Method for processing the captured image
     */
    private void processAndSetImage() {

        Intent intent = new Intent(this, PhotoEditActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.EXTRA_TEMP_PHOTO_PATH, mTempPhotoPath);
        intent.putExtra(Constants.PHOTO_FRAGMENT_EDIT_BUNDLE, bundle);

        // Toggle Visibility of the views
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // Called when you request permission to read and write to external storage
        switch (requestCode) {
            case REQUEST_STORAGE_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // If you get permission, launch the camera
                    launchCamera();
                } else {
                    // If you do not get permission, show a Toast
                    Toast.makeText(this, R.string.permission_denied, Snackbar.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    /**
     * Creates a temporary image file and captures a picture to store in it.
     */
    private void launchCamera() {
        // Check for the external storage permission
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            // If you do not have permission, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_STORAGE_PERMISSION);
        } else {
            // Launch the camera if the permission exists
            // Create the capture image intent
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the temporary File where the photo should go
                File photoFile = null;
                try {
                    photoFile = BitmapUtils.createTempImageFile(this);
                } catch (IOException ex) {
                    // Error occurred while creating the File
                    ex.printStackTrace();
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {

                    // Get the path of the temporary file
                    mTempPhotoPath = photoFile.getAbsolutePath();

                    // Get the content URI for the image file
                    Uri photoURI = FileProvider.getUriForFile(this,
                            FILE_PROVIDER_AUTHORITY,
                            photoFile);

                    // Add the URI so the camera can store the image
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                    // Launch the camera activity
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent startSettingsActivity = new Intent(this, Settings.class);
            startActivity(startSettingsActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //unregister the shared pref listener
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (PREFERENCES_HAVE_BEEN_UPDATED) {
//            invalidateData();
//            getSupportLoaderManager().restartLoader(MOVIE_QUERIES_LOADER_ID, null, this);

            PREFERENCES_HAVE_BEEN_UPDATED = false;
        }
    }

    @Override
    public void onImageSelected(String position) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.EXTRA_PHOTO_PATH, position);
        if (mTwoPane) {

//            TODO: Create for two pane in the future
        } else {
            Intent intent = new Intent(this, PhotoViewActivity.class);

            intent.putExtra(Constants.PHOTO_FRAGMENT_VIEW_BUNDLE, bundle);
            startActivity(intent);
        }


    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals((getString(R.string.pref_display_crit)))) {
//            mMovieAdapter.setSortingCriteria(sharedPreferences.getString(getString(R.string.pref_sort_crit),
//                    getString(R.string.pref_sort_crit_top_rated_key)));
            displayCriteria = sharedPreferences.getString(getString(R.string.pref_display_crit), getString(R.string.pref_display_crit_int_storage_key));
            PREFERENCES_HAVE_BEEN_UPDATED = true;
        }
    }
}
