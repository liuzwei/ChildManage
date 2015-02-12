package com.child.manage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.child.manage.base.BaseActivity;
import net.tsz.afinal.FinalActivity;

/**
 * Created by liuzwei on 2014/12/10.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 登录按钮
     */
    private Button mLogin;

    private EditText name;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        initView();
//        setListener();
    }

    /**
     * 绑定界面UI
     */
    private void initView() {
        mLogin = (Button) this.findViewById(R.id.loginbutton);
        mLogin.setOnClickListener(this);
        name = (EditText) this.findViewById(R.id.name);

    }

    /**
     * UI事件监听
     */
    private void setListener() {
        // 登录按钮监听
        mLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // 跳转到功能引导页
                Intent main = new Intent(LoginActivity.this,
                        MainActivity.class);
                if (name.getText().toString().equals("")) {
                    Toast.makeText(mContext, "请登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                main.putExtra("name", name.getText().toString());
                startActivity(main);
                finish();
            }
        });
    }

    public void onBackPressed() {
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loginbutton:
                // 跳转到功能引导页
                Intent main = new Intent(LoginActivity.this,
                        MainActivity.class);
                if (name.getText().toString().equals("")) {
                    Toast.makeText(mContext, "请输入账号", Toast.LENGTH_SHORT).show();
                    return;
                }
                main.putExtra("name", name.getText().toString());
                startActivity(main);
                break;
        }
    }
}
