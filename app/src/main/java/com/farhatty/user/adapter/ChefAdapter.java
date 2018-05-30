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
import com.farhatty.user.model.Chef;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 12/5/2017.
 */

public class ChefAdapter extends RecyclerView.Adapter<ChefAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<Chef> chefList;
    private List<Chef> chefListFiltered;
    private ChefAdapterListener listener;

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
                    listener.onChefSelected(chefListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public ChefAdapter(Context context, List<Chef> chefList, ChefAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.chefList = chefList;
        this.chefListFiltered = chefList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chef_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Chef Chef = chefListFiltered.get(position);
        holder.name.setText(Chef.getName());
        holder.location.setText(Chef.getLocation());

        Glide.with(context)
                .load(Chef.getImage()).apply ( RequestOptions.circleCropTransform() )
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return chefListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    chefListFiltered = chefList;
                } else {
                    List<Chef> filteredList = new ArrayList<>();
                    for (Chef row : chefList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getLocation().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    chefListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = chefListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                chefListFiltered = (ArrayList<Chef>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ChefAdapterListener {
        void onChefSelected(Chef Chef);
    }
}
