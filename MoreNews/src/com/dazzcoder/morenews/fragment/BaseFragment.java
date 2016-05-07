package com.dazzcoder.morenews.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {

	public String fragmentTitle;

	private boolean isVisible;

	private boolean isPrepared;

	private boolean isFirstLoad = true;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		isFirstLoad = true;
		View view = initViews(inflater, container, savedInstanceState);
		isPrepared = true;
		lazyLoad();
		return view;
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (getUserVisibleHint()) {
			isVisible = true;
			onVisible();
		} else {
			isVisible = false;
			onInvisible();
		}
	}


	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		if (!hidden) {
			isVisible = true;
			onVisible();
		} else {
			isVisible = false;
			onInvisible();
		}
	}

	protected void onVisible() {
		lazyLoad();
	}

	protected void onInvisible() {
	}

	protected void lazyLoad() {
		if (!isPrepared || !isVisible || !isFirstLoad) {
			return;
		}
		isFirstLoad = false;
		initData();
	}

	protected abstract View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

	protected abstract void initData();

	public String getTitle() {
		if (null == fragmentTitle) {
			setDefaultFragmentTitle(null);
		}
		return TextUtils.isEmpty(fragmentTitle) ? "" : fragmentTitle;
	}

	public void setTitle(String title) {
		fragmentTitle = title;
	}


	protected abstract void setDefaultFragmentTitle(String title);
}