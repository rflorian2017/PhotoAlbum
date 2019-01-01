package com.example.roby.photoalbum.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roby.photoalbum.R;
import com.example.roby.photoalbum.adapters.AlbumEntryAdapter;
import com.example.roby.photoalbum.model.AlbumEntryViewModel;
import com.example.roby.photoalbum.utils.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PhotoAlbumMasterListFragment extends Fragment implements AlbumEntryAdapter.AlbumEntryAdapterOnClickHandler {
    // Define a new interface OnImageClickListener that triggers a callback in the host activity
    OnImageClickListener mCallback;

    @BindView(R.id.rv_photos)
    RecyclerView mRecyclerView;

    private AlbumEntryAdapter albumEntryAdapter;

    // OnImageClickListener interface, calls a method in the host activity named onImageSelected
    public interface OnImageClickListener {
        void onImageSelected(String imagePath);
    }

    @Override
    public void onClick(String imagePath) {
        mCallback.onImageSelected(imagePath);
    }

    // Override onAttach to make sure that the container activity has implemented the callback
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // This makes sure that the host activity has implemented the callback interface
        // If not, it throws an exception
        try {
            mCallback = (OnImageClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnImageClickListener");
        }
    }

    public PhotoAlbumMasterListFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();

        final View rootView = inflater.inflate(R.layout.fragment_master_list, container, false);

        ButterKnife.bind(this, rootView);

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), Utils.calculateColumnNumber(getActivity(), Utils.PHOTO_CARD_WIDTH));//, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        albumEntryAdapter = new AlbumEntryAdapter(this);


        AlbumEntryViewModel albumViewModel = ViewModelProviders.of(this).get(AlbumEntryViewModel.class);
        albumViewModel.getImageData().observe(this.getActivity(), (recipeData) -> {
            albumEntryAdapter.setAlbumData(recipeData);
            if (null == recipeData) {
                //showErrorMessage();
            }
        });

        mRecyclerView.setAdapter(albumEntryAdapter);

        return rootView;
    }

}
