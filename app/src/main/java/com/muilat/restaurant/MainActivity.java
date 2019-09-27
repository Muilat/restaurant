package com.muilat.restaurant;

import android.content.DialogInterface;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.muilat.restaurant.adapter.RestaurantAdapter;
import com.muilat.restaurant.adapter.RestaurantBannerAdapter;
import com.muilat.restaurant.common.Common;
import com.muilat.restaurant.common.Dialog;
import com.muilat.restaurant.eventBus.RestaurantLoadEvent;
import com.muilat.restaurant.model.Restaurant;
import com.muilat.restaurant.service.PicassoImageLoadingService;
import com.muilat.restaurant.utilities.SessionManager;
import com.muilat.restaurant.volley.RestaurantApi;
import com.muilat.restaurant.volley.RetrofitClient;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ss.com.bannerslider.Slider;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    RestaurantApi restaurantApi;
    Dialog dialog;

    RecyclerView rv_restaurant;
    Slider banner_slider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        View header_view = navigationView.getHeaderView(0);

        TextView tv_username = header_view.findViewById(R.id.tv_username);
        TextView tv_phoneNumber = header_view.findViewById(R.id.tv_phoneNumber);

        tv_username.setText(Common.curentUser.getUsername());
        tv_phoneNumber.setText(Common.curentUser.getPhoneNumber());

        init();
        initView();

        loadRestaurants();
    }

    private void initView() {
        banner_slider = findViewById(R.id.slider_banner);
        rv_restaurant = findViewById(R.id.rv_restaurant);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rv_restaurant.setLayoutManager(linearLayoutManager);
        rv_restaurant.addItemDecoration(new DividerItemDecoration(this, linearLayoutManager.getOrientation()));
    }

    private void loadRestaurants() {
        dialog.displayLoader();
        restaurantApi = RetrofitClient.getInstance(Common.API_RESTAURANT_ENDPOINT).create(RestaurantApi.class);
        compositeDisposable.add(restaurantApi.getRestaurants()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(restaurants -> {
                    dialog.hideLoader();
                    EventBus.getDefault().post(new RestaurantLoadEvent(true, restaurants));
                    },
                    throwable -> {

                    }));
    }

    private void init(){
//        alertDialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();
//        alertDialog = AlertDialog(this).;
        Slider.init(new PicassoImageLoadingService());
        restaurantApi = RetrofitClient.getInstance(Common.API_RESTAURANT_ENDPOINT).create(RestaurantApi.class);
        dialog = new Dialog(this);

    }

    //register eventBus
    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    //unregister eventBus
    @Override
    public void onStop() {

        super.onStop();

        EventBus.getDefault().unregister(this);
    }

    //listen to event bus
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void processRestaurantLoadEvent(RestaurantLoadEvent event){

        Log.i(TAG, "processRestaurantLoadEvent: ");
        if(event.isSuccess()){
            displayBanner(event.getRestaurants());
            displayRestaurants(event.getRestaurants());
        }
        else {
            Log.e(TAG, event.getMessage());
        }

        dialog.hideLoader();
    }

    private void displayRestaurants(List<Restaurant> restaurants) {
        rv_restaurant.setAdapter(new RestaurantAdapter(this, restaurants));
    }

    private void displayBanner(List<Restaurant> restaurants) {
        banner_slider.setAdapter(new RestaurantBannerAdapter(restaurants));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        compositeDisposable.clear();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout) {
            AlertDialog confirmDialog = new AlertDialog.Builder(this)
                    .setTitle("Log Out")
                    .setMessage("Are you sure?")
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Common.curentUser = null;
                            SessionManager.logoutUser(MainActivity.this);

                        }
                    }).create();

            confirmDialog.show();
        }
        else if (id == R.id.nav_nearBy) {

        } else if (id == R.id.nav_order_history) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
