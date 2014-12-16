package com.child.manage.entity;

/**
 * author: ${zhanghailong}
 * Date: 2014/12/15
 * Time: 23:45
 * 类的功能、说明写在此处.
 */
public class kecheng {
    private String datetime;
    private String onetime;
    private String twotime;
    private String threetime;
    private String fourtime;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getOnetime() {
        return onetime;
    }

    public void setOnetime(String onetime) {
        this.onetime = onetime;
    }

    public String getTwotime() {
        return twotime;
    }

    public void setTwotime(String twotime) {
        this.twotime = twotime;
    }

    public String getThreetime() {
        return threetime;
    }

    public void setThreetime(String threetime) {
        this.threetime = threetime;
    }

    public String getFourtime() {
        return fourtime;
    }

    public void setFourtime(String fourtime) {
        this.fourtime = fourtime;
    }

    public kecheng(String datetime, String onetime, String twotime, String threetime, String fourtime) {
        this.datetime = datetime;
        this.onetime = onetime;
        this.twotime = twotime;
        this.threetime = threetime;
        this.fourtime = fourtime;
    }
}
