package com.child.manage.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.child.manage.R;
import com.child.manage.adapter.AnimateFirstDisplayListener;
import com.child.manage.base.BaseActivity;
import com.child.manage.entity.Account;
import com.child.manage.util.RoundImagePhoto;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * Created by liuzwei on 2014/11/18.
 */
public class SelectIdentityActivity extends BaseActivity implements View.OnClickListener{
    private LinearLayout father;
    private LinearLayout mother;

    private ImageView fatherPhoto;
    private ImageView motherPhoto;
    private TextView fatherName;
    private TextView motherName;

    private RoundImagePhoto roundImagePhoto;
    ImageLoader imageLoader = ImageLoader.getInstance();//图片加载类
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_identity_layout);
        initView();
        Account account = (Account) getIntent().getSerializableExtra(Constants.ACCOUNT_KEY);
        roundImagePhoto = new RoundImagePhoto(this);
        if (account != null){
            roundImagePhoto.readBitmapViaVolley(account.getF_cover(), fatherPhoto);
            roundImagePhoto.readBitmapViaVolley(account.getM_cover(), motherPhoto);
            fatherName.setText(account.getF_name());
            motherName.setText(account.getM_name());
        }


    }

    private void initView(){
        father = (LinearLayout) findViewById(R.id.father_linear);
        mother = (LinearLayout) findViewById(R.id.mother_linear);
        fatherPhoto = (ImageView) findViewById(R.id.identity_father);
        fatherName = (TextView) findViewById(R.id.identity_father_name);
        motherPhoto = (ImageView) findViewById(R.id.identity_mother);
        motherName = (TextView) findViewById(R.id.identity_mother_name);
        father.setOnClickListener(this);
        mother.setOnClickListener(this);
        
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.father_linear:
                save(Constants.IDENTITY, "0");
                break;
            case R.id.mother_linear:
                save(Constants.IDENTITY, "1");
                break;
        }
        Intent intent = new Intent(SelectIdentityActivity.this, CenterActivity.class);
        startActivity(intent);
    }
}
