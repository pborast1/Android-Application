package com.braceapps.dusky.IMEI;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by Paresh on 12/31/2014.
 */
public class ManagerIMEI {

    public int isIMEIValueStored(Context L_context) {
        int status = 0;
        // Context L_context= MainActivity.getContextOfApplication();

        SharedPreferences prefs = L_context.getSharedPreferences("IMEI", 0);
        String L_IMEI = prefs.getString("IMEIorESN", null);
        if (L_IMEI == null) {
            status = 0;
        } else if (L_IMEI == "") {
            status = 0;
        } else {
            if (new IMEIValidator().isValidIMEI(L_IMEI) == 1) {
                status = 1;
            } else
                status = -1;
        }
        return status;
    }

    public String getValidIMEI(Context L_context) {
        int status = 0;
        //Context L_context= MainActivity.getContextOfApplication();
        String L_IMEI = new OperationsIMEI().getIMEI(L_context);

        if (new IMEIValidator().isValidIMEI(L_IMEI) == 1) {
            status = 1;
        } else {
            status = -1;
        }


        if (status == 1) {
            return L_IMEI;
        } else {
            new ExpHandler().displayException(3, L_context);
            return "";   //write something to handle exception
        }


    }

    public int storeIMEI(Context L_context) {
        int status = 0;
        status = new StorageIMEI().setIMEIParameter(L_context);

        if (status == 0) {
            //handle exception here for com.example.paresh.integrationone.IMEI already exists
        }
        return status;
    }

    public String getStoredIMEI(Context L_context) {
        String L_IMEI = "";
        if (this.isIMEIValueStored(L_context) == 1) {
            SharedPreferences prefs = L_context.getSharedPreferences("IMEI", 0);
            L_IMEI = prefs.getString("IMEIorESN", null);
            return L_IMEI;
        } else {
            return null;  //handle exception if required
        }

    }
}
