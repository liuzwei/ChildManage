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
import com.child.manage.result.HomeResult;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

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

    /**
     * 默认壁纸
     */
    public Bitmap mDefault_Wallpager;
    /**
     * 默认标题壁纸
     */
    public Bitmap mDefault_TitleWallpager;
    /**
     * 默认头像
     */
    public Bitmap mDefault_Avatar;
    /**
     * 默认照片
     */
    public Bitmap mDefault_Photo;
    /**
     * 壁纸缓存
     */
    public HashMap<String, SoftReference<Bitmap>> mWallpagersCache = new HashMap<String, SoftReference<Bitmap>>();
    /**
     * 壁纸名称
     */
    public String[] mWallpagersName;
    /**
     * 标题壁纸缓存
     */
    public HashMap<String, SoftReference<Bitmap>> mTitleWallpagersCache = new HashMap<String, SoftReference<Bitmap>>();
    /**
     * 标题壁纸名称
     */
    public String[] mTitleWallpagersName;
    /**
     * 当前壁纸编号
     */
    public int mWallpagerPosition = 0;
    /**
     * 圆形头像缓存
     */
    public HashMap<String, SoftReference<Bitmap>> mAvatarCache = new HashMap<String, SoftReference<Bitmap>>();
    /**
     * 默认头像缓存
     */
    public HashMap<String, SoftReference<Bitmap>> mDefaultAvatarCache = new HashMap<String, SoftReference<Bitmap>>();
    /**
     * 头像名称
     */
    public String[] mAvatars;

    /**
     * 公共主页头像缓存
     */
    public HashMap<String, SoftReference<Bitmap>> mPublicPageAvatarCache = new HashMap<String, SoftReference<Bitmap>>();
    /**
     * 公共主页头像名称
     */
    public String[] mPublicPageAvatars;
