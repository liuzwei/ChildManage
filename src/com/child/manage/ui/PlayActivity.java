package com.child.manage.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.*;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.child.manage.R;
import com.child.manage.entity.MyNodeinfo;
import szy.utility.PlayListener;
import szy.utility.PlayView;

@SuppressLint("HandlerLeak")
public class PlayActivity extends Activity {
	private PlayView mPlayView;

	private boolean mBoolStop = false;
	
	private LinearLayout mLayoutProgress;

	private MyNodeinfo mNodeInfo;
	private LinearLayout mLayoutPlay;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setFormat(PixelFormat.TRANSLUCENT);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);

		setContentView(R.layout.play);
		mNodeInfo = (MyNodeinfo) getIntent().getSerializableExtra("nodeinfo");
		TextView textView = (TextView) findViewById(R.id.text_title);
		textView.setText(mNodeInfo.getsNodeName());
		mLayoutPlay = (LinearLayout) findViewById(R.id.llayout_playview);

		mLayoutProgress = (LinearLayout) findViewById(R.id.llayout_progress);
		Display dis = getWindowManager().getDefaultDisplay();
		LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
				dis.getWidth(),dis.getWidth()*3/4);
		mPlayView = new PlayView(this);
		mPlayView.setPlayWH(dis.getWidth(), dis.getWidth() * 3 / 4);
		mPlayView.setPlayListener(playListener);
		mLayoutPlay.addView(mPlayView, lParams);

		int nRet = mPlayView.startPlay(mNodeInfo.getsParentId(), mNodeInfo.getsNodeId());
		if (nRet == PlayView.SXT_AT_TREM) {
			showTipsDlg("摄像头到期");
		}else if (nRet == PlayView.SXT_NOT_ONLINE) {
			showTipsDlg("摄像头不在线");
		}
		showProgress();
		mBoolStop = false;
		
		Button btnBack = (Button) findViewById(R.id.play_menu);
		btnBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mBoolStop = true;
				if (mPlayView!=null) {
					mPlayView.Stop();
				}
				PlayActivity.this.finish();
			}
		});

	}
	
	private PlayListener playListener = new PlayListener() {

		@Override
		public void infoCallback(int type, String sInfo) {
			Message msg = new Message();
			msg.what = type;
			msg.obj = sInfo;
			mHandler.sendMessage(msg);
		}
	};
	
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case PlayView.PLAY_VIDEO_SUCCESS:
				hideProgress();
				break;
			case PlayView.PLAY_VIDEO_ERROR_WATCH_TOOMUCH:
				//超过观看人数上限
				showTipsDlg((String)msg.obj);
				break;
			case PlayView.PLAY_VIDEO_ERROR_RECV_TIMEOUT:
				//音视频数据接收失败
				showTipsDlg2(R.string.shujujieshousb);
				break;
			case PlayView.PLAY_VIDEO_ERROR_CONNECT_TIMEOUT:
				//连接服务器失败
				showTipsDlg2(R.string.fuwuqilianjiesb);
				break;
			default:
				break;
			}
		}
	};

	private void showTipsDlg2(int nMsg) {
		if (!mBoolStop) {
			hideProgress();
			Builder builder = new Builder(PlayActivity.this);
			builder.setMessage(nMsg);
			builder.setTitle(R.string.dlg_tishi);
			builder.setPositiveButton(R.string.btn_queding,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.create().show();
		}
	}

	private void showTipsDlg(String sMsg) {
		if (!mBoolStop) {
			hideProgress();
			Builder builder = new Builder(PlayActivity.this);
			builder.setMessage(sMsg);
			builder.setTitle(R.string.dlg_tishi);
			builder.setPositiveButton(R.string.btn_queding,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.create().show();
		}
	}
	
	private void showProgress() {
		mLayoutProgress.setVisibility(View.VISIBLE);
		mPlayView.setVisibility(View.GONE);
	}

	private void hideProgress() {
		mLayoutProgress.setVisibility(View.GONE);
		mPlayView.setVisibility(View.VISIBLE);
	}
	

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			mBoolStop = true;
			if (mPlayView!=null) {
				mPlayView.Stop();
			}
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		Display dis = getWindowManager().getDefaultDisplay();
		mLayoutPlay.removeView(mPlayView);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
			if (mPlayView != null) {
				LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
						dis.getWidth(), dis.getHeight());
				mPlayView.setPlayWH(dis.getWidth(), dis.getHeight());
				mLayoutPlay.addView(mPlayView, lParams);
				mPlayView.postInvalidate();
			}
		} else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			if (mPlayView != null) {
				LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(
						dis.getWidth(),dis.getWidth()*3/4);
				mPlayView.setPlayWH(dis.getWidth(),dis.getWidth()*3/4);
				mLayoutPlay.addView(mPlayView, lParams);
				mPlayView.postInvalidate();
			}
		}
	}


}
