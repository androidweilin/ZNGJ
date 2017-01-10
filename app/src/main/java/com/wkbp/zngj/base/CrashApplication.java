package com.wkbp.zngj.base;

import android.app.Application;
import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.wkbp.zngj.util.SharedPreferenceUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weilin on 2016/11/4 10:09
 */
public class CrashApplication extends Application {
    public static double longitude = 0.0;
    public static double latitude = 0.0;
    //声明LocationClient类对象
    public LocationClient mLocationClient = null;
    //声明LocationClientOption类对象
    public LocationClientOption option = null;
    public BDLocationListener myListener = new MyLocationListener();
    public static List<BaseActivity> allActivity = new ArrayList<>();
    public static Context context;

    public PushAgent pushAgent = null;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        // 初始化SharedPreferences
        SharedPreferenceUtils.getInstance().init(context);

        mLocationClient = new LocationClient(context);     //声明LocationClient类
        option = new LocationClientOption();
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(myListener);    //注册监听函数

        mLocationClient.start();

        /**
         * 友盟推送初始化
         */
        pushAgent = PushAgent.getInstance(context);
        pushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String s) {

            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
    }

    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {

            latitude = location.getLatitude();
            longitude = location.getLongitude();
            if (mLocationClient.isStarted()) {
                mLocationClient.stop();
            }
        }
    }
}
