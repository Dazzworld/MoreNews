package com.dazzcoder.morenews.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.http.LoadControler;
import com.android.http.RequestManager;
import com.dazzcoder.morenews.R;
import com.dazzcoder.morenews.activity.AboutAcitvity;
import com.dazzcoder.morenews.activity.ActivitySelectCity;
import com.dazzcoder.morenews.activity.AppSettingsActivity;
import com.dazzcoder.morenews.activity.FavoriteActivity;
import com.dazzcoder.morenews.activity.NewsActivity;
import com.dazzcoder.morenews.activity.SearchActivity;
import com.dazzcoder.morenews.bean.PM25;
import com.dazzcoder.morenews.bean.Weather;
import com.dazzcoder.morenews.model.Utils;
import com.dazzcoder.morenews.utils.Contacts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class SlideLeftFragment extends Fragment  implements RequestManager.RequestListener,NavigationView.OnNavigationItemSelectedListener {

	private LoadControler mLoadControler = null;
	Weather weather;
	String url = "http://c.m.163.com/nc/weather/" + Utils.encodeHtml("湖北|武汉");

	private ImageButton mSelectCity;
	private TextView mDateText, mPM25Text, mClimateWind, mLocation, mTemperature,mPollution;
	private NavigationView mNavigationView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.dialog_option, container, false);
		mDateText = (TextView) v.findViewById(R.id.date);
		mPM25Text = (TextView) v.findViewById(R.id.pm2_5);
		mClimateWind = (TextView) v.findViewById(R.id.climate);
		mTemperature = (TextView) v.findViewById(R.id.temperatures);
		mLocation = (TextView) v.findViewById(R.id.location);
		mPollution = (TextView) v.findViewById(R.id.pollution);
		mSelectCity = (ImageButton) v.findViewById(R.id.select_city);
		mNavigationView = (NavigationView) v.findViewById(R.id.nav_view);
		mNavigationView.setNavigationItemSelectedListener(this);
		mSelectCity.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), ActivitySelectCity.class);
				getActivity().startActivity(i);
			}
		});
		weather = new Weather();
		mLoadControler = RequestManager.getInstance().get(url, this, 1);
		return v;
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		if (mLoadControler!=null){
			mLoadControler.cancel();
		}
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

	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case R.id.nav_search:
				startActivity(new Intent(getActivity(), SearchActivity.class));
				break;
			case R.id.nav_settings:
				startActivity(new Intent(getActivity(), AppSettingsActivity.class));
				break;
			case R.id.nav_favorite:
				Intent i  = new Intent(getActivity(), FavoriteActivity.class);
				Bundle b = new Bundle();
				b.putString("table", Contacts.FAVORITE);
				i.putExtras(b);
				startActivity(i);
				break;
			case R.id.nav_history:
				Intent i1  = new Intent(getActivity(), FavoriteActivity.class);
				Bundle b1 = new Bundle();
				b1.putString("table", Contacts.HISTORY);
				i1.putExtras(b1);
				startActivity(i1);
				break;
			case R.id.nav_about:
				startActivity(new Intent(getActivity(), AboutAcitvity.class));
				break;
		}
		return false;
	}
}
