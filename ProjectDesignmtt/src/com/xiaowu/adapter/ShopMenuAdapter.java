/*
 * Copyright 2014 Niek Haarman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xiaowu.adapter;

import java.util.ArrayList;
import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nhaarman.listviewanimations.ArrayAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.UndoAdapter;
import com.xiaowu.activity.ShopPage;
import com.xiaowu.fragment.ClassifyMenuFragment;
import com.xiaowu.projectdesign.R;
import com.xiaowu.protocol.XwShopMenu.ClassifyMenu;
import com.xiaowu.protocol.XwShopMenu.ClassifyMenu.ShopMenu;
import com.xiaowu.utils.OrderDetail;
import com.xiaowu.utils.OrderDetail.DishInfo;
import com.xiaowu.widget.ConfirmDialog;

public class ShopMenuAdapter extends ArrayAdapter<String> implements
		UndoAdapter, StickyListHeadersAdapter {

	private final Context mContext;
	private List<ClassifyMenu> classifyMenuList;
	private List<ShopMenu> shopMenuList;
	private ShopMenu item;
	LayoutInflater layoutInflater;
	private Handler mHandler;

	private ConfirmDialog confirmDialog;

	private int[] headerPosition;
	private int temp = 0;

	public ShopMenuAdapter(final Context context,
			List<ClassifyMenu> classifyMenuList, Handler mHandler) {
		this.mContext = context;
		this.classifyMenuList = classifyMenuList;
		this.mHandler = mHandler;
		layoutInflater = LayoutInflater.from(context);

		shopMenuList = new ArrayList<ShopMenu>();
		for (ClassifyMenu cm : classifyMenuList) {
			for (ShopMenu sm : cm.getMenus()) {
				shopMenuList.add(sm);
			}
		}
		headerPosition = new int[classifyMenuList.size()];
		for (int i = 0; i < classifyMenuList.size(); i++) {
			headerPosition[i] = temp;
			temp += classifyMenuList.get(i).getMenus().size();
		}

	}

	@Override
	public long getItemId(final int position) {
		return position;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public View getView(final int position, View convertView,
			final ViewGroup parent) {
		if (mContext == null)
			return convertView;

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = layoutInflater.inflate(R.layout.adapter_menu_item,
					null);
			;
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		item = shopMenuList.get(position);

		int i;
		for (i = 0; i < OrderDetail.dishList.size(); i++) {
			if (item.getId() == OrderDetail.dishList.get(i).getDishId()) {
				holder.reduceBtn.setVisibility(View.VISIBLE);
				holder.dishCount.setVisibility(View.VISIBLE);
				holder.dishCount.setText(OrderDetail.dishList.get(i)
						.getDishCount() + "");
				break;
			}
		}
		if (i == OrderDetail.dishList.size()) {
			holder.reduceBtn.setVisibility(View.GONE);
			holder.dishCount.setVisibility(View.GONE);
		}

		holder.dishName.setText(item.getName());
		holder.dishSoldCount.setText("总共售出:" + item.getCount() + "份");
		holder.priceBtn.setText("￥" + item.getPrice());
		holder.priceBtn
				.setOnClickListener(new PriceBtnClickLis(mContext, item));
		holder.reduceBtn.setOnClickListener(new ReduceBtnClickLis(mContext,
				item));

		return convertView;
	}

	private class ReduceBtnClickLis implements OnClickListener {
		Context mContext;
		ShopMenu menu;

		public ReduceBtnClickLis(Context mContext, ShopMenu menu) {
			this.mContext = mContext;
			this.menu = menu;
		}

		@Override
		public void onClick(View arg0) {
			for (DishInfo d : OrderDetail.dishList) {
				if (d.getDishId() == menu.getId()) {
					d.setDishCount(d.getDishCount() - 1);
					OrderDetail.totalPrice -= d.getDishPrice();
					if (d.getDishCount() == 0) {
						OrderDetail.dishList.remove(d);
					}
					mHandler.obtainMessage(
							ClassifyMenuFragment.CHANGE_ORDER_INFO)
							.sendToTarget();
					break;
				}
			}
			notifyDataSetChanged();
		}
	}

	private class PriceBtnClickLis implements OnClickListener {
		Context mContext;
		ShopMenu menu;

		public PriceBtnClickLis(Context mContext, ShopMenu menu) {
			this.mContext = mContext;
			this.menu = menu;
		}

		@Override
		public void onClick(View arg0) {
			if (OrderDetail.dishList.size() == 0) {
				OrderDetail.shopInfo = ((ShopPage) mContext).getShopInfo();

			}
			for (DishInfo d : OrderDetail.dishList) {
				if (OrderDetail.shopInfo.getShopId() != ((ShopPage) mContext)
						.getShopInfo().getShopId()) {
					showConfirmDialog();
					return;
				}
				if (d.getDishId() == menu.getId()) {
					d.setDishCount(d.getDishCount() + 1);
					OrderDetail.totalPrice += d.getDishPrice();
					mHandler.obtainMessage(
							ClassifyMenuFragment.CHANGE_ORDER_INFO)
							.sendToTarget();
					notifyDataSetChanged();
					return;
				}
			}
			DishInfo di = new DishInfo();
			di.setDishCount(1);
			di.setDishId(menu.getId());
			di.setDishName(menu.getName());
			di.setDishPrice(menu.getPrice());
			OrderDetail.dishList.add(di);
			OrderDetail.totalPrice += menu.getPrice();
			mHandler.obtainMessage(ClassifyMenuFragment.CHANGE_ORDER_INFO)
					.sendToTarget();
			notifyDataSetChanged();
			return;

		}

	}

	private void showConfirmDialog() {
		confirmDialog = new ConfirmDialog(mContext);
		confirmDialog.setMessage("篮子里面有 " + OrderDetail.shopInfo.getShopName()
				+ "家的菜单,是否需要清空?");
		confirmDialog.setLeftButton("取消", new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				confirmDialog.dismiss();
			}
		});
		confirmDialog.setRightButton("清空", new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				OrderDetail.clear();
				OrderDetail.shopInfo = ((ShopPage) mContext).getShopInfo();
				confirmDialog.dismiss();
				mHandler.obtainMessage(
						ClassifyMenuFragment.CHANGE_ORDER_INFO).sendToTarget();
			}
		});
	}

	@NonNull
	@Override
	public View getUndoView(final int position, final View convertView,
			@NonNull final ViewGroup parent) {
		View view = convertView;
		if (view == null) {
			view = LayoutInflater.from(mContext).inflate(R.layout.undo_row,
					parent, false);
		}
		return view;
	}

	@NonNull
	@Override
	public View getUndoClickView(@NonNull final View view) {
		return view.findViewById(R.id.undo_row_undobutton);
	}

	@Override
	public View getHeaderView(final int position, final View convertView,
			final ViewGroup parent) {
		TextView view = (TextView) convertView;
		if (view == null) {
			view = (TextView) LayoutInflater.from(mContext).inflate(
					R.layout.list_header, parent, false);
		}

		view.setText(classifyMenuList.get((int) getHeaderId(position))
				.getClassifyName());

		return view;
	}

	@Override
	public long getHeaderId(final int position) {
		int length = headerPosition.length;
		for (int i = 0; i < length; i++) {
			if (position < headerPosition[i]) {
				return i - 1;
			}
		}
		return length - 1;
	}

	private class ViewHolder {
		private TextView dishName, dishSoldCount, reduceBtn, dishCount,
				priceBtn;

		public ViewHolder(View v) {
			dishName = (TextView) v.findViewById(R.id.ami_dish_name);
			dishSoldCount = (TextView) v.findViewById(R.id.ami_dish_count);
			reduceBtn = (TextView) v.findViewById(R.id.ami_reduce_btn);
			dishCount = (TextView) v.findViewById(R.id.ami_count);
			priceBtn = (TextView) v.findViewById(R.id.ami_dish_price);
		}

	}
}