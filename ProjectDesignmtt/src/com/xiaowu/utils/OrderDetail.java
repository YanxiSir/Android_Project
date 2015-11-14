package com.xiaowu.utils;

import java.util.ArrayList;
import java.util.List;

import com.xiaowu.protocol.XwShopList.ShopInfos;

public class OrderDetail {

	public static int totalPrice = 0;
	public static ShopInfos shopInfo ;
	public static List<DishInfo> dishList = new ArrayList<OrderDetail.DishInfo>();
	
	public static void clear(){
		totalPrice = 0;
		dishList.removeAll(dishList); 
		shopInfo = null;
	}

	public static class DishInfo {
		private int dishId;
		private String dishName;
		private int dishCount;
		private int dishPrice;

		public int getDishId() {
			return dishId;
		}

		public void setDishId(int dishId) {
			this.dishId = dishId;
		}

		public String getDishName() {
			return dishName;
		}

		public void setDishName(String dishName) {
			this.dishName = dishName;
		}

		public int getDishCount() {
			return dishCount;
		}

		public void setDishCount(int dishCount) {
			this.dishCount = dishCount;
		}

		public int getDishPrice() {
			return dishPrice;
		}

		public void setDishPrice(int dishPrice) {
			this.dishPrice = dishPrice;
		}

	}
}
