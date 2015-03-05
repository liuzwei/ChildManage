package com.child.manage.menu;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import com.child.manage.ChildApplication;
import com.child.manage.R;
import com.child.manage.adapter.AnimateFirstDisplayListener;
import com.child.manage.anim.UgcAnimations;
import com.child.manage.entity.Account;
import com.child.manage.ui.*;
import com.child.manage.util.ActivityForResultUtil;
import com.child.manage.util.PhotoUtil;
import com.child.manage.util.ViewUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 菜单界面
 *
 * @author rendongwei
 */
public class Desktop {
    private Context mContext;
    private Activity mActivity;
    private ChildApplication mKXApplication;
    private String mZname;
    /**
     * 当前界面的View
     */
    private View mDesktop;
    /**
     * 以下为控件,自己查看布局文件
     */
    private LinearLayout mWallpager;
    private RelativeLayout mTopLayout;
    private ImageView mAvatar;
    private TextView mName;
    private ListView mDisplay;

    /**
     * 桌面适配器
     */
    private DesktopAdapter mAdapter;
    /**
     * 判断当前的path菜单是否已经显示
     */
    private boolean mUgcIsShowing = false;
    /**
     * 接口对象,用来修改显示的View
     */
    private onChangeViewListener mOnChangeViewListener;

    private TextView desktop_name;//账号
    private Account mAccount;
    private String midentity;
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public Desktop(String identity,Account account,Context context, Activity activity, ChildApplication application) {
        mContext = context;
        mActivity = activity;
        midentity = identity;
        mAccount= account;
        mKXApplication = application;
        // 绑定布局到当前View
        mDesktop = LayoutInflater.from(context).inflate(R.layout.desktop, null);
        findViewById();
        setListener();
        init();
        if(midentity.equals("0")){
            desktop_name.setText(account.getF_name());
            imageLoader.displayImage(mAccount.getF_cover(), mAvatar, ChildApplication.txOptions, animateFirstListener);
        }
        if(midentity.equals("1")){
            desktop_name.setText(account.getM_name());
            imageLoader.displayImage(mAccount.getM_cover(), mAvatar, ChildApplication.txOptions, animateFirstListener);
        }
        if(account.getIs_teacher().equals("1")){
            desktop_name.setText(account.getNick_name());
            imageLoader.displayImage(mAccount.getCover(), mAvatar, ChildApplication.txOptions, animateFirstListener);
        }

    }

    /**
     * 绑定界面UI
     */
    private void findViewById() {
        mWallpager = (LinearLayout) mDesktop
                .findViewById(R.id.desktop_wallpager);
        mTopLayout = (RelativeLayout) mDesktop
                .findViewById(R.id.desktop_top_layout);
        mAvatar = (ImageView) mDesktop.findViewById(R.id.desktop_avatar);
        mName = (TextView) mDesktop.findViewById(R.id.desktop_name);
        mDisplay = (ListView) mDesktop.findViewById(R.id.desktop_display);

        desktop_name = (TextView) mDesktop.findViewById(R.id.desktop_name);


    }

