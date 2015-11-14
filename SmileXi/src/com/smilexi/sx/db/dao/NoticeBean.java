package com.smilexi.sx.db.dao;

public class NoticeBean {
	private int id;
	private String content;
	private int did;
	private int aid;
	private int qid;
	private int uid;
	private String createTime;
	
	
	
	public NoticeBean(int id, String content, int did, int aid, int qid,
			int uid, String createTime) {
		super();
		this.id = id;
		this.content = content;
		this.did = did;
		this.aid = aid;
		this.qid = qid;
		this.uid = uid;
		this.createTime = createTime;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getDid() {
		return did;
	}
	public void setDid(int did) {
		this.did = did;
	}
	public int getAid() {
		return aid;
	}
	public void setAid(int aid) {
		this.aid = aid;
	}
	public int getQid() {
		return qid;
	}
	public void setQid(int qid) {
		this.qid = qid;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
}
