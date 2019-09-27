package com.muilat.restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.muilat.restaurant.common.Common;
import com.muilat.restaurant.model.User;
import com.muilat.restaurant.volley.RestaurantApi;
import com.muilat.restaurant.volley.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class RegistrationActivity extends AppCompatActivity {

    private static final String TAG = "RegistrationActivity";

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    RestaurantApi restaurantApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Button btn_register = findViewById(R.id.btn_register);
        final EditText edt_username = findViewById(R.id.edt_username);
        final EditText edt_password = findViewById(R.id.edt_password);
        final EditText edt_email = findViewById(R.id.edt_email);
        final EditText edt_address = findViewById(R.id.edt_address);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = new User();



                boolean cancel = false;
                View focusView = null;

                String email = edt_email.getText().toString();
                String username = edt_username.getText().toString();
                String password = edt_password.getText().toString();
                String address = edt_address.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    edt_email.setError(getString(R.string.error_field_required));
                    focusView = edt_email;
                    cancel = true;
                }else if(TextUtils.isEmpty(username)){
                    edt_username.setError(getString(R.string.error_field_required));
                    focusView = edt_username;
                    cancel = true;
                }else if (!email.contains("@")) {
                    edt_email.setError(getString(R.string.invalid_email));
                    focusView = edt_email;
                    cancel = true;
                }else if (TextUtils.isEmpty(password)) {
                    edt_password.setError(getString(R.string.error_field_required));
                    focusView = edt_password;
                    cancel = true;
                }
                else if (TextUtils.isEmpty(address)) {
                    edt_address.setError(getString(R.string.error_field_required));
                    focusView = edt_address;
                    cancel = true;
                }

                if (cancel) {//indicating an error
                    focusView.requestFocus();
                } else {
                    user.setEmail(edt_email.getText().toString());
                    user.setPassword(edt_password.getText().toString());
                    user.setUsername(edt_username.getText().toString());
                    user.setAddress(edt_address.getText().toString());

                    restaurantApi = RetrofitClient.getInstance(Common.API_RESTAURANT_ENDPOINT).create(RestaurantApi.class);
                    compositeDisposable.add(restaurantApi.addUser(user)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Consumer<User>() {
                                @Override
                                public void accept(User user) throws Exception {
                                    Log.e(TAG, "accept: "+user.getEmail() );
                                }

                            }));
                }



            }
        });
    }

    private void init(){
//        alertDialog = new SpotsDialog.Builder().setContext(this).setCancelable(false).build();
//        alertDialog = AlertDialog(this).;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        compositeDisposable.clear();
    }


}
