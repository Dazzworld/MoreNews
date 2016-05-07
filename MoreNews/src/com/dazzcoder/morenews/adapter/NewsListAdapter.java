package com.dazzcoder.morenews.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bm.library.PhotoView;
import com.dazzcoder.morenews.AppContext;
import com.dazzcoder.morenews.R;
import com.dazzcoder.morenews.bean.News;
import com.dazzcoder.morenews.parser.NewsImageListener;

import java.util.List;

public class NewsListAdapter extends BaseAdapter {

	List<News> list;
	Context contex;
	int layoutid;

	public NewsListAdapter(List<News> list, Context contex, int layoutid) {
		super();
		this.list = list;
		this.contex = contex;
		this.layoutid = layoutid;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	class ViewHolder {
		TextView title;
		TextView dec;
		PhotoView docimg;
		LinearLayout photo_layout;
		PhotoView photo[] = new PhotoView[3];
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view;
		final ViewHolder vh;
		News news = list.get(position);

		if (convertView != null) {
			view = convertView;
			vh = (ViewHolder) view.getTag();
		} else {
			view = LayoutInflater.from(contex).inflate(layoutid, parent, false);
			vh = new ViewHolder();
			vh.title = (TextView) view.findViewById(R.id.news_title);
			vh.dec = (TextView) view.findViewById(R.id.news_dec);
			vh.docimg = (PhotoView) view.findViewById(R.id.image_view);
			vh.photo_layout = (LinearLayout) view.findViewById(R.id.photo_layout);
			vh.photo[0] = (PhotoView) view.findViewById(R.id.photo1);
			vh.photo[1] = (PhotoView) view.findViewById(R.id.photo2);
			vh.photo[2] = (PhotoView) view.findViewById(R.id.photo3);
			view.setTag(vh);
		}
		if (news.isPhotoSet() && news.getImgurl().size() > 1) {
			vh.dec.setVisibility(View.GONE);
			vh.docimg.setVisibility(View.GONE);
			vh.photo_layout.setVisibility(View.VISIBLE);
			Log.d("size", "list" + news.getImgurl().size());
			for (int i = 0; i < news.getImgurl().size(); i++) {
				Log.d("position", "list" + i);
				requestUrl(news.getImgurl().get(i), vh.photo[i]);

			}
		} else {
			vh.photo_layout.setVisibility(View.GONE);
			vh.dec.setVisibility(View.VISIBLE);
			vh.docimg.setVisibility(View.VISIBLE);
			vh.dec.setText(news.getDec());
			requestUrl(news.getImgurl().get(0), vh.docimg);
		}
		vh.title.setText(news.getTitle());

		return view;
	}

	/**
	 * @param url
	 */
	private void requestUrl(final String url, PhotoView imgView) {
		if (url == null) {
			imgView.setImageResource(R.drawable.ic_default_adimage);
			return;
		}
		AppContext.getImageLoader().get(url, new NewsImageListener(url, imgView));
	}

}
