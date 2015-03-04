package com.child.manage.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.child.manage.R;
import com.child.manage.base.BaseActivity;
import com.child.manage.data.NoticesDATA;
import com.child.manage.data.NoticesDetailDATA;
import com.child.manage.entity.NoticeNews;
import com.child.manage.ui.Constants;
import com.child.manage.util.InternetURL;
import com.child.manage.util.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * author: ${zhanghailong}
 * Date: 2015/3/5
 * Time: 0:03
 * 类的功能、说明写在此处.
 */
public class DetailNoticeActivity extends BaseActivity implements View.OnClickListener {
    private Button detail_notice_menu;
    private TextView detail_notice_title;//标题
    private TextView detail_notice_zhaiyao;//摘要
    private TextView detail_notice_cont;//内容
    private TextView detail_notice_dateline;//shijian

    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_notice);
        id = getIntent().getExtras().getString(Constants.NOTICE_INFO);
        initView();
        initData();
    }

    private void initView() {
        detail_notice_menu = (Button) this.findViewById(R.id.detail_notice_menu);
        detail_notice_menu.setOnClickListener(this);
        detail_notice_title = (TextView) this.findViewById(R.id.detail_notice_title);
        detail_notice_zhaiyao = (TextView) this.findViewById(R.id.detail_notice_zhaiyao);
        detail_notice_cont = (TextView) this.findViewById(R.id.detail_notice_cont);
        detail_notice_dateline = (TextView) this.findViewById(R.id.detail_notice_dateline);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.detail_notice_menu:
                finish();
                break;
        }
    }

    private void initData(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_NOTICE_DETAIL_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            NoticesDetailDATA data = getGson().fromJson(s, NoticesDetailDATA.class);
                            if (data.getCode() == 200){
                                NoticeNews noticeNews = data.getData();
                                detail_notice_title.setText(noticeNews.getTitle());
                                detail_notice_zhaiyao.setText(noticeNews.getSummary());
                                detail_notice_cont.setText(noticeNews.getContent());
                                detail_notice_dateline.setText(noticeNews.getTime());
                            }else {
                                Toast.makeText(getContext(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(getContext(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(mContext, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("id", id);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        mSingleQueue.add(request);
    }

}
