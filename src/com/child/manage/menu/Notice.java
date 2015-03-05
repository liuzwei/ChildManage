package com.child.manage.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
public class Notice {
    private Button mMenu;
    private Context mContext;
    private Activity mActivity;
    private ChildApplication mKXApplication;
    private View mHome;
    private RequestQueue mrq;
    private FlipperLayout.OnOpenListener mOnOpenListener;
    private PullToRefreshListView kechenglstv;
    private KechengAdapter adapter;
    private List<NoticeNews> lists = new ArrayList<NoticeNews>();
    private int pageIndex = 1;
    private static boolean IS_REFRESH = true;
    private Gson gson = new Gson();
     Account mAccount;
    String schoolId;
    public Notice(Account account,RequestQueue rq,Context context, Activity activity, ChildApplication application) {
        mContext = context;
        mAccount = account;
        mrq = rq;
        mActivity = activity;
        mKXApplication = application;
        mHome = LayoutInflater.from(context).inflate(R.layout.kecheng, null);
        findViewById();
        setListener();
        initData();
    }

    private void findViewById() {
        mMenu = (Button) mHome.findViewById(R.id.home_menu);
        kechenglstv = (PullToRefreshListView) mHome.findViewById(R.id.kechenglstv);
        adapter = new KechengAdapter(lists, mContext);
        kechenglstv.setAdapter(adapter);
        kechenglstv.setMode(PullToRefreshBase.Mode.BOTH);
        kechenglstv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(mActivity.getApplicationContext(), System.currentTimeMillis(),
                        DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);

                refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
                IS_REFRESH = true;
                pageIndex = 1;
                initData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                String label = DateUtils.formatDateTime(mActivity.getApplicationContext(), System.currentTimeMillis(),
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

    private void setListener() {
        mMenu.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (mOnOpenListener != null) {
                    mOnOpenListener.open();
                }
            }
        });

    }

    public View getView() {
        return mHome;
    }
    public void setOnOpenListener(FlipperLayout.OnOpenListener onOpenListener) {
        mOnOpenListener = onOpenListener;
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
                                Toast.makeText(mActivity, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
        mrq.add(request);
    }

    public Gson getGson() {
        return gson;
    }
}
