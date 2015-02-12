package com.child.manage.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import com.child.manage.R;
import com.child.manage.base.BaseActivity;
import com.child.manage.base.MyListView;

/**
 * 签到类
 *
 * @author rendongwei
 */
public class CheckInActivity extends BaseActivity {
    private Button mBack;
    private Button mCapture;
    private EditText mSearch;
    private MyListView mDisplay;
    // 显示的内容的总数量
    private int mCount;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.checkin_activity);
    }


}
