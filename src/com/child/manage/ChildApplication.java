package com.child.manage;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Vibrator;
import android.util.Log;
import android.widget.TextView;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.child.manage.db.DBManager;
import com.child.manage.result.HomeResult;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.message.PushAgent;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author: ${zhanghailong}
 * Date: 2014/12/13
 * Time: 11:31
 * 类的功能、说明写在此处.
 */
public class ChildApplication extends Application {
    //----------------百度地图------------------
    public LocationClient mLocationClient;
    public GeofenceClient mGeofenceClient;
    public MyLocationListener mMyLocationListener;
    public TextView mLocationResult, logMsg;
    public TextView trigger, exit;
    public Vibrator mVibrator;
    public static Double dwlocation_latitude;
    public static Double dwlocation_lontitude;
    //-----------------------------------

    public static DisplayImageOptions options;
    public static DisplayImageOptions txOptions;//头像图片
    public static DisplayImageOptions tpOptions;//详情页图片
    public static DisplayImageOptions adOptions;
    private DBManager dbManager;

    /**
     * 存放拍照上传的照片路径
     */
    public String mUploadPhotoPath;
    private static final String TAG = ChildApplication.class.getName();

    private PushAgent mPushAgent;

    public void onCreate() {
        super.onCreate();
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(getApplicationContext());
        initImageLoader(getApplicationContext());
//        MobclickAgent.updateOnlineConfig(getApplicationContext());
        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setDebugMode(true);
        dbManager = new DBManager(getApplicationContext());

        mLocationClient = new LocationClient(this.getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        mGeofenceClient = new GeofenceClient(getApplicationContext());
        mLocationClient.start();
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
    }

    public ChildApplication() {
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.tx)
                .showImageForEmptyUri(R.drawable.tx)    // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.tx)        // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                           // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                             // 设置下载的图片是否缓存在内存卡中
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .bitmapConfig(Bitmap.Config.RGB_565)          //图片的解码类型
//                .displayer(new RoundedBitmapDisplayer(5))
                .build();
        txOptions = new DisplayImageOptions.Builder()//头像
                .showImageOnLoading(R.drawable.tx)
                .showImageForEmptyUri(R.drawable.tx)    // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.tx)        // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                           // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                             // 设置下载的图片是否缓存在内存卡中
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)          //图片的解码类型头像
                .build();

        adOptions = new DisplayImageOptions.Builder()//广告
                .showImageOnLoading(R.drawable.hctp1)
                .showImageForEmptyUri(R.drawable.hctp1)    // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.hctp1)        // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                           // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                             // 设置下载的图片是否缓存在内存卡中
                .imageScaleType(ImageScaleType.EXACTLY)
                .bitmapConfig(Bitmap.Config.RGB_565)          //图片的解码类型广告位
                .build();

        tpOptions = new DisplayImageOptions.Builder()//图片
                .showImageOnLoading(R.drawable.hctp)
                .showImageForEmptyUri(R.drawable.hctp)    // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.hctp)        // 设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)                           // 设置下载的图片是否缓存在内存中
                .cacheOnDisc(true)                             // 设置下载的图片是否缓存在内存卡中
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .bitmapConfig(Bitmap.Config.RGB_565)          //图片的解码类型图片
                .build();
    }

    /**
     * 初始化图片加载组件ImageLoader
     *
     * @param context
     */
    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);
    }

    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            StringBuffer sb = new StringBuffer(256);

            sb.append("latitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());

            Log.i("BaiduLocationApiDem", sb.toString());
            dwlocation_latitude = location.getLatitude();
            dwlocation_lontitude = location.getLongitude();
        }
    }
    /**
     * 显示请求字符串
     * @param str
     */
    public void logMsg(String str) {
        try {
            if (mLocationResult != null)
                mLocationResult.setText(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
