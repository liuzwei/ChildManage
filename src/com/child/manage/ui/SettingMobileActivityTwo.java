package com.child.manage.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.child.manage.R;
import com.child.manage.base.BaseActivity;
import com.child.manage.data.ErrorDATA;
import com.child.manage.data.SuccessDATA;
import com.child.manage.util.InternetURL;
import com.child.manage.util.StringUtil;
import com.google.gson.Gson;

/**
 * author: ${zhanghailong}
 * Date: 2014/12/3
 * Time: 9:10
 * 类的功能、说明写在此处.
 */
public class SettingMobileActivityTwo extends BaseActivity implements View.OnClickListener {
    private String number;
    private Button back;//返回
    private EditText yzm ;//验证码
    private TextView set;//设置按钮
    private String yzmnumber;
    private RequestQueue mRequestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setmobiletwo);
        number = getIntent().getExtras().getString("number");
        initView();
    }

    private void initView() {
        back = (Button) this.findViewById(R.id.back);
        back.setOnClickListener(this);
        set = (TextView) this.findViewById( R.id.set);
        set.setOnClickListener(this);
        yzm = (EditText) this.findViewById(R.id.yzm);
        mRequestQueue = Volley.newRequestQueue(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.back:
                finish();
                break;
            case R.id.set:
                yzmnumber =  yzm.getText().toString();
                if(StringUtil.isNullOrEmpty(yzmnumber)){
                    Toast.makeText(this, "请输入验证码！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(yzmnumber.length() != 4){
                    Toast.makeText(this, "验证码格式不正确！", Toast.LENGTH_SHORT).show();
                    return;
                }
                getdata(number ,yzmnumber);
                break;

        }
    }

    private void getdata(final String number, String yzmnumber) {
        String uri = String.format(InternetURL.SET_MOBILE_URL+"?mobile=%s&code=%s",number, yzmnumber);
        StringRequest request = new StringRequest(Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Gson gson = new Gson();
                        try {
                            SuccessDATA data = gson.fromJson(s, SuccessDATA.class);
                            if (data.getCode() == 200){
                                //成功
                                Toast.makeText(SettingMobileActivityTwo.this, "绑定手机号成功！", Toast.LENGTH_SHORT).show();
                                finish();
                            }else{
                                Toast.makeText(SettingMobileActivityTwo.this, "验证码不正确！", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            ErrorDATA errorDATA = gson.fromJson(s, ErrorDATA.class);
                            if (errorDATA.getMsg().equals("failed")){
                                Toast.makeText(SettingMobileActivityTwo.this, "网络错误", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        mRequestQueue.add(request);
    }
}
