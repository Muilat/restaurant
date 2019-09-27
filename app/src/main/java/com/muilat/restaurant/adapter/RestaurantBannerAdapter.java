package com.muilat.restaurant.adapter;

import android.util.Log;

import com.muilat.restaurant.model.Restaurant;

import java.util.List;

import ss.com.bannerslider.adapters.SliderAdapter;
import ss.com.bannerslider.viewholder.ImageSlideViewHolder;

public class RestaurantBannerAdapter extends SliderAdapter {
    private static final String TAG = "RestaurantBannerAdapter";

    List<Restaurant> restaurants;

    public RestaurantBannerAdapter(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    @Override
    public int getItemCount() {
        return restaurants.size();
    }

    @Override
    public void onBindImageSlide(int position, ImageSlideViewHolder imageSlideViewHolder) {
        imageSlideViewHolder.bindImageSlide(restaurants.get(position).getImage());
        Log.i(TAG, "onBindImageSlide: "+restaurants.get(position).getImage());
    }
}
