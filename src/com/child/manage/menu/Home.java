package com.child.manage.menu;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
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
public class Home {
    private Context mContext;
    private Activity mActivity;
    private ChildApplication mKXApplication;
    private View mHome;
    private RequestQueue mrq;
    private Gson gson = new Gson();
    private FlipperLayout.OnOpenListener mOnOpenListener;

    private ImageView jiaohuback;
    private ListView listView;
    private List<AccountMessage> list = new ArrayList<AccountMessage>();
    private JiaohuAdapter adapter;
    private TextView publishAll;//群发消息
    private Account mAccount;
    private String mIdentity;
    public Home(String identity,Account account,RequestQueue rq,Context context, Activity activity, ChildApplication application) {
        mContext = context;
        mrq = rq;
        mIdentity = identity;
        mAccount = account;
        mActivity = activity;
        mKXApplication = application;
        mHome = LayoutInflater.from(context).inflate(R.layout.home, null);

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

        setListener();
        getData();
    }

    private void findViewById() {
        jiaohuback = (ImageView) mHome.findViewById(R.id.jiaohuback);

        listView = (ListView) mHome.findViewById(R.id.jiaohu_lstv);
        publishAll = (TextView) mHome.findViewById(R.id.publish_all);
    }

    private void setListener() {
        jiaohuback.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if (mOnOpenListener != null) {
                    mOnOpenListener.open();
                }
            }
        });
        publishAll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.startActivity(new Intent(mContext, SendGroupMessageActivity.class));
            }
        });
    }


    public View getView() {
        return mHome;
    }

    public void setOnOpenListener(FlipperLayout.OnOpenListener onOpenListener) {
        mOnOpenListener = onOpenListener;
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
        mrq.add(request);
    }

    public Gson getGson() {
        return gson;
    }
}
