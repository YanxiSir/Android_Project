package com.xiaowu.finals;

public class ServerFinals {

	public final static String H = "http://192.168.137.1:8080/XwWaimaiServiceProject/";

	public final static String SHOP_H = "http://192.168.137.1:8080/waimai/headImage/shop/";
	public final static String USER_H = "http://192.168.137.1:8080/waimai/headImage/user/";

	// public final static String H =
	// "http://58.154.189.114:8080/XwWaimaiServiceProject/";
	//
	// public final static String SHOP_H =
	// "http://58.154.189.114:8080/waimai/headImage/shop/";
	// public final static String USER_H =
	// "http://58.154.189.114:8080/waimai/headImage/user/";

	public final static String getTest = H + "TextServlet";

	public final static String login = H + "LoginServlet";
	public final static String register = H + "RegisterServlet";
	public final static String getShops = H + "GetShopInfosServlet";
	public final static String getShopMenus = H + "GetShopDetailServlet";
	public final static String submitOrder = H + "SubmitOrderServlet";
	public final static String getOrderList = H + "GetOrderListServlet";
	public final static String order_review = H + "OrderReviewServlet";
	/*
	 * shop API
	 */
	public final static String shop_login = H + "ShopLoginServlet";
	public final static String shop_update_basic_info = H
			+ "ShopUpdateBasicInfoServlet";
	public final static String shop_menu_operate = H + "ShopMenuOperate";
	public final static String shop_order_process = H
			+ "ShopOrderProcessServlet";
	
}
