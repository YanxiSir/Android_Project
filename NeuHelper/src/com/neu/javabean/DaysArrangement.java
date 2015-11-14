package com.neu.javabean;

import java.util.ArrayList;

public class DaysArrangement {
	private String daysDate;//时间区域
	private ArrayList<String> times;//班车安排
	public String getDaysDate() {
		return daysDate;
	}
	public void setDaysDate(String daysDate) {
		this.daysDate = daysDate;
	}
	public ArrayList<String> getTimes() {
		return times;
	}
	public void setTimes(ArrayList<String> times) {
		this.times = times;
	}
	
}
