package com.child.manage.menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.child.manage.ChildApplication;
import com.child.manage.R;
import com.child.manage.adapter.ShipuAdapter;
import com.child.manage.anim.UgcAnimations;
import com.child.manage.base.FlipperLayout;
import com.child.manage.entity.shipu;
import com.child.manage.ui.CheckInActivity;
import com.child.manage.ui.VoiceActivity;
import com.child.manage.ui.WriteRecordActivity;
import com.child.manage.util.ActivityForResultUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 菜单首页类
 *
 * @author rendongwei
 */
public class Shipu {
    private Button mMenu;
    private View mUgcView;
    private Context mContext;
    private Activity mActivity;
    private ChildApplication mKXApplication;
    private View mShipu;
    private FlipperLayout.OnOpenListener mOnOpenListener;

    private ListView shipulsv;
    private ShipuAdapter adapter;
    private List<shipu> lists = new ArrayList<shipu>();

    public Shipu(Context context, Activity activity, ChildApplication application) {
        mContext = context;
        mActivity = activity;
        mKXApplication = application;
        mShipu = LayoutInflater.from(context).inflate(R.layout.shipu, null);
        //构造虚拟数据
        lists.add(new shipu("2014-10-20 星期一", "早餐：牛奶  面包", "午餐：白饭 青菜 汤 蛋"));
        lists.add(new shipu("2014-10-21 星期二", "早餐：牛奶  面包", "午餐：白饭 青菜 汤 蛋"));
        lists.add(new shipu("2014-10-22 星期三", "早餐：牛奶  面包", "午餐：白饭 青菜 汤 蛋"));
        lists.add(new shipu("2014-10-23 星期四", "早餐：牛奶  面包", "午餐：白饭 青菜 汤 蛋"));
        lists.add(new shipu("2014-10-24 星期五", "早餐：牛奶  面包", "午餐：白饭 青菜 汤 蛋"));
        lists.add(new shipu("2014-10-25 星期六", "早餐：牛奶  面包", "午餐：白饭 青菜 汤 蛋"));
        lists.add(new shipu("2014-10-26 星期日", "早餐：牛奶  面包", "午餐：白饭 青菜 汤 蛋"));
        findViewById();
        setListener();

    }

    private void findViewById() {
        mMenu = (Button) mShipu.findViewById(R.id.shipu_menu);
        shipulsv = (ListView) mShipu.findViewById(R.id.shipulsv);
        adapter = new ShipuAdapter(lists, mContext);
        shipulsv.setAdapter(adapter);

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
        return mShipu;
    }

    public void setOnOpenListener(FlipperLayout.OnOpenListener onOpenListener) {
        mOnOpenListener = onOpenListener;
    }
}
