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
        lists.add(new yuer("http://www.liangxunwang.com:80/show/boke/xc/0529980/1216090847377.jpg","宝宝不能承受之重—解读维生素AD缺乏症","2014-01-01 08:00"));
        lists.add(new yuer("http://www.liangxunwang.com:80/show/boke/xc/0529980/1216090844226.jpg","信八达软件是最好的软件app","2014-12-23 20:00"));
        lists.add(new yuer("http://www.liangxunwang.com:80/show/boke/xc/0529980/1216090844226.jpg","谁扼杀孩子创造力","2014-12-12 20:00"));
        lists.add(new yuer("http://www.liangxunwang.com:80/show/boke/xc/0529980/1216090844226.jpg","运动胎教注意事项","2014-12-22 20:00"));
        lists.add(new yuer("http://www.liangxunwang.com:80/show/boke/xc/0529980/1216090844226.jpg","做春节礼貌好宝宝","2014-12-23 20:00"));
        lists.add(new yuer("http://www.liangxunwang.com:80/show/boke/xc/0529980/1216090844226.jpg","从小培养注意力","2014-12-13 20:00"));
        lists.add(new yuer("http://www.liangxunwang.com:80/show/boke/xc/0529980/1216090844226.jpg","明星爸妈育儿宝典","2014-2-23 20:00"));
        lists.add(new yuer("http://www.liangxunwang.com:80/show/boke/xc/0529980/1216090844226.jpg","亲子共读绘本精选","2014-11-23 20:00"));
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
