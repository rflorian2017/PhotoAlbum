package com.example.roby.photoalbum.fragments;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.roby.photoalbum.R;
import com.example.roby.photoalbum.database.AppDatabase;
import com.example.roby.photoalbum.model.AddAlbumEntryViewModel;
import com.example.roby.photoalbum.model.AlbumEntry;
import com.example.roby.photoalbum.model.AlbumEntryViewModelFactory;
import com.example.roby.photoalbum.utils.Constants;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlbumEntryFragment extends Fragment {

    private String mTmpImagePath;

    OnEditClickListener mOnEditClickListener;

    // Member variable for the Database
    private AppDatabase mDb;

    @BindView(R.id.album_photo_iv)
    ImageView mPhotoViewIv;

    @BindView(R.id.photo_name_tv)
    TextView mPhotoName;

    @BindView(R.id.photo_date_tv)
    TextView mPhotoDate;

    @BindView(R.id.btn_edit)
    Button mBtnEdit;

    @BindView(R.id.photo_description_tv)
    TextView mPhotoDescrTv;

    @BindView(R.id.photo_view_place_tv)
    TextView mPhotoPlaceTv;

    @BindView(R.id.photo_view_location_tv)
    TextView mPhotoLocationTv;

    public AlbumEntryFragment() {

    }

    public interface OnEditClickListener {
        void clickEditBtn(String image);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //make sure that the host has implemented the callback
        try {
            mOnEditClickListener = (AlbumEntryFragment.OnEditClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnEditClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // get transferred information from bundle
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            mTmpImagePath = bundle.getString(Constants.EXTRA_PHOTO_PATH);
        }
        View view = inflater.inflate(R.layout.fragment_photo_view, container, false);

        ButterKnife.bind(this, view);

        mPhotoName.setText(new File(mTmpImagePath).getName());

        Picasso.get()
                .load(new File(mTmpImagePath))
                .resize(600, 600)
                .centerCrop()
                .into(mPhotoViewIv);

        mDb = AppDatabase.getInstance(getContext());

        AlbumEntryViewModelFactory factory = new AlbumEntryViewModelFactory(mDb, mPhotoName.getText().toString());
        final AddAlbumEntryViewModel viewModel
                = ViewModelProviders.of(this, factory).get(AddAlbumEntryViewModel.class);

        // COMPLETED (12) Observe the LiveData object in the ViewModel. Use it also when removing the observer
        viewModel.getAlbumEntryLiveData().observe(this, new Observer<AlbumEntry>() {
            @Override
            public void onChanged(@Nullable AlbumEntry albumEntry) {
                viewModel.getAlbumEntryLiveData().removeObserver(this);
                populateUI(albumEntry);
            }
        });

        mBtnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnEditClickListener.clickEditBtn(mTmpImagePath);
            }
        });

        return view;
    }

    private void populateUI(AlbumEntry albumEntry) {
        if (albumEntry == null) {
            return;
        }
        mPhotoDescrTv.setText(albumEntry.getDescription());
        mPhotoPlaceTv.setText(albumEntry.getNearbyPlace());
        mPhotoLocationTv.setText(albumEntry.getLocation());
    }
}
