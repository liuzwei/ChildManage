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
import com.child.manage.adapter.KechengAdapter;
import com.child.manage.adapter.ShipuAdapter;
import com.child.manage.anim.UgcAnimations;
import com.child.manage.base.FlipperLayout;
import com.child.manage.entity.kecheng;
import com.child.manage.entity.shipu;
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
public class Kecheng {
    private Button mMenu;
	private Context mContext;
	private Activity mActivity;
	private ChildApplication mKXApplication;
	private View mHome;

	private FlipperLayout.OnOpenListener mOnOpenListener;
    private ListView kechenglstv;
    private KechengAdapter adapter;
    private List<kecheng> lists = new ArrayList<kecheng>();

	public Kecheng(Context context, Activity activity, ChildApplication application) {
		mContext = context;
		mActivity = activity;
		mKXApplication = application;
		mHome = LayoutInflater.from(context).inflate(R.layout.kecheng, null);
        //构造虚拟数据
        lists.add(new kecheng("星期一 2014-02-10","09:00 语文","11:00 游戏","02:00 游戏","04:00 园区游玩"));
        lists.add(new kecheng("星期二 2014-02-11","09:00 美术","11:00 数学","02:00 游戏","04:00 园区游玩"));
        lists.add(new kecheng("星期三 2014-02-12","09:00 英文","11:00 体育","02:00 游戏","04:00 园区游玩"));
        lists.add(new kecheng("星期四 2014-02-13","09:00 语文","11:00 游戏","02:00 游戏","04:00 园区游玩"));
        lists.add(new kecheng("星期五 2014-02-14","09:00 音乐","11:00 数学","02:00 游戏","04:00 园区游玩"));
        lists.add(new kecheng("星期六 2014-02-15","09:00 语文","11:00 数学","02:00 游戏","04:00 园区游玩"));
        lists.add(new kecheng("星期日 2014-02-16","09:00 语文","11:00 游玩","02:00 游戏","04:00 园区游玩"));
		findViewById();
		setListener();

	}

	private void findViewById() {
		mMenu = (Button) mHome.findViewById(R.id.home_menu);
        kechenglstv = (ListView) mHome.findViewById(R.id.kechenglstv);
        adapter = new KechengAdapter(lists, mContext);
        kechenglstv.setAdapter(adapter);
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
}
