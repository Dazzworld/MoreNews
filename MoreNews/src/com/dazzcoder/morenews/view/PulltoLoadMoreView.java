package com.dazzcoder.morenews.view;/**
 * Created by zc on 2016/1/5.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dazzcoder.morenews.R;

/**
 * User: CZC(867135418@QQ.COM)
 * Date: 2016-01-05
 * Time: 13:39
 * ReadMe:
 */

public class PulltoLoadMoreView extends FrameLayout {

    TextView mTipLoading;
    LinearLayout mLoadingAnimation;
    OnLoadMoreListener mListener;
    public PulltoLoadMoreView(Context context) {
        this(context, null);
    }

    public PulltoLoadMoreView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PulltoLoadMoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.listview_footer, this, true);
        mTipLoading = (TextView) findViewById(R.id.tips_loading);
        mLoadingAnimation = (LinearLayout) findViewById(R.id.loading);
        setBackgroundResource(R.drawable.btn_touch_bg);
        setOnClickListener(selfOnClickListener);
    }

    public void setOnLoadMoreListener(OnLoadMoreListener listener){
        if (listener!=null){
            mListener = listener;
        }
    }

    public void finishLoading(){
        mTipLoading.setVisibility(VISIBLE);
        mLoadingAnimation.setVisibility(GONE);
        if (mListener!=null){
            mListener.onFinish();
        }else{
            throw new NullPointerException("先设置监听器");
        }
    }

    OnClickListener selfOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mListener!=null){
                mListener.onStart();
            }
            mTipLoading.setVisibility(GONE);
            mLoadingAnimation.setVisibility(VISIBLE);
        }
    };

    public interface OnLoadMoreListener {
        public abstract void onStart();
        public abstract void onFinish();
    }
}
