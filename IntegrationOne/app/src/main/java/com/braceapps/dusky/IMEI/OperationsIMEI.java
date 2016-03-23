package com.braceapps.dusky.IMEI;

import android.content.Context;
import android.telephony.TelephonyManager;


/**
 * Created by Paresh on 12/31/2014.
 */
public class OperationsIMEI {

    public String getIMEI(Context L_context) {
        String L_StrVarIMEI = "";

        TelephonyManager telephonyManager = (TelephonyManager) L_context.getSystemService(L_context.TELEPHONY_SERVICE);
        L_StrVarIMEI = telephonyManager.getDeviceId();
        return L_StrVarIMEI;
    }


}
