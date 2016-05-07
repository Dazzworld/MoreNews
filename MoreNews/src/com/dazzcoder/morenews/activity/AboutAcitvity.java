package com.dazzcoder.morenews.activity;/**
 * Created by zc on 2016/1/4.
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.dazzcoder.morenews.R;
import com.dazzcoder.morenews.model.Utils;

/**
 * User: CZC(867135418@QQ.COM)
 * Date: 2016-01-04
 * Time: 23:14
 * ReadMe:
 */

public class AboutAcitvity extends AppCompatActivity {
    TextView mTvAppVersion;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mTvAppVersion = (TextView) findViewById(R.id.app_version);
        mTvAppVersion.setText("ver:"+Utils.getVersionName());
    }
}
