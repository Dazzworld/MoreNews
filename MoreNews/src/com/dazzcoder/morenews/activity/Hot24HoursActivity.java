package com.dazzcoder.morenews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.http.LoadControler;
import com.android.http.RequestManager;
import com.android.http.RequestManager.RequestListener;
import com.dazzcoder.morenews.AppManager;
import com.dazzcoder.morenews.R;
import com.dazzcoder.morenews.adapter.NewsListAdapter;
import com.dazzcoder.morenews.base.BaseActivity;
import com.dazzcoder.morenews.bean.News;
import com.dazzcoder.morenews.parser.JsonParser;
import com.dazzcoder.morenews.parser.NewsListJsonParser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * 24小时热点新闻
 */
public class Hot24HoursActivity extends AppCompatActivity implements RequestListener {

	private final String POST_URL = "http://c.m.163.com/nc/article/list/T1429173683626/0-20.html";
	private final String ID = "T1429173683626";
	private LoadControler mLoadControler = null;
	ListView mHotNewsList;
	ArrayList<News> list = new ArrayList<>();

	NewsListAdapter adapter;

	RelativeLayout mLoadingLayout;
	ImageButton mReloadButton;
	Toolbar toolbar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_24hourshotnews);
		AppManager.getAppManager().addActivity(this);
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mHotNewsList = (ListView) findViewById(R.id.hotnews_listview);
		mLoadingLayout = (RelativeLayout) findViewById(R.id.loadanima);
		mReloadButton = (ImageButton) findViewById(R.id.reload_err);
		mReloadButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getList();
			}
		});
		getList();
		adapter = new NewsListAdapter(list, this, R.layout.item_newslist);
		mHotNewsList.setAdapter(adapter);
		mHotNewsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				startReaderActivity(position, false);
			}
		});
	}

	public void getList(){
		
		mLoadControler = RequestManager.getInstance().get(POST_URL, this, 0);
	}
	public void startReaderActivity(int position, boolean isHead) {
		News news = list.get(position);
		Intent i = null;
		Bundle b = new Bundle();
		if (news.isPhotoSet()) {
			i = new Intent(this, ImageSetReaderActivity.class);
			b.putStringArray("photosetID", news.getPhotosetID());
		} else {
			i = new Intent(this, ReaderActivity.class);
			b.putString("id", news.getDocid());

		}
		i.putExtras(b);
		startActivity(i);
	}

	@Override
	public void onError(String arg0, String arg1, int arg2) {
		// TODO Auto-generated method stub
		mReloadButton.setVisibility(View.VISIBLE);
	}

	@Override
	public void onRequest() {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	@Override
	public void onSuccess(String arg0, Map<String, String> arg1, String arg2, int arg3) {
		// TODO Auto-generated method stub

		mLoadingLayout.setVisibility(View.GONE);
		JsonParser parser = new NewsListJsonParser(ID);
		list.addAll((Collection<? extends News>) parser.parseJson(arg0));
		adapter.notifyDataSetChanged();

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
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
			case android.R.id.home:
				finish();
				break;
		}
		return true;
	}
}
