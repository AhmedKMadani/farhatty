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
import com.farhatty.user.model.ServiceChef;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2/13/2018.
 */

public class ChefServiceAdapter extends RecyclerView.Adapter<ChefServiceAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<ServiceChef> servicechefList;
    private List<ServiceChef> servicechefListFiltered;
    private ServiceChefAdapterListener listener;

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
                    listener.onServiceChefelected(servicechefListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public ChefServiceAdapter(Context context, List<ServiceChef> servicechefList, ServiceChefAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.servicechefList = servicechefList;
        this.servicechefListFiltered = servicechefList;
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
        final ServiceChef servicechef = servicechefListFiltered.get(position);
        holder.service.setText(servicechef.getTitle_ar ());
        holder.price.setText( servicechef.getPrice()+ " " + context.getResources ().getString ( R.string.currency ));
        holder.descp.setText(servicechef.getDetails_ar (  ));
**/

        final int pos = position;

        holder.service.setText(servicechefListFiltered.get(position).getTitle_ar ());

        holder.price.setText(servicechefListFiltered.get(position).getPrice()+ " " + context.getResources ().getString ( R.string.currency ));

        holder.check.setChecked(servicechefListFiltered.get(position).isSelected());

        holder.check.setTag(servicechefListFiltered.get(position));


        holder.check.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                ServiceChef service = (ServiceChef) cb.getTag();

                service.setSelected(cb.isChecked());
                servicechefListFiltered.get(pos).setSelected(cb.isChecked());
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
        return servicechefListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    servicechefListFiltered = servicechefList;
                } else {
                    List<ServiceChef> filteredList = new ArrayList<> ();
                    for (ServiceChef row : servicechefList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTitle_ar ().toLowerCase().contains(charString.toLowerCase())  ) {
                            filteredList.add(row);
                        }
                    }

                    servicechefListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = servicechefListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                servicechefListFiltered = (ArrayList<ServiceChef>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ServiceChefAdapterListener {
        void onServiceChefelected(ServiceChef serviceChef);
    }

    public List<ServiceChef> getServicechefist() {
        return servicechefListFiltered;
    }
}




