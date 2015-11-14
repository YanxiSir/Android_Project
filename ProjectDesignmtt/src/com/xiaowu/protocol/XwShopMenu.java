package com.xiaowu.protocol;

import java.io.Serializable;
import java.util.List;

import com.xiaowu.utils.Tool;

public class XwShopMenu implements Serializable {

	private List<ClassifyMenu> classifyMenu;

	public List<ClassifyMenu> getClassifyMenu() {
		return classifyMenu;
	}

	public void setClassifyMenu(List<ClassifyMenu> classifyMenu) {
		this.classifyMenu = classifyMenu;
	}

	public static class ClassifyMenu implements Serializable {
		private int id;
		private String classifyName;
		private List<ShopMenu> menus;

		
		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public String getClassifyName() {
			return Tool.decodeUTF_8(classifyName);
		}

		public void setClassifyName(String classifyName) {
			this.classifyName = classifyName;
		}

		public List<ShopMenu> getMenus() {
			return menus;
		}

		public void setMenus(List<ShopMenu> menus) {
			this.menus = menus;
		}

		public static class ShopMenu implements Serializable {
			private int id;
			private String name;
			private int price;
			private int count;

			public int getId() {
				return id;
			}

			public void setId(int id) {
				this.id = id;
			}

			public String getName() {
				return Tool.decodeUTF_8(name);
			}

			public void setName(String name) {
				this.name = name;
			}

			public int getPrice() {
				return price;
			}

			public void setPrice(int price) {
				this.price = price;
			}

			public int getCount() {
				return count;
			}

			public void setCount(int count) {
				this.count = count;
			}

		}
	}
}
