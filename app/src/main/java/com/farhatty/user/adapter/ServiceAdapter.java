package com.farhatty.user.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.farhatty.user.R;
import com.farhatty.user.activity.ArtistActivity;
import com.farhatty.user.activity.ChefActivity;
import com.farhatty.user.activity.CoiffureActivity;
import com.farhatty.user.activity.HotelActivity;
import com.farhatty.user.activity.MenSalonsActivity;
import com.farhatty.user.activity.PhotographerActivity;
import com.farhatty.user.activity.WeddingHallsActivity;
import com.farhatty.user.activity.WeedingCarsActivity;
import com.farhatty.user.model.service;

import java.util.List;

/**
 * Created by AhmedKamal on 8/23/2017.
 */
public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.MyViewHolder> {

    private Context mContext;
    private List<service> serviceList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, count;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
            int position = getAdapterPosition();
        }
    }


    public ServiceAdapter(Context mContext, List<service> serviceList) {
        this.mContext = mContext;
        this.serviceList = serviceList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.service_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final service service = serviceList.get(position);
        holder.title.setText(service.getName());
        // loading album cover using Glide library
        Glide.with(mContext).load(service.getThumbnail()).into(holder.thumbnail);


        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(view.getContext(), String.valueOf(position),Toast.LENGTH_SHORT).show();

                 switch (position) {

                     case 0:
                         Intent i = new Intent(view.getContext(), WeddingHallsActivity.class);
                         view.getContext().startActivity(i);
                         break;


                     case 1:
                         Intent a = new Intent(view.getContext(), ArtistActivity.class);
                         view.getContext().startActivity(a);
                         break;


                     case 2:
                         Intent h = new Intent(view.getContext(), HotelActivity.class);
                         view.getContext().startActivity(h);
                         break;

                     case 3:
                         Intent c = new Intent(view.getContext(), CoiffureActivity.class);
                         view.getContext().startActivity(c);
                         break;


                     case 4:
                         Intent m = new Intent(view.getContext(), MenSalonsActivity.class);
                         view.getContext().startActivity(m);
                         break;


                     case 5:
                         Intent w = new Intent(view.getContext(), WeedingCarsActivity.class);
                         view.getContext().startActivity(w);
                         break;


                     case 6:
                         Intent p = new Intent(view.getContext(), PhotographerActivity.class);
                         view.getContext().startActivity(p);
                         break;


                     case 7:
                         Intent ch = new Intent(view.getContext(), ChefActivity.class);
                         view.getContext().startActivity(ch);
                         break;
                 }
            }
        });




    }
    @Override
    public int getItemCount () {
        return serviceList.size();
    }
}
