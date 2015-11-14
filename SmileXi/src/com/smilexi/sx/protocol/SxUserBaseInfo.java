package com.smilexi.sx.protocol;

import java.io.Serializable;

import android.text.TextUtils;

import com.smilexi.sx.finals.ServerFinals;

public class SxUserBaseInfo implements Serializable {

	private Integer userid;
	private String userphone;
	private String userpassword;
	private String useremail;
	private Integer schoolid;
	private String schoolname;
	private String portrait;
	private String nickname;
	private Integer genderid;
	private String birthday;
	private String createtime;
	private Integer authed;

	private Integer improveinfo;
	private String signstr;
	private String background;

	public Integer getImproveinfo() {
		return improveinfo;
	}

	public void setImproveinfo(Integer improveinfo) {
		this.improveinfo = improveinfo;
	}

	public String getSignstr() {
		return signstr;
	}

	public void setSignstr(String signstr) {
		this.signstr = signstr;
	}

	public Integer getUserid() {
		return userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	public String getUserphone() {
		return userphone;
	}

	public void setUserphone(String userphone) {
		this.userphone = userphone;
	}

	public String getUserpassword() {
		return userpassword;
	}

	public void setUserpassword(String userpassword) {
		this.userpassword = userpassword;
	}

	public String getUseremail() {
		return useremail;
	}

	public void setUseremail(String useremail) {
		this.useremail = useremail;
	}

	public Integer getSchoolid() {
		return schoolid;
	}

	public void setSchoolid(Integer schoolid) {
		this.schoolid = schoolid;
	}

	public String getSchoolname() {
		return schoolname;
	}

	public void setSchoolname(String schoolname) {
		this.schoolname = schoolname;
	}

	public String getPortrait() {
		if (TextUtils.isEmpty(portrait))
			return null;
		if (!portrait.startsWith("http://"))
			return ServerFinals.HP + portrait;
		return portrait;
	}

	public void setPortrait(String portrait) {
		this.portrait = portrait;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Integer getGenderid() {
		return genderid;
	}

	public void setGenderid(Integer genderid) {
		this.genderid = genderid;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public Integer getAuthed() {
		return authed;
	}

	public void setAuthed(Integer authed) {
		this.authed = authed;
	}

	public String getBackground() {
		return background;
	}

	public void setBackground(String background) {
		this.background = background;
	}

}
