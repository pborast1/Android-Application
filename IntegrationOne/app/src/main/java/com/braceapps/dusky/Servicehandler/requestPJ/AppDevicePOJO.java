package com.braceapps.dusky.Servicehandler.requestPJ;

/**
 * Created by paresh.boraste on 7/23/2015.
 */
public class AppDevicePOJO {
    String imei;

    public AppDevicePOJO(String imei) {
        this.imei = imei;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
}
