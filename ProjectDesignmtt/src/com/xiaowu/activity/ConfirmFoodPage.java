package com.xiaowu.activity;

import com.xiaowu.adapter.OrderSubmitAdapter;
import com.xiaowu.projectdesign.R;
import com.xiaowu.utils.OrderDetail;
import com.xiaowu.utils.OrderDetail.DishInfo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ConfirmFoodPage extends Activity {

	private RelativeLayout titleLeftRel, titleRightRel;
	private TextView titleCenterText;
	private ImageView titleLeftImage;

	private ListView listview;
	OrderSubmitAdapter adapter;
	private TextView foodCount,foodPrice,submitBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.confirm_food_page);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_left_center_right);

		initTitlebar();
		initUI();

	}

	private void initUI() {
		listview = (ListView) findViewById(R.id.confirm_food_listview);
		foodCount = (TextView) findViewById(R.id.confirm_food_count);
		foodPrice = (TextView) findViewById(R.id.confirm_food_price);
		submitBtn = (TextView) findViewById(R.id.confirm_food_submit);
		
		adapter = new OrderSubmitAdapter(ConfirmFoodPage.this, OrderDetail.dishList);
		listview.setAdapter(adapter); 
		
		int count = 0;
		for(DishInfo di : OrderDetail.dishList){
			count+=di.getDishCount();
		}
		foodCount.setText(count+"份美食");
		foodPrice.setText("￥"+OrderDetail.totalPrice); 
		
		submitBtn.setOnClickListener(submitBtnClickLis);
	}
	private OnClickListener submitBtnClickLis = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(ConfirmFoodPage.this,SubmitOrderActivity.class);
			startActivity(intent); 
		}
	};

	private void initTitlebar() {
		titleLeftRel = (RelativeLayout) findViewById(R.id.titlebar_lcr_left_rel);
		titleRightRel = (RelativeLayout) findViewById(R.id.titlebar_lcr_right_rel);
		titleCenterText = (TextView) findViewById(R.id.titlebar_lcr_center_text);
		titleLeftImage = (ImageView) findViewById(R.id.titlebar_lcr_left_image);
		titleLeftRel.setVisibility(View.VISIBLE);
		titleRightRel.setVisibility(View.GONE);

		titleLeftImage.setImageDrawable(getResources().getDrawable(
				R.drawable.back));
		titleCenterText.setText(OrderDetail.shopInfo.getShopName());// 需要shopname

		titleLeftRel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				onBackPressed();
			}
		});

	}

}
