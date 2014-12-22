package com.child.manage.menu;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.child.manage.ChildApplication;
import com.child.manage.R;
import com.child.manage.base.FlipperLayout;

/**
 * 菜单首页类
 * 
 * @author rendongwei
 * 
 */
public class About {

    private Button mMenu;
	private Context mContext;
	private Activity mActivity;
	private ChildApplication mKXApplication;
	private View mAbout;
	private FlipperLayout.OnOpenListener mOnOpenListener;




	public About(Context context, Activity activity, ChildApplication application) {
		mContext = context;
		mActivity = activity;
		mKXApplication = application;
        mAbout = LayoutInflater.from(context).inflate(R.layout.about, null);
		findViewById();
		setListener();

	}

	private void findViewById() {
		mMenu = (Button) mAbout.findViewById(R.id.about_menu);
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
		return mAbout;
	}

	public void setOnOpenListener(FlipperLayout.OnOpenListener onOpenListener) {
		mOnOpenListener = onOpenListener;
	}
}
