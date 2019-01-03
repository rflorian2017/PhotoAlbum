package com.example.roby.photoalbum.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.roby.photoalbum.R;
import com.example.roby.photoalbum.ui.AlbumEntryViewHolder;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class AlbumEntryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Store a member variable for the Album Entries
    private List<String> items;
    private HashMap<String,String> descriptionsPhotos;

    // Store the context for easy access
    private Context mContext;

    //Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    // Click handler to allow transition to a detail activity
    final private AlbumEntryAdapterOnClickHandler mClickHandler;

    public interface AlbumEntryAdapterOnClickHandler {
        void onClick(String param);

    }

    // Pass in the click handler to the constructor
    public AlbumEntryAdapter(AlbumEntryAdapterOnClickHandler clickHandler) {
        this.mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);

        RecyclerView.ViewHolder viewHolder = null;

        // Inflate the custom layout
        View recipeView = inflater.inflate(R.layout.album_entry_card, viewGroup, false);
        // Return a new holder instance
        viewHolder = new AlbumEntryViewHolder(recipeView, mClickHandler);
        ((AlbumEntryViewHolder) viewHolder).setAlbumEntryList(items);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        AlbumEntryViewHolder recipeViewHolder = (AlbumEntryViewHolder) viewHolder;
        recipeViewHolder.getmAlbumEntryIv().setScaleType(ImageView.ScaleType.FIT_CENTER);
        Picasso.get()
                .load(new File(items.get(position)))
                .resize(600, 600)
                .centerCrop()
                .into(recipeViewHolder.getmAlbumEntryIv());
    }

    @Override
    public int getItemCount() {
        if (null == items) return 0;
        //assume here that only one cursor is present. For now, as the movie app contains only one cursor, it is ok.
        /* TODO: In the future, try here to make it generic */

        return items.size();
    }

    public void setAlbumData(List<String> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void setDescriptions(HashMap<String,String> descriptionsPhotos){
        this.descriptionsPhotos = descriptionsPhotos;
    }

    public HashMap<String, String> getDescriptionsPhotos() {
        return descriptionsPhotos;
    }
}
