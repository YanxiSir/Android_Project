package com.smilexi.sx.request.domain;

public class UserRegisterRequest {
	private String userPhone;
	private String vCode;//ÑéÖ¤Âë
	private String password;
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public String getvCode() {
		return vCode;
	}
	public void setvCode(String vCode) {
		this.vCode = vCode;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
