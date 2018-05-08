package com.afaya.viewdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by Administrator on 2017/3/30.
 */

public class BaseActivity extends AppCompatActivity implements AfayaLog {
    private static final String TAG = "BaseActivity";
    @Override
    public  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myLog(OTHERTOOLS.getCurrentMethod());
        ActivityCollector.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myLog(OTHERTOOLS.getCurrentMethod());
        ActivityCollector.removeActivity(this);
    }

    @Override
    public void myLog(String str) {
        if(AfayaLog.D){
            Log.e(TAG, str);
        }
    }
}
