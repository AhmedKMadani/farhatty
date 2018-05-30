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
import com.farhatty.user.model.ServiceCoiffure;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2/13/2018.
 */

public class CoiffureServiceAdapter extends RecyclerView.Adapter<CoiffureServiceAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<ServiceCoiffure> servicecoiffureList;
    private List<ServiceCoiffure> servicecoiffureListFiltered;
    private ServiceCoiffureAdapterListener listener;

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
                    listener.onServiceCoiffureelected(servicecoiffureListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public CoiffureServiceAdapter(Context context, List<ServiceCoiffure> servicecoiffureList, ServiceCoiffureAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.servicecoiffureList = servicecoiffureList;
        this.servicecoiffureListFiltered = servicecoiffureList;
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
        final ServiceCoiffure servicecoiffure = servicecoiffureListFiltered.get(position);
        holder.service.setText(servicecoiffure.getTitle_ar ());
        holder.price.setText( servicecoiffure.getPrice()+ " " + context.getResources ().getString ( R.string.currency ));
        holder.descp.setText(servicecoiffure.getDetails_ar (  ));
**/
        final int pos = position;

        holder.service.setText(servicecoiffureListFiltered.get(position).getTitle_ar ());

        holder.price.setText(servicecoiffureListFiltered.get(position).getPrice()+ " " + context.getResources ().getString ( R.string.currency ));

        holder.check.setChecked(servicecoiffureListFiltered.get(position).isSelected());

        holder.check.setTag(servicecoiffureListFiltered.get(position));


        holder.check.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                ServiceCoiffure service = (ServiceCoiffure) cb.getTag();

                service.setSelected(cb.isChecked());
                servicecoiffureListFiltered.get(pos).setSelected(cb.isChecked());
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
        return servicecoiffureListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    servicecoiffureListFiltered = servicecoiffureList;
                } else {
                    List<ServiceCoiffure> filteredList = new ArrayList<> ();
                    for (ServiceCoiffure row : servicecoiffureList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTitle_ar ().toLowerCase().contains(charString.toLowerCase())  ) {
                            filteredList.add(row);
                        }
                    }

                    servicecoiffureListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = servicecoiffureListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                servicecoiffureListFiltered = (ArrayList<ServiceCoiffure>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ServiceCoiffureAdapterListener {
        void onServiceCoiffureelected(ServiceCoiffure servicecoiffure);
    }

    public List<ServiceCoiffure> getServicecoiffureist() {
        return servicecoiffureListFiltered;
    }
}



