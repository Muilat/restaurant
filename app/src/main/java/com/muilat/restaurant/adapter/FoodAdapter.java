package com.muilat.restaurant.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.muilat.restaurant.FoodDetailActivity;
import com.muilat.restaurant.FoodListActivity;
import com.muilat.restaurant.R;
import com.muilat.restaurant.common.Common;
import com.muilat.restaurant.common.Dialog;
import com.muilat.restaurant.database.CartController;
import com.muilat.restaurant.database.CartDAO;
import com.muilat.restaurant.database.CartDatabase;
import com.muilat.restaurant.database.CartDatatSource;
import com.muilat.restaurant.database.CartItem;
import com.muilat.restaurant.enums.FoodOptions;
import com.muilat.restaurant.eventBus.RestaurantLoadEvent;
import com.muilat.restaurant.interfaces.OnFoodOptionClickListener;
import com.muilat.restaurant.interfaces.OnRecyclerViewClickListener;
import com.muilat.restaurant.model.Food;
import com.muilat.restaurant.model.Restaurant;
import com.squareup.picasso.Picasso;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {
    Context context;
    List<Food> foods;
    CompositeDisposable compositeDisposable;

    CartController cartController;

    public void onStop(){
        compositeDisposable.clear();
    }

    public FoodAdapter(Context context, List<Food> foods) {
        this.context = context;
        this.foods = foods;
        compositeDisposable = new CompositeDisposable();
        cartController = new CartController(CartDatabase.getInstance(context).cartDAO()) ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if(context instanceof FoodListActivity)
            view = LayoutInflater.from(context)
                .inflate(R.layout.food_item_box, parent, false);
        else
            view = LayoutInflater.from(context)
                    .inflate(R.layout.food_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Food food = foods.get(position);
        holder.tv_name.setText(food.getName());
        holder.tv_description.setText(food.getDescription());
        holder.tv_price.setText(food.getPrice()+"");
//        Picasso.get().load(food.getImage()).into(holder.img_food);
        Picasso.get().load(R.drawable.food__).into(holder.img_food);

        holder.setListener(new OnFoodOptionClickListener() {

            @Override
            public void onClick(View view, int pos, FoodOptions option) {
                if(option == FoodOptions.CART){

                    CartItem cartItem = new CartItem();
                    cartItem.setFoodId(food.getId());
                    cartItem.setRestaurantId(Common.currentRestaurant.getId());
                    cartItem.setQuantity(1);
                    cartItem.setAddOn("qe");
                    cartItem.setSize("qe");


                    compositeDisposable.add(
                            cartController.insertOrReplaceAll(cartItem)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                            ()-> Toast.makeText(context, context.getResources().getString(R.string.food_added_to_cart), Toast.LENGTH_LONG).show(),
                                            throwable -> Toast.makeText(context, context.getResources().getString(R.string.something_went_wrong), Toast.LENGTH_LONG).show()
                                    ));

                }

                else if(option == FoodOptions.FAVOURTIE)
                    Toast.makeText(context, option+"   ", Toast.LENGTH_LONG).show();
                else {
                    Intent intent = new Intent(context, FoodDetailActivity.class);
                    intent.putExtra(FoodDetailActivity.ARG_FOOD, food);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_name;
        TextView tv_description;
        TextView tv_food_res;
        TextView tv_price;
        ImageView img_food, img_info, img_favourite, img_cart;

        OnFoodOptionClickListener mListener;


        void setListener(OnFoodOptionClickListener mListener) {
            this.mListener = mListener;
        }

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_food);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_description = itemView.findViewById(R.id.tv_description);
            tv_food_res = itemView.findViewById(R.id.tv_food_res);
            img_food = itemView.findViewById(R.id.img_food);
            img_info = itemView.findViewById(R.id.img_info);
            img_favourite = itemView.findViewById(R.id.img_favourite);
            img_cart = itemView.findViewById(R.id.img_cart);

            itemView.setOnClickListener(this);

            img_info.setOnClickListener(this);
            img_favourite.setOnClickListener(this);
            img_cart.setOnClickListener(this);




        }

        @Override
        public void onClick(View view) {
            FoodOptions option;

            if (view.getId() == R.id.img_cart)
                option = FoodOptions.CART;
            else if (view.getId() == R.id.img_favourite)
                option = FoodOptions.FAVOURTIE;
            else
                option = FoodOptions.INFO;

            mListener.onClick(view, getAdapterPosition(), option);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(foods.size() == 1){
            return Common.FULL_WIDTH_TYPE;
        }
        else{
            if(foods.size() % 2 == 0)
                return Common.DEFAULT_TYPE;
            else
                return (position > 1 && position == foods.size()-1) ? Common.FULL_WIDTH_TYPE: Common.DEFAULT_TYPE;
        }

    }
}
