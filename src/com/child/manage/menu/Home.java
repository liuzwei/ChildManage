package com.child.manage.menu;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout.LayoutParams;
import com.child.manage.ChildApplication;
import com.child.manage.R;
import com.child.manage.adapter.JiaohuAdapter;
import com.child.manage.anim.UgcAnimations;
import com.child.manage.base.FlipperLayout;
import com.child.manage.entity.AccountMessage;
import com.child.manage.ui.*;
import com.child.manage.util.ActivityForResultUtil;
import org.json.JSONArray;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 菜单首页类
 *
 * @author rendongwei
 */
public class Home {
    private Button mMenu;
    private View mUgcView;
    private Context mContext;
    private Activity mActivity;
    private ChildApplication mKXApplication;
    private View mHome;
    private View mPopView;
    private RelativeLayout mUgcLayout;
    private ImageView mUgc;
    private ImageView mUgcBg;
    private ImageView mUgcVoice;
    private ImageView mUgcPhoto;
    private ImageView mUgcRecord;
    private ImageView mUgcLbs;
    private FlipperLayout.OnOpenListener mOnOpenListener;


    /**
     * 判断当前的path菜单是否已经显示
     */
    private boolean mUgcIsShowing = false;


    private ListView listView;
    private List<AccountMessage> list = new ArrayList<AccountMessage>();
    private JiaohuAdapter adapter;
//    private TextView publishAll;//群发消息

    /**
     * {"uid":"101",
     * "name":"\u4e07\u8001\u5e08",
     * "group_id":"3","
     * user_type":" ","
     * cover":"http:\/\/yey.xqb668.com\/Uploads\/cover\/101_0.jpg","
     * dept":"\u73ed\u4e3b\u4efb"},{
     * <p/>
     * "uid":"90",
     * "name":"\u8a79\u8001\u5e08"
     * ,"group_id":"3","
     * user_type":" ",
     * "cover":"http:\/\/yey.xqb668.com\/Uploads\/cover\/90_0.jpg"
     * ,"dept":"\u8001\u5e2b"},{
     * <p/>
     * "uid":"89","
     * name":"teacher"
     * ,"group_id":"3",
     * "user_type":" ",
     * "cover":"http:\/\/yey.xqb668.com\/Uploads\/cover\/89_0.jpg",
     * "dept":"\u8001\u5e2b"}
     * *
     */

