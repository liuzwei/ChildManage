package com.child.manage.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.child.manage.R;
import com.child.manage.base.BaseActivity;

/**
 * author: ${zhanghailong}
 * Date: 2014/12/14
 * Time: 21:48
 * 类的功能、说明写在此处.
 */
public class SettingAboutActivity extends BaseActivity implements View.OnClickListener {
    private Button back;//返回

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setabout);
        initView();
    }

    private void initView() {
        back = (Button) this.findViewById(R.id.back);
        back.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;

        }
    }

}
