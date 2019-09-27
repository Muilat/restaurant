package com.muilat.restaurant.eventBus;

import com.muilat.restaurant.model.Restaurant;

import java.util.List;

public class RestaurantLoadEvent {
    boolean success;
    String message;
    List<Restaurant> restaurants;

    public RestaurantLoadEvent(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public RestaurantLoadEvent(boolean b, List<Restaurant> restaurants) {
        this.success = b;
        this.restaurants = restaurants;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Restaurant> getRestaurants() {
        return restaurants;
    }

    public void setRestaurants(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }
}