//    /**
//     * 表情
//     */
//    public int[] mFaces = { R.drawable.face_0, R.drawable.face_1,
//            R.drawable.face_2, R.drawable.face_3, R.drawable.face_4,
//            R.drawable.face_5, R.drawable.face_6, R.drawable.face_7,
//            R.drawable.face_8, R.drawable.face_9, R.drawable.face_10,
//            R.drawable.face_11, R.drawable.face_12, R.drawable.face_13,
//            R.drawable.face_14, R.drawable.face_15, R.drawable.face_16,
//            R.drawable.face_17, R.drawable.face_18, R.drawable.face_19,
//            R.drawable.face_20, R.drawable.face_21, R.drawable.face_22,
//            R.drawable.face_23, R.drawable.face_24, R.drawable.face_25,
//            R.drawable.face_26, R.drawable.face_27, R.drawable.face_28,
//            R.drawable.face_29, R.drawable.face_30, R.drawable.face_31,
//            R.drawable.face_32, R.drawable.face_33, R.drawable.face_34,
//            R.drawable.face_35, R.drawable.face_36, R.drawable.face_37,
//            R.drawable.face_38, R.drawable.face_39, R.drawable.face_40,
//            R.drawable.face_41, R.drawable.face_42, R.drawable.face_43,
//            R.drawable.face_44, R.drawable.face_45, R.drawable.face_46,
//            R.drawable.face_47, R.drawable.face_48, R.drawable.face_49,
//            R.drawable.face_50, R.drawable.face_51, R.drawable.face_52,
//            R.drawable.face_53, R.drawable.face_54, R.drawable.face_55,
//            R.drawable.face_56, R.drawable.face_57, R.drawable.face_58,
//            R.drawable.face_59, R.drawable.face_60, R.drawable.face_61,
//            R.drawable.face_62, R.drawable.face_63, R.drawable.face_64,
//            R.drawable.face_65, R.drawable.face_66, R.drawable.face_67,
//            R.drawable.face_68, R.drawable.face_69, R.drawable.face_70,
//            R.drawable.face_71, R.drawable.face_72, R.drawable.face_73,
//            R.drawable.face_74, R.drawable.face_75, R.drawable.face_76,
//            R.drawable.face_77, R.drawable.face_78, R.drawable.face_79,
//            R.drawable.face_80, R.drawable.face_81, R.drawable.face_82,
//            R.drawable.face_83, R.drawable.face_84, R.drawable.face_85,
//            R.drawable.face_86, R.drawable.face_87, R.drawable.face_88,
//            R.drawable.face_89, R.drawable.face_90, R.drawable.face_91,
//            R.drawable.face_92, R.drawable.face_93, R.drawable.face_94,
//            R.drawable.face_95, R.drawable.face_96, R.drawable.face_97,
//            R.drawable.face_98, R.drawable.face_99, R.drawable.face_100,
//            R.drawable.face_101, R.drawable.face_102, R.drawable.face_103,
//            R.drawable.face_104, R.drawable.face_105, R.drawable.face_106,
//            R.drawable.face_107, R.drawable.face_108, R.drawable.face_109,
//            R.drawable.face_110 };
//    /**
//     * 表情名称
//     */
//    public List<String> mFacesText = new ArrayList<String>();
//    /**
//     * 表情缓存
//     */
//    public HashMap<String, SoftReference<Bitmap>> mFaceCache = new HashMap<String, SoftReference<Bitmap>>();
    /**
     * 照片缓存
     */
    public HashMap<String, SoftReference<Bitmap>> mPhotoCache = new HashMap<String, SoftReference<Bitmap>>();
    /**
     * 照片名称
     */
    public String[] mPhotosName;
    /**
     * 转帖图片缓存
     */
    public HashMap<String, SoftReference<Bitmap>> mViewedCache = new HashMap<String, SoftReference<Bitmap>>();
    /**
     * 转帖图片名称
     */
    public String[] mViewedName;
    /**
     * 热门转帖图片缓存
     */
    public HashMap<String, SoftReference<Bitmap>> mViewedHotCache = new HashMap<String, SoftReference<Bitmap>>();
    /**
     * 热门转帖图片名称
     */
    public String[] mViewedHotName;
    /**
     * 游戏图片缓存
     */
    public HashMap<String, SoftReference<Bitmap>> mRecommendCache = new HashMap<String, SoftReference<Bitmap>>();
    /**
     * 附近照片缓存
     */
    public HashMap<String, SoftReference<Bitmap>> mNearbyPhoto = new HashMap<String, SoftReference<Bitmap>>();
    /**
     * 主页图片缓存
     */
    public HashMap<String, SoftReference<Bitmap>> mHomeCache = new HashMap<String, SoftReference<Bitmap>>();
    /**
     * 手机SD卡图片缓存
     */
    public HashMap<String, SoftReference<Bitmap>> mPhoneAlbumCache = new HashMap<String, SoftReference<Bitmap>>();
    /**
     * 手机SD卡图片的路径
     */
    public Map<String, List<Map<String, String>>> mPhoneAlbum = new HashMap<String, List<Map<String, String>>>();

    /**
     * 当期用户首页数据
     */
    public List<HomeResult> mMyHomeResults = new ArrayList<HomeResult>();

    /**
     * 存放存为草稿的日记标题
     */
    public String mDraft_DiaryTitle;
    /**
     * 存放存为草稿的日记内容
     */
    public String mDraft_DiaryContent;

    /**
     * 存放拍照上传的照片路径
     */
    public String mUploadPhotoPath;
    /**
     * 存放本地选取的照片集合
     */
    public List<Map<String, String>> mAlbumList = new ArrayList<Map<String, String>>();

    public void onCreate() {
        super.onCreate();
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(getApplicationContext());
        initImageLoader(getApplicationContext());

        mLocationClient = new LocationClient(this.getApplicationContext());
        mMyLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mMyLocationListener);
        mGeofenceClient = new GeofenceClient(getApplicationContext());
        mLocationClient.start();
        mVibrator = (Vibrator) getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        /**
         * 初始化表情名称
         */
//        for (int i = 0; i < mFaces.length; i++) {
//            mFacesText.add("[face_" + i + "]");
//        }
    }

    //    /**
//     * 根据编号获取表情图片
//     */
//    public Bitmap getFaceBitmap(int position) {
//        try {
//            String faceName = mFacesText.get(position);
//            Bitmap bitmap = null;
//            if (mFaceCache.containsKey(faceName)) {
//                SoftReference<Bitmap> reference = mFaceCache.get(faceName);
//                bitmap = reference.get();
//                if (bitmap != null) {
//                    return bitmap;
//                }
//            }
//            bitmap = BitmapFactory.decodeResource(getResources(),
//                    mFaces[position]);
//            mFaceCache.put(faceName, new SoftReference<Bitmap>(bitmap));
//            return bitmap;
//        } catch (Exception e) {
//            return null;
//        }
//    }
    public static DisplayImageOptions options;
    public static DisplayImageOptions txOptions;//头像图片
    public static DisplayImageOptions tpOptions;//详情页图片
    public static DisplayImageOptions adOptions;

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
                .showImageOnLoading(R.drawable.txhc)
                .showImageForEmptyUri(R.drawable.txhc)    // 设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.drawable.txhc)        // 设置图片加载或解码过程中发生错误显示的图片
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
}
