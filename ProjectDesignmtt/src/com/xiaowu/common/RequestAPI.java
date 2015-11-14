package com.xiaowu.common;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.sea_monster.core.exception.InternalException;
import com.sea_monster.core.exception.ParseException;
import com.xiaowu.app.AppManager;
import com.xiaowu.common.net.CWebService;
import com.xiaowu.common.net.CWebService.WebHandlerCallBack;
import com.xiaowu.common.net.CWebService.WebHandlerCallBack2;
import com.xiaowu.finals.ServerFinals;
import com.xiaowu.protocol.XwNoResultDefault;
import com.xiaowu.protocol.XwOrderList;
import com.xiaowu.protocol.XwShopList;
import com.xiaowu.protocol.XwShopMenu;
import com.xiaowu.protocol.XwStatus;
import com.xiaowu.protocol.XwUserInfo;
import com.xiaowu.protocol.parser.GsonParser;
import com.xiaowu.shop.protocol.XwShopInfo;
import com.xiaowu.utils.L;
import com.xiaowu.utils.T;

import android.content.Context;
import android.text.TextUtils;

public class RequestAPI {
	private Context mContext;

	public RequestAPI(Context mContext) {
		super();
		this.mContext = mContext;
	}

	public void NoResultRequest(String url, List<NameValuePair> params,
			final IApiCallback callback) {
		CWebService.reqSessionHandler(mContext, url, params,
				new WebHandlerCallBack() {

					@Override
					public void callBack(String result) {
						try {

							if (TextUtils.isEmpty(result))
								return;
							L.d(result);
							Type type = new TypeToken<XwStatus<XwNoResultDefault>>() {
							}.getType();
							GsonParser<XwStatus<XwNoResultDefault>> parser = new GsonParser<XwStatus<XwNoResultDefault>>(
									type);

							try {
								XwStatus<XwNoResultDefault> tmpResult = parser
										.parse(new JsonReader(new StringReader(
												result)));
								if (tmpResult.getResultcode() == 1) {
									callback.onComplete(null);
								} else {
									callback.onError(Integer.valueOf(tmpResult
											.getResultcode()));
								}

							} catch (ParseException e) {
								e.printStackTrace();
							} catch (InternalException e) { // TODO
															// Auto-generated
															// catch block
								e.printStackTrace();
							}
						} catch (IOException e1) { // TODO Auto-generated
							e1.printStackTrace();
						}
					}
				});
	}

	/*
	 * 获取 订单信息
	 */
	public void getOrderList(List<NameValuePair> params,
			final IApiCallback callback) {
		CWebService.reqSessionHandler(mContext, ServerFinals.getOrderList,
				params, new WebHandlerCallBack() {

					@Override
					public void callBack(String result) {
						try {
							L.d(result);
							if (TextUtils.isEmpty(result))
								return;
							Type type = new TypeToken<XwStatus<XwOrderList>>() {
							}.getType();
							GsonParser<XwStatus<XwOrderList>> parser = new GsonParser<XwStatus<XwOrderList>>(
									type);

							try {
								XwStatus<XwOrderList> tmpResult = parser
										.parse(new JsonReader(new StringReader(
												result)));
								if (tmpResult.getResultcode() == 1) {
									callback.onComplete(tmpResult.getResult());
								} else {
									callback.onError(Integer.valueOf(tmpResult
											.getResultcode()));
								}

							} catch (ParseException e) {
								e.printStackTrace();
							} catch (InternalException e) { // TODO
															// Auto-generated
															// catch block
								e.printStackTrace();
							}
						} catch (IOException e1) { // TODO Auto-generated
							e1.printStackTrace();
						}
					}
				});
	}

