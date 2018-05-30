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
import com.farhatty.user.model.AllCars;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed K Madani on 16/11/17.
 */

public class AllCarsAdapter extends RecyclerView.Adapter<AllCarsAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<AllCars> allcarList;
    private List<AllCars> allcarListFiltered;
    private AllCarsAdapterListener listener;

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
                    listener.onAllCarSelected(allcarListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public AllCarsAdapter(Context context, List<AllCars> allcarList, AllCarsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.allcarList = allcarList;
        this.allcarListFiltered = allcarList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.all_car_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final AllCars allcars = allcarListFiltered.get(position);
        holder.name.setText(allcars.getName());
        holder.location.setText(allcars.getLocation());

        Glide.with(context)
                .load(allcars.getImage()).apply (RequestOptions.circleCropTransform() )
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return allcarListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    allcarListFiltered = allcarList;
                } else {
                    List<AllCars> filteredList = new ArrayList<>();
                    for (AllCars row : allcarList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getPrice().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    allcarListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = allcarListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                allcarListFiltered = (ArrayList<AllCars>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface AllCarsAdapterListener {
        void onAllCarSelected(AllCars allcars);
    }
}
