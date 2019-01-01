package com.example.roby.photoalbum.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.roby.photoalbum.R;
import com.example.roby.photoalbum.utils.BitmapUtils;
import com.example.roby.photoalbum.utils.Constants;
import com.example.roby.photoalbum.utils.Utils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditAlbumEntryFragment extends Fragment {
    private Bitmap mResultsBitmap;
    private String mTmpImagePath;

    @BindView(R.id.album_photo_edit_iv)
    ImageView mPhotoEditIv;

    @BindView(R.id.photo_date_edit_tv)
    TextView mPhotoEditDateTime;

    @BindView(R.id.photo_name_edit_tv)
    TextView mPhotoName;

    public EditAlbumEntryFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // get transferred information from bundle
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mTmpImagePath = bundle.getString(Constants.EXTRA_TEMP_PHOTO_PATH);
        }
        View view = inflater.inflate(R.layout.fragment_photo_edit, container, false);

        ButterKnife.bind(this, view);


        // Resample the saved image to fit the ImageView
        mResultsBitmap = BitmapUtils.resamplePic(this.getContext(), mTmpImagePath);

        // Set the new bitmap to the ImageView
        mPhotoEditIv.setImageBitmap(mResultsBitmap);

        mPhotoEditDateTime.setText(Utils.ReadExif(mTmpImagePath).get(Utils.TAG_DATETIME));

        mPhotoName.setText(new File(mTmpImagePath).getName());

        return view;
    }
}
