package com.dazzcoder.morenews.activity;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.http.LoadControler;
import com.android.http.RequestManager;
import com.android.http.RequestManager.RequestListener;
import com.dazzcoder.morenews.AppManager;
import com.dazzcoder.morenews.R;
import com.dazzcoder.morenews.base.BaseActivity;
import com.dazzcoder.morenews.bean.News;
import com.dazzcoder.morenews.db.FavoriteDataBase;
import com.dazzcoder.morenews.model.Utils;
import com.dazzcoder.morenews.utils.Contacts;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 普通新闻阅读界面
 */
public class ReaderActivity extends BaseActivity implements RequestListener {
	private LoadControler mLoadControler = null;
	public static final String JSON_URL = "http://c.m.163.com/nc/article/%s/full.html";
	String id;
	String body = "";
	String img = "<img width=\"%f\" height=\"%f\" src=\"%s\" alt=\"%s\">";
	WebView webView;
	News news = new News();
	int mScreenWidth = 0;
	int mScreenHight = 0;
	private String url;
	int dd = 0;

	RelativeLayout mLoadingLayout;
	ImageButton mReloadButton,favoriteButton;
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Bundle bundle = getIntent().getExtras();
		id = bundle.getString("id");
		url = String.format(JSON_URL, id);
		super.onCreate(savedInstanceState);

		setContentView(R.layout.news_reader);
		AppManager.getAppManager().addActivity(this);
		initActionBar();
		mLoadingLayout = (RelativeLayout) findViewById(R.id.loadanima);
		mReloadButton = (ImageButton) findViewById(R.id.reload_err);
		mReloadButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getDocContent();
			}
		});
		webView = (WebView) findViewById(R.id.news_webview);

		webView.getSettings().setSupportZoom(false);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setUseWideViewPort(true);
		webView.loadUrl("file:///android_asset/newspage.html");
		webView.setWebViewClient(new MyWebViewClient());
		mScreenWidth = Utils.getWindowsWidth(this);
		mScreenHight = Utils.getWindowsHeight(this);
		
	}

	private void initActionBar() {
		setTranslucentStatus(false);
		OnClickListener onClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
					case R.id.back:
						finish();
						break;
					case R.id.add_favorite:
						FavoriteDataBase dataBase = new FavoriteDataBase(ReaderActivity.this, Contacts.FAVORITE);
						if (!dataBase.exists(news)){
							dataBase.insert(news);
							Toast.makeText(ReaderActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
						}else{
							Toast.makeText(ReaderActivity.this,"已存在,不用重复添加",Toast.LENGTH_SHORT).show();
						}
						break;
				}
			}
		};
		favoriteButton = (ImageButton) findViewById(R.id.add_favorite);
		favoriteButton.setOnClickListener(onClickListener);
		findViewById(R.id.back).setOnClickListener(onClickListener);
	}

	class MyWebViewClient extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			if (dd == 0){
				getDocContent();
				dd++;
			}
		}
	}

	public void getDocContent() {
		Log.d("API", url);
		mLoadControler = RequestManager.getInstance().get(url, this, 0);
	}

	@Override
	public void onError(String errorMsg, String url, int actionId) {
		// TODO Auto-generated method stub
		mReloadButton.setVisibility(View.VISIBLE);
	}

	@Override
	public void onRequest() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess(String response, Map<String, String> headers, String url, int actionId) {
		// TODO Auto-generated method stub
		
		Log.d("json", response);
		try {
			mLoadingLayout.setVisibility(View.GONE);
			webView.setVisibility(View.VISIBLE);
			JSONObject jsonObject = new JSONObject(response).getJSONObject(id);
			body = jsonObject.getString("body");

			if (jsonObject.has("link") && jsonObject.getJSONArray("link").length() >= 2) {
				JSONObject link = jsonObject.getJSONArray("link").getJSONObject(0);
				webView.loadUrl(link.getString("href") + ".html");
				Log.d("link", link.getString("href") + ".html");
				return;
			}
			if (jsonObject.has("img")) {
				String pixel[] = { String.valueOf(mScreenWidth), String.valueOf(mScreenHight) };
				JSONArray jsonArray = jsonObject.getJSONArray("img");

				List<String> imglist = new ArrayList<String>();
				for (int i = 0; i < jsonArray.length(); i++) {
					// Log.d("body--", jsonArray.length()+"size");
					JSONObject jsonObject2 = jsonArray.getJSONObject(i);
					if (jsonObject2.has("pixel")) {
						pixel = jsonObject2.getString("pixel").split("\\*");
					}

					Object data[] = new Object[5];
					/// data[0]=onload;
					float w = Float.parseFloat(pixel[0]);
					float h = Float.parseFloat(pixel[1]);
					// Log.d("body--", "w1:"+mScreenWidth+"H1:"+mScreenHight);
					float width = (w / (w + h)) * mScreenWidth;
					float height = (h / (w + h)) * mScreenHight;
					data[0] = width;
					data[1] = height;
					data[2] = jsonObject2.getString("src");
					data[3] = jsonObject2.getString("alt");

					imglist.add(String.format(img, data));

				}

				// Toast.makeText(this, jsonArray.length()+"--size",
				// Toast.LENGTH_SHORT).show();
				for (int i = 0; i < imglist.size(); i++) {
					// Log.d("TAG--", "index"+body.indexOf("<!--IMG#"+i+"-->"));
					// Log.d("body--", imglist.get(i));
					body = body.replace("<!--IMG#" + i + "-->", imglist.get(i));
				}
			}
			news.setTitle(jsonObject.getString("title"));
			news.setPubDate(jsonObject.getString("ptime"));
			news.setDec(jsonObject.getString("source"));
			favoriteButton.setVisibility(View.VISIBLE);
			Log.d("body--", body);
			webView.loadUrl("javascript:setTitle('" + news.getTitle() + "')");
			webView.loadUrl("javascript:setDec('" + news.getPubDate() + "','" + news.getDec() + "')");
			webView.loadUrl("javascript:setBody('" + body + "')");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.reader_menu, menu);
		return true;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mLoadControler != null) {
			mLoadControler.cancel();
		}
		AppManager.getAppManager().finishActivity(this);
	}

}
