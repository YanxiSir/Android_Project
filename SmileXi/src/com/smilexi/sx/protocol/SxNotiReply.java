package com.smilexi.sx.protocol;

import java.io.Serializable;
import java.util.List;

import com.smilexi.sx.finals.ServerFinals;
import com.smilexi.sx.protocol.SxDynamics.Dynamic;

public class SxNotiReply implements Serializable {

	private List<NotiReply> notiReplys;

	public List<NotiReply> getNotiReplys() {
		return notiReplys;
	}

	public void setNotiReplys(List<NotiReply> notiReplys) {
		this.notiReplys = notiReplys;
	}

	public static class NotiReply implements Serializable {

		private Dynamic dynamic;
		private int fuid;
		private String fName;
		private String fPortrait;
		private int replyType;
		private String content;
		private String lastContent;
		private String replyTime;
		
		

		public String getReplyTime() {
			return replyTime;
		}

		public void setReplyTime(String replyTime) {
			this.replyTime = replyTime;
		}

		public Dynamic getDynamic() {
			return dynamic;
		}

		public void setDynamic(Dynamic dynamic) {
			this.dynamic = dynamic;
		}

		public int getFuid() {
			return fuid;
		}

		public void setFuid(int fuid) {
			this.fuid = fuid;
		}

		public String getfName() {
			return fName;
		}

		public void setfName(String fName) {
			this.fName = fName;
		}

		public String getfPortrait() {
			return ServerFinals.HP + fPortrait;
		}

		public void setfPortrait(String fPortrait) {
			this.fPortrait = fPortrait;
		}

		public int getReplyType() {
			return replyType;
		}

		public void setReplyType(int replyType) {
			this.replyType = replyType;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getLastContent() {
			return lastContent;
		}

		public void setLastContent(String lastContent) {
			this.lastContent = lastContent;
		}

	}
}
