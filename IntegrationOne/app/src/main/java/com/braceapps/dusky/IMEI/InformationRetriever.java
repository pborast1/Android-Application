package com.braceapps.dusky.IMEI;

import android.content.Context;

/**
 * Created by Paresh on 12/31/2014.
 */
public class InformationRetriever {
    public String getIMEIValue(Context L_Context) {
        String IMEICode = null;
        ManagerIMEI L_ManagerIMEI = new ManagerIMEI();
        IMEICode = L_ManagerIMEI.getStoredIMEI(L_Context);
        return IMEICode;
    }
}
