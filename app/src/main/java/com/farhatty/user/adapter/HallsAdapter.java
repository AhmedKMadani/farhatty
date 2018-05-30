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
import com.farhatty.user.model.Halls;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed K Madani on 16/11/17.
 */

public class HallsAdapter extends RecyclerView.Adapter<HallsAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<Halls> hallsList;
    private List<Halls> hallsListFiltered;
    private HallAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price,location;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            price = (TextView) view.findViewById(R.id.price);
            location = (TextView) view.findViewById(R.id.location);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onHallSelected(hallsListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public HallsAdapter(Context context, List<Halls> hallsList, HallAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.hallsList = hallsList;
        this.hallsListFiltered = hallsList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hall_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Halls halls = hallsListFiltered.get(position);
        holder.name.setText(halls.getName());
        holder.price.setText(halls.getPrice()+ " " + context.getResources ().getString ( R.string.currency ));
        holder.location.setText(halls.getLocation());

        Glide.with(context)
                .load(halls.getImage()).apply (RequestOptions.circleCropTransform() )
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return hallsListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    hallsListFiltered = hallsList;
                } else {
                    List<Halls> filteredList = new ArrayList<>();
                    for (Halls row : hallsList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getPrice().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    hallsListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = hallsListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                hallsListFiltered = (ArrayList<Halls>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface HallAdapterListener {
        void onHallSelected(Halls halls);
    }
}
