package com.farhatty.user.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.farhatty.user.R;
import com.farhatty.user.model.Photo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 12/5/2017.
 */

public class PhotographerAdapter extends RecyclerView.Adapter<PhotographerAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<Photo> photoList;
    private List<Photo> photoListFiltered;
    private PhotographerAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price,location;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            location = (TextView) view.findViewById(R.id.location);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onPhotographerSelected(photoListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public PhotographerAdapter(Context context, List<Photo> photoList, PhotographerAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.photoList = photoList;
        this.photoListFiltered = photoList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.photo_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Photo Photo = photoListFiltered.get(position);
        holder.name.setText(Photo.getName());
        holder.location.setText(Photo.getLocation());

        Glide.with(context)
                .load(Photo.getImage()).apply ( RequestOptions.circleCropTransform() )
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return photoListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    photoListFiltered = photoList;
                } else {
                    List<Photo> filteredList = new ArrayList<>();
                    for (Photo row : photoList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getLocation().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    photoListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = photoListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                photoListFiltered = (ArrayList<Photo>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface PhotographerAdapterListener {
        void onPhotographerSelected(Photo Photo);
    }
}
