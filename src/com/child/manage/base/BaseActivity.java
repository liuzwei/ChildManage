package com.child.manage.base;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.child.manage.ChildApplication;
import com.child.manage.R;
import com.child.manage.upload.MultiPartStringRequest;
import com.child.manage.util.ToastUtil;
import com.google.gson.Gson;
import net.tsz.afinal.FinalHttp;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by liuzwei on 2014/11/11.
 */
public class BaseActivity extends Activity {
    public Context mContext;
    public SharedPreferences sp;
    public LayoutInflater inflater;
    public static FinalHttp finalHttp = new FinalHttp();

    private ActivityTack tack = ActivityTack.getInstanse();

    //    private Gson gson = new Gson();
    protected ChildApplication childApplication;
    /**
     * 屏幕的宽度和高度
     */
    protected int mScreenWidth;
    protected int mScreenHeight;
    /**
     * 表情控件
     */
    private PopupWindow mFacePop;
    private View mFaceView;
    protected ImageView mFaceClose;
    protected GridView mFaceGridView;


    private RequestQueue mRequestQueue;
    private static RequestQueue mSingleQueue;
    private ExecutorService appThread = Executors.newSingleThreadExecutor();

    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        childApplication = (ChildApplication) getApplication();
        /**
         * 获取屏幕宽度和高度
         */
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        mScreenWidth = metric.widthPixels;
        mScreenHeight = metric.heightPixels;

        initFace();

        mContext = getApplicationContext();
        sp = getSharedPreferences("environ_manage", Context.MODE_PRIVATE);
        inflater = LayoutInflater.from(mContext);
        tack.addActivity(this);
    }

//    public Gson getGson(){
//        return gson;
//    }

    protected Context getContext() {
        return mContext;
    }

    //存储sharepreference
    public void save(String key, Object value) {
        SharedPreferences.Editor editor = sp.edit();
//        editor.putString(key, gson.toJson(value)).commit();
    }

    public ActivityTack getTack() {
        return tack;
    }

    public static FinalHttp getFinalHttp() {
        if (finalHttp == null) {
            finalHttp = new FinalHttp();
        }
        return finalHttp;
    }

    /**
     * 初始化表情控件
     */
    private void initFace() {
        mFaceView = LayoutInflater.from(this).inflate(R.layout.face, null);
        mFaceClose = (ImageView) mFaceView.findViewById(R.id.face_close);
        mFaceGridView = (GridView) mFaceView.findViewById(R.id.face_gridview);
//        FaceAdapter mAdapter = new FaceAdapter(this);
//        mFaceGridView.setAdapter(mAdapter);
        mFacePop = new PopupWindow(mFaceView, mScreenWidth - 60, mScreenWidth,
                true);
        mFacePop.setBackgroundDrawable(new BitmapDrawable());
    }

    /**
     * 显示表情控件
     *
     * @param parent 显示位置的根布局
     */
    protected void showFace(View parent) {
        if (!mFacePop.isShowing()) {
            ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(BaseActivity.this
                                    .getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
            mFacePop.showAtLocation(parent, Gravity.CENTER, 0, 0);
        }
    }

//    /**
//     * 隐藏表情控件
//     */
//    protected void dismissFace() {
//        if (mFacePop != null && mFacePop.isShowing()) {
//            mFacePop.dismiss();
//        }
//    }
//
//    /**
//     * 表情适配器
//     *
//     * @author rendongwei
//     *
//     */
//    private class FaceAdapter extends BaseAdapter {
//
//        private Context mContext;
//
//        public FaceAdapter(Context context) {
//            mContext = context;
//        }
//
//        public int getCount() {
//            return childApplication.mFaces.length;
//        }
//
//        public Object getItem(int position) {
//            return childApplication.getFaceBitmap(position);
//        }
//
//        public long getItemId(int position) {
//            return position;
//        }
//
//        public View getView(int position, View convertView, ViewGroup parent) {
//            ImageView face = null;
//            if (convertView == null) {
//                face = new ImageView(mContext);
//                AbsListView.LayoutParams params = new AbsListView.LayoutParams(
//                        AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT);
//                int widthAndHeight = (int) TypedValue.applyDimension(
//                        TypedValue.COMPLEX_UNIT_DIP, 30, mContext
//                                .getResources().getDisplayMetrics());
//                params.width = widthAndHeight;
//                params.height = widthAndHeight;
//                face.setLayoutParams(params);
//                face.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
//            } else {
//                face = (ImageView) convertView;
//            }
//            face.setImageBitmap(childApplication.getFaceBitmap(position));
//            return face;
//        }
//    }

    /**
     * 获得线程池
     *
     * @return
     */
    public ExecutorService getAppThread() {
        return appThread;
    }

    public Gson getGson() {
        return gson;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }


    /**
     * 根据资源ID
     *
     * @param resId
     */
    public void alert(int resId) {
        ToastUtil.show(getApplicationContext(), resId);
    }


    public static void addPutUploadFileRequest(final String url,
                                               final Map<String, File> files, final Map<String, String> params,
                                               final Response.Listener<String> responseListener, final Response.ErrorListener errorListener,
                                               final Object tag) {
        if (null == url || null == responseListener) {
            return;
        }

        MultiPartStringRequest multiPartRequest = new MultiPartStringRequest(
                Request.Method.POST, url, responseListener, errorListener) {

            @Override
            public Map<String, File> getFileUploads() {
                return files;
            }

            @Override
            public Map<String, String> getStringUploads() {
                return params;
            }

        };

        mSingleQueue.add(multiPartRequest);
    }

}
