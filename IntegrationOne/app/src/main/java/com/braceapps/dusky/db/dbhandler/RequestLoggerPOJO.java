package com.braceapps.dusky.db.dbhandler;

/**
 * Created by Paresh on 3/12/2015.
 */
public class RequestLoggerPOJO {
    String contactNumber;
    String message;
    String time;
    String readStatus;
    String contactName;
    String type;
    String title;
    boolean lvUnorHide;
    int id;


    public RequestLoggerPOJO(String title, String contactNumber, String message, String time, String readStatus, String contactName, String type) {
        this.contactNumber = contactNumber;
        this.message = message;
        this.time = time;
        this.contactName = contactName;
        this.readStatus = readStatus;
        this.type = type;
        this.title = title;
        this.lvUnorHide=false;
    }
    public boolean isLvUnorHide() {
        return lvUnorHide;
    }

    public void setLvUnorHide(boolean lvUnorHide) {
        this.lvUnorHide = lvUnorHide;
    }

    public RequestLoggerPOJO() {

    }

    public int getid() {
        return id;
    }

    public void setid(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String gettitle() {
        return title;
    }

    public void setTitle(String type) {
        this.title = type;
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
