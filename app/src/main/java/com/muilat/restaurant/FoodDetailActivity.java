package com.muilat.restaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.muilat.restaurant.model.Food;

public class FoodDetailActivity extends AppCompatActivity {

    public static final String ARG_FOOD = "arg-food";

    private Food food;

    private ImageView img_food;
    private TextView tv_sale_price;
    private TextView tv_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);



        if(getIntent().hasExtra(ARG_FOOD)){
            food = getIntent().getParcelableExtra(ARG_FOOD);
        }

        initView();


    }

    private void initView() {
        img_food = findViewById(R.id.img_food);
        tv_description = findViewById(R.id.tv_description);
        tv_sale_price = findViewById(R.id.tv_sale_price);

//        img_food.setImageDrawable();
        tv_sale_price.setText(getString(R.string.sale_price, String.valueOf(food.getPrice())));
        tv_description.setText(food.getDescription());

        getSupportActionBar().setTitle(food.getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
//787524