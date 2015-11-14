package com.smilexi.sx.protocol;

import java.io.Serializable;

import com.smilexi.sx.finals.ServerFinals;

public class SxUserMainPageInfo implements Serializable {

	private String nickname;
	private String portrait;
	private String sign;
	private int genderid;
	private String phone;
	private String email;
	private int attented;

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getPortrait() {
		if (portrait != null && !portrait.startsWith("http://"))
			return ServerFinals.HP + portrait;
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public int getGenderid() {
		return genderid;
	}

	public void setGenderid(int genderid) {
		this.genderid = genderid;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getAttented() {
		return attented;
	}

	public void setAttented(int attented) {
		this.attented = attented;
	}
	
}
