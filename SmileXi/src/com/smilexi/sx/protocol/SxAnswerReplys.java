package com.smilexi.sx.protocol;

import java.io.Serializable;


public class SxAnswerReplys implements Serializable {

	private AnswerReply answerReplys;

	public AnswerReply getAnswerReplys() {
		return answerReplys;
	}

	public void setAnswerReplys(AnswerReply answerReplys) {
		this.answerReplys = answerReplys;
	}

	public static class AnswerReply implements Serializable {

		
		private int fuid;
		private String fName;
		private String fPortrait;
		private int tuid;
		private String tName;
		private String content;
		private String replyDate;
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
			return fPortrait;
		}
		public void setfPortrait(String fPortrait) {
			this.fPortrait = fPortrait;
		}
		public int getTuid() {
			return tuid;
		}
		public void setTuid(int tuid) {
			this.tuid = tuid;
		}
		public String gettName() {
			return tName;
		}
		public void settName(String tName) {
			this.tName = tName;
		}
		public String getContent() {
			return content;
		}
		public void setContent(String content) {
			this.content = content;
		}
		public String getReplyDate() {
			return replyDate;
		}
		public void setReplyDate(String replyDate) {
			this.replyDate = replyDate;
		}
		
		
	}
}
