package com.child.manage.data;


import com.child.manage.entity.Account;

/**
 * Created by liuzwei on 2014/11/17.
 */
public class AccountDATA {
    private int code;
    private String msg;
    private Account data;

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

    public Account getData() {
        return data;
    }

    public void setData(Account data) {
        this.data = data;
    }
}
