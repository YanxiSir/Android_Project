package com.xiaowu.protocol;

import java.io.Serializable;
import java.util.List;

import com.xiaowu.finals.ServerFinals;
import com.xiaowu.utils.Tool;

public class XwOrderList implements Serializable {

	private List<OrderInfo> orders;

	public List<OrderInfo> getOrders() {
		return orders;
	}

	public void setOrders(List<OrderInfo> orders) {
		this.orders = orders;
	}

	public static class OrderInfo implements Serializable {
		private int id;
		private float money;
		private int operateStatus;
		private String buyContent;
		private String buyDate;
		private int isReview;
		private int payWay;
		private String addrStr;
		private String sendTime;
		private String extra;
		private float diliverFee;

		private int uid;
		private String uName;
		private String uPortrait;

		private int sid;
		private String sName;
		private String sPortrait;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public float getMoney() {
			return money;
		}

		public void setMoney(float money) {
			this.money = money;
		}

		public int getOperateStatus() {
			return operateStatus;
		}

		public void setOperateStatus(int operateStatus) {
			this.operateStatus = operateStatus;
		}

		public String getBuyContent() {
			return Tool.decodeUTF_8(buyContent);
		}

		public void setBuyContent(String buyContent) {
			this.buyContent = buyContent;
		}

		public String getBuyDate() {
			return Tool.decodeUTF_8(buyDate);
		}

		public void setBuyDate(String buyDate) {
			this.buyDate = buyDate;
		}

		public int getIsReview() {
			return isReview;
		}

		public void setIsReview(int isReview) {
			this.isReview = isReview;
		}

		public int getPayWay() {
			return payWay;
		}

		public void setPayWay(int payWay) {
			this.payWay = payWay;
		}

		public String getAddrStr() {
			return Tool.decodeUTF_8(addrStr);
		}

		public void setAddrStr(String addrStr) {
			this.addrStr = addrStr;
		}

		public String getSendTime() {
			return Tool.decodeUTF_8(sendTime);
		}

		public void setSendTime(String sendTime) {
			this.sendTime = sendTime;
		}

		public String getExtra() {
			return Tool.decodeUTF_8(extra);
		}

		public void setExtra(String extra) {
			this.extra = extra;
		}

		public float getDiliverFee() {
			return diliverFee;
		}

		public void setDiliverFee(float diliverFee) {
			this.diliverFee = diliverFee;
		}

		public int getUid() {
			return uid;
		}

		public void setUid(int uid) {
			this.uid = uid;
		}

		public String getuName() {
			return Tool.decodeUTF_8(uName);
		}

		public void setuName(String uName) {
			this.uName = uName;
		}

		public String getuPortrait() {
			return ServerFinals.USER_H + uPortrait;
		}

		public void setuPortrait(String uPortrait) {
			this.uPortrait = uPortrait;
		}

		public int getSid() {
			return sid;
		}

		public void setSid(int sid) {
			this.sid = sid;
		}

		public String getsName() {
			return Tool.decodeUTF_8(sName);
		}

		public void setsName(String sName) {
			this.sName = sName;
		}

		public String getsPortrait() {
			return ServerFinals.SHOP_H + sPortrait;
		}

		public void setsPortrait(String sPortrait) {
			this.sPortrait = sPortrait;
		}

	}
}
