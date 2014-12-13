package com.child.manage.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.child.manage.R;
import com.child.manage.base.BaseActivity;

/**
 * 写记录类
 * 
 * @author rendongwei
 * 
 */
public class WriteRecordActivity extends BaseActivity implements View.OnClickListener {
    private ImageView writerecord_cannel;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.writerecord_activity);
        initView();
	}

    private void initView() {
        writerecord_cannel = (ImageView) this.findViewById(R.id.writerecord_cannel);
        writerecord_cannel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.writerecord_cannel:
                finish();
                break;
        }
    }
}
