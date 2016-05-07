package com.dazzcoder.morenews.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.bm.library.PhotoView;
import com.dazzcoder.morenews.AppContext;
import com.dazzcoder.morenews.R;
import com.dazzcoder.morenews.activity.ImageSetReaderActivity;
import com.dazzcoder.morenews.parser.NewsImageListener;

public class ImageSetFragment extends Fragment implements OnClickListener {

	String imgurl;
	private PhotoView mPhotoView;
	private ImageSetReaderActivity activtity;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Bundle bundle = getArguments();
		imgurl = bundle.getString("IMG");
		activtity = (ImageSetReaderActivity) getActivity();
		super.onCreate(savedInstanceState);
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View v = inflater.inflate(R.layout.fragment_imagereader, container, false);
		mPhotoView = (PhotoView) v.findViewById(R.id.photoview);
		mPhotoView.enable();
		mPhotoView.setOnClickListener(this);
		requestUrl(imgurl, mPhotoView);
		return v;
	}

	/**
	 * @param url
	 */
	private void requestUrl(final String url, PhotoView imgView) {
		if (url == null) {
			imgView.setImageResource(R.drawable.ic_default_adimage);
			return;
		}
		AppContext.getImageLoader().get(url, new NewsImageListener(url, imgView));
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		activtity.setFullScreen();
	}

}
