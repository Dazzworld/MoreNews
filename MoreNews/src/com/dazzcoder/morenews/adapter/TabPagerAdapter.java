package com.dazzcoder.morenews.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.ViewGroup;

import com.dazzcoder.morenews.fragment.NewsFragment;

import java.util.ArrayList;

public class TabPagerAdapter extends FragmentPagerAdapter {

	ArrayList<NewsFragment> list;
	FragmentManager fm;

	FragmentTransaction transaction;
	public TabPagerAdapter(FragmentManager fm, ArrayList<NewsFragment> list) {
		super(fm);
		this.fm = fm;
		// TODO Auto-generated constructor stub
		this.list = list;
	}

	@Override
	public Fragment getItem(int arg0) {
		// TODO Auto-generated method stub
		Log.d("TabPagerAdapter", "getItem: ");
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return list.get(position).getTitle();
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {

		NewsFragment f = (NewsFragment) super.instantiateItem(container, position);
		f.setPosition(position);
		return f;
	}

/*	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		NewsFragment f = (NewsFragment) object;
		if (transaction == null){
			transaction = fm.beginTransaction();
		}
		container.removeView(f.getView());
		transaction.remove(f);
		//super.destroyItem(container, position, object);
	}*/

	public void setFragments() {
		if (this.list != null) {
			FragmentTransaction ft = fm.beginTransaction();
			for (Fragment f : list) {
				ft.remove(f);
			}
			ft.commitAllowingStateLoss();
			ft = null;
			fm.executePendingTransactions();
		}
		notifyDataSetChanged();
	}

	@Override
	public int getItemPosition(Object object) {
		/*NewsFragment f = (NewsFragment) object;
		int position = f.getPosition();
		if (f.getTitle().equals(list.get(position).getTitle())){
			Log.d("TABADPATER", "getItemPosition: POSITION_UNCHANGED");
			return POSITION_UNCHANGED;
		}else {
			Log.d("TABADPATER", "getItemPosition: POSITION_NONE");
			return POSITION_NONE;
		}*/
		return POSITION_NONE;
	}
}
