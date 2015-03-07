package com.child.manage.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.child.manage.adapter.AnimateFirstDisplayListener;
import com.child.manage.anim.UgcAnimations;
import com.child.manage.base.BaseActivity;
import com.child.manage.base.FlipperLayout;
import com.child.manage.entity.Account;
import com.child.manage.ui.*;
import com.child.manage.util.ActivityForResultUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 菜单首页类
 *
 * @author rendongwei
 */
public class Set extends BaseActivity implements OnClickListener {
    private Button mMenu;
    private LinearLayout setzh;
    private LinearLayout setpass;
    private LinearLayout setbaby;
    private LinearLayout setemail;
    private LinearLayout aboutus;
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private  ImageView tx;
    private  TextView set_name;
    private Account account;
    String identity ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set);
        findViewById();
        account = getGson().fromJson(sp.getString(Constants.ACCOUNT_KEY, ""), Account.class);
        identity = getGson().fromJson(sp.getString(Constants.IDENTITY, ""), String.class);
        if(identity.equals("0")){
            set_name.setText( account.getF_name());
            imageLoader.displayImage(account.getF_cover(), tx, ChildApplication.options, animateFirstListener);
        }
        if(identity.equals("1")){
            set_name.setText( account.getM_name());
            imageLoader.displayImage(account.getM_cover(), tx, ChildApplication.options, animateFirstListener);
        }
        if(account.getIs_teacher().equals("1")){
            setbaby.setVisibility(View.GONE);
            set_name.setText(account.getNick_name());
            imageLoader.displayImage(account.getCover(), tx, ChildApplication.options, animateFirstListener);
        }

    }

    private void findViewById() {

        mMenu = (Button) this.findViewById(R.id.set_menu);
        mMenu.setOnClickListener(this);
        setzh = (LinearLayout) this.findViewById(R.id.setzh);
        setzh.setOnClickListener(this);
        setpass = (LinearLayout) this.findViewById(R.id.setpass);
        setpass.setOnClickListener(this);
        setbaby = (LinearLayout) this.findViewById(R.id.setbaby);
        setbaby.setOnClickListener(this);
        setemail = (LinearLayout) this.findViewById(R.id.setemail);
        setemail.setOnClickListener(this);
        aboutus = (LinearLayout) this.findViewById(R.id.aboutus);
        aboutus.setOnClickListener(this);

        tx = (ImageView) this.findViewById(R.id.tx);
        tx.setOnClickListener(this);
        set_name = (TextView) this.findViewById(R.id.set_name);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_menu:
                finish();
                break;
            case R.id.setzh:
                Intent zhanghao = new Intent(getContext(), SettingMobileActivity.class);
                startActivity(zhanghao);
                break;
            case R.id.setpass:
                Intent pass = new Intent(getContext(), SettingPassActivity.class);
                startActivity(pass);
                break;
            case R.id.setbaby:
                Intent babySet = new Intent(getContext(), BabySettingActivity.class);
                startActivity(babySet);
                break;
            case R.id.setemail:
                Intent email = new Intent(getContext(), SettingEmailActivity.class);
                startActivity(email);
                break;
            case R.id.aboutus:
                Intent about = new Intent(getContext(), SettingAboutActivity.class);
                startActivity(about);
                break;
            case R.id.tx:
                Intent mumSet = new Intent(getContext(), MumSettingActivity.class);
                startActivity(mumSet);
                break;
        }
    }
}
