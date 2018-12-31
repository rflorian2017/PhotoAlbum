package com.example.roby.photoalbum.ui;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.roby.photoalbum.R;
import com.example.roby.photoalbum.adapters.AlbumEntryAdapter;
import com.example.roby.photoalbum.model.AlbumEntry;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlbumEntryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.album_entry_iv)
    ImageView mAlbumEntryIv;

    public ImageView getmAlbumEntryIv() {
        return mAlbumEntryIv;
    }

    public void setmAlbumEntryIv(ImageView mAlbumEntryIv) {
        this.mAlbumEntryIv = mAlbumEntryIv;
    }


    private List<String> photos;

    public void setAlbumEntryList(List<String> photos) {
        this.photos = photos;
    }

    AlbumEntryAdapter.AlbumEntryAdapterOnClickHandler mClickHandler;

    public AlbumEntryViewHolder(@NonNull View itemView, AlbumEntryAdapter.AlbumEntryAdapterOnClickHandler clickHandler) {
        super(itemView);

        ButterKnife.bind(this, itemView);

        this.mClickHandler = clickHandler;
        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int adapterPosition = getAdapterPosition();
        String photo = photos.get(adapterPosition);
        mClickHandler.onClick(photo);
    }
}