	/*
	 * get 方式获取店家menu
	 */
	public void getShopMenus(int sid, final IApiCallback callback) {
		String url = ServerFinals.getShopMenus + "?sid=" + sid;
		CWebService.reqSessionHandlerGet(mContext, url,
				new WebHandlerCallBack() {

					@Override
					public void callBack(String result) {
						try {

							if (TextUtils.isEmpty(result))
								return;
							L.d(result);
							Type type = new TypeToken<XwStatus<XwShopMenu>>() {
							}.getType();
							GsonParser<XwStatus<XwShopMenu>> parser = new GsonParser<XwStatus<XwShopMenu>>(
									type);

							try {
								XwStatus<XwShopMenu> tmpResult = parser
										.parse(new JsonReader(new StringReader(
												result)));
								if (tmpResult.getResultcode() == 1) {
									callback.onComplete(tmpResult.getResult());
								} else {
									callback.onError(Integer.valueOf(tmpResult
											.getResultcode()));
								}

							} catch (ParseException e) {
								e.printStackTrace();
							} catch (InternalException e) {
								e.printStackTrace();
							}
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				});
	}

	/*
	 * 请求店家列表
	 */
	public void shopList(List<NameValuePair> params, final IApiCallback callback) {
		CWebService.reqSessionHandler(mContext, ServerFinals.getShops, params,
				new WebHandlerCallBack() {

					@Override
					public void callBack(String result) {
						try {
							L.d(result);
							if (TextUtils.isEmpty(result))
								return;
							Type type = new TypeToken<XwStatus<XwShopList>>() {
							}.getType();
							GsonParser<XwStatus<XwShopList>> parser = new GsonParser<XwStatus<XwShopList>>(
									type);

							try {
								XwStatus<XwShopList> tmpResult = parser
										.parse(new JsonReader(new StringReader(
												result)));
								if (tmpResult.getResultcode() == 1) {
									callback.onComplete(tmpResult.getResult());
								} else {
									callback.onError(Integer.valueOf(tmpResult
											.getResultcode()));
								}

							} catch (ParseException e) {
								e.printStackTrace();
							} catch (InternalException e) { // TODO
															// Auto-generated
															// catch block
								e.printStackTrace();
							}
						} catch (IOException e1) { // TODO Auto-generated
							e1.printStackTrace();
						}
					}
				});
	}

	/*
	 * 注册
	 */
	public void register(List<NameValuePair> params, final IApiCallback callback) {
		CWebService.reqSessionHandler(mContext, ServerFinals.register, params,
				new WebHandlerCallBack() {

					@Override
					public void callBack(String result) {
						try {
							if (TextUtils.isEmpty(result))
								return;
							Type type = new TypeToken<XwStatus<XwNoResultDefault>>() {
							}.getType();
							GsonParser<XwStatus<XwNoResultDefault>> parser = new GsonParser<XwStatus<XwNoResultDefault>>(
									type);

							try {
								XwStatus<XwNoResultDefault> tmpResult = parser
										.parse(new JsonReader(new StringReader(
												result)));
								if (tmpResult.getResultcode() == 1) {
									callback.onComplete(tmpResult.getResult());
								} else {
									callback.onError(Integer.valueOf(tmpResult
											.getResultcode()));
								}

							} catch (ParseException e) {
								e.printStackTrace();
							} catch (InternalException e) { // TODO
															// Auto-generated
															// catch block
								e.printStackTrace();
							}
						} catch (IOException e1) { // TODO Auto-generated
							e1.printStackTrace();
						}
					}
				});
	}

	/*
	 * 登录
	 */
	public void login(List<NameValuePair> params, final IApiCallback callback) 
	{
		
		CWebService.reqSessionHandler(mContext, ServerFinals.login, params,
				new WebHandlerCallBack() {

					@Override
					public void callBack(String result) {
						try {
							if (TextUtils.isEmpty(result))
								return;
							Type type = new TypeToken<XwStatus<XwUserInfo>>() {
							}.getType();
							GsonParser<XwStatus<XwUserInfo>> parser = new GsonParser<XwStatus<XwUserInfo>>(
									type);
							try {
								XwStatus<XwUserInfo> tmpResult = parser
										.parse(new JsonReader(new StringReader(
												result)));
								if (tmpResult.getResultcode() == 1) {
									callback.onComplete(tmpResult.getResult());
								} else {
									callback.onError(Integer.valueOf(tmpResult
											.getResultcode()));
								}

							} catch (ParseException e) {
								e.printStackTrace();
							} catch (InternalException e) { // TODO
															// Auto-generated
															// catch block
								e.printStackTrace();
							}
						} catch (IOException e1) { // TODO Auto-generated
							e1.printStackTrace();
						}
					}
				});
	}

	public void getTest(List<NameValuePair> params, final IApiCallback callback) {
		CWebService.reqSessionHandler(mContext, ServerFinals.H
				+ ServerFinals.getTest, params, new WebHandlerCallBack() {

			@Override
			public void callBack(String result) {
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(result))
					T.showShort(mContext, "result:" + result);
				else
					T.showShort(mContext, "nothing");
			}
		});
	}

	public static void getURLBitmap(String url, final IApiCallback callback) {

		CWebService.getURLBitmap(AppManager.getInstance(), url,
				new WebHandlerCallBack2() {

					@Override
					public void callBack(Object obj) {
						callback.onComplete(obj);
					}
				});

	}

	/*
	 * 卖家API
	 */
	public void shopLogin(List<NameValuePair> params,
			final IApiCallback callback) {
		CWebService.reqSessionHandler(mContext, ServerFinals.shop_login,
				params, new WebHandlerCallBack() {

					@Override
					public void callBack(String result) {
						try {
							if (TextUtils.isEmpty(result))
								return;
							Type type = new TypeToken<XwStatus<XwShopInfo>>() {
							}.getType();
							GsonParser<XwStatus<XwShopInfo>> parser = new GsonParser<XwStatus<XwShopInfo>>(
									type);

							try {
								XwStatus<XwShopInfo> tmpResult = parser
										.parse(new JsonReader(new StringReader(
												result)));
								if (tmpResult.getResultcode() == 1) {
									callback.onComplete(tmpResult.getResult());
								} else {
									callback.onError(Integer.valueOf(tmpResult
											.getResultcode()));
								}

							} catch (ParseException e) {
								e.printStackTrace();
							} catch (InternalException e) { // TODO
															// Auto-generated
															// catch block
								e.printStackTrace();
							}
						} catch (IOException e1) { // TODO Auto-generated
							e1.printStackTrace();
						}
					}
				});
	}

	public void shopUpdateBasicInfo(List<NameValuePair> params,
			final IApiCallback callback) {
		CWebService.reqSessionHandler(mContext,
				ServerFinals.shop_update_basic_info, params,
				new WebHandlerCallBack() {

					@Override
					public void callBack(String result) {
						try {
							if (TextUtils.isEmpty(result))
								return;
							Type type = new TypeToken<XwStatus<XwNoResultDefault>>() {
							}.getType();
							GsonParser<XwStatus<XwNoResultDefault>> parser = new GsonParser<XwStatus<XwNoResultDefault>>(
									type);

							try {
								XwStatus<XwNoResultDefault> tmpResult = parser
										.parse(new JsonReader(new StringReader(
												result)));
								if (tmpResult.getResultcode() == 1) {
									callback.onComplete(null);
								} else {
									callback.onError(Integer.valueOf(tmpResult
											.getResultcode()));
								}

							} catch (ParseException e) {
								e.printStackTrace();
							} catch (InternalException e) { // TODO
															// Auto-generated
															// catch block
								e.printStackTrace();
							}
						} catch (IOException e1) { // TODO Auto-generated
							e1.printStackTrace();
						}
					}
				});
	}

	public interface IApiCallback {
		void onError(int errorCode);

		void onComplete(Object object);
	}
}
