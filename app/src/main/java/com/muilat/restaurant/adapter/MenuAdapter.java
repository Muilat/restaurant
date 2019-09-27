package com.muilat.restaurant.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.muilat.restaurant.FoodListActivity;
import com.muilat.restaurant.R;
import com.muilat.restaurant.common.Common;
import com.muilat.restaurant.interfaces.OnRecyclerViewClickListener;
import com.muilat.restaurant.model.Menu;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    Context context;
    List<Menu> menus;

    public MenuAdapter(Context context, List<Menu> menus) {
        this.context = context;
        this.menus = menus;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.menu_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Menu menu = menus.get(position);
        holder.tv_category.setText(menu.getName());
//        Picasso.get().load(menu.getImage()).into(holder.img_category);
        Picasso.get().load(R.drawable.food__).into(holder.img_category);

        holder.setListener(new OnRecyclerViewClickListener() {
            @Override
            public void onClick(View view, int pos) {
                Intent intent = new Intent(context, FoodListActivity.class);
//                intent.putExtra(MenuActivity.ARG_RESTAURANT, restaurant);
                Common.currentMenu = menu;
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return menus.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tv_category;
        ImageView img_category;

        OnRecyclerViewClickListener mListener;

        void setListener(OnRecyclerViewClickListener mListener) {
            this.mListener = mListener;
        }

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_category = itemView.findViewById(R.id.tv_menu);
            img_category = itemView.findViewById(R.id.img_menu);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            mListener.onClick(view, getAdapterPosition());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(menus.size() == 1){
            return Common.FULL_WIDTH_TYPE;
        }
        else{
            if(menus.size() % 2 == 0)
                return Common.DEFAULT_TYPE;
            else
                return (position > 1 && position == menus.size()-1) ? Common.FULL_WIDTH_TYPE: Common.DEFAULT_TYPE;
        }

    }
}
