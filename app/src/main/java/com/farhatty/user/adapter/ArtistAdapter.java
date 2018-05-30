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
import com.farhatty.user.model.Artist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 12/5/2017.
 */

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<Artist> artistList;
    private List<Artist> artistListFiltered;
    private ArtistAdapterListener listener;

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
                    listener.onArtistSelected(artistListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public ArtistAdapter(Context context, List<Artist> artistList, ArtistAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.artistList = artistList;
        this.artistListFiltered = artistList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.artist_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Artist Artist = artistListFiltered.get(position);
        holder.name.setText(Artist.getName());
        holder.location.setText(Artist.getLocation());

        Glide.with(context)
                .load(Artist.getImage()).apply ( RequestOptions.circleCropTransform() )
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return artistListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    artistListFiltered = artistList;
                } else {
                    List<Artist> filteredList = new ArrayList<>();
                    for (Artist row : artistList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getLocation().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    artistListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = artistListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                artistListFiltered = (ArrayList<Artist>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ArtistAdapterListener {
        void onArtistSelected(Artist Artist);
    }
}
