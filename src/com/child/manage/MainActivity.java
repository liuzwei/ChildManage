package com.child.manage;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.ViewGroup;
import android.widget.Toast;
import com.child.manage.base.BaseActivity;
import com.child.manage.base.FlipperLayout;
import com.child.manage.entity.Account;
import com.child.manage.menu.*;
import com.child.manage.ui.Constants;
import com.child.manage.util.ActivityForResultUtil;
import com.child.manage.util.PhotoUtil;
import com.child.manage.util.ViewUtil;

import java.io.File;

public class MainActivity extends BaseActivity implements FlipperLayout.OnOpenListener {
    /**
     * 当前显示内容的容器(继承于ViewGroup)
     */
    private FlipperLayout mRoot;
    /**
     * 菜单界面
     */
    private Desktop mDesktop;

    /**
     * 园区介绍
     */
    private About about;
    /**
     * 互动天地
     */
    private Home mHome;
    /**
     * 即时动态
     */
    private Video dianping;
    /**
     * 给老师留言
     */
    private Liuyan liuyan;
    /**
     * 校车位置
     */
    private Baybayset video;
    /**
     * 育儿知识
     */
    private YuanWai yuer;
    /**
     * 每周课程
     */
    private Notice notice;
    /**
     * 宝宝食谱
     */
    private Shipu shipu;
    /**
     * 设置
     */
    private Set set;

    /**
     * 当前显示的View的编号
     */
    private int mViewPosition;
    /**
     * 退出时间
     */
    private long mExitTime;
    /**
     * 退出间隔
     */
    private static final int INTERVAL = 2000;
    /**
     *
     */
    public static Activity mInstance;
    private Account account;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        account = getGson().fromJson(sp.getString(Constants.ACCOUNT_KEY, ""), Account.class);
        String name = getGson().fromJson(sp.getString(Constants.USERNAME_KEY, ""), String.class);
        /**
         * 创建容器,并设置全屏大小
         */
        mRoot = new FlipperLayout(this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
                ViewGroup.LayoutParams.FILL_PARENT);
        mRoot.setLayoutParams(params);
        /**
         * 创建菜单界面和内容首页界面,并添加到容器中,用于初始显示
         */
        mDesktop = new Desktop(this, this, childApplication, name);
//        mHome = new Home(this, this, childApplication);
//        mRoot.addView(mDesktop.getView(), params);
//        mRoot.addView(mHome.getView(), params);

