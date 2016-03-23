package com.braceapps.dusky.IMEI;

import android.content.Context;
import android.util.Log;

/**
 * Created by Paresh on 12/31/2014.
 */
public class ExpHandler {
    public void displayException(int status, Context L_Context) {
        String postError = "";
        switch (status) {
            case 1:
                postError = "Unknown device error";
                break;
            case 2:
               // postError = "com.example.paresh.integrationone.IMEI already stored in the drive"; removed because no need to show any info its already stored
                break;
            case 3:
                postError = "Invalid/Unknown Device ID";
                break;
            case 4:
                postError = "Unable to Store Device ID";
                break;
            default:
                postError = "Unknown device error";
                break;
        }
        //Log.d(L_Context, postError, Toast.LENGTH_LONG).show();
        Log.e("imei", postError);
    }
}
