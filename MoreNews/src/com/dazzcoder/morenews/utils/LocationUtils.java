package com.dazzcoder.morenews.utils;/**
 * Created by zc on 2016/1/1.
 */

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

/**
 * User: CZC(867135418@QQ.COM)
 * Date: 2016-01-01
 * Time: 21:44
 * ReadMe:
 */

public class LocationUtils {
    private Context context;
    private LocationClient locationClient = null;

    private String location = "";

    public LocationUtils(Context context) {
        this.context = context;
        if (locationClient == null) {
            initGps();
        }
    }

    private void initGps() {
        try {
            locationClient = new LocationClient(context);
            locationClient.registerLocationListener(new LocationListener());
            LocationClientOption option = new LocationClientOption();
            option.setOpenGps(true);
            option.setAddrType("all");
            option.setCoorType("bd09ll");
            option.setScanSpan(5000);
            option.disableCache(true);
            option.setPriority(LocationClientOption.GpsFirst);
            locationClient.setLocOption(option);
            locationClient.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public class LocationListener implements BDLocationListener {

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
                //Log.d(TAG, "onReceiveLocation: " + sb.toString());
                location = sb.toString().replace("市", "");
                PreferenceHelper.write(context,"config","location",location);
                locationClient.stop();
            }
        }
    }
}
