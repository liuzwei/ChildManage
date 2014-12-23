package com.child.manage.entity;

/**
 * author: ${zhanghailong}
 * Date: 2014/12/15
 * Time: 23:45
 * 类的功能、说明写在此处.
 */
public class kecheng {
    private String datetime;
    private String pic;
    private String title;
    private String cont;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCont() {
        return cont;
    }

    public void setCont(String cont) {
        this.cont = cont;
    }

    public kecheng(String datetime, String pic, String title, String cont) {
        this.datetime = datetime;
        this.pic = pic;
        this.title = title;
        this.cont = cont;
    }
}
