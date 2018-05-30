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
import com.farhatty.user.model.Offers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Ahmed K Madani on 16/11/17.
 */

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<Offers> offersList;
    private List<Offers> offersListFiltered;
    private OffersAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, date,location;
        public ImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            date = (TextView) view.findViewById ( R.id.date );

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onOffersSelected(offersListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public OffersAdapter(Context context, List<Offers> offersList, OffersAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.offersList = offersList;
        this.offersListFiltered = offersList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offers_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Offers offers = offersListFiltered.get(position);
        holder.title.setText(offers.getTitle_ar ());
        holder.date.setText(offers.getDate());

        Glide.with(context)
                .load(offers.getImage()).apply (RequestOptions.circleCropTransform() )
                .into(holder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return offersListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    offersListFiltered = offersList;
                } else {
                    List<Offers> filteredList = new ArrayList<>();
                    for (Offers row : offersList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTitle_ar ().toLowerCase().contains(charString.toLowerCase()) ) {
                            filteredList.add(row);
                        }
                    }

                    offersListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = offersListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                offersListFiltered = (ArrayList<Offers>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }



        public interface OffersAdapterListener {
            void onOffersSelected(Offers offers);

            void onBackPressed();
        }


    private static String getFormattedDateSimple(Long dateTime) {
        SimpleDateFormat newFormat = new SimpleDateFormat("MMMM dd, yyyy");
        return newFormat.format(new Date (dateTime));
    }


}





