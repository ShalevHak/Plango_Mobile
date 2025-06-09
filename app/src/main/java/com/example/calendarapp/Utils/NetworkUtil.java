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
    /**
     * Checks whether the device currently has an active internet connection.
     * @param context any valid Context
     * @return true if connected, false otherwise
     */
    public static boolean isConnected(Context context){
        // Get the system ConnectivityManager to retrieve network status
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        Log.i("NETWORK","Checking connections");
        boolean connection = false;
        if(cm != null){
            // For Marshmallow (API 23) and above, use NetworkCapabilities
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                // Query the active network's capabilities
                NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());

                // Check if network is non-null and has WIFI or CELLULAR transport
                connection  = nc !=null &&
                        (nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                                ||nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));

            }
            else{
                // For older devices, use deprecated NetworkInfo API
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
    /**
     * Shows a blocking dialog (or Toast if Activity is unavailable) alerting the user to connect.
     * @param context a Context, ideally an Activity
     */
    public static void showInternetDialog(Context context) {
        Log.e("NETWORK", "No internet connection");
        // Post to the main thread to safely show UI elements
        new Handler(Looper.getMainLooper()).post(() -> {
            try {
                if (!(context instanceof Activity) || ((Activity) context).isFinishing()) {
                    // If context is not an activity or the activity is finishing, show a toast instead
                    Toast.makeText(context, "No Internet Connection. Please check your network settings.", Toast.LENGTH_LONG).show();
                    return;
                }

                // Build and display a non-cancelable AlertDialog
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
    /**
     * Registers a NetworkCallback to listen for connectivity changes at runtime.
     * @param context a valid Context
     */
    // Monitor network state using NetworkCallback
    public static void registerNetworkCallback(Context context) {
        // Immediately check current status
        checkInternetConnection(context);
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            // Listen for network changes
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

    /**
     * BroadcastReceiver to handle ACTION_CONNECTIVITY_CHANGE for pre-API 24 devices or fallback.
     */
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
