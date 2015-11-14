package com.smilexi.sx.protocol;

import java.io.Serializable;
import java.util.List;

import com.smilexi.sx.finals.ServerFinals;

public class SxQuestions implements Serializable {

	private List<Question> questions;

	public List<Question> getQuestions() {
		return questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public static class Question implements Serializable {

		private int uid;
		private String uname;
		private String uportrait;
		private int qid;
		private String qtitle;
		private String qcontent;
		private String qkey;
		private int qtypeid;
		private String qtypename;
		private int attencount;
		private int answercount;
		private int isattend;

		public int getUid() {
			return uid;
		}

		public void setUid(int uid) {
			this.uid = uid;
		}

		public String getUname() {
			return uname;
		}

		public void setUname(String uname) {
			this.uname = uname;
		}

		public String getUportrait() {
			return ServerFinals.HP + uportrait;
		}

		public void setUportrait(String uportrait) {

			this.uportrait = uportrait;
		}

		public int getQid() {
			return qid;
		}

		public void setQid(int qid) {
			this.qid = qid;
		}

		public String getQtitle() {
			return qtitle;
		}

		public void setQtitle(String qtitle) {
			this.qtitle = qtitle;
		}

		public String getQcontent() {
			return qcontent;
		}

		public void setQcontent(String qcontent) {
			this.qcontent = qcontent;
		}

		public String getQkey() {
			return qkey;
		}

		public void setQkey(String qkey) {
			this.qkey = qkey;
		}

		public int getQtypeid() {
			return qtypeid;
		}

		public void setQtypeid(int qtypeid) {
			this.qtypeid = qtypeid;
		}

		public String getQtypename() {
			return qtypename;
		}

		public void setQtypename(String qtypename) {
			this.qtypename = qtypename;
		}

		public int getAttencount() {
			return attencount;
		}

		public void setAttencount(int attencount) {
			this.attencount = attencount;
		}

		public int getAnswercount() {
			return answercount;
		}

		public void setAnswercount(int answercount) {
			this.answercount = answercount;
		}

		public int getIsattend() {
			return isattend;
		}

		public void setIsattend(int isattend) {
			this.isattend = isattend;
		}

	}
}
