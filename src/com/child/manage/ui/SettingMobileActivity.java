package com.child.manage.ui;

import android.content.Intent;
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
import com.child.manage.entity.Account;
import com.child.manage.util.InternetURL;
import com.child.manage.util.StringUtil;
import com.google.gson.Gson;

/**
 * author: ${zhanghailong}
 * Date: 2014/11/18
 * Time: 15:58
 * 类的功能、说明写在此处.
 */
public class SettingMobileActivity extends BaseActivity implements View.OnClickListener{
    private Button back;
    private EditText mobile;
    private String mobileNum;
    Account account ;
    private TextView set;
    private RequestQueue mRequestQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setmibole);
        account = getGson().fromJson(sp.getString(Constants.ACCOUNT_KEY, ""), Account.class);
        initView();
    }

    private void initView() {
        back = (Button) this.findViewById(R.id.back);
        back.setOnClickListener(this);
        mobile = (EditText) this.findViewById(R.id.mobile);
        mobile.setText(account.getMobile());
        set = (TextView) this.findViewById(R.id.set);
        set.setOnClickListener(this);
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
                //设置手机事件
                mobileNum = mobile.getText().toString();//邮箱
                if(StringUtil.isNullOrEmpty(mobileNum)){
                    Toast.makeText(this, "请输入手机号！", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mobileNum.length() != 11){
                    Toast.makeText(this, "手机号格式不正确！", Toast.LENGTH_SHORT).show();
                    return;
                }
                getYzm(mobileNum);
                break;
        }
    }
    private void getYzm(final String mobileNum) {
        String uri = String.format(InternetURL.GET_YZM_URL+"?mobile=%s&type=%d",mobileNum, 0);
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
                                Intent success = new Intent(SettingMobileActivity.this, SettingMobileActivityTwo.class);
                                success.putExtra("number", mobileNum);
                                startActivity(success);
                                finish();
                            }else{
                                Toast.makeText(SettingMobileActivity.this, "获取验证码失败！", Toast.LENGTH_SHORT).show();
                            }
                        }catch (Exception e){
                            ErrorDATA errorDATA = gson.fromJson(s, ErrorDATA.class);
                            if (errorDATA.getMsg().equals("failed")){
                                Toast.makeText(SettingMobileActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
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
