package com.neu.javabean;

import java.util.ArrayList;

public class TeacherBancheInfo {
	private String campusName;//校区名称
	private String startingPlace;//发车地点
	private ArrayList<DaysArrangement> days;
	public String getCampusName() {
		return campusName;
	}
	public void setCampusName(String campusName) {
		this.campusName = campusName;
	}
	public String getStartingPlace() {
		return startingPlace;
	}
	public void setStartingPlace(String startingPlace) {
		this.startingPlace = startingPlace;
	}
	public ArrayList<DaysArrangement> getDays() {
		return days;
	}
	public void setDays(ArrayList<DaysArrangement> days) {
		this.days = days;
	} 
	
	
	
}
