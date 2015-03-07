package com.child.manage.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.*;
import com.child.manage.ChildApplication;
import com.child.manage.R;
import com.child.manage.adapter.AnimateFirstDisplayListener;
import com.child.manage.base.ActivityTack;
import com.child.manage.base.BaseActivity;
import com.child.manage.entity.Account;
import com.child.manage.menu.*;
import com.child.manage.util.face.FaceConversionUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

public class CenterActivity extends BaseActivity implements
        View.OnClickListener{
    private Button center_back_menu;//返回按钮
    private SlideMenu slideMenu;
    private TextView leftmenu_jieshao;//园区介绍
    private TextView leftmenu_message;//互动天地
    private TextView leftmenu_video;//即时动态
    private TextView schoolCar;//校车通知
    private TextView leftmenu_yuanwai;//园外天地
    private TextView leftmenu_notice;//公告栏
    private TextView leftmenu_set;//设置

    private LinearLayout leftmenu_user;

    private ImageView center_about;
    private ImageView center_chat;
    private ImageView center_location;
    private ImageView center_yuanwai;
    private ImageView center_video;
    private ImageView center_notice;

    private long waitTime = 2000;
    private long touchTime = 0;
    private Account account;
    String identity ="";

    private ImageView desktop_avatar;
    private TextView left_name;
    private TextView left_guanxi;

    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        account = getGson().fromJson(sp.getString(Constants.ACCOUNT_KEY, ""), Account.class);
        identity = getGson().fromJson(sp.getString(Constants.IDENTITY, ""), String.class);
        initView();
        if(identity.equals("0")){
            left_guanxi.setText("父子");
            left_name.setText(account.getF_name());
            imageLoader.displayImage(account.getF_cover(), desktop_avatar, ChildApplication.options, animateFirstListener);
        }
        if(identity.equals("1")){
            left_guanxi.setText("母子");
            left_name.setText(account.getM_name());
            imageLoader.displayImage(account.getM_cover(), desktop_avatar, ChildApplication.options, animateFirstListener);
        }
        if(account.getIs_teacher().equals("1")){
            left_guanxi.setText("教师");
            left_name.setText(account.getNick_name());
            imageLoader.displayImage(account.getCover(), desktop_avatar, ChildApplication.options, animateFirstListener);
        }

        //初始化加载表情
        new Thread(new Runnable() {
            @Override
            public void run() {
                FaceConversionUtil.getInstace().getFileText(getApplication());
            }
        }).start();
    }

    private void initView() {
        center_back_menu = (Button) this.findViewById(R.id.center_back_menu);
        center_back_menu.setOnClickListener(this);
        center_back_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (slideMenu.isMainScreenShowing()) {
                    slideMenu.openMenu();
                } else {
                    slideMenu.closeMenu();
                }
            }
        });
        slideMenu = (SlideMenu) findViewById(R.id.slide_menu);
        leftmenu_user = (LinearLayout) slideMenu.findViewById(R.id.leftmenu_user);
        leftmenu_user.setOnClickListener(this);
        leftmenu_jieshao = (TextView) slideMenu.findViewById(R.id.leftmenu_jieshao);
        leftmenu_message = (TextView) slideMenu.findViewById(R.id.leftmenu_message);
        leftmenu_video = (TextView) slideMenu.findViewById(R.id.leftmenu_video);
        schoolCar = (TextView) slideMenu.findViewById(R.id.leftmenu_school_car);
        leftmenu_yuanwai = (TextView) slideMenu.findViewById(R.id.leftmenu_yuanwai);
        leftmenu_notice = (TextView) slideMenu.findViewById(R.id.leftmenu_notice);
        leftmenu_set = (TextView) slideMenu.findViewById(R.id.leftmenu_set);

        leftmenu_jieshao.setOnClickListener(this);
        leftmenu_message.setOnClickListener(this);
        leftmenu_video.setOnClickListener(this);
        schoolCar.setOnClickListener(this);
        leftmenu_yuanwai.setOnClickListener(this);
        leftmenu_notice.setOnClickListener(this);
        leftmenu_set.setOnClickListener(this);

        center_about = (ImageView) slideMenu.findViewById(R.id.center_about);
        center_chat = (ImageView) slideMenu.findViewById(R.id.center_chat);
        center_location = (ImageView) slideMenu.findViewById(R.id.center_location);
        center_yuanwai = (ImageView) slideMenu.findViewById(R.id.center_yuanwai);
        center_video = (ImageView) slideMenu.findViewById(R.id.center_video);
        center_notice = (ImageView) slideMenu.findViewById(R.id.center_notice);
        center_about.setOnClickListener(this);
        center_chat.setOnClickListener(this);
        center_location.setOnClickListener(this);
        center_yuanwai.setOnClickListener(this);
        center_video.setOnClickListener(this);
        center_notice.setOnClickListener(this);

        desktop_avatar = (ImageView) this.findViewById(R.id.desktop_avatar);
        left_name = (TextView) this.findViewById(R.id.left_name);
        left_guanxi = (TextView) this.findViewById(R.id.left_guanxi);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.leftmenu_user://
                if (slideMenu.isMainScreenShowing()) {
                    slideMenu.openMenu();
                } else {
                    slideMenu.closeMenu();
                }
                break;
            case R.id.leftmenu_jieshao://园区介绍
                Intent about = new Intent(CenterActivity.this, About.class);
                startActivity(about);
                break;
            case R.id.leftmenu_message://交互信息
                Intent jiaohu = new Intent(CenterActivity.this, JiaohuActivity.class);
                startActivity(jiaohu);
                break;
            case R.id.leftmenu_video://即时动态
                Intent photo = new Intent(this, Video.class);
                startActivity(photo);
                break;
            case R.id.leftmenu_school_car://校车通知
                if(account.getIs_teacher().equals("1")){//是教师登陆的话
                    Intent schoolbus = new Intent(CenterActivity.this, OpenglDemo.class);
                    startActivity(schoolbus);
                }else{
                    //家长登陆的话
                    Intent schoolbus = new Intent(CenterActivity.this, SchoolBusActivityFather.class);
                    startActivity(schoolbus);
                }
                break;
            case R.id.leftmenu_yuanwai://园外
                Intent txl = new Intent(CenterActivity.this, YuanWai.class);
                startActivity(txl);
                break;
            case R.id.leftmenu_notice://通知
                Intent yuying = new Intent(CenterActivity.this, Notice.class);
                startActivity(yuying);
                break;
            case R.id.leftmenu_set://设置
                Intent set = new Intent(CenterActivity.this, Set.class);
                startActivity(set);
                break;

            case R.id.center_about:
                Intent about1 = new Intent(CenterActivity.this, About.class);
                startActivity(about1);
                break;
            case R.id.center_chat:
                Intent jiaohu1 = new Intent(CenterActivity.this, JiaohuActivity.class);
                startActivity(jiaohu1);
                break;
            case R.id.center_location:
                if(account.getIs_teacher().equals("1")){//是教师登陆的话
                    Intent schoolbus = new Intent(CenterActivity.this, OpenglDemo.class);
                    startActivity(schoolbus);
                }else{
                    //家长登陆的话
                    Intent schoolbus = new Intent(CenterActivity.this, SchoolBusActivityFather.class);
                    startActivity(schoolbus);
                }
                break;
            case R.id.center_yuanwai:
                Intent txl1 = new Intent(CenterActivity.this, YuanWai.class);
                startActivity(txl1);
                break;
            case R.id.center_video:
                Intent photo1 = new Intent(this, Video.class);
                startActivity(photo1);
                break;
            case R.id.center_notice:
                Intent yuying1 = new Intent(CenterActivity.this, Notice.class);
                startActivity(yuying1);
                break;
        }
        //响应动作后关闭按钮
        slideMenu.closeMenu();
    }

    /**
     * 再摁退出程序
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode){
            long currentTime = System.currentTimeMillis();
            if ((currentTime - touchTime) >= waitTime){
                Toast.makeText(getContext(), "再摁退出登录", Toast.LENGTH_SHORT).show();
                touchTime = currentTime;
            }else {
                ActivityTack.getInstanse().exit(getContext());
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
