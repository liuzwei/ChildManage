package com.child.manage.base;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import net.tsz.afinal.FinalHttp;

/**
 * Created by liuzwei on 2014/11/11.
 */
public class BaseActivity extends Activity {
    public Context mContext;
    public SharedPreferences sp;
    public LayoutInflater inflater;
    public static FinalHttp finalHttp = new FinalHttp();

    private ActivityTack tack= ActivityTack.getInstanse();

//    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    public void save(String key, Object value){
        SharedPreferences.Editor editor = sp.edit();
//        editor.putString(key, gson.toJson(value)).commit();
    }

    public ActivityTack getTack() {
        return tack;
    }

    public static FinalHttp getFinalHttp() {
        if (finalHttp == null){
            finalHttp = new FinalHttp();
        }
        return finalHttp;
    }

}
