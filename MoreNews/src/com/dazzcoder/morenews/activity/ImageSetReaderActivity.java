package com.dazzcoder.morenews.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.http.LoadControler;
import com.android.http.RequestManager;
import com.android.http.RequestManager.RequestListener;
import com.dazzcoder.morenews.AppManager;
import com.dazzcoder.morenews.R;
import com.dazzcoder.morenews.adapter.ImageSetFragmentPagerAdapter;
import com.dazzcoder.morenews.base.BaseActivity;
import com.dazzcoder.morenews.bean.ImageNews;
import com.dazzcoder.morenews.parser.ImageSetJsonParser;
import com.dazzcoder.morenews.parser.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 图片新闻阅读类
 */
public class ImageSetReaderActivity extends BaseActivity implements RequestListener, OnClickListener {
	private LoadControler mLoadControler = null;
	private final String API_URL = "http://c.m.163.com/photo/api/set/%s/%s.json";
	private String commit = "%d跟帖";
	String url;
	private String id[];
	ViewPager mViewPager;
	ImageSetFragmentPagerAdapter adapter;
	private TextView mImageTitle, mImageNum, mImageContent;
	private ImageButton mBack;
	private RelativeLayout mActionbar;
	private RelativeLayout mImageSummy;
	private boolean isVisible = true;
	TextView mImageButton;
	private ImageNews imageSet;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Bundle bundle = getIntent().getExtras();
		id = bundle.getStringArray("photosetID");
		url = String.format(API_URL, id[0], id[1]);
		Log.d("IMG URL", url);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_reader);
		mViewPager = (ViewPager) findViewById(R.id.mReaderViewPager);
		mBack = (ImageButton) findViewById(R.id.imagereader_back);
		mBack.setOnClickListener(this);

		mImageTitle = (TextView) findViewById(R.id.image_title);
		mImageNum = (TextView) findViewById(R.id.image_num);
		mImageButton = (TextView) findViewById(R.id.comment_num);
		mImageContent = (TextView) findViewById(R.id.image_content);
		mActionbar = (RelativeLayout) findViewById(R.id.image_actionbar);
		mImageSummy = (RelativeLayout) findViewById(R.id.image_dec_layout);
		getDocContent();
	}

	public void getDocContent() {
		Log.d("API", url);
		mLoadControler = RequestManager.getInstance().get(url, this, 0);
	}

	@Override
	public void onError(String arg0, String arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRequest() {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	@Override
	public void onSuccess(String arg0, Map<String, String> arg1, String arg2, int arg3) {
		// TODO Auto-generated method stub
		Log.d("", arg0);
		JsonParser jsonParser = new ImageSetJsonParser();
		switch (arg3) {
		case 0:
			imageSet = (ImageNews) jsonParser.parseJson(arg0);
			initViewPager();
			initText();
			initCommit();
			break;
		case 1:
			try {
				JSONObject jsonObject = new JSONObject(arg0);
				int comment_num = jsonObject.getInt("prcount") + jsonObject.getInt("votecount");
				mImageButton.setText(String.format(commit, comment_num));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}

	}

	// ��ʼ��ViewPager���
	private void initViewPager() {
		// TODO Auto-generated method stub
		adapter = new ImageSetFragmentPagerAdapter(getSupportFragmentManager(), imageSet.getImages());
		mViewPager.setOffscreenPageLimit(10);
		mViewPager.setAdapter(adapter);
		mViewPager.setOnPageChangeListener(pageListener);
	}

	private void initCommit() {
		// TODO Auto-generated method stub
		mLoadControler = RequestManager.getInstance().get(imageSet.getCommenturl(), this, 1);
	}

	private void initText() {
		// TODO Auto-generated method stub
		HashMap<String, String> map = imageSet.getImages().get(0);
		String a = "%d/%d";
		String num = String.format(a, 1, imageSet.getAllnum());
		mImageTitle.setText(map.get("title"));
		mImageNum.setText(num);
		mImageContent.setText(map.get("content"));
	}

	public void setFullScreen() {
		if (isVisible) {
			isVisible = false;
			mActionbar.setVisibility(View.GONE);
			mImageSummy.setVisibility(View.GONE);
		} else {
			isVisible = true;
			mActionbar.setVisibility(View.VISIBLE);
			mImageSummy.setVisibility(View.VISIBLE);
		}
	}

	public OnPageChangeListener pageListener = new OnPageChangeListener() {

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
			HashMap<String, String> map = imageSet.getImages().get(position);
			String a = "%d/%d";
			String num = String.format(a, position + 1, imageSet.getAllnum());
			mImageTitle.setText(map.get("title"));
			mImageNum.setText(num);
			mImageContent.setText(map.get("content"));
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		finish();
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
