package com.example.calendarapp.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.example.calendarapp.Activities.MainActivity;

public class NetworkUtil {
    public static boolean isConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Log.i("NETWORK","Checking connections");
        boolean connection = false;
        if(cm != null){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());

                connection  = nc !=null &&
                        (nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                                ||nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));

            }
            else{
                android.net.NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                connection = activeNetwork != null && activeNetwork.isConnected();
            }
        }
        if(!connection) {
            Log.e("NETWORK", "No internet connection");
        }
        else{
            Log.i("NETWORK","Connected to internet ");
        }
        return connection;
    }
    public static void showInternetDialog(Context context) {
        Log.e("NETWORK", "No internet connection");
        new Handler(Looper.getMainLooper()).post(() -> {
            try {
                if (!(context instanceof Activity) || ((Activity) context).isFinishing()) {
                    // If context is not an activity or the activity is finishing, show a toast instead
                    Toast.makeText(context, "No Internet Connection. Please check your network settings.", Toast.LENGTH_LONG).show();
                    return;
                }

                new AlertDialog.Builder(context)
                        .setTitle("No Internet Connection")
                        .setMessage("Please turn on WIFI or mobile data to continue.")
                        .setCancelable(false)
                        .setPositiveButton("Settings", (dialog, which) -> {
                            context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        })
                        .setNegativeButton("Try again", (dialog, which) -> {
                            dialog.dismiss();
                            checkInternetConnection(context);
                        })
                        .show();
            } catch (Exception e) {
                Log.e("NETWORK", "Failed to show dialog, showing toast instead", e);
                Toast.makeText(context, "No Internet Connection. Please check your network settings.", Toast.LENGTH_LONG).show();
            }
        });
    }
    public static void checkInternetConnection(Context context){
        if(!NetworkUtil.isConnected(context)){
            NetworkUtil.showInternetDialog(context);
        }
    }
    // Monitor network state using NetworkCallback
    public static void registerNetworkCallback(Context context) {
        checkInternetConnection(context);
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback() {
                @Override
                public void onLost(Network network) {
                    showInternetDialog(context);
                }

                @Override
                public void onAvailable(Network network) {
                    // Network is back, no action needed
                    Log.i("NETWORK","Connected to internet");
                }
            });
        }
    }


    // BroadcastReceiver to listen for network changes
    public static class NetworkChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!NetworkUtil.isConnected(context)) {
                // Show alert or notification
                showInternetDialog(context);
            }
        }
    }
}
