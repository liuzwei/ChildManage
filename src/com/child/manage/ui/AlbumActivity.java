package com.kaixin.android.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.child.manage.R;
import com.child.manage.base.BaseActivity;


/**
 * 用户手机文件夹具体图片显示类
 *
 * @author rendongwei
 */
public class AlbumActivity extends BaseActivity {
    private Button mBack;
    private GridView mDisplay;
    private TextView mCount;


    private List<Map<String, String>> mList = new ArrayList<Map<String, String>>();// 存放当前文件夹中的图片的地址信息
    private List<String> mSelect = new ArrayList<String>();// 存放当前用户选择的图片的编号

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_activity);
        findViewById();
        setListener();


    }

    private void findViewById() {
        mBack = (Button) findViewById(R.id.album_back);
        mDisplay = (GridView) findViewById(R.id.album_display);
        mCount = (TextView) findViewById(R.id.album_count);

    }

    private void setListener() {
        mBack.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // 关闭当前界面
                finish();
            }
        });


    }


}
