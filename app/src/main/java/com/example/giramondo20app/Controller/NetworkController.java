package com.example.giramondo20app.Controller;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;

import com.example.giramondo20app.MainActivity;

public class NetworkController {
    MainActivity activity;

    public NetworkController(Activity activity) {
        this.activity = (MainActivity) activity;
    }

    public boolean isNetworkConnected(){
        final ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm != null){
            final Network network = cm.getActiveNetwork();
            final NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
            if(capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return true;
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean internetIsConnected(){
        final ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(cm != null) {
            final Network network = cm.getActiveNetwork();
            final NetworkCapabilities capabilities = cm.getNetworkCapabilities(network);
            return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
        }

        return false;
    }

}
