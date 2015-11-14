package com.smilexi.sx.request.domain;

public class UserGetDynamicsRequest {

	private int startId;
	private int count;
	private int oid;
	private int uid;
	private int wid;//指定用户id which id
	public int getStartId() {
		return startId;
	}
	public void setStartId(int startId) {
		this.startId = startId;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getOid() {
		return oid;
	}
	public void setOid(int oid) {
		this.oid = oid;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getWid() {
		return wid;
	}
	public void setWid(int wid) {
		this.wid = wid;
	}
	
	
	
}
