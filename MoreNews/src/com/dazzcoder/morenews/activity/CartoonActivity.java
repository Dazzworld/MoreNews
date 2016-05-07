package com.dazzcoder.morenews.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.dazzcoder.morenews.R;
import com.dazzcoder.morenews.base.BaseActivity;
import com.dazzcoder.morenews.fragment.NewsFragment;

/**
 *  漫画页面
 */
public class CartoonActivity extends BaseActivity {

	private final String POST_URL = "http://c.m.163.com/nc/article/list/T1444270454635/";
	private final String ID = "T1444270454635";
	FragmentManager fragmentManager;
	FragmentTransaction transaction;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cartoon);
		fragmentManager = getSupportFragmentManager();
		transaction = fragmentManager.beginTransaction();
		NewsFragment newsfragment = new NewsFragment();
		Bundle bundle = new Bundle();
		bundle.putString("apiurl", POST_URL);
		bundle.putString("id", ID);
		newsfragment.setArguments(bundle);
		transaction.add(R.id.cartoon_layout, newsfragment, "list").commit();
		transaction.hide(newsfragment).show(newsfragment);
	}

}
