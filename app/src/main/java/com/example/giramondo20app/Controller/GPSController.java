package com.example.giramondo20app.Controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;

import com.example.giramondo20app.MainActivity;

public class GPSController {

    private MainActivity activity;

    public GPSController(Activity activity) {
        this.activity = (MainActivity) activity;
    }

    //show dialog to able/disable GPS service
    public void buildAlertMessageIfNoGps() {
        final LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (manager != null && !manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) { //check the Gps service
            final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage("Il tuo GPS sembra essere disabilitato, vuoi abilitarlo?")
                    .setCancelable(false)
                    .setPositiveButton("Sì", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            activity.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            final AlertDialog alert = builder.create();
            alert.show();
        }
    }
}
