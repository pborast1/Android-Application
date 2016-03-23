package com.braceapps.dusky.Servicehandler.requestPJ;

/**
 * Created by paresh.boraste on 7/23/2015.
 */
public class PairPOJO {
    String master;
    String slave;

    public PairPOJO(String master, String slave) {
        this.master = master;
        this.slave = slave;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getSlave() {
        return slave;
    }

    public void setSlave(String slave) {
        this.slave = slave;
    }
}
