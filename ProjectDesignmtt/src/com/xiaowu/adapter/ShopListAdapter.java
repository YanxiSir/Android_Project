package com.xiaowu.adapter;

import java.util.List;

import com.xiaowu.activity.ShopPage;
import com.xiaowu.projectdesign.R;
import com.xiaowu.protocol.XwShopList.ShopInfos;
import com.xiaowu.utils.Tool;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ShopListAdapter extends BaseAdapter {
	List<ShopInfos> shops;
	Context mContext;
	ShopInfos item;

	public ShopListAdapter(List<ShopInfos> shops, Context mContext) {
		super();
		this.shops = shops;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return shops.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return shops.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub

		
		
		View view = View.inflate(mContext, R.layout.fragment1_item, null);
		ViewHolder holder = new ViewHolder(view);
		
		item = shops.get(arg0);

		holder.shopName.setText(item.getShopName());
		holder.priceText.setText("起送价"+item.getDiliverPrice()+"/配送费"+item.getDiliverFee());
		holder.otherText.setText("总销量"+item.getOrderCount());

		String path = item.getShopPicUrl();
		holder.shopImage.setTag(item.getShopPicUrl());
		if (holder.shopImage.getTag() != null && holder.shopImage.getTag().equals(path))
			Tool.imageLoader(mContext, holder.shopImage, path, null);
		holder.shopItemRel.setOnClickListener(new ShopItemClick(mContext,item));
		return view;
	}
	private class ShopItemClick implements OnClickListener{
		private Context mContext;
		private ShopInfos shopInfo;
		public ShopItemClick(Context context,ShopInfos shopInfo) {
			this.mContext = context;
			this.shopInfo = shopInfo;
		}
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(mContext,
					ShopPage.class);
			Bundle b = new Bundle();
			b.putSerializable("shopInfo", shopInfo);
			intent.putExtras(b);
			mContext.startActivity(intent);
		}
	}
	private class ViewHolder{
		private RelativeLayout shopItemRel;
		private ImageView shopImage;
		private TextView shopName,priceText,otherText;
		
		public ViewHolder(View v) {
			shopItemRel = (RelativeLayout) v.findViewById(R.id.si_shopitem_rel);
			shopImage = (ImageView) v.findViewById(R.id.si_shoppic);
			shopName = (TextView) v.findViewById(R.id.si_shopname);
			priceText = (TextView) v.findViewById(R.id.si_price);
			otherText = (TextView) v.findViewById(R.id.si_other);
		}
		
		
		
	}

}