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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.roby.photoalbum.R;
import com.example.roby.photoalbum.database.AppDatabase;
import com.example.roby.photoalbum.model.AlbumEntry;
import com.example.roby.photoalbum.utils.AppExecutors;
import com.example.roby.photoalbum.utils.BitmapUtils;
import com.example.roby.photoalbum.utils.Constants;
import com.example.roby.photoalbum.utils.Utils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditAlbumEntryFragment extends Fragment {
    private Bitmap mResultsBitmap;
    private String mTmpImagePath;

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

    // Member variable for the Database
    private AppDatabase mDb;

    public interface OnSaveClickListener {
        void clickSaveBtn(String image);
    }

    public EditAlbumEntryFragment() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //make sure that the host has implemented the callback
        try {
            mOnSaveClickListener = (EditAlbumEntryFragment.OnSaveClickListener) context;
        }
        catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnSaveClickListener");
        }
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

        mDb = AppDatabase.getInstance(getContext());

        // Resample the saved image to fit the ImageView
        mResultsBitmap = BitmapUtils.resamplePic(this.getContext(), mTmpImagePath);

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSaveButtonClicked();
                mOnSaveClickListener.clickSaveBtn(mTmpImagePath);
            }
        });

        // Set the new bitmap to the ImageView
        mPhotoEditIv.setImageBitmap(mResultsBitmap);

        mPhotoEditDateTime.setText(Utils.ReadExif(mTmpImagePath).get(Utils.TAG_DATETIME));

        mPhotoName.setText(new File(mTmpImagePath).getName());

        return view;
    }

    /**
     * onSaveButtonClicked is called when the "save" button is clicked.
     * It retrieves user input and inserts that new task data into the underlying database.
     */
    public void onSaveButtonClicked() {
        //AlbumEntry(String pictureName, String imagePath, String nearbyPlace, String location, String date)

        final AlbumEntry albumEntry = new AlbumEntry(mPhotoName.getText().toString(),
                mTmpImagePath,
                "",
                "",
                mPhotoEditDateTime.getText().toString(),
                meditTextDescr.getText().toString());
        final long[] new_row = new long[1];
        AppExecutors.getInstance().getDiskIO().execute(new Runnable() {
            @Override
            public void run() {
                    new_row[0] = mDb.albumEntryDAO().insertAlbumEntry(albumEntry);

            }
        });
    }
}
