package com.smilexi.sx.request.domain;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class LoginRequestInfo implements KvmSerializable {
	public String userPhone;
	public String password;

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public LoginRequestInfo() {
		super();
	}

	public LoginRequestInfo(String userPhone, String password) {
		super();
		this.userPhone = userPhone;
		this.password = password;
	}

	@Override
	public Object getProperty(int arg0) {

		switch (arg0) {
		case 0:
			return userPhone;
		case 1:
			return password;
		default:
			break;
		}

		return null;
	}

	@Override
	public int getPropertyCount() {
		return 2;
	}

	@Override
	public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
		switch (arg0) {
		case 0:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "userPhone";
			break;
		case 1:
			arg2.type = PropertyInfo.STRING_CLASS;
			arg2.name = "password";
			break;
		default:
			break;
		}
	}

	@Override
	public void setProperty(int arg0, Object arg1) {
		switch (arg0) {
		case 0:
			userPhone = arg1.toString();
			break;
		case 1:
			password = arg1.toString();
			break;
		default:
			break;
		}
	}
}
