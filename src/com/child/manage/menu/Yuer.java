package com.child.manage.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.child.manage.ChildApplication;
import com.child.manage.R;
import com.child.manage.adapter.ShipuAdapter;
import com.child.manage.adapter.YuerAdapter;
import com.child.manage.anim.UgcAnimations;
import com.child.manage.base.FlipperLayout;
import com.child.manage.entity.shipu;
import com.child.manage.entity.yuer;
import com.child.manage.ui.CheckInActivity;
import com.child.manage.ui.VoiceActivity;
import com.child.manage.ui.WriteRecordActivity;
import com.child.manage.util.ActivityForResultUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 菜单首页类
 * 
 * @author rendongwei
 * 
 */
public class Yuer {
    private Button mMenu;
	private Context mContext;
	private Activity mActivity;
	private ChildApplication mKXApplication;
	private View mYule;

	private FlipperLayout.OnOpenListener mOnOpenListener;

    private ListView yuerlstv;
    private YuerAdapter adapter;
    private List<yuer> lists = new ArrayList<yuer>();

	public Yuer(Context context, Activity activity, ChildApplication application) {
		mContext = context;
		mActivity = activity;
		mKXApplication = application;
        mYule = LayoutInflater.from(context).inflate(R.layout.yuer, null);
        //构造虚拟知识
        lists.add(new yuer("http://img3.chinateacher365.com/2008/09/923_200809282212081eA5g.jpg","北京宝华幼儿园","北京昭阳区"));
        lists.add(new yuer("http://img3.chinateacher365.com/2008/09/923_200809282212081eA5g.jpg","上海华幼幼儿园","上海徐汇区"));
        lists.add(new yuer("http://img3.chinateacher365.com/2008/09/923_200809282212081eA5g.jpg","成都康康幼儿园","成都金牛区"));
        lists.add(new yuer("http://img3.chinateacher365.com/2008/09/923_200809282212081eA5g.jpg","重庆泰迪熊幼儿园","重庆渝北区"));
        lists.add(new yuer("http://img3.chinateacher365.com/2008/09/923_200809282212081eA5g.jpg","北京宝华幼儿园","北京昭阳区"));
        lists.add(new yuer("http://img3.chinateacher365.com/2008/09/923_200809282212081eA5g.jpg","上海华幼幼儿园","上海徐汇区"));
        lists.add(new yuer("http://img3.chinateacher365.com/2008/09/923_200809282212081eA5g.jpg","成都康康幼儿园","成都金牛区"));
        lists.add(new yuer("http://img3.chinateacher365.com/2008/09/923_200809282212081eA5g.jpg","重庆泰迪熊幼儿园","重庆渝北区"));
		findViewById();
		setListener();

	}

	private void findViewById() {
		mMenu = (Button) mYule.findViewById(R.id.yuer_menu);
        yuerlstv = (ListView) mYule.findViewById(R.id.yuerlstv);
        adapter = new YuerAdapter(lists, mContext);
        yuerlstv.setAdapter(adapter);
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
}
