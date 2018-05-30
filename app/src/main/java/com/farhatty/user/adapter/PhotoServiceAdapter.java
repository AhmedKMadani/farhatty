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
import com.farhatty.user.model.ServicePhoto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2/13/2018.
 */

public class PhotoServiceAdapter extends RecyclerView.Adapter<PhotoServiceAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<ServicePhoto> servicephotoList;
    private List<ServicePhoto> servicephotoListFiltered;
    private ServicePhotoAdapterListener listener;

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
                    listener.onServicePhotoelected(servicephotoListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }


    public PhotoServiceAdapter(Context context, List<ServicePhoto> servicephotoList, ServicePhotoAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this.servicephotoList = servicephotoList;
        this.servicephotoListFiltered = servicephotoList;
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
        final ServicePhoto servicephoto = servicephotoListFiltered.get(position);
        holder.service.setText(servicephoto.getTitle_ar ());
        holder.price.setText( servicephoto.getPrice()+ " " + context.getResources ().getString ( R.string.currency ));
        holder.descp.setText(servicephoto.getDetails_ar (  ));

         **/
        final int pos = position;

        holder.service.setText(servicephotoListFiltered.get(position).getTitle_ar ());

        holder.price.setText(servicephotoListFiltered.get(position).getPrice()+ " " + context.getResources ().getString ( R.string.currency ));

        holder.check.setChecked(servicephotoListFiltered.get(position).isSelected());

        holder.check.setTag(servicephotoListFiltered.get(position));


        holder.check.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                ServicePhoto service = (ServicePhoto) cb.getTag();

                service.setSelected(cb.isChecked());
                servicephotoListFiltered.get(pos).setSelected(cb.isChecked());
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
        return servicephotoListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    servicephotoListFiltered = servicephotoList;
                } else {
                    List<ServicePhoto> filteredList = new ArrayList<> ();
                    for (ServicePhoto row : servicephotoList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTitle_ar ().toLowerCase().contains(charString.toLowerCase())  ) {
                            filteredList.add(row);
                        }
                    }

                    servicephotoListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = servicephotoListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                servicephotoListFiltered = (ArrayList<ServicePhoto>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface ServicePhotoAdapterListener {
        void onServicePhotoelected(ServicePhoto servicephoto);
    }

    public List<ServicePhoto> getServicephotoist() {
        return servicephotoListFiltered;
    }
}



