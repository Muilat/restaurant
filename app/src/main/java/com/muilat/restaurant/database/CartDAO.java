package com.muilat.restaurant.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface CartDAO {
    @Query("SELECT * FROM cart")
    Flowable<List<CartItem>> getAllCart();

    @Query("SELECT * FROM cart WHERE restaurantId=:restaurantId")
    Flowable<List<CartItem>> getAllResCart(Long restaurantId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertOrReplaceAll(CartItem... cartItems);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long updateCart(CartItem cartItem);

    @Delete
    Single<Integer> deleteCart(CartItem cartItem);

    @Query("DELETE FROM cart")
    Single<Integer> clearCart();

    @Query("SELECT COUNT(*) FROM cart")
    Single<Integer> countCartItems();


    @Query("SELECT COUNT(*) FROM cart WHERE restaurantId=:restaurantId")
    Single<Integer> countResCartItems(Long restaurantId);


}
