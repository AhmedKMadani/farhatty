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
import com.farhatty.user.model.AllHotel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed K Madani on 16/11/17.
 */

public class AllHotelAdapter extends RecyclerView.Adapter<AllHotelAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<AllHotel> allhotelList;
    private List<AllHotel> allhotelListFiltered;
    private AllHotelAdapterListener listener;

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
                    listener.onAllHotelSelected(allhotelListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public AllHotelAdapter(Context context, List<AllHotel> allhotelList, AllHotelAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.allhotelList = allhotelList;
        this.allhotelListFiltered = allhotelList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_hotel_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final AllHotel allhotel = allhotelListFiltered.get(position);
        holder.name.setText(allhotel.getName());
        holder.location.setText(allhotel.getLocation());

        Glide.with(context)
                .load(allhotel.getImage()).apply (RequestOptions.circleCropTransform() )
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return allhotelListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    allhotelListFiltered = allhotelList;
                } else {
                    List<AllHotel> filteredList = new ArrayList<>();
                    for (AllHotel row : allhotelList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getPrice().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    allhotelListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = allhotelListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                allhotelListFiltered = (ArrayList<AllHotel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface AllHotelAdapterListener {
        void onAllHotelSelected(AllHotel allhotel);
    }
}
