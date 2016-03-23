package com.braceapps.dusky.Registration.AppRegistration;

import android.content.Context;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by Paresh on 1/4/2015.
 */
public class GCMRegisterer {

    public String registerAppWithGCMServer(Context param_Context) {
        int L_status = 0;           //first time registration identifier(yet to be used)
        String regId = "";
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(param_Context);  //Creating GCM object to call registration function

        if (gcm != null) {
            try {
                InstanceID instanceID = InstanceID.getInstance(param_Context);
                String gcmRegistrationToken = instanceID.getToken(Config.GOOGLE_PROJECT_ID,
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                regId =gcmRegistrationToken;// gcm.register(Config.GOOGLE_PROJECT_ID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            ExpHandler expObject = new ExpHandler();
            expObject.displayException(1, param_Context);  //exception toast for empty GCM/invalid project ID
        }
        return regId;
    }
}
