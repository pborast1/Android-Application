package com.braceapps.dusky.uicomponents.uipojo;

/**
 * Created by Paresh on 2/26/2015.
 */
public class OptionCardPOJO {
    String tag;
    String detail;
    String startlink;

    public OptionCardPOJO(String tag, String detail, String startlink) {
        this.tag = tag;
        this.detail = detail;
        this.startlink = startlink;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getStartlink() {
        return startlink;
    }

    public void setStartlink(String startlink) {
        this.startlink = startlink;
    }
}


