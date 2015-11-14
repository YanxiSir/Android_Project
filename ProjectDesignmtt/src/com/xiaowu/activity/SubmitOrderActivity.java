package com.xiaowu.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.xiaowu.common.RequestAPI.IApiCallback;
import com.xiaowu.common.XwContext;
import com.xiaowu.db.dao.AddressBean;
import com.xiaowu.db.dao.AddressDao;
import com.xiaowu.finals.ServerFinals;
import com.xiaowu.projectdesign.R;
import com.xiaowu.utils.OrderDetail;
import com.xiaowu.utils.OrderDetail.DishInfo;
import com.xiaowu.utils.T;
import com.xiaowu.widget.LoadingDialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SubmitOrderActivity extends Activity implements OnClickListener {
	public static final int TYPE_CHANGE_TIME = 0;
	public static final int TYPE_ADD_EXTRA = 1;
	public static final int TYPE_FOR_ADDRESS = 2;

	private RelativeLayout titleLeftRel, titleRightRel;
	private TextView titleCenterText;
	private ImageView titleLeftImage;

	private LinearLayout addrLl, timeLl, extraLl;
	private TextView addr, phone, sendTime, extraInfo, totalPrice, foodPrice,
			feePrice, submitBtn;

	AddressDao dao;
	List<AddressBean> beans;
	AddressBean bean;
	
	private LoadingDialog loadingDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_submit_order);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_left_center_right);
		initTitlebar();
		initUI();
	}

	private void initUI() {
		addrLl = (LinearLayout) findViewById(R.id.act_suborder_addr_ll);
		timeLl = (LinearLayout) findViewById(R.id.act_suborder_time_ll);
		extraLl = (LinearLayout) findViewById(R.id.act_suborder_extra_ll);
		addr = (TextView) findViewById(R.id.act_suborder_addr);
		phone = (TextView) findViewById(R.id.act_suborder_phone);
		sendTime = (TextView) findViewById(R.id.act_suborder_time);
		extraInfo = (TextView) findViewById(R.id.act_suborder_extra);
		totalPrice = (TextView) findViewById(R.id.act_suborder_totalprice);
		foodPrice = (TextView) findViewById(R.id.act_suborder_foodprice);
		feePrice = (TextView) findViewById(R.id.act_suborder_fee);
		submitBtn = (TextView) findViewById(R.id.act_suborder_submit_btn);

		dao = new AddressDao(SubmitOrderActivity.this);
		beans = dao.findAll();
		if (beans == null || beans.size() == 0) {
			addr.setText("");
			phone.setText("");
		} else {
			bean = beans.get(0);
			addr.setText(bean.getAddr());
			phone.setText(bean.getPhone() + "," + bean.getUsername());
		}

		int feeInt = OrderDetail.shopInfo.getDiliverFee();
		foodPrice.setText("￥" + (OrderDetail.totalPrice ));
		feePrice.setText("￥" + feeInt);
		totalPrice.setText("￥" + (OrderDetail.totalPrice+feeInt));

		addrLl.setOnClickListener(this);
		timeLl.setOnClickListener(this);
		extraLl.setOnClickListener(this);
		submitBtn.setOnClickListener(this);

	}

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

	@Override
	public void onClick(View arg0) {
		Intent intent = null;
		String str = "";
		switch (arg0.getId()) {
		case R.id.act_suborder_addr_ll:
			intent = new Intent(SubmitOrderActivity.this, AddressActivity.class);
			startActivityForResult(intent, TYPE_FOR_ADDRESS);

			break;
		case R.id.act_suborder_extra_ll:
			intent = new Intent(SubmitOrderActivity.this,
					ChangeInfoActivity.class);
			intent.putExtra("operateId", TYPE_ADD_EXTRA);
			intent.putExtra("operateStr", extraInfo.getText().toString().trim());
			startActivityForResult(intent, TYPE_ADD_EXTRA);
			break;
		case R.id.act_suborder_time_ll:
			intent = new Intent(SubmitOrderActivity.this,
					ChangeInfoActivity.class);
			intent.putExtra("operateId", TYPE_CHANGE_TIME);
			intent.putExtra("operateStr", sendTime.getText().toString().trim());
			startActivityForResult(intent, TYPE_CHANGE_TIME);
			break;
		case R.id.act_suborder_submit_btn:
			submitOrderRequest();
			break;

		default:
			break;
		}
	}

	private void submitOrderRequest() {
		int uid = XwContext.getInstance().getUserInfo().getUid();
		int sid = OrderDetail.shopInfo.getShopId();
		
		int moneyInt = OrderDetail.totalPrice+OrderDetail.shopInfo.getDiliverFee();
		
		String money = String.valueOf(moneyInt);
		String payWay = String.valueOf(1);// 1,货到付款
		String addrStr = addr.getText().toString().trim() + ","
				+ phone.getText().toString().trim();
		if (TextUtils.isEmpty(addrStr)) {
			T.showShort(SubmitOrderActivity.this, "收货信息不能为空");
			return;
		}
		String sTime = sendTime.getText().toString().trim();
		String extra = extraInfo.getText().toString().trim();
		int diliverFee = OrderDetail.shopInfo.getDiliverFee();
		StringBuffer content = new StringBuffer();
		for (int i = 0; i < OrderDetail.dishList.size(); i++) {
			DishInfo di = OrderDetail.dishList.get(i);
			if (i != 0) {
				content.append(";");
			}
			content.append(di.getDishId() + "-" + di.getDishCount() + "-"
					+ di.getDishPrice() + "-" + di.getDishName());
		}
		try {
			loadingDialog = new LoadingDialog(SubmitOrderActivity.this, "正在提交订单...");
			loadingDialog.show();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("uid", String.valueOf(uid)));
			params.add(new BasicNameValuePair("sid", String.valueOf(sid)));
			params.add(new BasicNameValuePair("money", money));
			params.add(new BasicNameValuePair("payWay", payWay));
			params.add(new BasicNameValuePair("addr", URLEncoder.encode(
					addrStr, "utf-8")));
			params.add(new BasicNameValuePair("sendTime", URLEncoder.encode(
					sTime, "utf-8")));
			params.add(new BasicNameValuePair("extra", URLEncoder.encode(extra,
					"utf-8")));
			params.add(new BasicNameValuePair("diliverFee", String
					.valueOf(diliverFee)));
			params.add(new BasicNameValuePair("content", URLEncoder.encode(
					content.toString(), "utf-8")));
			
			XwContext.getInstance().getmRequestAPI().NoResultRequest(ServerFinals.submitOrder, params, new IApiCallback() {
				
				@Override
				public void onError(int errorCode) {
					loadingDialog.dismiss();
					T.showShort(SubmitOrderActivity.this, "订单提交失败");
				}
				
				@Override
				public void onComplete(Object object) {
					loadingDialog.dismiss();
					T.showShort(SubmitOrderActivity.this, "订单已提交");
					OrderDetail.clear();
				}
			});
			
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		String content = "";
		switch (requestCode) {
		case TYPE_ADD_EXTRA:
			if (data == null)
				return;
			content = data.getStringExtra("content");
			extraInfo.setText(content);
			break;
		case TYPE_CHANGE_TIME:
			if (data == null)
				return;
			content = data.getStringExtra("content");
			sendTime.setText(content);
			break;
		case TYPE_FOR_ADDRESS:
			if (data == null)
				return;
			int addId = data.getIntExtra("addId", -1);
			if (dao == null)
				dao = new AddressDao(SubmitOrderActivity.this);
			beans = dao.findAll();
			for (AddressBean b : beans) {
				if (b.getId() == addId) {
					addr.setText(b.getAddr());
					phone.setText(b.getPhone()+b.getUsername());
					break;
				}
			}
			break;
		default:
			break;
		}
	}

}
