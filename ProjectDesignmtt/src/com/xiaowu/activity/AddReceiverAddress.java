package com.xiaowu.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaowu.db.dao.AddressBean;
import com.xiaowu.db.dao.AddressDao;
import com.xiaowu.projectdesign.R;
import com.xiaowu.utils.T;

public class AddReceiverAddress extends Activity {

	/*
	 * titlebar
	 */
	private RelativeLayout titleLeftRel, titleRightRel_1, titleRightRel_2;
	private LinearLayout titleLeftLl;
	private TextView titleLeftText;
	private ImageView titleRightImage_1, titleRightImage_2;
	
	private TextView usernameText,phoneText,addrText,submitBtn;
	
	int count = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.add_receiver_address);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_ltext_rtwo_btn);
		count = getIntent().getIntExtra("count", -1);
		InitTitle();
		initUI();
	}
	private void initUI(){
		usernameText = (TextView) findViewById(R.id.add_addr_username);
		phoneText = (TextView) findViewById(R.id.add_addr_phone);
		addrText = (TextView) findViewById(R.id.add_addr_addr);
		submitBtn = (TextView) findViewById(R.id.add_addr_submitbtn);
		
		submitBtn.setOnClickListener(titleRightRelClickLis); 
		
	}
	private void InitTitle() {

		titleLeftRel = (RelativeLayout) findViewById(R.id.title_tbb_left_rel);
		titleLeftLl = (LinearLayout) findViewById(R.id.title_tbb_left_ll);
		titleLeftText = (TextView) findViewById(R.id.title_tbb_left_text);
		titleRightRel_1 = (RelativeLayout) findViewById(R.id.title_tbb_right_rel_one);
		titleRightRel_2 = (RelativeLayout) findViewById(R.id.title_tbb_right_rel_two);
		titleRightImage_1 = (ImageView) findViewById(R.id.title_tbb_right_image_one);
		titleRightImage_2 = (ImageView) findViewById(R.id.title_tbb_right_image_two);

		titleLeftRel.setVisibility(View.VISIBLE);
		titleRightRel_1.setVisibility(View.GONE);

		titleLeftLl.setVisibility(View.VISIBLE);
		titleRightRel_2.setVisibility(View.VISIBLE);

		titleRightImage_2.setImageDrawable(getResources().getDrawable(
				R.drawable.sure));
		titleLeftRel.setOnClickListener(titleLeftRelClickLis);
		titleLeftText.setText("编辑新的收货地址");
		titleRightRel_2.setOnClickListener(titleRightRelClickLis); 

	}
	private OnClickListener titleRightRelClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			String username = usernameText.getText().toString().trim();
			String phone = phoneText.getText().toString().trim();
			String addr = addrText.getText().toString().trim();
			if(TextUtils.isEmpty(username) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(addr)){
				T.showShort(AddReceiverAddress.this, "信息填写完整再提交");
				return ;
			}
			AddressBean bean = new AddressBean();
			bean.setUsername(username);
			bean.setPhone(phone);
			bean.setAddr(addr);
			
			if(count > 0){
				bean.setId(count+1);
			}
			
			AddressDao dao = new AddressDao(AddReceiverAddress.this);
			boolean flag = dao.add(bean);
			if(!flag){
				T.showShort(AddReceiverAddress.this, "添加失败");
			}
			Intent intent = new Intent(AddReceiverAddress.this,AddressActivity.class);
			startActivity(intent); 
			finish();
//			if(flag){
//				setResult(RESULT_OK); 
//				finish();
//			}else{
//				T.showShort(AddReceiverAddress.this, "添加失败");
//				setResult(RESULT_CANCELED);
//				finish();
//			}
		}
	};
	private OnClickListener titleLeftRelClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			onBackPressed();
		}
	};
	@Override
	public void onBackPressed() {
		finish();
	};

}
