package com.muilat.restaurant.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart")
public class CartItem {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "foodId")
    private Long foodId;

    @ColumnInfo(name = "restaurantId")
    private Long restaurantId;

    @ColumnInfo(name = "addOn")
    private String addOn;

    @ColumnInfo(name = "size")
    private String size;

    @ColumnInfo(name = "quantity")
    private int quantity;

    public CartItem() {
    }



    @NonNull
    public Long getFoodId() {
        return foodId;
    }

    public void setFoodId(@NonNull Long foodId) {
        this.foodId = foodId;
    }

    public String getAddOn() {
        return addOn;
    }

    public void setAddOn(String addOn) {
        this.addOn = addOn;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Long getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Long restaurantId) {
        this.restaurantId = restaurantId;
    }
}
