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
import com.farhatty.user.model.ServiceHalls;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2/13/2018.
 */

public class HallsServiceAdapter extends RecyclerView.Adapter<HallsServiceAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<ServiceHalls> servicehallsList;
    private List<ServiceHalls> servicehallsListFiltered;
    private ServiceHallAdapterListener listener;




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
                    listener.onServiceHallSelected(servicehallsListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }



    public HallsServiceAdapter(Context context, List<ServiceHalls> servicehallsList, ServiceHallAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.servicehallsList = servicehallsList;
        this.servicehallsListFiltered = servicehallsList;
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
        final ServiceHalls servicehalls = servicehallsListFiltered.get(position);
        holder.service.setText(servicehalls.getTitle_ar ());
        holder.price.setText( servicehalls.getPrice()+ " " + context.getResources ().getString ( R.string.currency ));
        holder.descp.setText(servicehalls.getDetails_ar (  ));
**/
        final int pos = position;

        holder.service.setText(servicehallsListFiltered.get(position).getTitle_ar ());

        holder.price.setText(servicehallsListFiltered.get(position).getPrice()+ " " + context.getResources ().getString ( R.string.currency ));

        holder.check.setChecked(servicehallsListFiltered.get(position).isSelected());

        holder.check.setTag(servicehallsListFiltered.get(position));


        holder.check.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                ServiceHalls service = (ServiceHalls) cb.getTag();

                service.setSelected(cb.isChecked());
                servicehallsListFiltered.get(pos).setSelected(cb.isChecked());
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
        return servicehallsListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    servicehallsListFiltered = servicehallsList;
                } else {
                    List<ServiceHalls> filteredList = new ArrayList<> ();
                    for (ServiceHalls row : servicehallsList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTitle_ar ().toLowerCase().contains(charString.toLowerCase())  ) {
                            filteredList.add(row);
                        }
                    }

                    servicehallsListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = servicehallsListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                servicehallsListFiltered = (ArrayList<ServiceHalls>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ServiceHallAdapterListener {
        void onServiceHallSelected(ServiceHalls servicehalls);
    }

    public List<ServiceHalls> getServicehallsist() {
        return servicehallsListFiltered;
    }
}

