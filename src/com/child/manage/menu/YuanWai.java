package com.child.manage.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.child.manage.ChildApplication;
import com.child.manage.R;
import com.child.manage.adapter.YuerAdapter;
import com.child.manage.base.BaseActivity;
import com.child.manage.base.FlipperLayout;
import com.child.manage.data.YEYDATA;
import com.child.manage.entity.YouerYuan;
import com.child.manage.library.PullToRefreshBase;
import com.child.manage.library.PullToRefreshListView;
import com.child.manage.ui.Constants;
import com.child.manage.ui.DetailYouerYuanActivity;
import com.child.manage.upload.MultiPartStack;
import com.child.manage.util.InternetURL;
import com.child.manage.util.StringUtil;
import com.child.manage.widget.ContentListView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜单首页类
 *
 * @author rendongwei
 */
public class YuanWai{
    private Button mMenu;
    private Context mContext;
    private Activity mActivity;
    private RequestQueue mrq;
    private ChildApplication mKXApplication;
    private View mYule;

    private FlipperLayout.OnOpenListener mOnOpenListener;

    private PullToRefreshListView yuerlstv;
    private YuerAdapter adapter;
    private List<YouerYuan> lists = new ArrayList<YouerYuan>();

    private int pageIndex = 1;
    private static boolean IS_REFRESH = true;
    private Gson gson = new Gson();

    public YuanWai(RequestQueue rq, Context context, Activity activity, ChildApplication application) {
        mrq = rq;
        mContext = context;
        mActivity = activity;
        mKXApplication = application;
        mYule = LayoutInflater.from(context).inflate(R.layout.yuer, null);
        findViewById();
        setListener();
        initData();
    }

    private void findViewById() {
        mMenu = (Button) mYule.findViewById(R.id.yuer_menu);
        yuerlstv = (PullToRefreshListView) mYule.findViewById(R.id.yuerlstv);

        adapter = new YuerAdapter(lists, mContext);
        yuerlstv.setAdapter(adapter);
        yuerlstv.setMode(PullToRefreshBase.Mode.BOTH);
        yuerlstv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
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
        yuerlstv.setAdapter(adapter);
        yuerlstv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detail = new Intent(mContext, DetailYouerYuanActivity.class);
                YouerYuan record = lists.get(position-1);
                detail.putExtra(Constants.DETAIL_YUANWAI, record);
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
        return mYule;
    }

    public void setOnOpenListener(FlipperLayout.OnOpenListener onOpenListener) {
        mOnOpenListener = onOpenListener;
    }
    private void initData(){
        StringRequest request = new StringRequest(
                Request.Method.POST,
                InternetURL.GET_YUANWAITIANDI_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (StringUtil.isJson(s)) {
                            YEYDATA data = getGson().fromJson(s, YEYDATA.class);
                            if (data.getCode() == 200){
                                lists.clear();
                                lists.addAll(data.getData());
                                yuerlstv.onRefreshComplete();
                                adapter.notifyDataSetChanged();
                            }else {
                                Toast.makeText(mActivity, R.string.get_data_error, Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(mActivity, R.string.get_data_error, Toast.LENGTH_SHORT).show();
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
                params.put("pageIndex", String.valueOf(pageIndex));
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                params.put("Content-Type","application/x-www-form-urlencoded");
                return params;
            }
        };
        mrq.add(request);
    }

    public Gson getGson() {
        return gson;
    }
}
