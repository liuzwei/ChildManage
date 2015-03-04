package com.child.manage.entity;

import java.io.Serializable;

/**
 * author: ${zhanghailong}
 * Date: 2014/12/15
 * Time: 23:45
 * 类的功能、说明写在此处.
 */
public class NoticeNews implements Serializable {
    private String id;
    private String title;
    private String summary;
    private String content ;
    private String pic;
    private String time ;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
