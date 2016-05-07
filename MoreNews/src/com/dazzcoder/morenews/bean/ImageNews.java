package com.dazzcoder.morenews.bean;

import java.util.ArrayList;
import java.util.HashMap;

public class ImageNews {
	private int allnum;
	private String url;
	private String boardid = "photoview_bbs";
	private String postid;
	private String commenturl = "http://comment.api.163.com/api/json/thread/total/%s/%s";
	private ArrayList<HashMap<String, String>> images;

	public ImageNews() {
		super();
		// TODO Auto-generated constructor stub
		images = new ArrayList<>();
	}

	public int getAllnum() {
		return allnum;
	}

	public void setAllnum(int allnum) {
		this.allnum = allnum;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getBoardid() {
		return boardid;
	}

	public void setBoardid(String boardid) {
		this.boardid = boardid;
	}

	public void addImage(HashMap<String, String> map) {
		images.add(map);
	}

	public String getPostid() {
		return postid;
	}

	public void setPostid(String postid) {
		this.postid = postid;
	}

	public String getCommenturl() {
		commenturl = String.format(commenturl, boardid, postid);
		return commenturl;
	}

	public ArrayList<HashMap<String, String>> getImages() {
		return images;
	}

}
