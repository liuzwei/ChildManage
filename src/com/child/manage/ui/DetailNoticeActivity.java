package com.child.manage.ui;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.child.manage.ChildApplication;
import com.child.manage.R;
import com.child.manage.adapter.AnimateFirstDisplayListener;
import com.child.manage.base.BaseActivity;
import com.child.manage.data.NoticesDetailDATA;
import com.child.manage.entity.NoticeNews;
import com.child.manage.util.CommonUtil;
import com.child.manage.util.InternetURL;
import com.child.manage.util.MxgsaTagHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

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
    private ImageView notice_detail_cover;//图片
    private String id;

    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

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
        notice_detail_cover = (ImageView) this.findViewById(R.id.notice_detail_cover);
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
        String uri = String.format(InternetURL.GET_NOTICE_DETAIL_URL+"?id=%s", id);
        StringRequest request = new StringRequest(
                Request.Method.GET,
                uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        if (CommonUtil.isJson(s)){
                            NoticesDetailDATA data = getGson().fromJson(s, NoticesDetailDATA.class);
                            if (data.getCode() == 200){
                                NoticeNews noticeNews = data.getData();
                                detail_notice_title.setText(noticeNews.getTitle());
                                detail_notice_zhaiyao.setText(noticeNews.getSummary());
                                detail_notice_cont.setText(Html.fromHtml(noticeNews.getContent(), null, new MxgsaTagHandler(DetailNoticeActivity.this)));
                                detail_notice_dateline.setText(noticeNews.getTime());
                                if( noticeNews.getPic() == null || noticeNews.getPic().equals("") ){
                                    notice_detail_cover.setVisibility(View.GONE);
                                }else{
                                    notice_detail_cover.setVisibility(View.VISIBLE);
                                    imageLoader.displayImage(String.format(Constants.API_HEAD + "%s",noticeNews.getPic()),
                                            notice_detail_cover, ChildApplication.options, animateFirstListener);
                                }
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
        mSingleQueue.add(request);
    }

}
