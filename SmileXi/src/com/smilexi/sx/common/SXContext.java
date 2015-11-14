package com.smilexi.sx.common;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;





import com.smilexi.sx.protocol.SxUserBaseInfo;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

public class SXContext {
	public Context mContext;

	private static SXContext self;
	private SmileXiApi mSmileXiApi;
	
	/** µÇÂ¼ÐÅÏ¢ **/
//	private UserInfo userInfo;
	private SxUserBaseInfo userInfo;

	public static SXContext getInstance() {

		if (self == null) {
			self = new SXContext();
		}

		return self;
	}

	public void init(Context context) {
		this.mContext = context;
		mSmileXiApi = new SmileXiApi(context); 
	}
	
	
	
	public void getUserInfoFromServer() {

	}
	public boolean persistenceUserInfo(final Context context, SxUserBaseInfo userInfo) {
		
		setUserInfo(userInfo); 
		
		return false;
	}
	
	

	public SmileXiApi getmSmileXiApi() {
		return mSmileXiApi;
	}

	public SxUserBaseInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(SxUserBaseInfo userInfo) {
		this.userInfo = userInfo;
	}
	
	
}