    /**
     * UI事件监听
     */
    private void setListener() {
        // 头布局监听
        mTopLayout.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                // 切换界面View为用户首页
                if (mOnChangeViewListener != null) {
                    mOnChangeViewListener.onChangeView(ViewUtil.USER);
                    mAdapter.setChoose(-1);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

    }

    /**
     * 界面初始化
     */
    private void init() {
        /**
         * 设置墙纸、姓名、签名、头像以及菜单界面
         ;        */
        mAdapter = new DesktopAdapter(mContext);
        mDisplay.setAdapter(mAdapter);
    }


    /**
     * 修改头像
     *
     * @param bitmap 修改的头像
     */
    public void setAvatar(Bitmap bitmap) {
        mAvatar.setImageBitmap(bitmap);
    }

    /**
     * 界面修改方法
     *
     * @param onChangeViewListener
     */
    public void setOnChangeViewListener(
            onChangeViewListener onChangeViewListener) {
        mOnChangeViewListener = onChangeViewListener;
    }


    /**
     * 获取菜单界面
     *
     * @return 菜单界面的View
     */
    public View getView() {
        return mDesktop;
    }

    /**
     * 切换显示界面的接口
     *
     * @author rendongwei
     */
    public interface onChangeViewListener {
        public abstract void onChangeView(int arg0);
    }

    /**
     * 菜单适配器
     *
     * @author rendongwei
     */
    public class DesktopAdapter extends BaseAdapter {

        private Context mContext;
        private String[] mName = {"园区介绍", "互动天地", "即时动态", "宝宝位置", "园外天地", "公告栏",
                "设置"};
        private int[] mIcon = {R.drawable.left_hudong,
                R.drawable.left_baobaochegnzhangdianping, R.drawable.left_geilaoshily,
                R.drawable.left_yuancheng, R.drawable.left_yuerzhishi,
                R.drawable.baobaoshipu, R.drawable.left_set};
        private int[] mIconPressed = {R.drawable.left_hudong,
                R.drawable.left_baobaochegnzhangdianping, R.drawable.left_geilaoshily,
                R.drawable.left_yuancheng, R.drawable.left_yuerzhishi,
                R.drawable.baobaoshipu, R.drawable.left_set};
        private int mChoose = 0;

        public DesktopAdapter(Context context) {
            mContext = context;
        }

        public int getCount() {
            return 7;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public void setChoose(int choose) {
            mChoose = choose;
        }

        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.desktop_item, null);
                holder = new ViewHolder();
                holder.layout = (LinearLayout) convertView
                        .findViewById(R.id.desktop_item_layout);
                holder.icon = (ImageView) convertView
                        .findViewById(R.id.desktop_item_icon);
                holder.name = (TextView) convertView
                        .findViewById(R.id.desktop_item_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.name.setText(mName[position]);
            if (position == mChoose) {
                holder.name.setTextColor(Color.parseColor("#ffffffff"));
                holder.icon.setImageResource(mIconPressed[position]);
                holder.layout.setBackgroundColor(Color.parseColor("#20000000"));
            } else {
                holder.name.setTextColor(Color.parseColor("#7fffffff"));
                holder.icon.setImageResource(mIcon[position]);
                holder.layout.setBackgroundResource(Color
                        .parseColor("#00000000"));
            }
            convertView.setOnClickListener(new OnClickListener() {

                public void onClick(View v) {
                    if (mOnChangeViewListener != null) {
                        switch (position) {
                            case ViewUtil.ABOUT:
                                mOnChangeViewListener
                                        .onChangeView(ViewUtil.ABOUT);
                                break;
                            case ViewUtil.HOME:
                                mOnChangeViewListener.onChangeView(ViewUtil.HOME);
                                break;
                            case ViewUtil.DIANPING:
                                mOnChangeViewListener
                                        .onChangeView(ViewUtil.DIANPING);
                                break;
                            case ViewUtil.SHIPIN:
                                mOnChangeViewListener.onChangeView(ViewUtil.SHIPIN);
                                break;
                            case ViewUtil.YUER:
                                mOnChangeViewListener.onChangeView(ViewUtil.YUER);
                                break;
                            case ViewUtil.KECHENG:
                                mOnChangeViewListener.onChangeView(ViewUtil.KECHENG);
                                break;
                            case ViewUtil.SET:
                                mOnChangeViewListener.onChangeView(ViewUtil.SET);
                                break;
                            default:
                                mOnChangeViewListener.onChangeView(ViewUtil.HOME);
                                break;
                        }
                        mChoose = position;
                        notifyDataSetChanged();
                    }

                }
            });
            return convertView;
        }

        class ViewHolder {
            LinearLayout layout;
            ImageView icon;
            TextView name;
        }
    }

}
