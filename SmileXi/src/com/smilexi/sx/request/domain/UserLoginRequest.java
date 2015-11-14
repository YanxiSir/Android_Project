package com.smilexi.sx.request.domain;

public class UserLoginRequest {
	private String userPhone;
	private String password;
	
	
	
	public String getUserPhone() {
		return userPhone;
	}
	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "UserLoginInfo [userPhone=" + userPhone + ", password="
				+ password + "]";
	}
	
	
	
	
}
