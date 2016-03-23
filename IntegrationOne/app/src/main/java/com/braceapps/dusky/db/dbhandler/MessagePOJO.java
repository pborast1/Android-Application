package com.braceapps.dusky.db.dbhandler;

/**
 * Created by Paresh on 1/20/2015.
 */
public class MessagePOJO {
    String contactNumber;
    String message;
    String time;
    String readStatus;
    String contactName;
    boolean lvUnorHide;

    public MessagePOJO(String contactNumber, String message, String time, String readStatus, String contactName) {
        this.contactNumber = contactNumber;
        this.message = message;
        this.time = time;
        this.contactName = contactName;
        this.readStatus = readStatus;
        this.lvUnorHide=false;
    }
    public MessagePOJO(String contactNumber, String message, String time, String readStatus, String contactName,Boolean mLVUnorHide) {
        this.contactNumber = contactNumber;
        this.message = message;
        this.time = time;
        this.contactName = contactName;
        this.readStatus = readStatus;
        this.lvUnorHide=mLVUnorHide;
    }
    public MessagePOJO(String contactNumber, String message, String time, String contactName) {
        this.contactNumber = contactNumber;
        this.message = message;
        this.time = time;
        this.contactName = contactName;
        this.readStatus = "0";
        this.lvUnorHide=false;
    }

    public MessagePOJO() {

    }
    public boolean isLvUnorHide() {
        return lvUnorHide;
    }

    public void setLvUnorHide(boolean lvUnorHide) {
        this.lvUnorHide = lvUnorHide;
    }
    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReadStatus() {
        return readStatus;
    }

    public void setReadStatus(String readStatus) {
        this.readStatus = readStatus;
    }
}
