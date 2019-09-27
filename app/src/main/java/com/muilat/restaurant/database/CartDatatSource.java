package com.muilat.restaurant.database;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface CartDatatSource {



    Flowable<List<CartItem>> getAllCart();

    Completable insertOrReplaceAll(CartItem... cartItems);

    Long updateCart(CartItem cartItem);

    Single<Integer> deleteCart(CartItem cartItem);

    Single<Integer> clearCart();
    Single<Integer> countCartItems();
    Flowable<List<CartItem>> getAllResCart(Long restaurantId);
    Single<Integer> countResCartItems(Long restaurantId);

}
