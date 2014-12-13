package com.child.manage.ui;

import android.content.Intent;
import android.os.Bundle;
import com.child.manage.LoginActivity;
import com.child.manage.R;
import com.child.manage.base.BaseActivity;

/**
 * author: ${zhanghailong}
 * Date: 2014/12/13
 * Time: 11:29
 * 类的功能、说明写在此处.
 */
public class StartActivity extends BaseActivity implements Runnable {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);
        // 启动一个线程
        new Thread(this).start();
    }

    public void run() {
        try {
            // 3秒后跳转到登录界面
            Thread.sleep(3000);
            startActivity(new Intent(StartActivity.this, LoginActivity.class));
            finish();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
