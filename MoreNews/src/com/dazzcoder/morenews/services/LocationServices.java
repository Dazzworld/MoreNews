package com.dazzcoder.morenews.services;/**
 * Created by zc on 2015/12/23.
 */

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.dazzcoder.morenews.utils.Contacts;
import com.dazzcoder.morenews.utils.PreferenceHelper;

/**
 * User: CZC(867135418@QQ.COM)
 * Date: 2015-12-23
 * Time: 19:43
 * ReadMe: 定位服务
 */

public class LocationServices extends Service implements BDLocationListener {
    private final String TAG =  "LocationServices";
    private LocationClient locationClient = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (locationClient == null){
            initGps();
        }

        Log.d("service", "isRunning");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        if (locationClient!=null){
            locationClient.stop();
        }
    }

    private void initGps() {
        try{
            locationClient = new LocationClient(this);
            locationClient.registerLocationListener(this);
            LocationClientOption option = new LocationClientOption();
            option.setOpenGps(true);
            option.setAddrType("all");
            option.setCoorType("bd09ll");
            option.setScanSpan(5000);
            option.disableCache(true);
            option.setPriority(LocationClientOption.GpsFirst);
            locationClient.setLocOption(option);
            locationClient.start();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sendLocation(String location) {
        //通知Activity
        Intent intent = new Intent();
        intent.setAction("locationAction");
        intent.putExtra("location", location);
        sendBroadcast(intent);
        locationClient.unRegisterLocationListener(this);
        stopSelf();
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
        if (bdLocation == null)
            return;
        StringBuffer sb = new StringBuffer(256);
        if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {
//				sb.append(location.getAddrStr());
            sb.append(bdLocation.getCity());
        } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
            sb.append(bdLocation.getCity());
        }
        if (sb.toString() != null && sb.toString().length() > 0) {
            Log.d(TAG, "onReceiveLocation: " + sb.toString());
            PreferenceHelper.write(this, Contacts.MAIN_PREFERENCES_NAME, "location", sb.toString().replace("市",""));
            //sendLocation(sb.toString().replace("市",""));
            LocationServices.this.stopSelf();
        }
    }
}
