package com.dazzcoder.morenews.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dazzcoder.morenews.fragment.ImageSetFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ImageSetFragmentPagerAdapter extends FragmentPagerAdapter {
	List<HashMap<String, String>> list;

	public ImageSetFragmentPagerAdapter(FragmentManager fm, ArrayList<HashMap<String, String>> arrayList) {
		super(fm);
		this.list = arrayList;
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		ImageSetFragment fragment = new ImageSetFragment();
		Bundle data = new Bundle();
		data.putString("IMG", list.get(arg0).get("url"));

		fragment.setArguments(data);
		return fragment;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

}