        about = new About(this, this, childApplication);
        mRoot.addView(mDesktop.getView(), params);
        mRoot.addView(about.getView(), params);
        setContentView(mRoot);
        setListener();
        mInstance = this;
    }

    /**
     * UI事件监听
     */
    private void setListener() {
        about.setOnOpenListener(this);
        /**
         * 监听菜单界面切换显示内容(onChangeViewListener接口在Desktop中定义)
         */
        mDesktop.setOnChangeViewListener(new Desktop.onChangeViewListener() {

            public void onChangeView(int arg0) {
                mViewPosition = arg0;
                switch (arg0) {
                    case ViewUtil.USER:

                        break;
                    case ViewUtil.ABOUT:
                        mRoot.close(about.getView());
                        break;
                    case ViewUtil.DIANPING:
//                        if (dianping == null) {
//                            dianping = new Video(MainActivity.this,
//                                    MainActivity.this, childApplication);
//                            dianping.setOnOpenListener(MainActivity.this);
//                        }
//                        mRoot.close(dianping.getView());
                        Intent video =  new Intent(getContext(), Video.class);
                        startActivity(video);
                        break;
                    case ViewUtil.HOME:
                        if (mHome == null) {
                            mHome = new Home(MainActivity.this, MainActivity.this, childApplication);
                            mHome.setOnOpenListener(MainActivity.this);
                        }
                        mRoot.close(mHome.getView());
                        break;
                    case ViewUtil.SHIPIN:
//                        if (video == null) {
//                            video = new Baybayset(MainActivity.this, MainActivity.this, childApplication);
//                            video.setOnOpenListener(MainActivity.this);
//                        }
//                        mRoot.close(video.getView());
                        break;
                    case ViewUtil.YUER:
                        if (yuer == null) {
                            yuer = new YuanWai(mRequestQueue ,MainActivity.this, MainActivity.this, childApplication);
                            yuer.setOnOpenListener(MainActivity.this);
                        }
                        mRoot.close(yuer.getView());
                        break;
                    case ViewUtil.KECHENG:
                        if (notice == null) {
                            notice = new Notice(account,mRequestQueue,MainActivity.this, MainActivity.this, childApplication);
                            notice.setOnOpenListener(MainActivity.this);
                        }
                        mRoot.close(notice.getView());
                        break;
//                    case ViewUtil.SHIPU:
//                        if(shipu == null){
//                            shipu = new Shipu(MainActivity.this,MainActivity.this,childApplication);
//                            shipu.setOnOpenListener(MainActivity.this);
//                        }
//                        mRoot.close(shipu.getView());
//                        break;
                    case ViewUtil.SET://设置
                        if (set == null) {
                            set = new Set(MainActivity.this,
                                    MainActivity.this, childApplication);
                            set.setOnOpenListener(MainActivity.this);
                        }
                        mRoot.close(set.getView());
                        break;
                    default:
                        break;
                }
            }
        });
        /**
         * 隐藏path菜单
         */
        mRoot.setOnUgcDismissListener(new FlipperLayout.onUgcDismissListener() {

            public void dismiss() {
                switch (mViewPosition) {
                    case ViewUtil.USER:
//                        mViewPosition.dismissUgc();
                        break;

                    case ViewUtil.HOME:
                        mHome.dismissUgc();
                        break;
                }
            }
        });
        /**
         * 显示path菜单
         */
        mRoot.setOnUgcShowListener(new FlipperLayout.onUgcShowListener() {

            public void show() {
                switch (mViewPosition) {
                    case ViewUtil.USER:
//                        mUserInfo.showUgc();
                        break;

                    case ViewUtil.HOME:
                        mHome.showUgc();
                        break;
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            /**
             * 通过照相修改头像
             */
            case ActivityForResultUtil.REQUESTCODE_UPLOADAVATAR_CAMERA:
                if (resultCode == RESULT_OK) {
                    if (!Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED)) {
                        Toast.makeText(this, "SD不可用", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    File file = new File(childApplication.mUploadPhotoPath);
                    startPhotoZoom(Uri.fromFile(file));
                } else {
                    Toast.makeText(this, "取消上传", Toast.LENGTH_SHORT).show();
                }
                break;
            /**
             * 通过本地修改头像
             */
            case ActivityForResultUtil.REQUESTCODE_UPLOADAVATAR_LOCATION:
                Uri uri = null;
                if (data == null) {
                    Toast.makeText(this, "取消上传", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (resultCode == RESULT_OK) {
                    if (!Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED)) {
                        Toast.makeText(this, "SD不可用", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    uri = data.getData();
                    startPhotoZoom(uri);
                } else {
                    Toast.makeText(this, "照片获取失败", Toast.LENGTH_SHORT).show();
                }
                break;
            /**
             * 裁剪修改的头像
             */
            case ActivityForResultUtil.REQUESTCODE_UPLOADAVATAR_CROP:
                if (data == null) {
                    Toast.makeText(this, "取消上传", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    saveCropPhoto(data);
                }
                break;
            /**
             * 通过照相上传图片
             */
            case ActivityForResultUtil.REQUESTCODE_UPLOADPHOTO_CAMERA:
                if (resultCode == RESULT_OK) {
                    if (!Environment.getExternalStorageState().equals(
                            Environment.MEDIA_MOUNTED)) {
                        Toast.makeText(this, "SD不可用", Toast.LENGTH_SHORT).show();
                        return;
                    }
//                    Intent intent = new Intent();
//                    intent.setClass(MainActivity.this, ImageFilterActivity.class);
//                    String path = PhotoUtil.saveToLocal(PhotoUtil.createBitmap(
//                            mKXApplication.mUploadPhotoPath, mScreenWidth,
//                            mScreenHeight));
//                    intent.putExtra("path", path);
//                    startActivity(intent);
                } else {
                    Toast.makeText(this, "取消上传", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 系统裁剪照片
     *
     * @param uri
     */
    private void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        intent.putExtra("scale", true);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);
        startActivityForResult(intent,
                ActivityForResultUtil.REQUESTCODE_UPLOADAVATAR_CROP);
    }

    /**
     * 保存裁剪的照片
     *
     * @param data
     */
    private void saveCropPhoto(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Bitmap bitmap = extras.getParcelable("data");
            bitmap = PhotoUtil.toRoundCorner(bitmap, 15);
            if (bitmap != null) {
                uploadPhoto(bitmap);
            }
        } else {
            Toast.makeText(this, "获取裁剪照片错误", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 更新头像
     */
    private void uploadPhoto(Bitmap bitmap) {
//        mUserInfo.setAvatar(bitmap);
        mDesktop.setAvatar(bitmap);
    }

    /**
     * 返回键监听
     */
    public void onBackPressed() {
        /**
         * 如果界面的path菜单没有关闭时,先将path菜单关闭,否则则判断两次返回时间间隔,小于两秒则退出程序
         */
        if (mRoot.getScreenState() == FlipperLayout.SCREEN_STATE_OPEN) {
//            if (mDesktop.getUgcIsShowing()) {
//                mDesktop.closeUgc();
//            } else {
            exit();
//            }
        } else {
            switch (mViewPosition) {
                case ViewUtil.USER:
//                    if (mUserInfo.getUgcIsShowing()) {
//                        mUserInfo.closeUgc();
//                    } else {
//                        exit();
//                    }
                    break;
                case ViewUtil.HOME:
                    if (mHome.getUgcIsShowing()) {
                        mHome.closeUgc();
                    } else {
                        exit();
                    }
                    break;
                default:
                    exit();
                    break;
            }

        }

    }

    /**
     * 判断两次返回时间间隔,小于两秒则退出程序
     */
    private void exit() {
        if (System.currentTimeMillis() - mExitTime > INTERVAL) {
            Toast.makeText(this, "再按一次返回键,可直接退出程序", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
    }

    public void open() {
        if (mRoot.getScreenState() == FlipperLayout.SCREEN_STATE_CLOSE) {
            mRoot.open();
        }
    }


}
