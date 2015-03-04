package com.child.manage.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.child.manage.R;
import com.child.manage.base.BaseActivity;
import com.child.manage.data.YEYDATA;
import com.child.manage.entity.YouerYuan;
import com.child.manage.menu.YuanWai;
import com.child.manage.util.InternetURL;
import com.child.manage.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * author: ${zhanghailong}
 * Date: 2015/3/4
 * Time: 23:11
 * 类的功能、说明写在此处.
 */
public class DetailYouerYuanActivity extends BaseActivity implements View.OnClickListener {
    private Button detail_youeryuan_menu;
    private TextView youeryuan_name;
    private TextView youeryuan_dateline;
    private TextView youeryuan_address;
    private TextView youeryuan_tel;
    private TextView youeryuan_cont;
    private LinearLayout location_about;
    YouerYuan yy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_youeryuan);
        yy = (YouerYuan)getIntent().getExtras().get(Constants.DETAIL_YUANWAI);
        initView();
    }

    private void initView() {
        detail_youeryuan_menu = (Button) this.findViewById(R.id.detail_youeryuan_menu);
        detail_youeryuan_menu.setOnClickListener(this);
        youeryuan_name = (TextView) this.findViewById(R.id.youeryuan_name);
        youeryuan_dateline = (TextView) this.findViewById(R.id.youeryuan_dateline);
        youeryuan_address = (TextView) this.findViewById(R.id.youeryuan_address);
        youeryuan_tel = (TextView) this.findViewById(R.id.youeryuan_tel);
        youeryuan_cont = (TextView) this.findViewById(R.id.youeryuan_cont);
        location_about = (LinearLayout) this.findViewById(R.id.location_about);
        location_about.setOnClickListener(this);


        youeryuan_name.setText(yy.getName());
        youeryuan_dateline.setText(yy.getBusiness_time());
        youeryuan_address.setText(yy.getAddress());
        youeryuan_tel.setText(yy.getTel());
        youeryuan_cont.setText(yy.getInfo());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.detail_youeryuan_menu:
                finish();
                break;
            case R.id.location_about:
                Intent location = new Intent(this,LocationAboutActivity.class);
                location.putExtra(Constants.LAT, yy.getLat());
                location.putExtra(Constants.LON, yy.getLng());
                startActivity(location);
                break;
        }
    }

}
