package com.child.manage.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.*;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.child.manage.ChildApplication;
import com.child.manage.R;
import com.child.manage.adapter.GrowingAdapter;
import com.child.manage.anim.UgcAnimations;
import com.child.manage.base.FlipperLayout;
import com.child.manage.entity.Favours;
import com.child.manage.entity.FavoursObj;
import com.child.manage.entity.Growing;
import com.child.manage.ui.CheckInActivity;
import com.child.manage.ui.VoiceActivity;
import com.child.manage.ui.WriteRecordActivity;
import com.child.manage.util.ActivityForResultUtil;
import com.child.manage.widget.ContentListView;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 菜单首页类
 * 
 * @author rendongwei
 * 
 */
public class Dianping {
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

	private ListView mPopDisplay;
    private ContentListView listView;
    private GrowingAdapter adapter;
    private List<Growing> growingList = new ArrayList<Growing>();

	/**
	 * 判断当前的path菜单是否已经显示
	 */
	private boolean mUgcIsShowing = false;

	public Dianping(Context context, Activity activity, ChildApplication application) {
		mContext = context;
		mActivity = activity;
		mKXApplication = application;
		mHome = LayoutInflater.from(context).inflate(R.layout.dianming, null);
		mPopView = LayoutInflater.from(context).inflate(
				R.layout.home_popupwindow, null);
		findViewById();
		setListener();
        initData();

	}

    /**
     *
     * {"growing_id":"169",
     * "child_id":"1",
     * "id":"169","
     * uid":"73","
     * dept":"\u5927\u5b9d\u5f88\u4e56",
     * "url":"http:\/\/yey.xqb668.com\/Uploads\/2014-12-03\/547de2abd9689.jpg",
     * "type":"1",
     * "publisher":"\u5927\u5b9d\u5b9d\u7684\u5988\u5988",
     * "publisher_cover":"http:\/\/yey.xqb668.com\/Uploads\/14169822369527.file",
     * "publish_uid":"73","
     * is_share":"1","
     * school_id":"1","
     * class_id":"1",
     * "pt":"2",
     * "dateline":"1417536176",
     * "user_type":"2",
     * "testtest":" ",
     * "time":"2014-12-03 00:02:56",
     * "is_favoured":"",
     * "comments":[{"name":"\u5927\u5b9d\u5b9d\u7684\u5988\u5988",
     * "cover":"http:\/\/yey.xqb668.com\/Uploads\/cover\/89_0.jpg",
     * "content":"\u5927\u6cd5\u5e08","time":"2014-12-03 00:03:56","
     * uid":"73","dateline":"1417536236","tid":"53","user_type":"2"}],"
     * favours":{"count":"1","list":[
     *
     * {"name":"\u5927\u5b9d\u5b9d\u7684\u5988\u5988",
     * "cover":"http:\/\/yey.xqb668.com\/Uploads\/cover\/89_0.jpg",
     * "time":"","
     * uid":"73","
     * user_type":"2"}]}},
     * */

    private void initData() {
        for(int i=0;i<10;i++){
            Favours favours = new Favours();
            growingList.add(new Growing("100", "100", "2014-01-02","1", "小刚宝", "小刚宝的爸爸",
                    "http:\\/\\/yey.xqb668.com\\/Uploads\\/cover\\/89_0.jpg",
                    "73", "1",
                    "1", "2", 1417536176, "2",
                    "2014-12-03 00:02:56", "http:\\/\\/yey.xqb668.com\\/Uploads\\/cover\\/89_0.jpg",
                    "", null, null, false));
        }
    }

    private void findViewById() {
		mMenu = (Button) mHome.findViewById(R.id.dianping_menu);
		mUgcView = (View) mHome.findViewById(R.id.home_ugc);
		mUgcLayout = (RelativeLayout) mUgcView.findViewById(R.id.ugc_layout);
		mUgc = (ImageView) mUgcView.findViewById(R.id.ugc);

		mUgcBg = (ImageView) mUgcView.findViewById(R.id.ugc_bg);
		mUgcVoice = (ImageView) mUgcView.findViewById(R.id.ugc_voice);
		mUgcPhoto = (ImageView) mUgcView.findViewById(R.id.ugc_photo);
		mUgcRecord = (ImageView) mUgcView.findViewById(R.id.ugc_record);
		mUgcLbs = (ImageView) mUgcView.findViewById(R.id.ugc_lbs);
		mPopDisplay = (ListView) mPopView
				.findViewById(R.id.home_popupwindow_display);

        listView = (ContentListView) mHome.findViewById(R.id.dianping_display);
        adapter = new GrowingAdapter(growingList, mContext);
        listView.setAdapter(adapter);
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

    private void getBaby(){
//        String uri = String.format(InternetURL.GET_BABY_URL +"?uid=%s", uid);
//        StringRequest request = new StringRequest(
//                Request.Method.GET,
//                uri,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String s) {
//                        Gson gson = new Gson();
//                        try{
//                            BabyDATA data = gson.fromJson(s, BabyDATA.class);
//                            babies.addAll(data.getData());
//                            List<String> names = new ArrayList<String>();
//                            for (int i=0; i<babies.size()+1; i++){
//                                if (i==0){
//                                    names.add("成长管理");
//                                }else {
//                                    names.add(babies.get(i-1).getName());
//                                }
//                            }
//                            spinnerAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, names);
//                            spinnerAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
//                            growingManager.setAdapter(spinnerAdapter);
//                            growingManager.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                                @Override
//                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                                    if (position == 0) {
//                                        child_id = "";
//                                    } else{
//                                        Baby baby = babies.get(position-1);
//                                        child_id = baby.getId();
//                                    }
//                                    getData(ContentListView.REFRESH);
//
//                                }
//
//                                @Override
//                                public void onNothingSelected(AdapterView<?> parent) {
//
//                                }
//                            });
//                        }catch (Exception e){
////                            ErrorDATA data = gson.fromJson(s, ErrorDATA.class);
////                            if (data.getCode() == 500){
////                                Log.i("ErrorData", "获取baby信息数据错误");
////                            }
//                        }
//
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//
//                    }
//                }
//        );
//        mRequestQueue.add(request);
    }

    //下拉刷新
//    @Override
    public void onRefresh() {
//        pageIndex = 1;
//        getData(ContentListView.REFRESH);
    }

    //上拉加载
//    @Override
    public void onLoad() {
//        pageIndex++;
//        getData(ContentListView.LOAD);
    }

    /**
     * 再摁退出程序
     * @param keyCode
     * @param event
     * @return
     */
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (event.getAction() == KeyEvent.ACTION_DOWN && KeyEvent.KEYCODE_BACK == keyCode){
//            long currentTime = System.currentTimeMillis();
//            if ((currentTime - touchTime) >= waitTime){
//                Toast.makeText(mContext, "再摁退出登录", Toast.LENGTH_SHORT).show();
//                touchTime = currentTime;
//            }else {
//                ActivityTack.getInstanse().exit(mContext);
//            }
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

}
