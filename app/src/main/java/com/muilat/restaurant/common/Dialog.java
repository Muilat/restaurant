package com.muilat.restaurant.common;

import android.app.ProgressDialog;
import android.content.Context;

import com.muilat.restaurant.R;

public class Dialog {
    private static ProgressDialog pDialog;

    public Dialog(Context context){
        pDialog = new ProgressDialog(context);
        pDialog.setMessage(context.getString(R.string.loading));
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);}

    public void displayLoader(){

        pDialog.show();
    }
    public void hideLoader(){
        pDialog.dismiss();
    }
}
