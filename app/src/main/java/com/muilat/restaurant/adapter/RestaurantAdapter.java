package com.muilat.restaurant.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.muilat.restaurant.MenuActivity;
import com.muilat.restaurant.R;
import com.muilat.restaurant.common.Common;
import com.muilat.restaurant.interfaces.OnRecyclerViewClickListener;
import com.muilat.restaurant.model.Restaurant;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {
    Context context;
    List<Restaurant> restaurants;

    public RestaurantAdapter(Context context, List<Restaurant> restaurants) {
        this.context = context;
        this.restaurants = restaurants;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.restaurant_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Restaurant restaurant = restaurants.get(position);
        holder.tv_res_address.setText(restaurant.getAddress());
        holder.tv_res_name.setText(restaurant.getName());
//        Picasso.get().load(restaurant.getImage()).into(holder.img_res_image);
        Picasso.get().load(R.drawable.food__).into(holder.img_res_image);

        holder.setListener(new OnRecyclerViewClickListener() {
            @Override
            public void onClick(View view, int pos) {
                Intent intent = new Intent(context, MenuActivity.class);
//                intent.putExtra(MenuActivity.ARG_RESTAURANT, restaurant);
                Common.currentRestaurant = restaurant;
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_res_name, tv_res_address;
        ImageView img_res_image;

        OnRecyclerViewClickListener mListener;


        public void setListener(OnRecyclerViewClickListener mListener) {
            this.mListener = mListener;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_res_name = itemView.findViewById(R.id.tv_res_name);
            tv_res_address = itemView.findViewById(R.id.tv_res_address);
            img_res_image = itemView.findViewById(R.id.img_res_image);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            mListener.onClick(view, getAdapterPosition());
        }
    }
}
