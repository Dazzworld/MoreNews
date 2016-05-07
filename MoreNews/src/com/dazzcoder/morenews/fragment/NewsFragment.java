package com.dazzcoder.morenews.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.http.LoadControler;
import com.android.http.RequestManager;
import com.android.http.RequestManager.RequestListener;
import com.bigkoo.convenientbanner.CBViewHolderCreator;
import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.ConvenientBanner.Transformer;
import com.dazzcoder.morenews.R;
import com.dazzcoder.morenews.activity.ImageSetReaderActivity;
import com.dazzcoder.morenews.activity.ReaderActivity;
import com.dazzcoder.morenews.adapter.NetworkImageHolderView;
import com.dazzcoder.morenews.adapter.NewsListAdapter;
import com.dazzcoder.morenews.bean.News;
import com.dazzcoder.morenews.bean.NewsClassification;
import com.dazzcoder.morenews.cache.CacheManager;
import com.dazzcoder.morenews.cache.CacheObject;
import com.dazzcoder.morenews.db.FavoriteDataBase;
import com.dazzcoder.morenews.parser.HeadLineJsonParser;
import com.dazzcoder.morenews.parser.JsonParser;
import com.dazzcoder.morenews.parser.NewsListJsonParser;
import com.dazzcoder.morenews.utils.Contacts;
import com.dazzcoder.morenews.view.CircleRefreshLayout;
import com.dazzcoder.morenews.view.PulltoLoadMoreView;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;
import in.srain.cube.views.ptr.util.PtrLocalDisplay;

public class NewsFragment extends BaseFragment implements RequestListener{

