package com.dazzcoder.morenews.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.android.http.LoadControler;
import com.android.http.RequestManager;
import com.dazzcoder.morenews.R;
import com.dazzcoder.morenews.adapter.SearchResultAdapter;
import com.dazzcoder.morenews.bean.News;
import com.dazzcoder.morenews.model.Utils;
import com.dazzcoder.morenews.utils.PreferenceHelper;
import com.dazzcoder.morenews.view.SearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity implements RequestManager.RequestListener {

    final String HOTWORD = "http://c.3g.163.com/nc/search/hotWord.html";
    final String SEARCH_API = "http://c.3g.163.com/search/comp/MA%3D%3D/20/";
    SearchView mSearchView;
    Toolbar toolbar;
    LinearLayout mSearchHistory;
    GridView mHotWordView;
    LoadControler loadControler = null;
    ArrayList<String> hotword = new ArrayList<>();
    String history = "";

    ImageButton mClearHistory;
    ArrayAdapter adapter;
    LinearLayout mHistoty, mHotword;
    ListView mSearchResult;
    ArrayList<News> searchResult = new ArrayList<>();
    SearchResultAdapter resultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mHistoty = (LinearLayout) findViewById(R.id.history_frame);
        mHotword = (LinearLayout) findViewById(R.id.hotword_frame);
        mSearchView = (SearchView) findViewById(R.id.search_view);
        mSearchHistory = (LinearLayout) findViewById(R.id.search_history);
        mClearHistory = (ImageButton) findViewById(R.id.claer_history);
        mSearchResult = (ListView) findViewById(R.id.search_result);
        mHotWordView = (GridView) findViewById(R.id.gridView_hotword);
        resultAdapter = new SearchResultAdapter(this,searchResult,R.layout.search_result);
        adapter = new ArrayAdapter(this, R.layout.hotword_girdview_item, R.id.hotword, hotword);
        mSearchResult.setAdapter(resultAdapter);
        mHotWordView.setAdapter(adapter);
        readHistory();
        initListener();
        loadControler = RequestManager.getInstance().get(HOTWORD, this, 0);

    }

    private void initListener() {
        mClearHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PreferenceHelper.write(SearchActivity.this, "search_history", "history", "");
                mHistoty.setVisibility(View.GONE);
            }
        });
        mHotWordView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSearchView.addTextInEditer(hotword.get(position));
            }
        });
        mSearchResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                News n = (News) resultAdapter.getItem(position);
                Intent i = new Intent(SearchActivity.this,ReaderActivity.class);
                Bundle b = new Bundle();
                b.putString("id",n.getDocid());
                i.putExtras(b);
                startActivity(i);
            }
        });
        mSearchView.setOnSearchListener(new SearchView.OnSearchListener() {
            @Override
            public void startSearch(String text) {
                Log.d("LISTENER", "startSearch: run");
                addSearchHistory(text);
                loadControler = RequestManager.getInstance().get(SEARCH_API+ Utils.encodeHtml(text),SearchActivity.this,1);
            }

            @Override
            public void delSearch() {
                searchResult.clear();
                resultAdapter.notifyDataSetChanged();
                mSearchResult.setVisibility(View.GONE);
                mHistoty.setVisibility(View.VISIBLE);
                mHotword.setVisibility(View.VISIBLE);
            }
        });
    }

    public void readHistory() {
        String s = PreferenceHelper.readString(this, "search_history", "history", "");
        if (s.isEmpty()) {
            mHistoty.setVisibility(View.GONE);
            return;
        }
        String word[] = s.split("-");
        for (int i = 0; i < word.length; i++) {
            addSearchHistory(word[i]);
        }
    }

    public void addSearchHistory(String text) {
        if (history.contains(text)) {
            return;
        }
        mHistoty.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextView textView = new TextView(this);
        textView.setText(text);
        textView.setLayoutParams(params);
        textView.setPadding(10, 10, 10, 10);
        textView.setTextSize(16);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSearchView.addTextInEditer(textView.getText().toString());
            }
        });
        mSearchHistory.addView(textView, 0);
        mSearchView.invalidate();
        history = history.isEmpty() ? text : history + "-" + text;
        PreferenceHelper.write(this, "search_history", "history", history);
    }

    @Override
    public void onRequest() {

    }

    @Override
    public void onSuccess(String s, Map<String, String> map, String s1, int i) {
        try {
            switch (i) {
                case 0:
                    JSONArray jsonArray = new JSONObject(s).getJSONArray("hotWordList");
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                        hotword.add(jsonObject.getString("hotWord"));
                    }
                    adapter.notifyDataSetChanged();
                    break;
                case 1:
                    JSONArray jsonArray1 = new JSONObject(s).getJSONObject("doc").getJSONArray("result");
                    for (int j = 0; j < jsonArray1.length(); j++) {
                        JSONObject jsonObject = jsonArray1.getJSONObject(j);
                        News news = new News();
                        news.setTitle(jsonObject.getString("title"));
                        news.setDocid(jsonObject.getString("docid"));
                        news.setPubDate(jsonObject.getString("ptime"));
                        searchResult.add(news);
                    }
                    resultAdapter.notifyDataSetChanged();
                    mHistoty.setVisibility(View.GONE);
                    mHotword.setVisibility(View.GONE);
                    mSearchResult.setVisibility(View.VISIBLE);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(String s, String s1, int i) {

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
