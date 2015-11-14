package com.xiaowu.protocol;

import java.io.Serializable;
import java.util.List;

import com.xiaowu.finals.ServerFinals;
import com.xiaowu.utils.Tool;

public class XwShopList implements Serializable{

	private List<ShopInfos> shops;
	
	public List<ShopInfos> getShops() {
		return shops;
	}

	public void setShops(List<ShopInfos> shops) {
		this.shops = shops;
	}

	public static class ShopInfos implements Serializable{
		private int shopId;
		private String shopName;
		private String shopPicUrl;
		private int diliverPrice;
		private int diliverTime;
		private int diliverFee;
		private int orderCount;
		private float reviewGrade;
		private String intro;
		private String startTime;
		private String endTime;
		private String addr;
		private String phone;
		
		public String getIntro() {
			return Tool.decodeUTF_8(intro);
		}
		public void setIntro(String intro) {
			intro = intro;
		}
		public String getStartTime() {
			return startTime;
		}
		public void setStartTime(String startTime) {
			this.startTime = startTime;
		}
		public String getEndTime() {
			return endTime;
		}
		public void setEndTime(String endTime) {
			this.endTime = endTime;
		}
		public String getAddr() {
			return Tool.decodeUTF_8(addr);
		}
		public void setAddr(String addr) {
			this.addr = addr;
		}
		public String getPhone() {
			return phone;
		}
		public void setPhone(String phone) {
			this.phone = phone;
		}
		public int getShopId() {
			return shopId;
		}
		public void setShopId(int shopId) {
			this.shopId = shopId;
		}
		public String getShopName() {
			return Tool.decodeUTF_8(shopName);
		}
		public void setShopName(String shopName) {
			this.shopName = shopName;
		}
		public String getShopPicUrl() {
			return ServerFinals.SHOP_H+shopPicUrl;
		}
		public void setShopPicUrl(String shopPicUrl) {
			this.shopPicUrl = shopPicUrl;
		}
		public int getDiliverPrice() {
			return diliverPrice;
		}
		public void setDiliverPrice(int diliverPrice) {
			this.diliverPrice = diliverPrice;
		}
		public int getDiliverTime() {
			return diliverTime;
		}
		public void setDiliverTime(int diliverTime) {
			this.diliverTime = diliverTime;
		}
		public int getDiliverFee() {
			return diliverFee;
		}
		public void setDiliverFee(int diliverFee) {
			this.diliverFee = diliverFee;
		}
		public int getOrderCount() {
			return orderCount;
		}
		public void setOrderCount(int orderCount) {
			this.orderCount = orderCount;
		}
		public float getReviewGrade() {
			return reviewGrade;
		}
		public void setReviewGrade(float reviewGrade) {
			this.reviewGrade = reviewGrade;
		}
		
		
	}
}
