package com.child.manage.data;


import com.child.manage.entity.Account;
import com.child.manage.entity.RegisterDem;

/**
 * Created by liuzwei on 2014/11/17.
 */
public class RegDATA {
    private int code;
    private String msg;
    private RegisterDem data;

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

    public RegisterDem getData() {
        return data;
    }

    public void setData(RegisterDem data) {
        this.data = data;
    }
}
