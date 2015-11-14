package com.xiaowu.common;

import com.xiaowu.protocol.XwUserInfo;
import com.xiaowu.shop.protocol.XwShopInfo;

import android.content.Context;

public class XwContext {
	public Context mContext;
	private XwUserInfo userInfo;
	private XwShopInfo shopInfo;
	
	private static XwContext self;
	private RequestAPI mRequestAPI;
	
	public static XwContext getInstance(){
		if(self == null)
			self = new XwContext();
		return self;
	}
	public void init(Context context){
		this.mContext = context;
		mRequestAPI = new RequestAPI(context);
	}
	public RequestAPI getmRequestAPI() {
		return mRequestAPI;
	}
	public XwUserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(XwUserInfo userInfo) {
		this.userInfo = userInfo;
	}
	public XwShopInfo getShopInfo() {
		return shopInfo;
	}
	public void setShopInfo(XwShopInfo shopInfo) {
		this.shopInfo = shopInfo;
	}
	
	
	
	
}
