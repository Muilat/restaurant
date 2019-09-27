package com.muilat.restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.muilat.restaurant.common.Common;
import com.muilat.restaurant.model.User;
import com.muilat.restaurant.utilities.SessionManager;
import com.muilat.restaurant.volley.RestaurantApi;
import com.muilat.restaurant.volley.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

//import dmax.dialog.SpotsDialog;


public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    RestaurantApi restaurantApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        final Thread splashTimer = new Thread() {
            @Override
            public void run() {

                try{
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {

//                    Dexter.withActivity(SplashActivity.this)
//                            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
//                            .withListener(new PermissionListener() {
//                                @Override
//                                public void onPermissionGranted(PermissionGrantedResponse response) {
                                    if(SessionManager.isLoggedIn(SplashActivity.this)){
                                        //TODO::replace with access token login
                                        restaurantApi = RetrofitClient.getInstance(Common.API_RESTAURANT_ENDPOINT).create(RestaurantApi.class);
                                        compositeDisposable.add(restaurantApi.getUser(SessionManager.getSessionField(SplashActivity.this, SessionManager.KEY_USERID, (long)0))
                                                .subscribeOn(Schedulers.io())
                                                .observeOn(AndroidSchedulers.mainThread())
                                                .subscribe(
                                                        user -> {
                                                            Common.curentUser = user;
                                                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                                                            startActivity(intent);
                                                            finish();
                                                        },
                                                        throwable -> {
                                                            Toast.makeText(SplashActivity.this, getString(R.string.login_to_cont), Toast.LENGTH_LONG).show();
                                                            Log.e(TAG, "run: ", throwable );
                                                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                                                            startActivity(intent);
                                                            finish();

                                                        }
                                                ));


                                    }
                                    else {
                                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
//
//                                }
//
//                                @Override
//                                public void onPermissionDenied(PermissionDeniedResponse response) {
//                                    Toast.makeText(SplashActivity.this, getString(R.string.must_grant_permisssion), Toast.LENGTH_LONG).show();
//
//                                }
//
//                                @Override
//                                public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
//
//                                }
//                            }).check();


                }
            }
        };

        init();

        splashTimer.start();
    }

    private void init(){
        restaurantApi = RetrofitClient.getInstance(Common.API_RESTAURANT_ENDPOINT).create(RestaurantApi.class);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        compositeDisposable.clear();
    }


}
