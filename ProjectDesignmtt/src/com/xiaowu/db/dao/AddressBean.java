package com.xiaowu.db.dao;

public class AddressBean {

	private int id;
	private String addr;
	private String username;
	private String phone;
	
	public AddressBean() {
	}
	public AddressBean(int id, String addr, String username, String phone) {
		super();
		this.id = id;
		this.addr = addr;
		this.username = username;
		this.phone = phone;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	
}
