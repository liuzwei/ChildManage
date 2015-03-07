package com.child.manage.menu;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.child.manage.ChildApplication;
import com.child.manage.R;
import com.child.manage.base.BaseActivity;
import com.child.manage.base.FlipperLayout;

/**
 * 菜单首页类
 *
 * @author rendongwei
 */
public class About extends BaseActivity implements View.OnClickListener {

    private Button mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        findViewById();
    }

    private void findViewById() {
        mMenu = (Button) this.findViewById(R.id.about_menu);
        mMenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.about_menu:
                finish();
                break;
        }
    }
}
