package com.dazzcoder.morenews.bean;

public class City {

	private String name;
	private String pinyi;
	private String province;


	public City(String province,String name, String pinyi) {
		super();
		this.province = province;
		this.name = name;
		this.pinyi = pinyi;
	}

	public City() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPinyi() {
		return pinyi;
	}

	public void setPinyi(String pinyi) {
		this.pinyi = pinyi;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}
}
