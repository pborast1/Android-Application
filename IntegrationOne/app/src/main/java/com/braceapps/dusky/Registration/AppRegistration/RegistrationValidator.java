package com.braceapps.dusky.Registration.AppRegistration;

import android.text.TextUtils;

/**
 * Created by Paresh on 1/4/2015.
 */
public class RegistrationValidator {

    public int isValidRegId(String param_RegID) {
        int L_Status = 1;
        if (TextUtils.isEmpty(param_RegID)) {
            L_Status = -1;
        }

        return L_Status;
    }
}
