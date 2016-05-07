package com.dazzcoder.morenews.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.dazzcoder.morenews.R;
import com.dazzcoder.morenews.adapter.SearchResultAdapter;
import com.dazzcoder.morenews.base.BaseActivity;
import com.dazzcoder.morenews.bean.News;
import com.dazzcoder.morenews.db.FavoriteDataBase;
import com.dazzcoder.morenews.model.Utils;
import com.dazzcoder.morenews.utils.Contacts;
import com.yydcdut.sdlv.Menu;
import com.yydcdut.sdlv.SlideAndDragListView;

import java.util.ArrayList;
import java.util.List;

public class FavoriteActivity extends AppCompatActivity {

	SlideAndDragListView mListView;
	TextView NoData;
	FavoriteDataBase dataBase;
	ArrayList<News> favoriteList = new ArrayList<>();
	SearchResultAdapter mAdapter;
	String TB = "";
	Toolbar toolbar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_favorite);
		Bundle b = getIntent().getExtras();
		TB = b.getString("table");
		toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		mListView = (SlideAndDragListView) findViewById(R.id.favorite_list);
		NoData = (TextView) findViewById(R.id.favorite_no_data);
		mAdapter = new SearchResultAdapter(this , favoriteList ,R.layout.search_result);
		initData();
	}

	private void initData() {
		Menu menu = new Menu((int)getResources().getDimension(R.dimen.small_list_item_height),new ColorDrawable(0xfff),false);
		menu.addItem(new com.yydcdut.sdlv.MenuItem.Builder().setWidth(90)
				.setDirection(com.yydcdut.sdlv.MenuItem.DIRECTION_RIGHT)
				.setText("删除").setTextSize(15).build());
		mListView.setMenu(menu);
		mListView.setAdapter(mAdapter);
		dataBase = new FavoriteDataBase(this, TB);
		final List<News> list = dataBase.query();
		if (list.size()<=0){
			NoData.setVisibility(View.VISIBLE);
			return;
		}
		favoriteList.addAll(list);
		mAdapter.notifyDataSetChanged();

		mListView.setOnSlideListener(new SlideAndDragListView.OnSlideListener() {
			@Override
			public void onSlideOpen(View view, View view1, int i, int i1) {

			}

			@Override
			public void onSlideClose(View view, View view1, int i, int i1) {

			}
		});
		mListView.setOnMenuItemClickListener(new SlideAndDragListView.OnMenuItemClickListener() {
			@Override
			public int onMenuItemClick(View view, int i, int i1, int i2) {
				switch (i2) {
					case com.yydcdut.sdlv.MenuItem.DIRECTION_RIGHT:
							News news =list.get(i);
							favoriteList.remove(i);
							dataBase.delete(news.getTitle());
							return Menu.ITEM_DELETE_FROM_BOTTOM_TO_TOP;
					default:
						return Menu.ITEM_NOTHING;
				}
			}
		});
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
