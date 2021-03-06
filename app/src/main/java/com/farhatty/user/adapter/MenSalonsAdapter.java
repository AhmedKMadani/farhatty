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
import com.farhatty.user.model.Men;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 12/5/2017.
 */

public class MenSalonsAdapter extends RecyclerView.Adapter<MenSalonsAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<Men> menList;
    private List<Men> menListFiltered;
    private MenSalonsAdapterListener listener;

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
                    listener.onMenSalonsSelected(menListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public MenSalonsAdapter(Context context, List<Men> menList, MenSalonsAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.menList = menList;
        this.menListFiltered = menList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.men_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Men Men = menListFiltered.get(position);
        holder.name.setText(Men.getName());
        holder.location.setText(Men.getLocation());

        Glide.with(context)
                .load(Men.getImage()).apply ( RequestOptions.circleCropTransform() )
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return menListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    menListFiltered = menList;
                } else {
                    List<Men> filteredList = new ArrayList<>();
                    for (Men row : menList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getLocation ().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }

                    menListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = menListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                menListFiltered = (ArrayList<Men>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface MenSalonsAdapterListener {
        void onMenSalonsSelected(Men Men);
    }
}

