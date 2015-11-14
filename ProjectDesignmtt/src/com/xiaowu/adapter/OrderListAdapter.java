package com.xiaowu.adapter;

import java.util.List;

import com.xiaowu.activity.ChangeInfoActivity;
import com.xiaowu.activity.OrderDetailActivity;
import com.xiaowu.projectdesign.R;
import com.xiaowu.protocol.XwOrderList.OrderInfo;
import com.xiaowu.utils.Tool;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class OrderListAdapter extends BaseAdapter {

	public static final int TYPE_REVIEW_ORDER = 200;

	public static final int TYPE_USER = 1;
	public static final int TYPE_SHOP = 2;

	List<OrderInfo> orders;
	Context mcontext;
	int typ;
	LayoutInflater mInflater;
	OrderInfo item;

	public OrderListAdapter(List<OrderInfo> orders, Context mcontext, int typ) {
		super();
		this.orders = orders;
		this.mcontext = mcontext;
		this.typ = typ;
		mInflater = LayoutInflater.from(mcontext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return orders.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return orders.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View view = mInflater.inflate(R.layout.fragment2_item, null);
		ViewHolder holder = new ViewHolder(view);

		item = orders.get(arg0);

		holder.timeText.setText(item.getBuyDate());

		showDifferentContent(holder);

		holder.valueBtn.setOnClickListener(new ReviewBtnClick(arg0));

		holder.priceText.setText("￥" + item.getMoney());
		if (typ == TYPE_USER) {
			holder.nameText.setText(item.getsName());
			Tool.imageLoader(mcontext, holder.shopImage, item.getsPortrait(),
					null);
		} else if (typ == TYPE_SHOP) {
			holder.nameText.setText(item.getuName());
			Tool.imageLoader(mcontext, holder.shopImage, item.getuPortrait(),
					null);
		}

		return view;
	}

	private class ReviewBtnClick implements OnClickListener {
		int positon;

		public ReviewBtnClick(int positon) {
			super();
			this.positon = positon;
		}

		@Override
		public void onClick(View arg0) {
			Intent i = new Intent(mcontext, ChangeInfoActivity.class);
			i.putExtra("operateId", TYPE_REVIEW_ORDER);
			i.putExtra("oid", orders.get(positon).getId());
			mcontext.startActivity(i);
		}
	}

	private void showDifferentContent(ViewHolder holder) {
		if (typ == TYPE_SHOP) {
			holder.valueBtn.setVisibility(View.GONE);
			if (item.getOperateStatus() == 4) {
				holder.reviewText.setText("已完成");
				holder.reviewText.setTextColor(mcontext.getResources()
						.getColor(R.color.red));
			} else {
				holder.reviewText.setText("未完成");
			}
		} else if (typ == TYPE_USER) {
			if (item.getOperateStatus() == 4) {
				if (item.getIsReview() == 1) {
					holder.reviewText.setText("已评价");
					holder.reviewText.setTextColor(mcontext.getResources()
							.getColor(R.color.red));
					holder.valueBtn.setVisibility(View.GONE);
				} else {
					holder.reviewText.setText("未评价");
					holder.valueBtn.setVisibility(View.VISIBLE);
				}
			} else {
				holder.reviewText.setText("订单未完成");
				holder.valueBtn.setVisibility(View.GONE);
			}
		}
	}

	private class ViewHolder {
		private TextView timeText, reviewText, nameText, priceText, valueBtn;
		private ImageView shopImage;

		public ViewHolder(View v) {
			timeText = (TextView) v.findViewById(R.id.order_item_time);
			reviewText = (TextView) v.findViewById(R.id.order_item_isreview);
			nameText = (TextView) v.findViewById(R.id.order_item_name);
			priceText = (TextView) v.findViewById(R.id.order_item_price);
			valueBtn = (TextView) v.findViewById(R.id.order_item_value_btn);
			shopImage = (ImageView) v.findViewById(R.id.order_item_image);
		}

	}

}
