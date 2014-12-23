package com.child.manage.menu;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.model.LatLng;
import com.child.manage.ChildApplication;
import com.child.manage.R;
import com.child.manage.base.FlipperLayout;

/**
 * 菜单首页类
 * 
 * @author rendongwei
 * 
 */
public class Baybayset {
    private Button mMenu;
	private Context mContext;
	private Activity mActivity;
	private ChildApplication mKXApplication;
	private View mHome;


	private FlipperLayout.OnOpenListener mOnOpenListener;

    // 地图相关
    MapView mMapView;
    BaiduMap mBaiduMap;
    Bitmap bitmap;
    private LocationClient locationClient = null;
    private static final int UPDATE_TIME = 60000;
    private static int LOCATION_COUTNS = 0;
    private Double lat;
    private Double lon;


	public Baybayset(Context context, Activity activity, ChildApplication application) {
		mContext = context;
		mActivity = activity;
		mKXApplication = application;
		mHome = LayoutInflater.from(context).inflate(R.layout.baybayset, null);
		findViewById();
		setListener();

        //设置定位条件
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);        //是否打开GPS
        option.setCoorType("bd09ll");       //设置返回值的坐标类型。
//        option.setPriority(LocationClientOption.NetWorkFirst);  //设置定位优先级
        option.setProdName("RIvp33GcGSGSwwntWPGXMxBs"); //设置产品线名称。强烈建议您使用自定义的产品线名称，方便我们以后为您提供更高效准确的定位服务。
        option.setScanSpan(UPDATE_TIME);    //设置定时定位的时间间隔。单位毫秒
        locationClient = new LocationClient(mContext.getApplicationContext());
        locationClient.setLocOption(option);
        //注册位置监听器
        locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                // TODO Auto-generated method stub
                if (location == null) {
                    return;
                }
                lat = location.getLatitude();
                lon = location.getLongitude();
                //定位到当期位置
                LatLng ll = new LatLng(lat, lon);
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
                mBaiduMap.setMapStatus( u );
                MydrawPointCurrentLocation(lat, lon);
            }
        });
        locationClient.start();
        locationClient.requestLocation();

	}

	private void findViewById() {
		mMenu = (Button) mHome.findViewById(R.id.baybay_menu);
        // 初始化地图
        mMapView = (MapView) mHome.findViewById(R.id.bmapView);
        mBaiduMap = mMapView.getMap();
//        mBaiduMap.setOnMapDrawFrameCallback(this);
        bitmap = BitmapFactory.decodeResource(mContext.getResources(),
                R.drawable.ground_overlay);

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
		return mHome;
	}

	public void setOnOpenListener(FlipperLayout.OnOpenListener onOpenListener) {
		mOnOpenListener = onOpenListener;
	}

    public void MydrawPointCurrentLocation(Double lat, Double lng){
        //定义Maker坐标点
        LatLng point = new LatLng(lat,lng);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.currentlocation);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);

    }

}