	public static final String TAG = "NewsFragment";
	private String name;
	private String endWiths = "%d-%d.html";
	private String API_URL;
	private String rel_url;
	private String ID;
	private int start = 0;
	private int end = 20;
	private ListView mNewsList;
	ArrayList<News> newslist = new ArrayList<>();
	List<News> headline = new ArrayList<>();
	private NewsListAdapter mAdapter;
	private LoadControler mLoadControler = null;
	ConvenientBanner<News> convenientBanner;
	RelativeLayout mLoadingLayout;
	ImageButton mReloadButton;
	PulltoLoadMoreView loadMoreView;
	PtrFrameLayout refresh_layout;
	int position = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Bundle args = getArguments();
		API_URL = args != null ? args.getString("apiurl") : "";
		rel_url = API_URL + String.format(endWiths, start, end);
		ID = args != null ? args.getString("id") : "";
		name = args != null ? args.getString("name") : "";
		super.onCreate(savedInstanceState);
	}

	@Override
	protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_news, container, false);

		// mProgressBar.setIndeterminateDrawable(new
		// ChromeFloatingCirclesDrawable.Builder(getActivity()).build());
		mNewsList = (ListView) view.findViewById(R.id.news_list);
		refresh_layout = (PtrFrameLayout) view.findViewById(R.id.refresh_layout);
		initRefreshLayout();
		mLoadingLayout = (RelativeLayout) view.findViewById(R.id.loadanima);
		mReloadButton = (ImageButton) view.findViewById(R.id.reload_err);
		mReloadButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getDocList();
			}
		});
		mAdapter = new NewsListAdapter(newslist, getActivity(), R.layout.item_newslist);

		initHeadline();
		initFooterView();
		mNewsList.setAdapter(mAdapter);
		mNewsList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				startReaderActivity(position, false);
			}
		});

		return view;
	}

	private void initRefreshLayout() {
		final MaterialHeader header = new MaterialHeader(getContext());
		int[] colors = getResources().getIntArray(R.array.google_colors);
		header.setColorSchemeColors(colors);
		header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
		header.setPadding(0, PtrLocalDisplay.dp2px(15), 0, PtrLocalDisplay.dp2px(10));
		header.setPtrFrameLayout(refresh_layout);

		refresh_layout.setLoadingMinTime(1000);
		refresh_layout.setDurationToCloseHeader(1500);
		refresh_layout.setHeaderView(header);
		refresh_layout.addPtrUIHandler(header);
		refresh_layout.postDelayed(new Runnable() {
			@Override
			public void run() {
				refresh_layout.autoRefresh(false);
			}
		}, 100);

		refresh_layout.setPtrHandler(new PtrHandler() {
			@Override
			public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
				if (mAdapter.getCount() == 0 || mNewsList == null) {
					return true;
				}
				//CLog.d("test", "checkCanDoRefresh: %s %s", mListView.getFirstVisiblePosition(), mListView.getChildAt(0).getTop());
				return mNewsList.getFirstVisiblePosition() == 0 && mNewsList.getChildAt(0).getTop() == 0;
			}

			@Override
			public void onRefreshBegin(final PtrFrameLayout frame) {
				getDocList();
			}
		});
	}

	public void initFooterView(){
		loadMoreView = new PulltoLoadMoreView(getActivity());
		loadMoreView.setOnLoadMoreListener(new PulltoLoadMoreView.OnLoadMoreListener() {
			@Override
			public void onStart() {
				loadMore();
			}

			@Override
			public void onFinish() {

			}
		});
		mNewsList.addFooterView(loadMoreView);
	}

	protected void loadMore() {
		// TODO Auto-generated method stub
		rel_url = API_URL + String.format(endWiths,end + 20, end);
		end = end + 20;
		mLoadControler = RequestManager.getInstance().get(rel_url, this, true, 1);
	}

	private  void startReaderActivity(int position, boolean isHead) {
		News news = isHead ? headline.get(position) : newslist.get(position - 1);
		Intent i = null;
		Bundle b = new Bundle();
		if (news.isPhotoSet()) {
			i = new Intent(getActivity(), ImageSetReaderActivity.class);
			b.putStringArray("photosetID", news.getPhotosetID());
		} else {
			new FavoriteDataBase(getActivity(), Contacts.HISTORY).insert(news);
			i = new Intent(getActivity(), ReaderActivity.class);
			b.putString("id", news.getDocid());
		}
		i.putExtras(b);
		startActivity(i);
	}

	private void initHeadline() {
		// TODO Auto-generated method stub
		convenientBanner = new ConvenientBanner<>(getActivity(), true);
		AbsListView.LayoutParams params = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
				PtrLocalDisplay.dp2px(200));
		convenientBanner.setLayoutParams(params);
		convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {

			@Override
			public NetworkImageHolderView createHolder() {
				// TODO Auto-generated method stub
				return new NetworkImageHolderView(R.layout.headline, headline);
			}
		}, headline).setPageIndicator(new int[] { R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused })

				.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)

				.setPageTransformer(Transformer.DefaultTransformer);
		convenientBanner.setOnItemClickListener(new com.bigkoo.convenientbanner.OnItemClickListener() {

			@Override
			public void onItemClick(int position) {
				// TODO Auto-generated method stub
				startReaderActivity(position, true);
			}
		});
		mNewsList.addHeaderView(convenientBanner);
	}

	private void getDocList() {
		rel_url = API_URL + String.format(endWiths, start, end);
		mLoadControler = RequestManager.getInstance().get(rel_url, this, true, 0);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		if (CacheManager.isExistDataCache(getActivity(), ID + ".DAT")) {
			new ReadCacheTask(getActivity()).execute(ID + ".DAT");
		} else {
			getDocList();
		}

	}

	@Override
	protected void setDefaultFragmentTitle(String title) {

	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	@Override
	public void onError(String arg0, String arg1, int actionId) {
		// TODO Auto-generated method stub
		
		switch (actionId) {
		case 0:
			mReloadButton.setVisibility(View.VISIBLE);
			break;

		case 1:
			Toast.makeText(getActivity(), "刷新成功", Toast.LENGTH_SHORT).show();
			break;
		}
		
	}

	@Override
	public void onRequest() {
		// TODO Auto-generated method stub

	}

	@SuppressWarnings("unchecked")
	@Override
	public void onSuccess(String response, Map<String, String> headers, String url, int actionId) {
		// TODO Auto-generated method stub
		Log.d("onSuccess", "--Success ");
		Log.d("actionId", ":" + actionId);
		JsonParser parser = new NewsListJsonParser(ID);
		JsonParser hlparser = new HeadLineJsonParser(ID);
		switch (actionId) {
		case 0:
			mLoadingLayout.setVisibility(View.GONE);
			mNewsList.setVisibility(View.VISIBLE);
			newslist.clear();
			headline.clear();
			headline.addAll((Collection<? extends News>) hlparser.parseJson(response));
			newslist.addAll((Collection<? extends News>) parser.parseJson(response));
			refresh_layout.refreshComplete();
			if (getUserVisibleHint()){
				Toast.makeText(getActivity(),"刷新成功",Toast.LENGTH_SHORT).show();
			}
			break;
		case 1:

			List<News> news = (List<News>) parser.parseJson(response);
			Log.d(TAG, "onSuccess: "+news.size());
			newslist.addAll(news);
			loadMoreView.finishLoading();
			break;
		}
		CacheObject cache = new CacheObject(new Object[] { headline, newslist });
		saveCache(cache, ID + ".DAT");
		notifyAllDataSetChanged();
	}

	public void notifyAllDataSetChanged() {

		if (mAdapter != null) {
			mAdapter.notifyDataSetChanged();
		}
		if (convenientBanner != null) {
			convenientBanner.notifyDataSetChanged();
		}
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		convenientBanner.stopTurning();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		convenientBanner.startTurning(5000);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (mLoadControler != null) {
			mLoadControler.cancel();
		}
	}

	public void saveCache(CacheObject cache, String key) {
		new SaveCacheTask(getActivity(), cache, key).execute();
	}

	@SuppressWarnings("unchecked")
	public void restoreCache(CacheObject data) {
		mLoadingLayout.setVisibility(View.GONE);
		mNewsList.setVisibility(View.VISIBLE);
		headline.clear();
		headline.addAll((Collection<? extends News>) data.getData()[0]);
		newslist.clear();
		newslist.addAll((Collection<? extends News>) data.getData()[1]);
		notifyAllDataSetChanged();
	}

	public class SaveCacheTask extends AsyncTask<Void, Void, Void> {

		private final WeakReference<Context> mContext;
		private final Serializable seri;
		private final String key;

		private SaveCacheTask(Context context, Serializable seri, String key) {
			mContext = new WeakReference<Context>(context);
			this.seri = seri;
			this.key = key;
		}

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			CacheManager.saveObject(mContext.get(), seri, key);
			return null;
		}
	}

	public class ReadCacheTask extends AsyncTask<String, Void, CacheObject> {
		private final WeakReference<Context> mContext;

		private ReadCacheTask(Context context) {
			mContext = new WeakReference<Context>(context);
		}

		@Override
		protected CacheObject doInBackground(String... params) {
			// TODO Auto-generated method stub
			if (mContext.get() != null) {
				CacheObject cache = (CacheObject) CacheManager.readObject(mContext.get(), params[0]);
				return cache;
			}
			return null;
		}

		@Override
		protected void onPostExecute(CacheObject result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			restoreCache(result);
		}

	}
}
