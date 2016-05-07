package com.dazzcoder.morenews.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.http.LoadControler;
import com.android.http.RequestManager;
import com.android.http.RequestManager.RequestListener;
import com.dazzcoder.morenews.R;
import com.dazzcoder.morenews.activity.ActivitySelectCity;
import com.dazzcoder.morenews.activity.NewsActivity;
import com.dazzcoder.morenews.bean.PM25;
import com.dazzcoder.morenews.bean.Weather;
import com.dazzcoder.morenews.model.Utils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class QuickOptionDialog extends Dialog  implements RequestListener{

	int actionBarHighet;
	int statusBarHight;
	ImageView mDialogIndicator;
	
	private LoadControler mLoadControler = null;
	Weather weather;
	String url = "http://c.m.163.com/nc/weather/" + Utils.encodeHtml("湖北|武汉");
	
	private TextView mDateText, mPM25Text, mClimateWind, mLocation, mTemperature,mPollution;

	private ImageButton mSelectCity;
	public QuickOptionDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		// TODO Auto-generated constructor stub
	}

	@SuppressLint("InflateParams")
	public QuickOptionDialog(final Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
		View v = LayoutInflater.from(context).inflate(R.layout.dialog_option, null);
		mDateText = (TextView) v.findViewById(R.id.date);
		mPM25Text = (TextView) v.findViewById(R.id.pm2_5);
		mClimateWind = (TextView) v.findViewById(R.id.climate);
		mTemperature = (TextView) v.findViewById(R.id.temperatures);
		mLocation = (TextView) v.findViewById(R.id.location);
		mPollution = (TextView) v.findViewById(R.id.pollution);
		mSelectCity = (ImageButton) v.findViewById(R.id.select_city);
		mSelectCity.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(context, ActivitySelectCity.class);
				context.startActivity(i);
			}
		});
		/*v.findViewById(R.id.dialog_outside).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				QuickOptionDialog.this.dismiss();
			}

		});*/
		setContentView(v);
		weather = new Weather();
		mLoadControler = RequestManager.getInstance().get(url, this, 1);
	}

	public QuickOptionDialog(Context context) {
		this(context, R.style.CustomDialog);
		mLocation.setText(((NewsActivity)context).getLocation());
		actionBarHighet = context.getResources().getDimensionPixelSize(R.dimen.abc_action_bar_default_height_material);
		statusBarHight = new SystemBarTintManager((Activity) context).getConfig().getStatusBarHeight();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		getWindow().setGravity(Gravity.BOTTOM);
		WindowManager m = getWindow().getWindowManager();
		Display d = m.getDefaultDisplay();
		WindowManager.LayoutParams p = getWindow().getAttributes();
		p.width = d.getWidth();
		p.height = d.getHeight() - statusBarHight;

		getWindow().setAttributes(p);
	}
	
	protected void initText() {
		// TODO Auto-generated method stub
		PM25 pm2d5 = weather.getPm25();
		mDateText.setText(weather.getDate() + " " + weather.getWeek());
		mPM25Text.setText("PM2.5: " + pm2d5.getPm25());
		mClimateWind.setText(weather.getClimate() + " " + weather.getWind());
		mTemperature.setText(weather.getRt_temperature());
		mPollution.setText(Utils.getPollutionLevel(pm2d5.getPm25()));
	}

	@Override
	public void onError(String arg0, String arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRequest() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSuccess(String arg0, Map<String, String> arg1, String arg2, int arg3) {
		// TODO Auto-generated method stub
		try {
			Log.d("Service onSuccess", arg0);
			
			JSONObject jsonObject = new JSONObject(arg0);
			weather.setDate(jsonObject.getString("dt"));
			weather.setRt_temperature(jsonObject.getString("rt_temperature"));
			JSONArray weatherArray = jsonObject.getJSONArray("湖北|武汉");
			JSONObject PM2D5 = jsonObject.getJSONObject("pm2d5");
			PM25 pm2d5 = new PM25();
			pm2d5.setPm25(Integer.valueOf(PM2D5.getString("pm2_5")));
			pm2d5.setAqi(Integer.valueOf(PM2D5.getString("aqi")));
			pm2d5.setBg1(PM2D5.getString("nbg1"));
			pm2d5.setBg2(PM2D5.getString("nbg2"));
			JSONObject today = weatherArray.getJSONObject(0);
			weather.setClimate(today.getString("climate"));
			weather.setWind(today.getString("wind"));
			weather.setWeek(today.getString("week"));
			weather.setTemperature(today.getString("temperature"));
			weather.setLongli(today.getString("nongli"));
			weather.setPm25(pm2d5);
			Weather day = new Weather();
			JSONObject nextday = weatherArray.getJSONObject(1);
			day.setClimate(nextday.getString("climate"));
			day.setWind(nextday.getString("wind"));
			day.setWeek(nextday.getString("week"));
			day.setTemperature(nextday.getString("temperature"));
			day.setLongli(nextday.getString("nongli"));
			nextday = weatherArray.getJSONObject(2);
			Weather day2 = new Weather();
			day2.setClimate(nextday.getString("climate"));
			day2.setWind(nextday.getString("wind"));
			day2.setWeek(nextday.getString("week"));
			day2.setTemperature(nextday.getString("temperature"));
			day2.setLongli(nextday.getString("nongli"));
			day.setNextDay(day2);
			weather.setNextDay(day);
			initText();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


}
