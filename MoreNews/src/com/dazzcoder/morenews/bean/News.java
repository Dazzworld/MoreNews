package com.dazzcoder.morenews.bean;

import java.io.Serializable;
import java.util.List;

public class News implements Serializable {

	private static final long serialVersionUID = 2L;
	private String title;
	private String dec = "NO";
	private List<String> imgurl;
	private String pubDate;
	private String url;
	private String docid;
	private String photosetID[];
	private String imageUrl;
	private boolean isPhotoSet = false;

	public String[] getPhotosetID() {
		return photosetID;
	}

	public void setPhotosetID(String[] photosetID) {
		this.photosetID = photosetID;
	}

	public boolean isPhotoSet() {

		isPhotoSet = photosetID != null ? true : false;

		return isPhotoSet;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDec() {
		return dec;
	}

	public void setDec(String dec) {
		this.dec = dec;
	}

	public List<String> getImgurl() {
		return imgurl;
	}

	public void setImgurl(List<String> imgurl) {
		this.imgurl = imgurl;
	}

	public String getPubDate() {
		return pubDate;
	}

	public void setPubDate(String pubDate) {
		this.pubDate = pubDate;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDocid() {
		return docid;
	}

	public void setDocid(String docid) {
		this.docid = docid;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

}