    public Home(Context context, Activity activity, ChildApplication application) {
        list.add(new AccountMessage("100", "http://yey.xqb668.com//Uploads//cover//89_0.jpg", "Tom", "老师", 0, "Hello welcome to xinbada app", "2014-10-20"));
        list.add(new AccountMessage("89", "http:\\/\\/yey.xqb668.com\\/Uploads\\/cover\\/90_0.jpg", "Teacher", "老师", 0, "你好", "2014-10-20"));
        list.add(new AccountMessage("90", "http://yey.xqb668.com//Uploads//cover//89_0.jpg", "Jim", "老师", 0, "haha haha", "2014-10-20"));
        list.add(new AccountMessage("101", "http:\\/\\/yey.xqb668.com\\/Uploads\\/cover\\/101_0.jpg", "Lucy", "老师", 0, "你好", "2014-10-20"));
        mContext = context;
        mActivity = activity;
        mKXApplication = application;
        mHome = LayoutInflater.from(context).inflate(R.layout.home, null);
        mPopView = LayoutInflater.from(context).inflate(
                R.layout.home_popupwindow, null);
        findViewById();
        adapter = new JiaohuAdapter(list, mContext);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent chat = new Intent(mContext, ChatActivity.class);
                chat.putExtra(Constants.ACCOUNT_MESSAGE, list.get(position));
                mContext.startActivity(chat);
            }
        });

        setListener();

    }

    private void findViewById() {
        mMenu = (Button) mHome.findViewById(R.id.home_menu);
        mUgcView = (View) mHome.findViewById(R.id.home_ugc1);
        mUgcLayout = (RelativeLayout) mUgcView.findViewById(R.id.ugc_layout);
        mUgc = (ImageView) mUgcView.findViewById(R.id.ugc);
        mUgcBg = (ImageView) mUgcView.findViewById(R.id.ugc_bg);
        mUgcVoice = (ImageView) mUgcView.findViewById(R.id.ugc_voice);
        mUgcPhoto = (ImageView) mUgcView.findViewById(R.id.ugc_photo);
        mUgcRecord = (ImageView) mUgcView.findViewById(R.id.ugc_record);
        mUgcLbs = (ImageView) mUgcView.findViewById(R.id.ugc_lbs);
        listView = (ListView) mHome.findViewById(R.id.jiaohu_lstv);
    }

    private void setListener() {
        mMenu.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                if (mOnOpenListener != null) {
                    mOnOpenListener.open();
                }
            }
        });

        // Path监听
        mUgcView.setOnTouchListener(new OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // 判断是否已经显示,显示则关闭并隐藏
                if (mUgcIsShowing) {
                    mUgcIsShowing = false;
                    UgcAnimations.startCloseAnimation(mUgcLayout, mUgcBg, mUgc,
                            500);
                    return true;
                }
                return false;
            }
        });
        // Path监听
        mUgc.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // 判断是否显示,已经显示则隐藏,否则则显示
                mUgcIsShowing = !mUgcIsShowing;
                if (mUgcIsShowing) {
                    UgcAnimations.startOpenAnimation(mUgcLayout, mUgcBg, mUgc,
                            500);
                } else {
                    UgcAnimations.startCloseAnimation(mUgcLayout, mUgcBg, mUgc,
                            500);
                }
            }
        });
        // Path 语音按钮监听
        mUgcVoice.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Animation anim = UgcAnimations.clickAnimation(500);
                anim.setAnimationListener(new AnimationListener() {

                    public void onAnimationStart(Animation animation) {

                    }

                    public void onAnimationRepeat(Animation animation) {

                    }

                    public void onAnimationEnd(Animation animation) {
                        mContext.startActivity(new Intent(mContext,
                                VoiceActivity.class));
                        closeUgc();
                    }
                });
                mUgcVoice.startAnimation(anim);
            }
        });
        // Path 拍照按钮监听
        mUgcPhoto.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Animation anim = UgcAnimations.clickAnimation(500);
                anim.setAnimationListener(new AnimationListener() {

                    public void onAnimationStart(Animation animation) {

                    }

                    public void onAnimationRepeat(Animation animation) {

                    }

                    public void onAnimationEnd(Animation animation) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File dir = new File("/sdcard/KaiXin/Camera/");
                        if (!dir.exists()) {
                            dir.mkdirs();
                        }
                        mKXApplication.mUploadPhotoPath = "/sdcard/KaiXin/Camera/"
                                + UUID.randomUUID().toString();
                        File file = new File(
                                mKXApplication.mUploadPhotoPath);
                        if (!file.exists()) {
                            try {
                                file.createNewFile();
                            } catch (IOException e) {

                            }
                        }
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(file));
                        mActivity
                                .startActivityForResult(
                                        intent,
                                        ActivityForResultUtil.REQUESTCODE_UPLOADPHOTO_CAMERA);
                        closeUgc();
                    }
                });
                mUgcPhoto.startAnimation(anim);
            }
        });
        // Path 记录按钮监听
        mUgcRecord.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Animation anim = UgcAnimations.clickAnimation(500);
                anim.setAnimationListener(new AnimationListener() {

                    public void onAnimationStart(Animation animation) {

                    }

                    public void onAnimationRepeat(Animation animation) {

                    }

                    public void onAnimationEnd(Animation animation) {
                        mContext.startActivity(new Intent(mContext,
                                WriteRecordActivity.class));
                        closeUgc();
                    }
                });
                mUgcRecord.startAnimation(anim);
            }
        });
        // Path 签到按钮监听
        mUgcLbs.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                Animation anim = UgcAnimations.clickAnimation(500);
                anim.setAnimationListener(new AnimationListener() {

                    public void onAnimationStart(Animation animation) {

                    }

                    public void onAnimationRepeat(Animation animation) {

                    }

                    public void onAnimationEnd(Animation animation) {
                        mContext.startActivity(new Intent(mContext,
                                CheckInActivity.class));
                        closeUgc();
                    }
                });
                mUgcLbs.startAnimation(anim);
            }
        });
    }

    /**
     * 获取Path菜单显示状态
     *
     * @return 显示状态
     */
    public boolean getUgcIsShowing() {
        return mUgcIsShowing;
    }

    /**
     * 关闭Path菜单
     */
    public void closeUgc() {
        mUgcIsShowing = false;
        UgcAnimations.startCloseAnimation(mUgcLayout, mUgcBg, mUgc, 500);
    }

    /**
     * 显示Path菜单
     */
    public void showUgc() {
        if (mUgcView != null) {
            mUgcView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 关闭Path菜单
     */
    public void dismissUgc() {
        if (mUgcView != null) {
            mUgcView.setVisibility(View.GONE);
        }
    }

    public View getView() {
        return mHome;
    }

    public void setOnOpenListener(FlipperLayout.OnOpenListener onOpenListener) {
        mOnOpenListener = onOpenListener;
    }
}
