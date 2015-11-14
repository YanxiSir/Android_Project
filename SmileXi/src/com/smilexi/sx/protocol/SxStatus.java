package com.smilexi.sx.protocol;

import java.io.Serializable;
import java.lang.reflect.Type;


public class SxStatus<T> implements Serializable {
	private Type type;
	private Integer resultcode;
	private T result;
	
	public SxStatus(Type type){
		this.type = type;
	}
	
	public Integer getResultcode() {
		return resultcode;
	}

	public void setResultcode(Integer resultcode) {
		this.resultcode = resultcode;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}
	
}