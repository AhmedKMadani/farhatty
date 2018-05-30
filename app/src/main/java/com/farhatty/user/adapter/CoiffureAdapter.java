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
import com.farhatty.user.model.Coiffure;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 12/5/2017.
 */

public class CoiffureAdapter extends RecyclerView.Adapter<CoiffureAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<Coiffure> coiffureList;
    private List<Coiffure> coiffureListFiltered;
    private CoiffureAdapterListener listener;

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
                    listener.onCoiffureSelected(coiffureListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public CoiffureAdapter(Context context, List<Coiffure> coiffureList, CoiffureAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.coiffureList = coiffureList;
        this.coiffureListFiltered = coiffureList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.coiffure_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Coiffure Coiffure = coiffureListFiltered.get(position);
        holder.name.setText(Coiffure.getName());
        holder.location.setText(Coiffure.getLocation());

        Glide.with(context)
                .load(Coiffure.getImage()).apply ( RequestOptions.circleCropTransform() )
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return coiffureListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    coiffureListFiltered = coiffureList;
                } else {
                    List<Coiffure> filteredList = new ArrayList<>();
                    for (Coiffure row : coiffureList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getLocation ().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    coiffureListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = coiffureListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                coiffureListFiltered = (ArrayList<Coiffure>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface CoiffureAdapterListener {
        void onCoiffureSelected(Coiffure Coiffure);
    }
}

