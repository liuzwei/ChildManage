package com.child.manage.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.child.manage.R;
import com.child.manage.base.BaseActivity;
import com.child.manage.data.AccountDATA;
import com.child.manage.data.ErrorDATA;
import com.child.manage.data.RegDATA;
import com.child.manage.data.SuccessDATA;
import com.child.manage.entity.Account;
import com.child.manage.util.InternetURL;
import com.child.manage.util.StringUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * author: ${zhanghailong}
 * Date: 2015/3/7
 * Time: 7:43
 * 类的功能、说明写在此处.
 */
public class RegisterAcitivity extends BaseActivity implements View.OnClickListener {
    private ImageView regist_back;
    private EditText regist_nickname;
    private EditText regist_pwr;
    private EditText regist_sure;
    private TextView regist_sub;

    private String name;
    private String pwr;
    private String sure;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registone_xml);
        initview();
    }

    private void initview() {
        regist_back = (ImageView) this.findViewById(R.id.regist_back);
        regist_back.setOnClickListener(this);
        regist_nickname = (EditText) this.findViewById(R.id.regist_nickname);
        regist_pwr = (EditText) this.findViewById(R.id.regist_pwr);
        regist_sure = (EditText) this.findViewById(R.id.regist_sure);
        regist_sub = (TextView) this.findViewById(R.id.regist_sub);
        regist_sub.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case  R.id.regist_back:
                finish();
                break;
            case R.id.regist_sub:
                name = regist_nickname.getText().toString();
                pwr = regist_pwr.getText().toString();
                sure = regist_sure.getText().toString();
                if(StringUtil.isNullOrEmpty(name)){
                    Toast.makeText(this, "请输入昵称", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(StringUtil.isNullOrEmpty(pwr)){
                    Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(pwr.length()<6){
                    Toast.makeText(this, "密码最少6位", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(StringUtil.isNullOrEmpty(sure)){
                    Toast.makeText(this, "请输入确认密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!pwr.equals(sure)){
                    Toast.makeText(this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog = new ProgressDialog(RegisterAcitivity.this);
                progressDialog.setMessage("登录中...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                register();
                break;
        }
    }
    //获得验证码的方法
    private void register(){
        //组装请求url
        String uri = String.format(InternetURL.REGISTER_PAI + "?user_name=%s&password=%s", name, pwr);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        Gson gson = new Gson();
                        try {
                            RegDATA data = gson.fromJson(s, RegDATA.class);
                            if(data.getData().getUid() >0){
                                Toast.makeText(getContext(), "注册成功，请登录", Toast.LENGTH_SHORT).show();
                                finish();
                            }else {
                                Toast.makeText(getContext(), "注册失败", Toast.LENGTH_SHORT).show();
                            }

                        }catch (Exception e){
                            ErrorDATA errorDATA = gson.fromJson(s, ErrorDATA.class);
                            if (errorDATA.getMsg().equals("failed")){
                                Toast.makeText(getContext(), "注册失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                        progressDialog.dismiss();
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progressDialog.dismiss();
                Toast.makeText(getContext(), "请求超时，稍后重试", Toast.LENGTH_SHORT).show();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(request);
    }


}
