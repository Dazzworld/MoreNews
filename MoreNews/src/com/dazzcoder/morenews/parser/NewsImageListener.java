package com.dazzcoder.morenews.parser;

import android.graphics.Bitmap;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader.ImageContainer;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.bm.library.PhotoView;

/**
 * 图片加载监听类
 */
public class NewsImageListener implements ImageListener {
	private String mUrl;
	private PhotoView imageView;

	public NewsImageListener(String url, PhotoView imgView) {
		this.mUrl = url;
		this.imageView = imgView;
	}

	@Override
	public void onErrorResponse(VolleyError error) {
		System.out.println("error");
	}

	@Override
	public void onResponse(ImageContainer response, boolean isImmediate) {
		setImageView(response.getBitmap(), mUrl);
	}

	public void setImageView(Bitmap bitmap, String mUrl) {
		// TODO Auto-generated method stub
		imageView.setImageBitmap(bitmap);
	}
}
