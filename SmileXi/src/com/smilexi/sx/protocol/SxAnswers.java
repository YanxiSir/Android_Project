package com.smilexi.sx.protocol;

import java.io.Serializable;
import java.util.List;

import com.smilexi.sx.finals.ServerFinals;
import com.smilexi.sx.protocol.SxQuestions.Question;

public class SxAnswers implements Serializable {

	private List<Answer> answers;

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}
	
//	private Question question;
//	
//	
//
//	public Question getQuestion() {
//		return question;
//	}
//
//	public void setQuestion(Question question) {
//		this.question = question;
//	}



	public static class Answer implements Serializable {

		
		private int qid;
		private String qTitle;
		
		private int id;
		private String answerDate;
		private String content;

		private int userId;
		private String userName;
		private String userPortrait;
		
		private int isZan;
		private int isCollect;
		
		private int zanCount;
		private int shoucangCount;
		private int replyCount;

		
		
		
		public int getQid() {
			return qid;
		}

		public void setQid(int qid) {
			this.qid = qid;
		}

		public String getqTitle() {
			return qTitle;
		}

		public void setqTitle(String qTitle) {
			this.qTitle = qTitle;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getAnswerDate() {
			return answerDate;
		}

		public void setAnswerDate(String answerDate) {
			this.answerDate = answerDate;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public int getUserId() {
			return userId;
		}

		public void setUserId(int userId) {
			this.userId = userId;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		public String getUserPortrait() {
			return ServerFinals.HP + userPortrait;
		}

		public void setUserPortrait(String userPortrait) {
			this.userPortrait = userPortrait;
		}

		public int getZanCount() {
			return zanCount;
		}

		public void setZanCount(int zanCount) {
			this.zanCount = zanCount;
		}

		public int getIsZan() {
			return isZan;
		}

		public void setIsZan(int isZan) {
			this.isZan = isZan;
		}

		public int getIsCollect() {
			return isCollect;
		}

		public void setIsCollect(int isCollect) {
			this.isCollect = isCollect;
		}

		public int getShoucangCount() {
			return shoucangCount;
		}

		public void setShoucangCount(int shoucangCount) {
			this.shoucangCount = shoucangCount;
		}

		public int getReplyCount() {
			return replyCount;
		}

		public void setReplyCount(int replyCount) {
			this.replyCount = replyCount;
		}
		

	}
}
