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
import com.child.manage.anim.UgcAnimations;
import com.child.manage.ui.*;
import com.child.manage.util.ActivityForResultUtil;
import com.child.manage.util.PhotoUtil;
import com.child.manage.util.ViewUtil;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 菜单界面
 * 
 * @author rendongwei
 * 
 */
public class Desktop {
	private Context mContext;
	private Activity mActivity;
	private ChildApplication mKXApplication;
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
//	private View mUgcView;
//	private RelativeLayout mUgcLayout;
//	private ImageView mUgc;
//	private ImageView mUgcBg;
//	private ImageView mUgcVoice;
//	private ImageView mUgcPhoto;
//	private ImageView mUgcRecord;
//	private ImageView mUgcLbs;

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

	public Desktop(Context context, Activity activity, ChildApplication application) {
		mContext = context;
		mActivity = activity;
		mKXApplication = application;
		// 绑定布局到当前View
		mDesktop = LayoutInflater.from(context).inflate(R.layout.desktop, null);
		findViewById();
		setListener();
		init();
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

//		mUgcView = (View) mDesktop.findViewById(R.id.desktop_ugc);
//		mUgcLayout = (RelativeLayout) mUgcView.findViewById(R.id.ugc_layout);
//		mUgc = (ImageView) mUgcView.findViewById(R.id.ugc);
//		mUgcBg = (ImageView) mUgcView.findViewById(R.id.ugc_bg);
//
//		mUgcVoice = (ImageView) mUgcView.findViewById(R.id.ugc_voice);
//		mUgcPhoto = (ImageView) mUgcView.findViewById(R.id.ugc_photo);
//		mUgcRecord = (ImageView) mUgcView.findViewById(R.id.ugc_record);
//		mUgcLbs = (ImageView) mUgcView.findViewById(R.id.ugc_lbs);

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
//		// Path监听
//		mUgcView.setOnTouchListener(new OnTouchListener() {
//
//			public boolean onTouch(View v, MotionEvent event) {
//				// 判断是否已经显示,显示则关闭并隐藏
//				if (mUgcIsShowing) {
//					mUgcIsShowing = false;
//					UgcAnimations.startCloseAnimation(mUgcLayout, mUgcBg, mUgc,
//                            500);
//					return true;
//				}
//				return false;
//			}
//		});
//		// Path监听
//		mUgc.setOnClickListener(new OnClickListener() {
//
//			public void onClick(View v) {
//				// 判断是否显示,已经显示则隐藏,否则则显示
//				mUgcIsShowing = !mUgcIsShowing;
//				if (mUgcIsShowing) {
//					UgcAnimations.startOpenAnimation(mUgcLayout, mUgcBg, mUgc,
//							500);
//				} else {
//					UgcAnimations.startCloseAnimation(mUgcLayout, mUgcBg, mUgc,
//							500);
//				}
//			}
//		});
//
//		//语音按钮监听
//		mUgcVoice.setOnClickListener(new OnClickListener() {
//
//			public void onClick(View v) {
//				Animation anim = UgcAnimations.clickAnimation(500);
//				anim.setAnimationListener(new AnimationListener() {
//
//					public void onAnimationStart(Animation animation) {
//
//					}
//
//					public void onAnimationRepeat(Animation animation) {
//
//					}
//
//					public void onAnimationEnd(Animation animation) {
//						mContext.startActivity(new Intent(mContext,
//								VoiceActivity.class));
//						closeUgc();
//					}
//				});
//				mUgcVoice.startAnimation(anim);
//			}
//		});
//		// 拍照按钮监听
//		mUgcPhoto.setOnClickListener(new OnClickListener() {
//
//			public void onClick(View v) {
//				Animation anim = UgcAnimations.clickAnimation(500);
//				anim.setAnimationListener(new AnimationListener() {
//
//					public void onAnimationStart(Animation animation) {
//
//					}
//
//					public void onAnimationRepeat(Animation animation) {
//
//					}
//
//					public void onAnimationEnd(Animation animation) {
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        File dir = new File("/sdcard/KaiXin/Camera/");
//                        if (!dir.exists()) {
//                            dir.mkdirs();
//                        }
//                        mKXApplication.mUploadPhotoPath = "/sdcard/KaiXin/Camera/"
//                                + UUID.randomUUID().toString();
//                        File file = new File(
//                                mKXApplication.mUploadPhotoPath);
//                        if (!file.exists()) {
//                            try {
//                                file.createNewFile();
//                            } catch (IOException e) {
//
//                            }
//                        }
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT,
//                                Uri.fromFile(file));
//                        mActivity
//                                .startActivityForResult(
//                                        intent,
//                                        ActivityForResultUtil.REQUESTCODE_UPLOADPHOTO_CAMERA);
//						closeUgc();
//					}
//				});
//				mUgcPhoto.startAnimation(anim);
//			}
//		});
//		// 记录按钮监听
//		mUgcRecord.setOnClickListener(new OnClickListener() {
//
//			public void onClick(View v) {
//				Animation anim = UgcAnimations.clickAnimation(500);
//				anim.setAnimationListener(new AnimationListener() {
//
//					public void onAnimationStart(Animation animation) {
//
//					}
//
//					public void onAnimationRepeat(Animation animation) {
//
//					}
//
//					public void onAnimationEnd(Animation animation) {
//						mContext.startActivity(new Intent(mContext,
//								WriteRecordActivity.class));
//						closeUgc();
//					}
//				});
//				mUgcRecord.startAnimation(anim);
//			}
//		});
//		// 相册按钮监听
//		mUgcLbs.setOnClickListener(new OnClickListener() {
//
//			public void onClick(View v) {
//				Animation anim = UgcAnimations.clickAnimation(500);
//				anim.setAnimationListener(new AnimationListener() {
//					public void onAnimationStart(Animation animation) {
//
//					}
//
//					public void onAnimationRepeat(Animation animation) {
//
//					}
//
//					public void onAnimationEnd(Animation animation) {
//						mContext.startActivity(new Intent(mContext,
//                                PhoneAlbumActivity.class));
//						closeUgc();
//					}
//				});
//				mUgcLbs.startAnimation(anim);
//			}
//		});

	}

