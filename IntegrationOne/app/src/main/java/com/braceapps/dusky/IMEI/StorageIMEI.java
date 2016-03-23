package com.braceapps.dusky.IMEI;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Paresh on 12/31/2014.
 */
public class StorageIMEI {

    public int setIMEIParameter(Context L_context) {
        int status = 0;
        ManagerIMEI L_ManagerIMEI_obj = new ManagerIMEI();
        if (L_ManagerIMEI_obj.isIMEIValueStored(L_context) == 0) {
            String L_IMEIValue = L_ManagerIMEI_obj.getValidIMEI(L_context);
            if (!L_IMEIValue.matches("")) {
                SharedPreferences.Editor editor = L_context.getSharedPreferences("IMEI", 0).edit();
                editor.putString("IMEIorESN", L_IMEIValue);
                editor.commit();
                status = 1;  //success value
            }

        } else {
            status = -1;  //Setting the value to some error value viz. already loaded info.
        }
        if (status == -1) {
            new ExpHandler().displayException(2, L_context);
        }
        return status;
    }
}
