package com.dazzcoder.morenews.bean;

public class Weather {

	private String date;
	private String wind;
	private String longli;
	private String climate;
	private String temperature;
	private String rt_temperature;

	public String getRt_temperature() {
		return rt_temperature;
	}

	public void setRt_temperature(String rt_temperature) {
		this.rt_temperature = rt_temperature;
	}

	private String week;
	private Weather nextDay;
	private PM25 pm25;

	public PM25 getPm25() {
		return pm25;
	}

	public void setPm25(PM25 pm25) {
		this.pm25 = pm25;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getWind() {
		return wind;
	}

	public void setWind(String wind) {
		this.wind = wind;
	}

	public String getLongli() {
		return longli;
	}

	public void setLongli(String longli) {
		this.longli = longli;
	}

	public String getClimate() {
		return climate;
	}

	public void setClimate(String climate) {
		this.climate = climate;
	}

	public String getTemperature() {
		return temperature;
	}

	public void setTemperature(String temperature) {
		this.temperature = temperature;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public Weather getNextDay() {
		return nextDay;
	}

	public void setNextDay(Weather nextDay) {
		this.nextDay = nextDay;
	}
}
