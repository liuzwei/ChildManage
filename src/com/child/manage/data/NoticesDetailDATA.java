package com.child.manage.data;


import com.child.manage.entity.NoticeNews;

import java.util.List;

/**
 * Created by liuzwei on 2014/11/17.
 */
public class NoticesDetailDATA {
    private int code;
    private String msg;
    private NoticeNews data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public NoticeNews getData() {
        return data;
    }

    public void setData(NoticeNews data) {
        this.data = data;
    }
}
