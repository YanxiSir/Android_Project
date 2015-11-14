package com.xiaowu.shop.protocol;


import com.xiaowu.finals.ServerFinals;
import com.xiaowu.utils.Tool;

public class XwShopInfo {
	
	private int id;
	private String name;
	private String phone;
	private String portrait;
	private String addr;
	private String intro;
	private String startTime;
	private String endTime;
	private int diliverPrice;
	private int diliverFee;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return Tool.decodeUTF_8(name);
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPortrait() {
		return ServerFinals.SHOP_H +portrait;
	}
	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}
	public String getAddr() {
		return Tool.decodeUTF_8(addr);
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getIntro() {
		return Tool.decodeUTF_8(intro);
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public int getDiliverPrice() {
		return diliverPrice;
	}
	public void setDiliverPrice(int diliverPrice) {
		this.diliverPrice = diliverPrice;
	}
	public int getDiliverFee() {
		return diliverFee;
	}
	public void setDiliverFee(int diliverFee) {
		this.diliverFee = diliverFee;
	}
	
	
	
}
