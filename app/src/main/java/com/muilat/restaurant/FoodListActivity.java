package com.muilat.restaurant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.muilat.restaurant.adapter.FoodAdapter;
import com.muilat.restaurant.common.Common;
import com.muilat.restaurant.common.Dialog;
import com.muilat.restaurant.utilities.SpacesItemDecoration;
import com.muilat.restaurant.volley.RestaurantApi;
import com.muilat.restaurant.volley.RetrofitClient;
import com.nex3z.notificationbadge.NotificationBadge;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class FoodListActivity extends AppCompatActivity {

    private static final String TAG = "FoodListActivity";

    public static final String ARG_RESTAURANT = "arg-restaurant";

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    RestaurantApi restaurantApi;

    FoodAdapter foodAdapter, searchAdapter;

    RecyclerView rv_food;
    ImageView img_menu_image;
    NotificationBadge badge;
    FloatingActionButton fab_cart;

    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        getSupportActionBar().setTitle(Common.currentMenu.getName());


        rv_food = findViewById(R.id.rv_food);
        img_menu_image = findViewById(R.id.img_food);
        badge = findViewById(R.id.badge);
        fab_cart = findViewById(R.id.fab_cart);

        init();
        initView();
    }

    private void initView() {
        foodAdapter = new FoodAdapter(this, Common.currentMenu.getFood());

        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                //display one or two item depending on count/ odd/even
                if(foodAdapter != null){
                    switch (foodAdapter.getItemViewType(position)){
                        case Common.DEFAULT_TYPE: return 1;
                        case Common.FULL_WIDTH_TYPE: return 2;
                        default: return -1;
                    }
                }
                else return -1;
            }
        });

        rv_food.setLayoutManager(layoutManager);
        rv_food.addItemDecoration(new SpacesItemDecoration(8));
        rv_food.setAdapter(foodAdapter);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
//        rv_food.setLayoutManager(linearLayoutManager);

        fab_cart.setOnClickListener(view -> {
            Intent intent = new Intent(FoodListActivity.this, CartActivity.class);
            startActivity(intent);
        });

    }

    private void init(){
        dialog = new Dialog(this);
        restaurantApi = RetrofitClient.getInstance(Common.API_RESTAURANT_ENDPOINT).create(RestaurantApi.class);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);

        MenuItem menuItem = menu.findItem(R.id.menu_search);
        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView)menuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                starSearch(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                rv_food.setAdapter(foodAdapter);
                return true;
            }
        });

        return true;
    }

    private void starSearch(String query) {
        dialog.displayLoader();
        compositeDisposable.add(restaurantApi.searchFood(query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(foods -> {
                            dialog.hideLoader();

                            if(foods.size() == 0){
                                Toast.makeText(FoodListActivity.this, getString(R.string.no_result_found), Toast.LENGTH_SHORT).show();
                                rv_food.setAdapter(null);
                            }
                            else {
                                searchAdapter = new FoodAdapter(FoodListActivity.this, foods);
                                rv_food.setAdapter(searchAdapter);
                            }

                        },
                        throwable -> {
                            dialog.hideLoader();
                            Log.e(TAG, "starSearch: "+throwable.getMessage() );
                        }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        compositeDisposable.clear();
        if(foodAdapter != null)
            foodAdapter.onStop();
    }
}
