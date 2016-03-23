package com.braceapps.dusky.telephonymessage.services;

/**
 * Created by paresh.boraste on 7/8/2015.
 */
public class RequestPOJO {
    String slave;
    String title;
    String msg;
    String contactNumber;
    String contactName;

    public RequestPOJO(String slave, String title, String msg, String contactNumber, String contactName) {
        this.slave = slave;
        this.title = title;
        this.msg = msg;
        this.contactNumber = contactNumber;
        this.contactName = contactName;
    }

    public String getSlave() {
        return slave;
    }

    public void setSlave(String slave) {
        this.slave = slave;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }
}
