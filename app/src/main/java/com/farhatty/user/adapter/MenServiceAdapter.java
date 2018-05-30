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
import com.farhatty.user.model.ServiceMen;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2/13/2018.
 */

public class MenServiceAdapter extends RecyclerView.Adapter<MenServiceAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<ServiceMen> servicemenList;
    private List<ServiceMen> servicemenListFiltered;
    private ServiceMenAdapterListener listener;

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
                    listener.onServiceMenelected(servicemenListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public MenServiceAdapter(Context context, List<ServiceMen> servicemenList, ServiceMenAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.servicemenList = servicemenList;
        this.servicemenListFiltered = servicemenList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.serhall_row_item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final ServiceMen servicemen = servicemenListFiltered.get(position);
        holder.service.setText(servicemen.getTitle_ar ());
        holder.price.setText( servicemen.getPrice()+ " " + context.getResources ().getString ( R.string.currency ));
        holder.descp.setText(servicemen.getDetails_ar (  ));

        final int pos = position;

        holder.service.setText(servicemenListFiltered.get(position).getTitle_ar ());

        holder.price.setText(servicemenListFiltered.get(position).getPrice()+ " " + context.getResources ().getString ( R.string.currency ));

        holder.check.setChecked(servicemenListFiltered.get(position).isSelected());

        holder.check.setTag(servicemenListFiltered.get(position));


        holder.check.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                ServiceMen service = (ServiceMen) cb.getTag();

                service.setSelected(cb.isChecked());
                servicemenListFiltered.get(pos).setSelected(cb.isChecked());
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
        return servicemenListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    servicemenListFiltered = servicemenList;
                } else {
                    List<ServiceMen> filteredList = new ArrayList<> ();
                    for (ServiceMen row : servicemenList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTitle_ar ().toLowerCase().contains(charString.toLowerCase())  ) {
                            filteredList.add(row);
                        }
                    }

                    servicemenListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = servicemenListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                servicemenListFiltered = (ArrayList<ServiceMen>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ServiceMenAdapterListener {
        void onServiceMenelected(ServiceMen servicemen);
    }

    public List<ServiceMen> getServicemenist() {
        return servicemenListFiltered;
    }
}



