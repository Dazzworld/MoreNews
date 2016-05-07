package com.dazzcoder.morenews.bean;

public class NewsClassification {
	private String name;
	private String apiurl;
	private String id;
	private int intitle = 1;
	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getApiurl() {
		return apiurl;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setApiurl(String apiurl) {
		this.apiurl = apiurl;
	}

	public void setId(String id) {
		this.id = id;
	}

	public boolean isIntitle() {
		return intitle==1?true:false;
	}

	public void setIntitle(int intitle) {
		this.intitle = intitle;
	}

	public NewsClassification(String name, String apiurl, String id) {
		super();
		this.name = name;
		this.apiurl = apiurl;
		this.id = id;
	}
	public NewsClassification(){

	}

}
