package com.xiaowu.shop.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaowu.activity.ChangeInfoActivity;
import com.xiaowu.projectdesign.R;
import com.xiaowu.shop.activity.ShopMenuEditActivity;
import com.xiaowu.widget.ConfirmDialog;

public class ShopMenuClassifyAdapter extends BaseAdapter {
	private Context mContext;
	ArrayList<String> list;
	LayoutInflater mInflater;

	private Handler mHandler;

	ConfirmDialog confirmDialog;

	public ShopMenuClassifyAdapter(Context mContext, ArrayList<String> list,
			Handler mHandler) {
		super();
		this.mContext = mContext;
		this.list = list;
		this.mHandler = mHandler;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View view = mInflater.inflate(R.layout.adapter_menu_group, null);

		RelativeLayout groupRel = (RelativeLayout) view
				.findViewById(R.id.adapter_item_group_rel);
		ImageView leftImage = (ImageView) view
				.findViewById(R.id.adapter_item_group_image);
		TextView center = (TextView) view
				.findViewById(R.id.adapter_item_group_title);
		TextView rightBtn = (TextView) view
				.findViewById(R.id.adapter_item_group_del);

		groupRel.setBackground(mContext.getResources().getDrawable(
				R.drawable.line_bg));
		leftImage.setVisibility(View.GONE);
		center.setText(list.get(arg0).toString());
		rightBtn.setVisibility(View.VISIBLE);

		groupRel.setOnClickListener(new ClassifyOnClickLis(arg0));
		rightBtn.setOnClickListener(new delClickLis(arg0));

		return view;
	}

	private class delClickLis implements OnClickListener {
		private int positon;

		public delClickLis(int positon) {
			super();
			this.positon = positon;
		}

		@Override
		public void onClick(View arg0) {
			confirmDialog = new ConfirmDialog(mContext);
			confirmDialog.setMessage("删除分类会同时删除里面的菜谱，是否继续？");
			confirmDialog.setLeftButton("取消", new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					confirmDialog.dismiss();
				}
			});
			confirmDialog.setRightButton("删除", new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					confirmDialog.dismiss();
					Bundle b = new Bundle();
					b.putInt("pid", positon);
					mHandler.obtainMessage(
							ShopMenuEditActivity.TYPE_DEL_CLASSIFY, b)
							.sendToTarget();
				}
			});

		}
	}

	private class ClassifyOnClickLis implements OnClickListener {
		private int positon;

		public ClassifyOnClickLis(int positon) {
			super();
			this.positon = positon;
		}

		@Override
		public void onClick(View arg0) {

			Bundle b = new Bundle();
			b.putInt("pid", positon);
			mHandler.obtainMessage(ShopMenuEditActivity.TYPE_UPDATE_CLASSIFY, b)
					.sendToTarget();
		}

	}

}
