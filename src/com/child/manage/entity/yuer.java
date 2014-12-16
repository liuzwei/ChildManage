package com.child.manage.entity;

/**
 * author: ${zhanghailong}
 * Date: 2014/12/16
 * Time: 0:00
 * 类的功能、说明写在此处.
 */
public class yuer {
    private String pic;
    private String title;
    private String datetime;

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

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public yuer(String pic, String title, String datetime) {
        this.pic = pic;
        this.title = title;
        this.datetime = datetime;
    }
}
