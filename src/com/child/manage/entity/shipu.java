package com.child.manage.entity;

/**
 * author: ${zhanghailong}
 * Date: 2014/12/15
 * Time: 23:02
 * 类的功能、说明写在此处.
 */
public class shipu {
    private String datetime;
    private String zaocan;
    private String zhongcan;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getZaocan() {
        return zaocan;
    }

    public void setZaocan(String zaocan) {
        this.zaocan = zaocan;
    }

    public String getZhongcan() {
        return zhongcan;
    }

    public void setZhongcan(String zhongcan) {
        this.zhongcan = zhongcan;
    }

    public shipu(String datetime, String zaocan, String zhongcan) {
        this.datetime = datetime;
        this.zaocan = zaocan;
        this.zhongcan = zhongcan;
    }
}

