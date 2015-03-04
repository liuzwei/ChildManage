package com.child.manage.data;


import com.child.manage.entity.Trace;
import com.child.manage.entity.YouerYuan;

import java.util.List;

/**
 * Created by liuzwei on 2014/11/17.
 */
public class YEYDATA {
    private int code;
    private String msg;
    private List<YouerYuan> data;

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

    public List<YouerYuan> getData() {
        return data;
    }

    public void setData(List<YouerYuan> data) {
        this.data = data;
    }
}
