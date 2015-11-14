package com.xiaowu.shop.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaowu.projectdesign.R;
import com.xiaowu.protocol.XwShopMenu.ClassifyMenu;
import com.xiaowu.protocol.XwShopMenu.ClassifyMenu.ShopMenu;
import com.xiaowu.shop.activity.ShopMainActivity;

public class ShopMenuExpandeAdapter extends BaseExpandableListAdapter {

	private Context mContext;
	private List<ClassifyMenu> mData = null;
	private List<String> mGroupStrings;
	private LayoutInflater mInflater = null;

	private Handler mHandler;

	public ShopMenuExpandeAdapter(Context mContext,
			List<ClassifyMenu> classifyMenuList, Handler mHandler) {
		super();
		this.mContext = mContext;
		this.mData = classifyMenuList;
		this.mHandler = mHandler;
		mInflater = LayoutInflater.from(mContext);
		mGroupStrings = new ArrayList<String>();
		for (ClassifyMenu cm : classifyMenuList) {
			mGroupStrings.add(cm.getClassifyName());
		}
	}

	public void setData(List<ClassifyMenu> classifyMenuList) {
		mData = classifyMenuList;
	}

	@Override
	public ShopMenu getChild(int groupPosition, int childPosition) {
		return mData.get(groupPosition).getMenus().get(childPosition);
	}

	@Override
	public long getChildId(int arg0, int arg1) {
		return arg1;
	}

	@Override
	public int getChildrenCount(int arg0) {
		return mData.get(arg0).getMenus().size();
	}

	@Override
	public List<ShopMenu> getGroup(int arg0) {
		return mData.get(arg0).getMenus();
	}

	@Override
	public int getGroupCount() {
		return mData.size();
	}

	@Override
	public long getGroupId(int arg0) {
		return arg0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.adapter_menu_group, null);
		}

		GroupViewHolder holder = new GroupViewHolder(convertView);

		holder.classifyName.setText(mGroupStrings.get(groupPosition));

		if (isExpanded) {
			holder.flagImage.setImageDrawable(mContext.getResources()
					.getDrawable(R.drawable.jiantou_down));
		} else {
			holder.flagImage.setImageDrawable(mContext.getResources()
					.getDrawable(R.drawable.jiantou_right));
		}

		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.adapter_menu_child, null);
		}
		ChildViewHolder holder = new ChildViewHolder(convertView);

		ShopMenu item = mData.get(groupPosition).getMenus().get(childPosition);

		holder.dishName.setText(item.getName());
		holder.dishCount.setText("»¹Ê£ " + item.getCount() + " ·Ý");
		holder.dishPrice.setText("£¤" + item.getPrice());

		holder.dishLl.setOnClickListener(new DishLlClickLis(groupPosition,childPosition));

		return convertView;
	}

	private class DishLlClickLis implements OnClickListener {
		int groupPosition;
		int childPosition;

		public DishLlClickLis(int groupPosition, int childPosition) {
			super();
			this.groupPosition = groupPosition;
			this.childPosition = childPosition;
		}

		@Override
		public void onClick(View arg0) {
			int mid = mData.get(groupPosition).getMenus().get(childPosition).getId();
			Bundle b = new Bundle();
			b.putInt("mid", mid);
			b.putInt("gPosition", groupPosition);
			b.putInt("cPosition", childPosition); 
			mHandler.obtainMessage(ShopMainActivity.TYPE_LONG_CLICK_MENU, b)
					.sendToTarget();
		}
	}

	private class GroupViewHolder {
		public TextView classifyName;
		private ImageView flagImage;
		private RelativeLayout groupRel;

		public GroupViewHolder(View v) {
			flagImage = (ImageView) v
					.findViewById(R.id.adapter_item_group_image);
			classifyName = (TextView) v
					.findViewById(R.id.adapter_item_group_title);
			groupRel = (RelativeLayout) v
					.findViewById(R.id.adapter_item_group_rel);
		}

	}

	private class ChildViewHolder {
		public TextView dishName, dishCount, dishPrice;
		private LinearLayout dishLl;

		public ChildViewHolder(View v) {
			dishLl = (LinearLayout) v.findViewById(R.id.adapter_menu_child_ll);
			dishName = (TextView) v.findViewById(R.id.adapter_menu_child_name);
			dishCount = (TextView) v
					.findViewById(R.id.adapter_menu_child_left_count);
			dishPrice = (TextView) v
					.findViewById(R.id.adapter_menu_child_price);
		}

	}

}
