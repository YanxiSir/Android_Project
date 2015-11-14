package com.smilexi.sx.request.domain;

public class UserImproveInfoRequest {
	private int uid;
	private String username;
	private String signStr;
	private String email;
	private String realName;
	private int genderId;//1 Å®Éú,2 ÄÐÉú
	private String portrait;
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getSignStr() {
		return signStr;
	}
	public void setSignStr(String signStr) {
		this.signStr = signStr;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getRealName() {
		return realName;
	}
	public void setRealName(String realName) {
		this.realName = realName;
	}
	public int getGenderId() {
		return genderId;
	}
	public void setGenderId(int genderId) {
		this.genderId = genderId;
	}
	public String getPortrait() {
		return portrait;
	}
	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}
	
	
	
}
