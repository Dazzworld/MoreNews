package com.dazzcoder.morenews;/**
 * Created by zc on 2015/12/24.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.dazzcoder.morenews.activity.NewsActivity;
import com.dazzcoder.morenews.bean.NewsClassification;
import com.dazzcoder.morenews.db.CategoryDateBase;
import com.dazzcoder.morenews.model.Utils;
import com.dazzcoder.morenews.services.LocationServices;
import com.dazzcoder.morenews.utils.Contacts;
import com.dazzcoder.morenews.utils.PreferenceHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * User: CZC(867135418@QQ.COM)
 * Date: 2015-12-24
 * Time: 17:31
 * ReadMe:
 */

public class AppStart extends AppCompatActivity {

    CategoryDateBase dateBase = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // 防止第三方跳转时出现双实例

        Activity aty = AppManager.getActivity(NewsActivity.class);
        if (aty != null && !aty.isFinishing()) {
            finish();
        }

        final View view = View.inflate(this, R.layout.app_start, null);
        setContentView(view);
        // 渐变展示启动屏
        AlphaAnimation aa = new AlphaAnimation(0.5f, 1.0f);
        aa.setDuration(1000);
        view.startAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation arg0) {
                redirectTo();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationStart(Animation animation) {

                initDataBase();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        int cacheVersion = PreferenceHelper.readInt(this, Contacts.MAIN_PREFERENCES_NAME,
                Contacts.FIRST_START, -1);
        int currentVersion = Utils.getVersionCode();
        if (cacheVersion < currentVersion) {
            PreferenceHelper.write(this, Contacts.MAIN_PREFERENCES_NAME, Contacts.FIRST_START,
                    currentVersion);
            startService(new Intent(AppContext.getAppContext(), LocationServices.class));
        }
    }

    private void initDataBase() {
        if (!getDatabasePath(Contacts.NEWS_CATEGORY_DB_NAME).exists()) {
            dateBase = new CategoryDateBase(this);
            try {
                dateBase.initData(initList());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public List<NewsClassification> initList() throws IOException, JSONException {
        ArrayList<NewsClassification> list = new ArrayList<>();

        BufferedReader buffreader = new BufferedReader(new InputStreamReader(getAssets().open("news_class.json")));

        String tmp = "";
        StringBuilder sb = new StringBuilder();
        while ((tmp = buffreader.readLine()) != null) {
            sb.append(tmp);
            // Log.d("json====", tmp);
        }
        if (buffreader != null) {
            buffreader.close();
        }

        JSONObject jsonObj = new JSONObject(sb.toString());

        JSONArray jsonArray = jsonObj.getJSONArray("channelList");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jo = jsonArray.getJSONObject(i);
            NewsClassification category = new NewsClassification(jo.getString("name"), jo.getString("url"), jo.getString("id"));
            if (i > 7) {
                category.setIntitle(0);
            }
            list.add(category);

        }
        return list;
    }

    /**
     * 跳转到...
     */
    private void redirectTo() {
        //Intent uploadLog = new Intent(this, LocationServices.class);
        // startService(uploadLog);
        Intent intent = new Intent(this, NewsActivity.class);
        startActivity(intent);
        finish();
    }
}
