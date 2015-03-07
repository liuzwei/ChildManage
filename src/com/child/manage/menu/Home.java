package com.child.manage.menu;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.child.manage.ChildApplication;
import com.child.manage.R;
import com.child.manage.adapter.JiaohuAdapter;
import com.child.manage.anim.UgcAnimations;
import com.child.manage.base.BaseActivity;
import com.child.manage.base.FlipperLayout;
import com.child.manage.data.AccountMessageDATA;
import com.child.manage.entity.Account;
import com.child.manage.entity.AccountMessage;
import com.child.manage.ui.*;
import com.child.manage.util.ActivityForResultUtil;
import com.child.manage.util.CommonUtil;
import com.child.manage.util.InternetURL;
import com.google.gson.Gson;
import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 菜单首页类
 *
 * @author rendongwei
 */
public class Home extends BaseActivity implements View.OnClickListener{
    private ImageView jiaohuback;
    private ListView listView;
    private List<AccountMessage> list = new ArrayList<AccountMessage>();
    private JiaohuAdapter adapter;
    private TextView publishAll;//群发消息
    private Account mAccount;
    private String mIdentity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        findViewById();
        adapter = new JiaohuAdapter(list, mContext);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent chat = new Intent(mContext, ChatActivity.class);
                chat.putExtra(Constants.ACCOUNT_MESSAGE, list.get(position));
                mContext.startActivity(chat);
            }
        });
        mAccount = getGson().fromJson(sp.getString(Constants.ACCOUNT_KEY, ""), Account.class);
        mIdentity = getGson().fromJson(sp.getString(Constants.IDENTITY, ""), String.class);
        getData();
    }

    private void findViewById() {
        jiaohuback = (ImageView) this.findViewById(R.id.jiaohuback);
        listView = (ListView) this.findViewById(R.id.jiaohu_lstv);
        publishAll = (TextView) this.findViewById(R.id.publish_all);

        jiaohuback.setOnClickListener(this);
        publishAll.setOnClickListener(this);
    }

    private void getData(){
        String uri = String.format(InternetURL.JIAOHU_MESSAGE_LIST+"?uid=%s&user_type=%s", mAccount.getUid(), mIdentity);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (CommonUtil.isJson(s)){
                            AccountMessageDATA data = getGson().fromJson(s, AccountMessageDATA.class);
                            list.addAll(data.getData());
                            adapter.notifyDataSetChanged();
                        }else {
                            Toast.makeText(mContext, "数据错误，请稍后重试", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(mContext, "服务器异常，请稍后重试", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        mRequestQueue.add(request);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.publish_all:
                startActivity(new Intent(mContext, SendGroupMessageActivity.class));
                break;
            case R.id.jiaohuback:
                finish();
                break;
        }
    }
}
