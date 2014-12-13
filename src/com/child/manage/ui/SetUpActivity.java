package com.child.manage.ui;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import com.child.manage.LoginActivity;
import com.child.manage.MainActivity;
import com.child.manage.R;
import com.child.manage.base.BaseActivity;

/**
 * 设置类
 * 
 * @author rendongwei
 * 
 */
public class SetUpActivity extends BaseActivity {
	private Button mBack;
	private LinearLayout mExport;
	private LinearLayout mUpload;
	private LinearLayout mFeedBack;
	private LinearLayout mSetUp;
	private Button mCancelAccount;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setup_activity);

	}



	/**
	 * 注销对话框
	 */
	private void CancelAccountDialog() {
		Builder builder = new Builder(this);
		builder.setTitle("注销登录");
		builder.setMessage("确定注销登录吗?");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				if (!MainActivity.mInstance.isFinishing()) {
					MainActivity.mInstance.finish();
				}
				startActivity(new Intent(SetUpActivity.this,
						LoginActivity.class));
				finish();
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		builder.create().show();
	}
}
