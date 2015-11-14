package com.xiaowu.protocol;

import java.io.Serializable;

import com.xiaowu.utils.Tool;

public class XwUserInfo implements Serializable{
	private int uid;
	private String uname;
	private String phone;
	private float leftMoney;
	
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getUname() {
		return Tool.decodeUTF_8(uname);
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public float getLeftMoney() {
		return leftMoney;
	}
	public void setLeftMoney(float leftMoney) {
		this.leftMoney = leftMoney;
	}
	
	
	
}
