package com.muilat.restaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.muilat.restaurant.adapter.MenuAdapter;
import com.muilat.restaurant.common.Common;
import com.muilat.restaurant.database.CartController;
import com.muilat.restaurant.database.CartDatabase;
import com.muilat.restaurant.model.Menu;
import com.muilat.restaurant.utilities.SpacesItemDecoration;
import com.muilat.restaurant.volley.RestaurantApi;
import com.muilat.restaurant.volley.RetrofitClient;
import com.nex3z.notificationbadge.NotificationBadge;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MenuActivity extends AppCompatActivity {

    private static final String TAG = "MenuActivity";

    public static final String ARG_RESTAURANT = "arg-restaurant";

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    RestaurantApi restaurantApi;
    CartController cartController;

    MenuAdapter menuAdapter;

    RecyclerView rv_menu;
    ImageView img_res_image;
    NotificationBadge badge;
    FloatingActionButton fab_cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);




        init();
        initView();
    }

    private void initView() {
        rv_menu = findViewById(R.id.rv_menu);
        img_res_image = findViewById(R.id.img_res_image);
        badge = findViewById(R.id.badge);
        fab_cart = findViewById(R.id.fab_cart);


        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        menuAdapter = new MenuAdapter(this, Common.currentRestaurant.getMenu());

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //display one or two item depending on count/ odd/even
                if(menuAdapter != null){
                    switch (menuAdapter.getItemViewType(position)){
                        case Common.DEFAULT_TYPE: return 1;
                        case Common.FULL_WIDTH_TYPE: return 2;
                        default: return -1;
                    }
                }
                else return -1;
            }
        });
        rv_menu.setLayoutManager(layoutManager);
        rv_menu.addItemDecoration(new SpacesItemDecoration(8));
        rv_menu.setAdapter(menuAdapter);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        rv_menu.setLayoutManager(linearLayoutManager);

        countCartItem();


        fab_cart.setOnClickListener(view -> {
            Intent intent = new Intent(MenuActivity.this, CartActivity.class);
            startActivity(intent);
        });

    }

    private void countCartItem() {
        cartController.countResCartItems(Common.currentRestaurant.getId())
//        cartController.countCartItems()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Integer>(){

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        badge.setText(String.valueOf(integer));
                        Log.e(TAG, "onSuccess: "+integer );
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void init(){
        restaurantApi = RetrofitClient.getInstance(Common.API_RESTAURANT_ENDPOINT).create(RestaurantApi.class);
        cartController = new CartController(CartDatabase.getInstance(this).cartDAO()) ;

    }

    @Override
    protected void onResume() {
        super.onResume();
        countCartItem();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        compositeDisposable.clear();
    }
}
