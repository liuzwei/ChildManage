package com.child.manage.data;


import com.child.manage.entity.NoticeNews;
import com.child.manage.entity.Trace;

import java.util.List;

/**
 * Created by liuzwei on 2014/11/17.
 */
public class NoticesDATA {
    private int code;
    private String msg;
    private List<NoticeNews> data;

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

    public List<NoticeNews> getData() {
        return data;
    }

    public void setData(List<NoticeNews> data) {
        this.data = data;
    }
}
