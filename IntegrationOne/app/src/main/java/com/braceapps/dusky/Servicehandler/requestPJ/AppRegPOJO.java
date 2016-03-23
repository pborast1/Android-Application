package com.braceapps.dusky.Servicehandler.requestPJ;

/**
 * Created by paresh.boraste on 7/23/2015.
 */
public class AppRegPOJO {
    String regid;
    String imei;

    public AppRegPOJO( String imei,String regid) {
        this.regid = regid;
        this.imei = imei;
    }

    public String getRegid() {
        return regid;
    }

    public void setRegid(String regid) {
        this.regid = regid;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
}
