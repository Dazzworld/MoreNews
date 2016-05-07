package com.dazzcoder.morenews.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.CBPageAdapter;
import com.bm.library.PhotoView;
import com.dazzcoder.morenews.AppContext;
import com.dazzcoder.morenews.R;
import com.dazzcoder.morenews.bean.News;
import com.dazzcoder.morenews.parser.NewsImageListener;

import java.util.List;

public class NetworkImageHolderView implements CBPageAdapter.Holder<News> {
	private PhotoView imageView;
	private TextView title;
	private int layoutid;
	private List<News> list;

	public NetworkImageHolderView(int layoutid, List<News> list) {
		super();
		this.layoutid = layoutid;
		this.list = list;
	}

	@Override
	public View createView(Context context) {
		// TODO Auto-generated method stub
		View v = LayoutInflater.from(context).inflate(layoutid, null);
		imageView = (PhotoView) v.findViewById(R.id.headline_img);
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		title = (TextView) v.findViewById(R.id.headline_title);
		return v;
	}

	@Override
	public void UpdateUI(Context context, int position, News data) {
		// TODO Auto-generated method stub
		News news = list.get(position);
		imageView.setImageResource(R.drawable.ic_launcher);
		AppContext.getImageLoader().get(data.getImageUrl(), new NewsImageListener(data.getImageUrl(), imageView));
		title.setText(news.getTitle());

	}

}
