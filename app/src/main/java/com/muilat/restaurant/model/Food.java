package com.muilat.restaurant.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Food implements Parcelable{

    private Long id;
    private String name;
    private String  description;
    private String  image;
    private double  price;
    private boolean isSize;
    private boolean isAddOn;
    private double discount;

    public Food() {
    }

    protected Food(Parcel in) {
        if (in.readByte() == 0) {
            id = null;
        } else {
            id = in.readLong();
        }
        name = in.readString();
        description = in.readString();
        image = in.readString();
        price = in.readDouble();
        isSize = in.readByte() != 0;
        isAddOn = in.readByte() != 0;
        discount = in.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(id);
        }
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(image);
        dest.writeDouble(price);
        dest.writeByte((byte) (isSize ? 1 : 0));
        dest.writeByte((byte) (isAddOn ? 1 : 0));
        dest.writeDouble(discount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isSize() {
        return isSize;
    }

    public void setSize(boolean size) {
        isSize = size;
    }

    public boolean isAddOn() {
        return isAddOn;
    }

    public void setAddOn(boolean addOn) {
        isAddOn = addOn;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }
}
