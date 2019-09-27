package com.muilat.restaurant.common;

import com.muilat.restaurant.model.Menu;
import com.muilat.restaurant.model.Restaurant;
import com.muilat.restaurant.model.User;

public class Common {
    public static final String API_RESTAURANT_ENDPOINT = "http://10.0.2.2:9090/";
    public static final int DEFAULT_TYPE = 0;
    public static final int FULL_WIDTH_TYPE = 1;

    public static User curentUser;
    public static Restaurant currentRestaurant;
    public static Menu currentMenu;
}
