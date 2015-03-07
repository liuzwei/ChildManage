package com.child.manage.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.*;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.child.manage.ChildApplication;
import com.child.manage.R;
import com.child.manage.adapter.KechengAdapter;
import com.child.manage.base.BaseActivity;
import com.child.manage.base.FlipperLayout;
import com.child.manage.data.AccountMessageDATA;
import com.child.manage.data.NoticesDATA;
import com.child.manage.entity.Account;
import com.child.manage.ui.DetailNoticeActivity;
import com.child.manage.entity.NoticeNews;
import com.child.manage.library.PullToRefreshBase;
import com.child.manage.library.PullToRefreshListView;
import com.child.manage.ui.*;
import com.child.manage.util.CommonUtil;
import com.child.manage.util.InternetURL;
import com.child.manage.util.StringUtil;
import com.google.gson.Gson;

import java.util.*;

/**
 * 菜单首页类
 *
 * @author rendongwei
 */
public class Notice extends BaseActivity implements View.OnClickListener{
    private Button mMenu;
    private PullToRefreshListView kechenglstv;
    private KechengAdapter adapter;
    private List<NoticeNews> lists = new ArrayList<NoticeNews>();
    private int pageIndex = 1;
    private static boolean IS_REFRESH = true;
    private Account mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kecheng);
        mAccount = getGson().fromJson(sp.getString(Constants.ACCOUNT_KEY, ""), Account.class);
        findViewById();
        initData();
    }

    private void findViewById() {
        mMenu = (Button) this.findViewById(R.id.home_menu);
        mMenu.setOnClickListener(this);
        kechenglstv = (PullToRefreshListView) this.findViewById(R.id.kechenglstv);
        adapter = new KechengAdapter(lists, mContext);
        kechenglstv.setAdapter(adapter);
        kechenglstv.setMode(PullToRefreshBase.Mode.BOTH);
        kechenglstv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getContext().getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = true;
                pageIndex = 1;
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(getContext().getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = false;
                pageIndex++;
                initData();
            }
        });
        kechenglstv.setAdapter(adapter);
        kechenglstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detail = new Intent(mContext, DetailNoticeActivity.class);
                NoticeNews record = lists.get(position-1);
                detail.putExtra(Constants.NOTICE_INFO,record.getId());
                mContext.startActivity(detail);
            }
        });
    }
    private void initData(){
        String uri = String.format(InternetURL.GET_NOTICE_URL+"?school_id=%s&pageIndex=%s&pageSize=%s", mAccount.getSchool_id(), String.valueOf(pageIndex),"20");
        StringRequest request = new StringRequest(
                Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (CommonUtil.isJson(s)){
                            NoticesDATA data = getGson().fromJson(s, NoticesDATA.class);
                            if (data.getCode() == 200){
                                if(IS_REFRESH){
                                    lists.clear();
                                }
                                lists.addAll(data.getData());
                                kechenglstv.onRefreshComplete();
                                adapter.notifyDataSetChanged();
                            }else {
                                Toast.makeText(getContext(), R.string.get_data_error, Toast.LENGTH_SHORT).show();
                            }
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
            case R.id.home_menu:
                finish();
                break;
        }
    }
}
