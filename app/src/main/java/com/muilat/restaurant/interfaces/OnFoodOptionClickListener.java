package com.muilat.restaurant.interfaces;

import android.view.View;

import com.muilat.restaurant.enums.FoodOptions;

public interface OnFoodOptionClickListener {
    void onClick(View view, int pos, FoodOptions option);

}
