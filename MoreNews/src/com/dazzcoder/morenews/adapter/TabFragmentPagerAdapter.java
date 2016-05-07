package com.dazzcoder.morenews.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.dazzcoder.morenews.fragment.NewsFragment;

import java.util.ArrayList;

public class TabFragmentPagerAdapter extends FragmentStatePagerAdapter {

	ArrayList<NewsFragment> list;

	FragmentManager fm;

	public TabFragmentPagerAdapter(FragmentManager fm) {
		super(fm);
		this.fm = fm;
	}

	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return list.get(position).getTitle();
	}

	@Override
	public Fragment getItem(int position) {
		return list.get(position);
	}

	public TabFragmentPagerAdapter(FragmentManager fm, ArrayList<NewsFragment> list) {
		super(fm);
		// TODO Auto-generated constructor stub
		this.fm = fm;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public int getItemPosition(Object object) {
		NewsFragment f = (NewsFragment) object;
		int position = f.getPosition();
		if (f.getTitle().equals(list.get(position).getTitle())){
			Log.d("TABADPATER", "getItemPosition: POSITION_UNCHANGED");
			return POSITION_UNCHANGED;
		}else {
			Log.d("TABADPATER", "getItemPosition: POSITION_NONE");
			return POSITION_NONE;
		}
	}
}
