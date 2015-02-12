package com.child.manage.ui;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.child.manage.R;
import com.child.manage.base.BaseActivity;
import com.child.manage.util.ActivityForResultUtil;
import com.child.manage.util.RecordUtil;
import com.child.manage.util.TextUtil;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 录音类
 *
 * @author rendongwei
 */
public class VoiceActivity extends BaseActivity implements OnClickListener {
    private LinearLayout mParent;
    private Button mRecord;
    private Button luyin_menu;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.voice_activity);
        findViewById();
        setListener();

    }

    private void findViewById() {
        mParent = (LinearLayout) findViewById(R.id.voice_parent);
        mRecord = (Button) findViewById(R.id.voice_record_btn);
        luyin_menu = (Button) this.findViewById(R.id.luyin_menu);
        luyin_menu.setOnClickListener(this);
    }

    private void setListener() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.luyin_menu:
                finish();
                break;
        }
    }
}
