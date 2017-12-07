package com.dev.sevice.findbymap;

import android.*;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 11/16/2017 AD.
 */

public class GlobalFunctions {
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    public static boolean CheckFullPermission(int requestCode, String permissions[], int[] grantResults, final Context context, final Activity activity) {

        boolean pass = false;

        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(android.Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);

                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    if (perms.get(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && perms.get(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    } else {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.READ_PHONE_STATE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.ACCESS_FINE_LOCATION)
                                || ActivityCompat.shouldShowRequestPermissionRationale(activity, android.Manifest.permission.CAMERA)) {

                            new AlertDialog.Builder(context)
                                    .setMessage("Do you want to enable permission?")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    CheckPermission.checkAndRequestPermissions(context, activity);
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    Toast.makeText(context, "Go to settings and enable permissions", Toast.LENGTH_LONG).show();
                                                    activity.finish();
                                                    System.exit(0);
                                                    break;
                                            }
                                        }
                                    })
                                    .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    CheckPermission.checkAndRequestPermissions(context, activity);
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    Toast.makeText(context, "Go to settings and enable permissions", Toast.LENGTH_LONG).show();
                                                    activity.finish();
                                                    System.exit(0);
                                                    break;
                                            }
                                        }
                                    })
                                    .create()
                                    .show();
                        } else {
                            pass = true;
                        }
                    }
                }
            }
        }
        return pass;
    }

    public static boolean subFullPermission(final Context context, final Activity activity) {  // Unecessary
        boolean pass = false;
        if (CheckPermission.checkAndRequestPermissions(context, activity)) {
            pass = true;
        } else {
            CheckPermission.checkAndRequestPermissions(context, activity);
        }
        return pass;
    }
}
