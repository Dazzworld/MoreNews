package com.dazzcoder.morenews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.baidu.location.LocationClient;
import com.dazzcoder.morenews.AppManager;
import com.dazzcoder.morenews.R;
import com.dazzcoder.morenews.adapter.TabFragmentPagerAdapter;
import com.dazzcoder.morenews.base.BaseActivity;
import com.dazzcoder.morenews.bean.NewsClassification;
import com.dazzcoder.morenews.db.CategoryDateBase;
import com.dazzcoder.morenews.fragment.NewsFragment;
import com.dazzcoder.morenews.model.Utils;
import com.dazzcoder.morenews.view.QuickOptionDialog;

import java.util.ArrayList;

/**
 * 主页面
 */
public class NewsActivity extends BaseActivity {

    public static final String TAG = "NewsActivity";
    private String location = "";

    private LocationClient locationClient = null;

    public static final int TABDATA_CHANGED = 11;
    private ArrayList<NewsClassification> tabs = new ArrayList<>();
    private ArrayList<NewsFragment> fragments = new ArrayList<>();
    private ImageButton mNewsClassEdit;
    public ImageView mShadeImageLeft;
    public ImageView mShadeImageRight;
    private int mScreenWidth = 0;

    private ViewPager mViewPager;
    private PagerSlidingTabStrip mIndicator;

    TabFragmentPagerAdapter mAdapter;

    int current = 0;

    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        AppManager.getAppManager().addActivity(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.main_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout,toolbar,android.R.string.ok,android.R.string.cancel);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //mTintManager.setStatusBarDarkMode(true, this);
        mTintManager.setStatusBarTintEnabled(true);
        mTintManager.setStatusBarTintResource(R.color.material_blue_500);
        //mTintManager.setStatusBarDarkMode(false, this);

        mScreenWidth = Utils.getWindowsWidth(this);
        mShadeImageLeft = (ImageView) findViewById(R.id.shade_left);
        mShadeImageRight = (ImageView) findViewById(R.id.shade_right);
        mNewsClassEdit = (ImageButton) findViewById(R.id.newsclass_edit);

        mIndicator = (PagerSlidingTabStrip) findViewById(R.id.indicator);
        mViewPager = (ViewPager) findViewById(R.id.mViewPager);


        setChangelView();
        mNewsClassEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                current = mViewPager.getCurrentItem();
                Intent i = new Intent(NewsActivity.this, NewsClassEditActivity.class);
                startActivityForResult(i, TABDATA_CHANGED);

            }
        });

    }


    private void setChangelView() {
        initData();
        initFragment();
    }

    private void resetData() {
        initData();
        mIndicator.notifyDataSetChanged();
        mAdapter.notifyDataSetChanged();
    }

    public void initData() {
        tabs.clear();
        tabs.addAll(new CategoryDateBase(this).queryDisplayCategory());
        fragments.clear();
        Log.d(TAG, "initFragment: run" + fragments.size());
        int count = tabs.size();

        for (int i = 0; i < count; i++) {
            Bundle data = new Bundle();
            data.putString("apiurl", tabs.get(i).getApiurl());
            data.putString("id", tabs.get(i).getId());
            data.putString("name", tabs.get(i).getName());
            NewsFragment newfragment = new NewsFragment();
            newfragment.setArguments(data);
            newfragment.setTitle(tabs.get(i).getName());
            fragments.add(newfragment);
        }
    }

    private void initFragment() {
        mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setOffscreenPageLimit(10);
        mViewPager.setAdapter(mAdapter);
        mIndicator.setViewPager(mViewPager);
        setShadeDisplay();
        mIndicator.setOnScrollChangedListener(new com.astuetz.OnScrollChangeListener() {

            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
                // TODO Auto-generated method stub
                View v = mIndicator.getChildAt(0);
                if (v == null) {
                    return;
                }
                if (v.getWidth() <= mScreenWidth) {
                    mShadeImageLeft.setVisibility(View.GONE);
                    mShadeImageRight.setVisibility(View.GONE);
                    return;
                }
                if (l == 0) {
                    mShadeImageLeft.setVisibility(View.GONE);
                    mShadeImageRight.setVisibility(View.VISIBLE);
                    return;
                }
                if (v.getWidth() - l + mNewsClassEdit.getWidth() == mScreenWidth) {
                    mShadeImageLeft.setVisibility(View.VISIBLE);
                    mShadeImageRight.setVisibility(View.GONE);
                    return;
                }
                mShadeImageLeft.setVisibility(View.VISIBLE);
                mShadeImageRight.setVisibility(View.VISIBLE);
            }
        });

    }

    public void setShadeDisplay() {
        View v = mIndicator.getChildAt(0);
        if (v == null) {
            return;
        }
        if (v.getWidth() <= mScreenWidth) {
            mShadeImageLeft.setVisibility(View.GONE);
            mShadeImageRight.setVisibility(View.GONE);
            return;
        }
        if (mIndicator.getCurrentPosition() == 0) {
            mShadeImageLeft.setVisibility(View.GONE);
            mShadeImageRight.setVisibility(View.VISIBLE);
            return;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.more_24hours:
                Intent i = new Intent(this, Hot24HoursActivity.class);
                startActivity(i);
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        // unregisterReceiver(broadcastReceiver);
        //stopService(intentSer);
        if (locationClient != null) {
            locationClient.stop();
        }
        // AppManager.getAppManager().finishActivity(this);
    }

    public String getLocation() {

        return location;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // super.onActivityResult(requestCode, resultCode, data);
        Log.d("edit", "onActivityResult: run");

        switch (requestCode) {
            case TABDATA_CHANGED:
                if (resultCode == RESULT_OK) {
                    Log.d("edit", "onActivityResult: run");
                    resetData();
                    // Toast.makeText(this,"resultCode: 333",Toast.LENGTH_SHORT).show();
                    //mIndicator.notifyDataSetChanged();
                    // mViewPager.setCurrentItem(da);
                }
                break;
        }

    }

    private long exitTime = 0;

    @Override
    public void onBackPressed() {

        //super.onBackPressed();
        if (System.currentTimeMillis() - exitTime >= 2000) {
            exitTime = System.currentTimeMillis();
            Toast.makeText(this, "在按一次退出",
                    Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }
}