	/**
	 * 界面初始化
	 */
	private void init() {
		/**
		 * 设置墙纸、姓名、签名、头像以及菜单界面
		 */
		mName.setText("15165833525");
		mAvatar.setImageBitmap(PhotoUtil.toRoundCorner(BitmapFactory
                .decodeResource(mContext.getResources(), R.drawable.tx), 15));
		mAdapter = new DesktopAdapter(mContext);
		mDisplay.setAdapter(mAdapter);
	}



	/**
	 * 修改头像
	 * 
	 * @param bitmap
	 *            修改的头像
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

//	/**
//	 * 获取Path菜单显示状态
//	 *
//	 * @return 显示状态
//	 */
//	public boolean getUgcIsShowing() {
//		return mUgcIsShowing;
//	}
//
//	/**
//	 * 关闭Path菜单
//	 */
//	public void closeUgc() {
//		mUgcIsShowing = false;
//		UgcAnimations.startCloseAnimation(mUgcLayout, mUgcBg, mUgc, 500);
//	}

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
	 * 
	 */
	public interface onChangeViewListener {
		public abstract void onChangeView(int arg0);
	}

	/**
	 * 菜单适配器
	 * 
	 * @author rendongwei
	 * 
	 */
	public class DesktopAdapter extends BaseAdapter {

		private Context mContext;
		private String[] mName = { "园区介绍","互动天地", "给宝宝成长点评", "远程视频", "育儿知识", "每周课程", "宝宝食谱",
				"设置" };
		private int[] mIcon = { R.drawable.left_hudong,
				R.drawable.left_baobaochegnzhangdianping, R.drawable.left_geilaoshily,
				R.drawable.left_yuancheng, R.drawable.left_yuerzhishi,
				R.drawable.left_meizhoukecheng,
				R.drawable.baobaoshipu, R.drawable.left_set };
		private int[] mIconPressed = {  R.drawable.left_hudong,
                R.drawable.left_baobaochegnzhangdianping, R.drawable.left_geilaoshily,
                R.drawable.left_yuancheng, R.drawable.left_yuerzhishi,
                R.drawable.left_meizhoukecheng,
                R.drawable.baobaoshipu, R.drawable.left_set  };
		private int mChoose = 0;

		public DesktopAdapter(Context context) {
			mContext = context;
		}

		public int getCount() {
			return 8;
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
						case ViewUtil.SHIPU:
							mOnChangeViewListener
									.onChangeView(ViewUtil.SHIPU);
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
