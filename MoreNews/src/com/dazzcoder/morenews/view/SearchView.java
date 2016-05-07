package com.dazzcoder.morenews.view;/**
 * Created by zc on 2016/1/3.
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dazzcoder.morenews.R;

/**
 * User: CZC(867135418@QQ.COM)
 * Date: 2016-01-03
 * Time: 19:49
 * ReadMe:
 */

public class SearchView extends LinearLayout implements View.OnClickListener,TextWatcher,TextView.OnEditorActionListener {
    private EditText mEdit;
    private ImageButton mClear,mGoSearch;
    private Context mContext;
    OnSearchListener mListener = null;
    public SearchView(Context context) {
        this(context, null);
    }

    public SearchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.seach_view,this,true);
        mEdit = (EditText) findViewById(R.id.search_edit);
        mClear = (ImageButton) findViewById(R.id.search_clear_btn);
        mGoSearch = (ImageButton) findViewById(R.id.search_go_btn);
        mClear.setOnClickListener(this);
        mGoSearch.setOnClickListener(this);
        mEdit.addTextChangedListener(this);
        mEdit.setOnEditorActionListener(this);
    }


    /**
     * 通知监听者 进行搜索操作
     * @param text
     */
    private void notifyStartSearching(String text){

        if (text.replace(" ","").isEmpty()){
            Toast.makeText(mContext,"搜索内容不能为空",Toast.LENGTH_SHORT).show();
            return;
        }
        if (mListener != null) {
            mListener.startSearch(mEdit.getText().toString());
        }
        //隐藏软键盘
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public void setOnSearchListener(OnSearchListener listener){
        if (listener!=null){
            mListener = listener;
        }
    }
    public void addTextInEditer(String text){
        mEdit.setText(text);
        mEdit.setSelection(text.length());
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_clear_btn:
                mEdit.getText().clear();
                if (mListener!=null){
                    mListener.delSearch();
                }
                break;
            case R.id.search_go_btn:
                notifyStartSearching(mEdit.getText().toString());
                break;
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.toString().isEmpty()){
            mClear.setVisibility(GONE);
        }else {
            mClear.setVisibility(VISIBLE);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId== EditorInfo.IME_ACTION_SEARCH){
            notifyStartSearching(mEdit.getText().toString());
        }
        return true;
    }

    public interface OnSearchListener{

        public abstract void startSearch(String text);
        public abstract void delSearch();
    }
}
