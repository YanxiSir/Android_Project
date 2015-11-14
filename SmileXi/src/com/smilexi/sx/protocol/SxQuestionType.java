package com.smilexi.sx.protocol;

import java.io.Serializable;
import java.util.List;

public class SxQuestionType implements Serializable{

	private List<QuestionType> qTypes;

	public List<QuestionType> getqTypes() {
		return qTypes;
	}

	public void setqTypes(List<QuestionType> qTypes) {
		this.qTypes = qTypes;
	}

	public static class QuestionType implements Serializable {
		private int id;
		private String name;
		private String describe;
		private String portrait;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescribe() {
			return describe;
		}

		public void setDescribe(String describe) {
			this.describe = describe;
		}

		public String getPortrait() {
			return portrait;
		}

		public void setPortrait(String portrait) {
			this.portrait = portrait;
		}

	}
}
