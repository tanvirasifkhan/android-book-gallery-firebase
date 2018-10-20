package com.example.asifkhan.bookgalleryfirebase.helpers;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.asifkhan.bookgalleryfirebase.R;

public class Config {

    //show custom success toast
    public static void showToast(String message,Context context){
        LayoutInflater layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=layoutInflater.inflate(R.layout.custom_toast_layout,null);
        Toast toast=new Toast(context);
        toast.setText(message);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }

    //check internet availability
    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        return (networkInfo!=null && networkInfo.isConnected())?true:false;
    }
}
