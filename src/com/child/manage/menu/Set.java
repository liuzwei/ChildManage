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
import android.widget.*;
import com.child.manage.ChildApplication;
import com.child.manage.R;
import com.child.manage.anim.UgcAnimations;
import com.child.manage.base.FlipperLayout;
import com.child.manage.ui.*;
import com.child.manage.util.ActivityForResultUtil;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 菜单首页类
 *
 * @author rendongwei
 */
public class Set implements OnClickListener {
    private Button mMenu;
    private Context mContext;
    private Activity mActivity;
    private ChildApplication mKXApplication;
    private View mSet;


    private FlipperLayout.OnOpenListener mOnOpenListener;

    private LinearLayout setzh;
    private LinearLayout setpass;
    private LinearLayout setbaby;
    private LinearLayout setemail;
    private LinearLayout aboutus;

    public Set(Context context, Activity activity, ChildApplication application) {
        mContext = context;
        mActivity = activity;
        mKXApplication = application;
        mSet = LayoutInflater.from(context).inflate(R.layout.set, null);

        findViewById();
        setListener();

    }

    private void findViewById() {
        mMenu = (Button) mSet.findViewById(R.id.set_menu);
        setzh = (LinearLayout) mSet.findViewById(R.id.setzh);
        setzh.setOnClickListener(this);
        setpass = (LinearLayout) mSet.findViewById(R.id.setpass);
        setpass.setOnClickListener(this);
        setbaby = (LinearLayout) mSet.findViewById(R.id.setbaby);
        setbaby.setOnClickListener(this);
        setemail = (LinearLayout) mSet.findViewById(R.id.setemail);
        setemail.setOnClickListener(this);
        aboutus = (LinearLayout) mSet.findViewById(R.id.aboutus);
        aboutus.setOnClickListener(this);
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
        return mSet;
    }

    public void setOnOpenListener(FlipperLayout.OnOpenListener onOpenListener) {
        mOnOpenListener = onOpenListener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setzh:
                Intent zhanghao = new Intent(mContext, SettingZhActivity.class);
                mContext.startActivity(zhanghao);
                break;
            case R.id.setpass:
                Intent pass = new Intent(mContext, SettingPassActivity.class);
                mContext.startActivity(pass);
                break;
            case R.id.setbaby:
                Intent baby = new Intent(mContext, SettingBabyActivity.class);
                mContext.startActivity(baby);
                break;
            case R.id.setemail:
                Intent email = new Intent(mContext, SettingEmailActivity.class);
                mContext.startActivity(email);
                break;
            case R.id.aboutus:
                Intent about = new Intent(mContext, SettingAboutActivity.class);
                mContext.startActivity(about);
                break;
        }
    }
}
