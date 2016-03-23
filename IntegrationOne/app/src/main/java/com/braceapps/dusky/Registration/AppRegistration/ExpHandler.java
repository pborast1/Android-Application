package com.braceapps.dusky.Registration.AppRegistration;

import android.content.Context;
import android.util.Log;

/**
 * Created by Paresh on 1/4/2015.
 */
public class ExpHandler {
    public void displayException(int status, Context param_Context) {
        String postError = "";
        switch (status) {
            case 1:
                postError = "Could not coonnect GCM server";
                break;
            case 2:
                postError = "RegID already stored in the drive";
                break;
            case 3:
                postError = "Invalid/Unknown RegID";
                break;
            case 4:
                postError = "Unable to Store RegID";
                break;
            default:
                postError = "Unknown system error";
                break;
        }
        // Toast.makeText(param_Context, postError, Toast.LENGTH_LONG).show();
        Log.e("reg id", postError);
    }
}
