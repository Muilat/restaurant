package com.muilat.restaurant;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.muilat.restaurant.common.Common;
import com.muilat.restaurant.model.User;
import com.muilat.restaurant.utilities.SessionManager;
import com.muilat.restaurant.volley.RestaurantApi;
import com.muilat.restaurant.volley.RetrofitClient;

//import dmax.dialog.SpotsDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";

    Button  btn_login;
    TextView btn_register;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    RestaurantApi restaurantApi;
    AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);

        init();

        final EditText edt_username = findViewById(R.id.edt_username);
        final EditText edt_password = findViewById(R.id.edt_password);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User();



                boolean cancel = false;
                View focusView = null;

                String password = edt_password.getText().toString();
                String username = edt_username.getText().toString();

                if(TextUtils.isEmpty(username)){
                    edt_username.setError(getString(R.string.error_field_required));
                    focusView = edt_username;
                    cancel = true;
                }else if (TextUtils.isEmpty(password)) {
                    edt_password.setError(getString(R.string.error_field_required));
                    focusView = edt_password;
                    cancel = true;
                }

                if (cancel) {//indicating an error
                    focusView.requestFocus();
                } else {


                    restaurantApi = RetrofitClient.getInstance(Common.API_RESTAURANT_ENDPOINT).create(RestaurantApi.class);
                    compositeDisposable.add(restaurantApi.login(username, password)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<User>() {
                                @Override
                                public void accept(User user) throws Exception {
                                    Common.curentUser = user;
                                    Log.e(TAG, "accept: "+Common.curentUser.getEmail() );

                                    SessionManager.setData(LoginActivity.this, SessionManager.KEY_USERID, user.getId());
                                    SessionManager.setData(LoginActivity.this, SessionManager.IS_LOGGED_IN, true);
                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            }));
                }



            }
        });

    }

    private void init(){
//        alertDialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();
//        alertDialog = AlertDialog(this).;
        restaurantApi = RetrofitClient.getInstance(Common.API_RESTAURANT_ENDPOINT).create(RestaurantApi.class);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        compositeDisposable.clear();
    }

    public void register(View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
        //finish();
    }
}
