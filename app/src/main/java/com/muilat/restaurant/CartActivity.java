package com.muilat.restaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.muilat.restaurant.adapter.FoodAdapter;
import com.muilat.restaurant.common.Common;
import com.muilat.restaurant.common.Dialog;
import com.muilat.restaurant.database.CartController;
import com.muilat.restaurant.database.CartDatabase;
import com.muilat.restaurant.database.CartItem;
import com.muilat.restaurant.utilities.SpacesItemDecoration;
import com.muilat.restaurant.volley.RestaurantApi;
import com.muilat.restaurant.volley.RetrofitClient;

import org.reactivestreams.Subscriber;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CartActivity extends AppCompatActivity {

    private static final String TAG = "CartActivity";

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    RestaurantApi restaurantApi;
    CartController cartController;

    FoodAdapter foodAdapter;

    RecyclerView rv_food;
    ImageView img_menu_image;

    List<CartItem> cartItemList;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        getSupportActionBar().setTitle(Common.currentMenu.getName());


        rv_food = findViewById(R.id.rv_food);
        img_menu_image = findViewById(R.id.img_food);

        init();
        initView();
    }

    private void initView() {
        foodAdapter = new FoodAdapter(this, Common.currentMenu.getFood());

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);


        rv_food.setLayoutManager(layoutManager);
        rv_food.addItemDecoration(new SpacesItemDecoration(8));
        rv_food.setAdapter(foodAdapter);

        getCartItem();


    }

    private void init(){
        dialog = new Dialog(this);
        restaurantApi = RetrofitClient.getInstance(Common.API_RESTAURANT_ENDPOINT).create(RestaurantApi.class);
        cartItemList = new ArrayList<>();
        cartController = new CartController(CartDatabase.getInstance(this).cartDAO()) ;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        compositeDisposable.clear();
        if(foodAdapter != null)
            foodAdapter.onStop();
    }


    private void getCartItem() {
        dialog.displayLoader();
        cartController.getAllResCart(Common.currentRestaurant.getId())
//        cartController.countCartItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(cartItems -> {
                    List<Long> ids = new ArrayList<>();
                    cartItemList = cartItems;
                    for (CartItem cartItem :cartItems) {
                        ids.add(cartItem.getFoodId());
                    }

                    getCartFoods(ids);

                },
                        throwable -> {
                            dialog.hideLoader();
                            Log.e(TAG, "getCartItem: ",throwable );
                        }
                );
    }

    private void getCartFoods(List<Long> ids) {
        compositeDisposable.add(restaurantApi.getFoodsByIds(ids)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(foods -> {
                            dialog.hideLoader();

                            if(foods.size() == 0){
                                Toast.makeText(CartActivity.this, getString(R.string.no_result_found), Toast.LENGTH_SHORT).show();
                                rv_food.setAdapter(null);
                            }
                            else {
                                foodAdapter = new FoodAdapter(CartActivity.this, foods);
                            }

                        },
                        throwable -> {
                            dialog.hideLoader();
                            Log.e(TAG, "getCartFoods: ", throwable );
                        }));
    }

}
