package com.child.manage.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.child.manage.ChildApplication;
import com.child.manage.R;
import com.child.manage.base.BaseActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 手机图片文件类
 * 
 * @author rendongwei
 * 
 */
public class PhoneAlbumActivity extends BaseActivity {
	private Button mCancel;
	private ListView mDisplay;
	public static Activity mInstance;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.phonealbum_activity);

	}



}
