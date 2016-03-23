package com.braceapps.dusky.Registration.RegistrationDetails;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

/**
 * Created by Paresh on 1/4/2015.
 */
public class InfoRetriever {
    public String getStoredRegId(Context param_Context) {
        String storedRegId = "";
        SharedPreferences prefs = param_Context.getSharedPreferences("RegistrationID", 0);
        String L_RegId = prefs.getString("RegId", null);
        if (isRegIdStored(param_Context) == 1) {
            storedRegId = L_RegId;
        }
        return storedRegId;
    }

    public int isRegIdStored(Context param_Context) {
        int status = 0;
        SharedPreferences prefs = param_Context.getSharedPreferences("RegistrationID", 0);
        String L_RegId = prefs.getString("RegId", null);
        if (!TextUtils.isEmpty(L_RegId)) {
            status = 1;
        } else
            status = -1;
        return status;
    }
}
