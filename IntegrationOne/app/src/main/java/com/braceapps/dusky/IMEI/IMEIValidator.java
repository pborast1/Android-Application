package com.braceapps.dusky.IMEI;

/**
 * Created by Paresh on 12/31/2014.
 */
public class IMEIValidator {
    public int isValidIMEI(String L_IMEI) {
        int valid = 1;
        if (L_IMEI == null) {
            valid = -1;
        }
        if (L_IMEI.matches("")) {
            valid = -1;
        }
        if (L_IMEI.length() != 15) {
            valid = -1;
        }
        return valid;
    }
}
