package com.braceapps.dusky.db.dbhandler;

/**
 * Created by Paresh on 2/11/2015.
 */
public class CallLogPOJO {
    String contactNumber;
    String contactName;
    String type;
    String time;
    boolean lvUnorHide;

    public CallLogPOJO() {
    }



    public CallLogPOJO(String contactNumber, String contactName, String type, String time) {
        this.contactNumber = contactNumber;
        this.contactName = contactName;
        this.type = type;
        this.time = time;
        this.lvUnorHide=false;
    }
    public CallLogPOJO(String contactNumber, String contactName, String type, String time,Boolean mLVUnorHide) {
        this.contactNumber = contactNumber;
        this.contactName = contactName;
        this.type = type;
        this.time = time;
        this.lvUnorHide=mLVUnorHide;
    }
    public boolean isLvUnorHide() {
        return lvUnorHide;
    }

    public void setLvUnorHide(boolean lvUnorHide) {
        this.lvUnorHide = lvUnorHide;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
