package com.farhatty.user.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.farhatty.user.R;
import com.farhatty.user.model.BookingTrack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed K Madani on 16/11/17.
 */

public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<BookingTrack> trackList;
    private List<BookingTrack> trackListFiltered;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name,book_id,book_date,book_status,event_date,phone,total;


        public MyViewHolder(View view) {
            super(view);
            book_id = (TextView) view.findViewById(R.id.book_id);
            book_date = (TextView) view.findViewById(R.id.book_date);
            book_status = (TextView) view.findViewById(R.id.book_status);
            name = (TextView) view.findViewById(R.id.name);
            event_date = (TextView) view.findViewById(R.id.event_date);
            phone = (TextView) view.findViewById(R.id.phone);
            total = (TextView) view.findViewById(R.id.total);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                }
            });
        }
    }


    public TrackAdapter(Context context, List <BookingTrack> trackList, FragmentActivity activity) {
        this.context = context;
        this.trackList = trackList;
        this.trackListFiltered = trackList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.booking_raw_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final BookingTrack track = trackListFiltered.get(position);
        holder.book_id.setText(track.getId ());
        holder.book_date.setText(track.getBookingDate ());
        holder.book_status.setText(track.getStatus ());
        holder.name.setText(track.getName ());
        holder.event_date.setText(track.getDate ());
        holder.phone.setText(track.getPhone ());
        holder.total.setText(track.getTotal ()+ " " + context.getResources ().getString ( R.string.currency ));


    }

    @Override
    public int getItemCount() {
        return trackListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    trackListFiltered = trackList;
                } else {
                    List<BookingTrack> filteredList = new ArrayList<>();
                    for (BookingTrack row : trackList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) ) {
                            filteredList.add(row);
                        }
                    }

                    trackListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = trackListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                trackListFiltered = (ArrayList<BookingTrack>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface BookingTracckAdapterListener {
        void onBookingSelected(BookingTrack BookingTrack);
    }
}
