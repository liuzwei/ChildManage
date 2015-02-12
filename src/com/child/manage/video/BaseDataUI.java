package com.child.manage.video;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.PowerManager;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author qinglei.yin@192.168.88.9<br>
 *         2014-4-4 下午2:25:02<br>
 * @declaration 界面数据处理基类<br>
 */
public class BaseDataUI extends Activity {
    private PowerManager.WakeLock mWakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_this = this;
        mResources = this.getResources();
    }

    @SuppressWarnings("deprecation")


    @Override
    protected void onResume() {
        super.onResume();
        if (mWakeLock == null) {
            //屏幕长亮
            PowerManager localPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
            mWakeLock = localPowerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
        }

        // 屏幕将停留在设定的状态，一般为亮、暗状态
        if (null != mWakeLock && (!mWakeLock.isHeld())) {
            mWakeLock.acquire();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //释放掉正在运行的cpu或关闭屏幕。
        if (null != mWakeLock) {
            mWakeLock.release();
        }
    }

    protected static BaseDataUI m_this;

    public static void sendToast(final Object msg) {
        if (null == m_this) return;
        m_this.sendMyToast(msg);
    }

    public Resources mResources;

    public String gRstr(int id) {
        String str = "";
        if (mResources != null) {
            str = mResources.getString(id);
            str = (TextUtils.isEmpty(str)) ? "" : str;
        }
        return str;
    }

    /**
     * =======================================================
     */
    public void sendMyToast(final Object msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String _msg = "";
                if (msg instanceof Integer) {
                    _msg = getResources().getString((Integer) msg);
                } else if (msg instanceof String) {
                    _msg = (String) msg;
                }
                Toast.makeText(BaseDataUI.this, _msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getEtStr(int resID) {
        return ((EditText) findViewById(resID)).getText().toString();
    }

    public void setEtStr(int resID, String value) {
        ((EditText) findViewById(resID)).setText(value);
    }

    public String[] devID = {"101752", "100134", "100082", "100133"};
    public String name = "admin";
    public String pwd = "123456";
    /**=======================================================*/
}
