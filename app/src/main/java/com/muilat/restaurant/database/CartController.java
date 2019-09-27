package com.muilat.restaurant.database;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class CartController implements CartDatatSource{

    private CartDAO cartDAO ;

    public CartController(CartDAO cartDAO) {
        this.cartDAO = cartDAO;
    }


    @Override
    public Flowable<List<CartItem>> getAllCart() {
        return cartDAO.getAllCart();
    }

    @Override
    public Completable insertOrReplaceAll(CartItem... cartItems) {
        return cartDAO.insertOrReplaceAll();
    }

    @Override
    public Long updateCart(CartItem cartItem) {
        return cartDAO.updateCart(cartItem);
    }

    @Override
    public Single<Integer> deleteCart(CartItem cartItem) {
        return cartDAO.deleteCart(cartItem);
    }

    @Override
    public Single<Integer> clearCart() {
        return cartDAO.clearCart();
    }

    @Override
    public Single<Integer> countCartItems() {
        return cartDAO.countCartItems();
    }

    @Override
    public Flowable<List<CartItem>> getAllResCart(Long restaurantId) {
        return cartDAO.getAllResCart(restaurantId);
    }

    @Override
    public Single<Integer> countResCartItems(Long restaurantId) {
        return cartDAO.countResCartItems(restaurantId);
    }


//        Flowable<List<CartItem>> getAllCart();
//
//    Completable insertOrReplaceAll(CartItem... cartItems);
//
//    Single<Integer> updateCart(CartItem cartItem);
//
//    Single<Integer> deleteCart(CartItem cartItem);
//
//    Single<Integer> clearCart();
}
