package com.smilexi.sx.protocol;

import java.io.Serializable;
import java.util.List;

import com.smilexi.sx.finals.ServerFinals;
import com.smilexi.sx.protocol.SxDynamics.Dynamic;

public class SxDynamicReply implements Serializable {

	private List<Reply> replys;

	public List<Reply> getReplys() {
		return replys;
	}

	public void setReplys(List<Reply> replys) {
		this.replys = replys;
	}

//	private Dynamic dynamic;
//
//	public Dynamic getDynamic() {
//		return dynamic;
//	}
//
//	public void setDynamic(Dynamic dynamic) {
//		this.dynamic = dynamic;
//	}

	public static class Reply implements Serializable {

		private int fromUserId;
		private String fromUserName;
		private String fromUserPortrait;
		private int toUserId;
		private String toUserName;
		private String content;
		private String replyTime;

		public int getFromUserId() {
			return fromUserId;
		}

		public void setFromUserId(int fromUserId) {
			this.fromUserId = fromUserId;
		}

		public String getFromUserName() {
			return fromUserName;
		}

		public void setFromUserName(String fromUserName) {
			this.fromUserName = fromUserName;
		}

		public String getFromUserPortrait() {
			if (fromUserPortrait != null
					|| !fromUserPortrait.startsWith("http://"))
				return ServerFinals.HP + fromUserPortrait;
			return fromUserPortrait;
		}

		public void setFromUserPortrait(String fromUserPortrait) {
			this.fromUserPortrait = fromUserPortrait;
		}

		public int getToUserId() {
			return toUserId;
		}

		public void setToUserId(int toUserId) {
			this.toUserId = toUserId;
		}

		public String getToUserName() {
			return toUserName;
		}

		public void setToUserName(String toUserName) {
			this.toUserName = toUserName;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public String getReplyTime() {
			return replyTime;
		}

		public void setReplyTime(String replyTime) {
			this.replyTime = replyTime;
		}

	}
}
