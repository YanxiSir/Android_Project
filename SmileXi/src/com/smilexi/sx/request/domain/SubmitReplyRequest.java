package com.smilexi.sx.request.domain;

public class SubmitReplyRequest {

	private int did;
	private int fuid;
	private String funame;
	private int tuid;
	private String tuname;
	private String content;
	public int getDid() {
		return did;
	}
	public void setDid(int did) {
		this.did = did;
	}
	public int getFuid() {
		return fuid;
	}
	public void setFuid(int fuid) {
		this.fuid = fuid;
	}
	public String getFuname() {
		return funame;
	}
	public void setFuname(String funame) {
		this.funame = funame;
	}
	public int getTuid() {
		return tuid;
	}
	public void setTuid(int tuid) {
		this.tuid = tuid;
	}
	public String getTuname() {
		return tuname;
	}
	public void setTuname(String tuname) {
		this.tuname = tuname;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	
}
