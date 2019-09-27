package com.muilat.restaurant.volley;

import com.muilat.restaurant.model.Food;
import com.muilat.restaurant.model.Menu;
import com.muilat.restaurant.model.Restaurant;
import com.muilat.restaurant.model.User;


import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestaurantApi {
    @GET("users/{id}")
    Observable<User> getUser(@Path("id") Long id);

    @POST("users/add")
    Observable<User> addUser(@Body User user);

    @GET("restaurants")
    Observable<List<Restaurant>> getRestaurants();

    @GET("menus?restaurantId={id}")
    Observable<List<Menu>> getMenu(@Query("id") Long resId );

    @GET("foods?ids={ids}")
    Observable<List<Food>> getFoodsByIds(@Query("ids") List<Long> ids );

    @GET("foods/search/{searchQuery}")
    Observable<List<Food>> searchFood(@Path("searchQuery") String searchQuery);


    @GET("users/login/{username}/{password}")
    Observable<User> login(@Path("username") String username, @Path("password") String password);

}
