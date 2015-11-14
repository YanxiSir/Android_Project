package com.smilexi.sx.protocol;

import java.io.Serializable;
import java.util.List;

import com.smilexi.sx.finals.ServerFinals;

public class SxAttentioned implements Serializable {

	private List<Attentioned> atts;

	public List<Attentioned> getAtts() {
		return atts;
	}

	public void setAtts(List<Attentioned> atts) {
		this.atts = atts;
	}

	public static class Attentioned implements Serializable {

		private int uid;
		private String username;
		private String portrait;
		private String sign;

		public int getUid() {
			return uid;
		}

		public void setUid(int uid) {
			this.uid = uid;
		}

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPortrait() {
			if (portrait != null || !portrait.startsWith("http://"))
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

	}
}
