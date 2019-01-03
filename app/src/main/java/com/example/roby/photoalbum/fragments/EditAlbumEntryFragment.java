package com.example.roby.photoalbum.fragments;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.roby.photoalbum.R;
import com.example.roby.photoalbum.database.AppDatabase;
import com.example.roby.photoalbum.model.AlbumEntry;
import com.example.roby.photoalbum.utils.AppExecutors;
import com.example.roby.photoalbum.utils.BitmapUtils;
import com.example.roby.photoalbum.utils.Constants;
import com.example.roby.photoalbum.utils.Utils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;
import static android.support.constraint.Constraints.TAG;

public class EditAlbumEntryFragment extends Fragment {
    private static final int PLACE_PICKER_REQUEST = 1;
    private static final int REQUEST_SLOCATION_PERMISSION = 2;
    private Bitmap mResultsBitmap;

    private String mTmpImagePath;
    private String mFinalImagePath;

    OnSaveClickListener mOnSaveClickListener;

    @BindView(R.id.album_photo_edit_iv)
    ImageView mPhotoEditIv;

    @BindView(R.id.photo_date_edit_tv)
    TextView mPhotoEditDateTime;

    @BindView(R.id.photo_name_edit_tv)
    TextView mPhotoName;

    @BindView(R.id.btn_save)
    Button mBtnSave;

    @BindView(R.id.photo_description_et)
    EditText meditTextDescr;

    @BindView(R.id.photo_edit_place_tv)
    TextView mPhotoPlace;

    @BindView(R.id.photo_edit_location_tv)
    TextView mPhotoLocation;

    @BindView(R.id.btn_add_place)
    Button mBtnAddPlace;

    // Member variable for the Database
    private AppDatabase mDb;

    public interface OnSaveClickListener {
        String clickSaveBtn(String image, Bitmap mResultsBitmap);
    }

    public EditAlbumEntryFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //make sure that the host has implemented the callback
        try {
            mOnSaveClickListener = (EditAlbumEntryFragment.OnSaveClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnSaveClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // get transferred information from bundle
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mFinalImagePath = bundle.getString(Constants.EXTRA_PHOTO_PATH);
            mTmpImagePath = bundle.getString(Constants.EXTRA_TEMP_PHOTO_PATH);
        }
        View view = inflater.inflate(R.layout.fragment_photo_edit, container, false);

        ButterKnife.bind(this, view);

        mDb = AppDatabase.getInstance(getContext());

        // Resample the saved image to fit the ImageView
        mResultsBitmap = BitmapUtils.resamplePic(this.getContext(), mFinalImagePath == null ? mTmpImagePath : mFinalImagePath);

        final String[] newPath = {""};
        mBtnSave.setOnClickListener(v -> {
            newPath[0] = mOnSaveClickListener.clickSaveBtn(mFinalImagePath == null ? mTmpImagePath : mFinalImagePath, mResultsBitmap);
            onSaveButtonClicked(newPath[0]);
        });

        mBtnAddPlace.setOnClickListener(v-> {
            onAddPlaceButtonClicked();
        });

        // Set the new bitmap to the ImageView
        mPhotoEditIv.setImageBitmap(mResultsBitmap);

        mPhotoEditDateTime.setText(Utils.ReadExif(mFinalImagePath == null ? mTmpImagePath : mFinalImagePath).get(Utils.TAG_DATETIME));

        mPhotoName.setText(new File(mFinalImagePath == null ? mTmpImagePath : mFinalImagePath).getName());

        return view;
    }

    public void onAddPlaceButtonClicked() {

        if (ActivityCompat.checkSelfPermission(this.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // If you do not have permission, request it
            ActivityCompat.requestPermissions(this.getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_SLOCATION_PERMISSION);
            Toast.makeText(this.getContext(), "Error permission not granted", Toast.LENGTH_LONG).show();
            return;
        }
        try {
            // Start a new Activity for the Place Picker API, this will trigger {@code #onActivityResult}
            // when a place is selected or with the user cancels.
            PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
            Intent i = builder.build(this.getActivity());
            startActivityForResult(i, PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            Log.e(TAG, String.format("GooglePlayServices Not Available [%s]", e.getMessage()));
        } catch (GooglePlayServicesNotAvailableException e) {
            Log.e(TAG, String.format("GooglePlayServices Not Available [%s]", e.getMessage()));
        } catch (Exception e) {
            Log.e(TAG, String.format("PlacePicker Exception: %s", e.getMessage()));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST && resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(this.getContext(), data);
            if (place == null) {
                Log.i(TAG, "No place selected");
                return;
            }

            // Extract the place information from the API
            String placeName = place.getName().toString();
            String placeAddress = place.getLatLng().toString();

            mPhotoLocation.setText(placeAddress);
            mPhotoPlace.setText(placeName);
        }
    }

    /**
     * onSaveButtonClicked is called when the "save" button is clicked.
     * It retrieves user input and inserts that new task data into the underlying database.
     */
    public void onSaveButtonClicked(String newPath) {
        //AlbumEntry(String pictureName, String imagePath, String nearbyPlace, String location, String date)

        final AlbumEntry albumEntry = new AlbumEntry(new File(newPath).getName(),
                mFinalImagePath == null ? newPath: mFinalImagePath,
                mPhotoPlace.getText().toString(),
                mPhotoLocation.getText().toString(),
                mPhotoEditDateTime.getText().toString(),
                meditTextDescr.getText().toString());
        AppExecutors.getInstance().getDiskIO().execute(() -> mDb.albumEntryDAO().insertAlbumEntry(albumEntry));
    }
}
