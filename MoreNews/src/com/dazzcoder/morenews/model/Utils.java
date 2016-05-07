package com.dazzcoder.morenews.model;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Gravity;

import com.dazzcoder.morenews.AppContext;

public class Utils {
	public static final int NETTYPE_WIFI = 0x01;
	public static final int NETTYPE_CMWAP = 0x02;
	public static final int NETTYPE_CMNET = 0x03;

	public final static int getWindowsWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.widthPixels;
	}

	public final static int getWindowsHeight(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm.heightPixels;
	}

	public static boolean isOnline(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnected()) {
			return true;
		}
		return false;
	}

	public static boolean isPhotoSet(String url) {

		return url.contains("|");
	}

	public static String[] getPhotoID(String url) {
		if (url != null) {
			String id[] = url.split("\\|");
			id[0] = id[0].substring(id[0].length() - 4, id[0].length());
			return id;
		}

		return null;
	}

	public static String encodeHtml(String a) {
		String b;
		b = Base64.encodeToString(a.getBytes(), Base64.DEFAULT);
		b = b.replace("=", "%3D").replace("\n", "");

		return b + ".html";
	}

	public static int getNetworkType() {
		int netType = 0;
		ConnectivityManager connectivityManager = (ConnectivityManager) AppContext.getAppContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
		if (networkInfo == null) {
			return netType;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE) {
			String extraInfo = networkInfo.getExtraInfo();
			if (!isEmpty(extraInfo)) {
				if (extraInfo.toLowerCase().equals("cmnet")) {
					netType = NETTYPE_CMNET;
				} else {
					netType = NETTYPE_CMWAP;
				}
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI) {
			netType = NETTYPE_WIFI;
		}
		return netType;
	}

	public static boolean isEmpty(String input) {
		if (input == null || "".equals(input))
			return true;

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
				return false;
			}
		}
		return true;
	}
	
	
	public static int getWeatherImage(String weather){
		
		return 0;
		
	}

	/**
	 * dip转为 px
	 */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/**
	 *  px 转为 dip
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static ProgressDialog showProgress(Activity activity, String hintText) {
		Activity mActivity = null;
		if (activity.getParent() != null) {
			mActivity = activity.getParent();
			if (mActivity.getParent() != null) {
				mActivity = mActivity.getParent();
			}
		} else {
			mActivity = activity;
		}
		final Activity finalActivity = mActivity;
		ProgressDialog window = ProgressDialog.show(finalActivity, "", hintText);
		window.getWindow().setGravity(Gravity.CENTER);

		window.setCancelable(false);
		return window;
	}

	public static int getVersionCode() {
		int versionCode = 0;
		try {
			versionCode = AppContext
					.getAppContext()
					.getPackageManager()
					.getPackageInfo(AppContext.getAppContext().getPackageName(),
							0).versionCode;
		} catch (PackageManager.NameNotFoundException ex) {
			versionCode = 0;
		}
		return versionCode;
	}

	public static String getVersionName(){
		String versionName = "0.0";
		try {
			versionName = AppContext
					.getAppContext()
					.getPackageManager()
					.getPackageInfo(AppContext.getAppContext().getPackageName(),
							0).versionName;
		} catch (PackageManager.NameNotFoundException ex) {
			versionName = "0.0";
		}
		return versionName;
	}

	public static String getPollutionLevel(int value){
		String level[]={"优","良","轻度污染","中度污染","重度污染","严重污染"};
		int type = 0;
		if (value>50&&value<=100){
			type=1;
		}else if(value>100&&value<=150){
			type=2;
		}else if (value>150&&value<=200){
			type=3;
		}else if (value>200&&value<=300){
			type=4;
		}else if (value>300){
			type=5;
		}
		return level[type];
	}
}
