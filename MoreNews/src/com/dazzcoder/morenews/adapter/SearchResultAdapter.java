package com.dazzcoder.morenews.adapter;/**
 * Created by zc on 2016/1/4.
 */

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dazzcoder.morenews.R;
import com.dazzcoder.morenews.bean.News;

import java.util.List;

/**
 * User: CZC(867135418@QQ.COM)
 * Date: 2016-01-04
 * Time: 18:12
 * ReadMe:
 */

public class SearchResultAdapter extends BaseAdapter {

    Context mContext;
    List<News> list;
    int layoutRes;

    public SearchResultAdapter(Context mContext, List<News> list, int layoutRes) {
        this.mContext = mContext;
        this.list = list;
        this.layoutRes = layoutRes;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    class ViewHolder{
        TextView name,date;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        News news = list.get(position);
        View v = null;
        ViewHolder vh = null;
        if (convertView!=null){
            v=convertView;
            vh = (ViewHolder) v.getTag();
        }else {
            v= LayoutInflater.from(mContext).inflate(layoutRes,parent,false);
            vh = new ViewHolder();
            vh.name = (TextView) v.findViewById(R.id.search_result_name);
            vh.date = (TextView) v.findViewById(R.id.search_result_date);
            v.setTag(vh);
        }
        vh.name.setText(Html.fromHtml(news.getTitle()));
        vh.date.setText(news.getPubDate());
        return v;
    }
}
