package com.example.roby.photoalbum.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.roby.photoalbum.R;

import butterknife.ButterKnife;

public class AlbumEntryFragment extends Fragment {

    public AlbumEntryFragment() {

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
        if(bundle != null) {
//            selectedRecipe = bundle.getParcelable(RecipeDetailActivity.RECIPE_PARCEL);
//            currentStep = bundle.getParcelable(RecipeDetailActivity.RECIPE_STEP_PARCEL);
//            mTwoPane = bundle.getBoolean(RecipeDetailActivity.TWO_PANE_BUNDLE);
        }
        View view = inflater.inflate(R.layout.fragment_photo_view, container, false);

        ButterKnife.bind(this, view);
        return view;
    }
}
