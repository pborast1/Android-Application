package com.braceapps.dusky.Registration.InternalStorageRegistration;

import android.content.Context;
import android.content.SharedPreferences;

import com.braceapps.dusky.Registration.AppRegistration.ExpHandler;
import com.braceapps.dusky.Registration.RegistrationDetails.InfoRetriever;

/**
 * Created by Paresh on 1/4/2015.
 */
public class StorageRegistration {

    public int storeRegId(Context param_context, String RegId) {
        int L_status = 0;
        InfoRetriever objInfoRetriever = new InfoRetriever();
        int infostatus = objInfoRetriever.isRegIdStored(param_context);
        if (infostatus != 1) {
            SharedPreferences.Editor editor = param_context.getSharedPreferences("RegistrationID", 0).edit();
            editor.putString("RegId", RegId);
            editor.commit();
            L_status = 1;
        } else {
            ExpHandler objExpHandler = new ExpHandler();
            if (infostatus == 1) {
                objExpHandler.displayException(2, param_context);
                L_status = -1;
            }
        }
        return L_status;
    }
}
