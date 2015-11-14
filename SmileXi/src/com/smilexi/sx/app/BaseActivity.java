package com.smilexi.sx.app;

import com.smilexi.sx.R;
import com.smilexi.sx.common.SXContext;
import com.smilexi.sx.protocol.SxUserBaseInfo;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class BaseActivity extends FragmentActivity {

	public int MyId = 0,MySchoolId = 0,MyGenderId = 0;
	public String MyEmail = "", MyPhone = "", MyNickName = "", MySchoolName = "", MyPortrait = "",MySignWord = "";
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
	}
	
	

	public void setUserInfo(){
		MyId = SXContext.getInstance().getUserInfo().getUserid();
		MyEmail = SXContext.getInstance().getUserInfo().getUseremail();
		MyPhone = SXContext.getInstance().getUserInfo().getUserphone();
		MyNickName = SXContext.getInstance().getUserInfo().getNickname();
		MySchoolName = SXContext.getInstance().getUserInfo().getSchoolname(); 
		MySchoolId = SXContext.getInstance().getUserInfo().getSchoolid();
		MyPortrait = SXContext.getInstance().getUserInfo().getPortrait();
		
		MySignWord = SXContext.getInstance().getUserInfo().getSignstr();
		MyGenderId = SXContext.getInstance().getUserInfo().getGenderid();
	}

	public int getMyId() {
		return MyId;
	}

	public void setMyId(int myId) {
		MyId = myId;
	}

	public int getMySchoolId() {
		return MySchoolId;
	}

	public void setMySchoolId(int mySchoolId) {
		MySchoolId = mySchoolId;
	}

	public String getMyEmail() {
		return MyEmail;
	}

	public void setMyEmail(String myEmail) {
		MyEmail = myEmail;
	}

	public String getMyPhone() {
		return MyPhone;
	}

	public void setMyPhone(String myPhone) {
		MyPhone = myPhone;
	}

	public String getMyNickName() {
		return MyNickName;
	}

	public void setMyNickName(String myNickName) {
		MyNickName = myNickName;
	}

	public String getMySchoolName() {
		return MySchoolName;
	}

	public void setMySchoolName(String mySchoolName) {
		MySchoolName = mySchoolName;
	}

	public String getMyPortrait() {
		return MyPortrait;
	}

	public void setMyPortrait(String myPortrait) {
		MyPortrait = myPortrait;
	}
	
	public String getMySignWord() {
		return MySignWord;
	}

	public void setMySignWord(String mySignWord) {
		MySignWord = mySignWord;
	}
	
	
	
}
