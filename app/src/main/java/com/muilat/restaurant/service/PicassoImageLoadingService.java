package com.muilat.restaurant.service;

import android.content.Context;
import android.widget.ImageView;

import com.muilat.restaurant.R;
import com.squareup.picasso.Picasso;

import ss.com.bannerslider.ImageLoadingService;

public class PicassoImageLoadingService implements ImageLoadingService {
    Context context;

//    public PicassoImageLoadingService(Context context) {
//        this.context = context;
//    }

    @Override
    public void loadImage(String url, ImageView imageView) {
        Picasso.get().load(R.drawable.food).into(imageView);
    }

    @Override
    public void loadImage(int resource, ImageView imageView) {
        Picasso.get().load(resource).into(imageView);

    }

    @Override
    public void loadImage(String url, int placeHolder, int errorDrawable, ImageView imageView) {
        Picasso.get().load(url).error(errorDrawable).placeholder(placeHolder).into(imageView);

    }
}
