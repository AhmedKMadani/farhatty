package com.farhatty.user.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.farhatty.user.R;
import com.farhatty.user.model.OrdersList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2/13/2018.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder>
        implements Filterable {
    private Context context;
    private List<OrdersList> orderListorder;
    private List<OrdersList> orderListorderFiltered;
    private OrderAdapterListener listener;




    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView service, price,descp;


        public MyViewHolder(View view) {
            super(view);
            service = (TextView) view.findViewById( R.id.service);
            price = (TextView) view.findViewById(R.id.price);


            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                   // listener.onOrderSelected(orderListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }



    public OrderAdapter(Context context, List <OrdersList> orderListorder) {
        this.context = context;
        this.listener = listener;
        this.orderListorder = orderListorder;
        this.orderListorderFiltered = orderListorder;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

         final OrdersList ordersList = orderListorderFiltered.get(position);
         holder.service.setText(ordersList.getTitle ());
         holder.price.setText( ordersList.getPrice()+ " " + context.getResources ().getString ( R.string.currency ));


            }




    @Override
    public int getItemCount() {
        return orderListorderFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    orderListorderFiltered = orderListorder;
                } else {
                    List<OrdersList> filteredList = new ArrayList<> ();
                    for (OrdersList row : orderListorder) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getTitle ().toLowerCase().contains(charString.toLowerCase())  ) {
                            filteredList.add(row);
                        }
                    }

                    orderListorderFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = orderListorderFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                orderListorderFiltered = (ArrayList<OrdersList>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface OrderAdapterListener {
        void onOrderListSelected(OrdersList ordersListorder);
    }

    public List<OrdersList> getOrderListist() {
        return orderListorderFiltered;
    }
}

