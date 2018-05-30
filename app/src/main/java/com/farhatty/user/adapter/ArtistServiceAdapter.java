package com.farhatty.user.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.farhatty.user.R;
import com.farhatty.user.model.ServiceArtist;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2/13/2018.
 */

public class ArtistServiceAdapter extends RecyclerView.Adapter<ArtistServiceAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<ServiceArtist> serviceartistList;
    private List<ServiceArtist> serviceartistListFiltered;
    private ServiceArtistAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView service, price,descp;
        public CheckBox check;

        public MyViewHolder(View view) {
            super(view);
            service = (TextView) view.findViewById( R.id.service);
            price = (TextView) view.findViewById(R.id.price);
            descp = (TextView) view.findViewById(R.id.descp);
            check = (CheckBox) view.findViewById ( R.id.checkBox );

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onServiceArtistSelected(serviceartistListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public ArtistServiceAdapter(Context context, List<ServiceArtist> serviceartistList, ServiceArtistAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.serviceartistList = serviceartistList;
        this.serviceartistListFiltered = serviceartistList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.serhall_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        /**
        final ServiceArtist serviceartist = serviceartistListFiltered.get(position);
        holder.service.setText(serviceartist.getTitle_ar ());
        holder.price.setText( serviceartist.getPrice()+ " " + context.getResources ().getString ( R.string.currency ));
        holder.descp.setText(serviceartist.getDetails_ar (  ));
**/
        final int pos = position;

        holder.service.setText(serviceartistListFiltered.get(position).getTitle_ar ());

        holder.price.setText(serviceartistListFiltered.get(position).getPrice()+ " " + context.getResources ().getString ( R.string.currency ));

        holder.check.setChecked(serviceartistListFiltered.get(position).isSelected());

        holder.check.setTag(serviceartistListFiltered.get(position));


        holder.check.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                ServiceArtist service = (ServiceArtist) cb.getTag();

                service.setSelected(cb.isChecked());
                serviceartistListFiltered.get(pos).setSelected(cb.isChecked());
/**
 Toast.makeText(
 v.getContext(),
 "Clicked on Checkbox: " + cb.getText() + " is "
 + cb.isChecked(), Toast.LENGTH_LONG).show();
 **/
            }
        });

    }




    @Override
    public int getItemCount() {
        return serviceartistListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    serviceartistListFiltered = serviceartistList;
                } else {
                    List<ServiceArtist> filteredList = new ArrayList<> ();
                    for (ServiceArtist row : serviceartistList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTitle_ar ().toLowerCase().contains(charString.toLowerCase())  ) {
                            filteredList.add(row);
                        }
                    }

                    serviceartistListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = serviceartistListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                serviceartistListFiltered = (ArrayList<ServiceArtist>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ServiceArtistAdapterListener {
        void onServiceArtistSelected(ServiceArtist serviceartist);
    }

        public List<ServiceArtist> getServiceartistListist() {
            return serviceartistListFiltered;
        }
    }



