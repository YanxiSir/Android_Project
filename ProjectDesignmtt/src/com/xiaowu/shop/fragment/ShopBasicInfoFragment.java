package com.xiaowu.shop.fragment;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.xiaowu.common.RequestAPI.IApiCallback;
import com.xiaowu.common.XwContext;
import com.xiaowu.projectdesign.R;
import com.xiaowu.shop.activity.ShopMainActivity;
import com.xiaowu.shop.interfaces.RefreshFragment;
import com.xiaowu.shop.protocol.XwShopInfo;
import com.xiaowu.utils.T;
import com.xiaowu.utils.Tool;
import com.xiaowu.widget.LoadingDialog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ShopBasicInfoFragment extends Fragment implements RefreshFragment{

	private ImageView shopImage;
	private EditText nameEdit, phoneEdit, addrEdit, introEdit, sTimeEdit,
			eTimeEdit, priceEdit, feeEdit;
	private TextView submitBtn;

	private XwShopInfo info;
	private LoadingDialog loadingDialog;

	private View rootView;// 缓存Fragment view

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.shop_fra_basic, container,
					false);
		}
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
			return rootView;
		}
		info = XwContext.getInstance().getShopInfo();

		initUI(rootView);
		initData();
		return rootView;
	}

	public void initData() {
		if (nameEdit != null)
			nameEdit.setText(info.getName());
		phoneEdit.setText(info.getPhone());
		addrEdit.setText(info.getAddr());
		introEdit.setText(info.getIntro());
		sTimeEdit.setText(info.getStartTime());
		eTimeEdit.setText(info.getEndTime());
		priceEdit.setText(info.getDiliverPrice() + "");
		feeEdit.setText(info.getDiliverFee() + "");

		Tool.imageLoader(getActivity(), shopImage, info.getPortrait(), null);
	}

	private void initUI(View v) {
		shopImage = (ImageView) v.findViewById(R.id.basic_image);
		nameEdit = (EditText) v.findViewById(R.id.basic_name);
		phoneEdit = (EditText) v.findViewById(R.id.basic_phone);
		addrEdit = (EditText) v.findViewById(R.id.basic_addr);
		introEdit = (EditText) v.findViewById(R.id.basic_intro);
		sTimeEdit = (EditText) v.findViewById(R.id.basic_start_time);
		eTimeEdit = (EditText) v.findViewById(R.id.basic_end_time);
		priceEdit = (EditText) v.findViewById(R.id.basic_diliver_price);
		feeEdit = (EditText) v.findViewById(R.id.basic_diliver_fee);
		submitBtn = (TextView) v.findViewById(R.id.basic_submit_btn);
		submitBtn.setVisibility(View.GONE);
	}

	public void setEditEnable(boolean flag) {
		nameEdit.setEnabled(flag);
		phoneEdit.setEnabled(flag);
		addrEdit.setEnabled(flag);
		introEdit.setEnabled(flag);
		sTimeEdit.setEnabled(flag);
		eTimeEdit.setEnabled(flag);
		priceEdit.setEnabled(flag);
		feeEdit.setEnabled(flag);
		if (flag)
			submitBtn.setVisibility(View.VISIBLE);
		else
			submitBtn.setVisibility(View.GONE);
		submitBtn.setOnClickListener(submitBtnClickLis);
	}

	private OnClickListener submitBtnClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			updateBasicInfoRequest();
		}
	};

	private void updateShopInfo() {
		String name = nameEdit.getText().toString().trim();
		String phone = phoneEdit.getText().toString().trim();
		String addr = addrEdit.getText().toString().trim();
		String sTime = sTimeEdit.getText().toString().trim();
		String eTime = eTimeEdit.getText().toString().trim();
		String price = priceEdit.getText().toString().trim();
		String fee = feeEdit.getText().toString().trim();
		String intro = introEdit.getText().toString().trim();

		info.setName(name);
		info.setPhone(phone);
		info.setAddr(addr);
		info.setStartTime(sTime);
		info.setEndTime(eTime);
		info.setIntro(intro);
		if (TextUtils.isEmpty(price))
			info.setDiliverPrice(0);
		else
			info.setDiliverPrice(Integer.parseInt(price));
		if (TextUtils.isEmpty(fee))
			info.setDiliverPrice(0);
		else
			info.setDiliverPrice(Integer.parseInt(fee));
		XwContext.getInstance().setShopInfo(info);
	}

	private void updateBasicInfoRequest() {
		int id = info.getId();
		String name = nameEdit.getText().toString().trim();
		String phone = phoneEdit.getText().toString().trim();
		String addr = addrEdit.getText().toString().trim();
		String sTime = sTimeEdit.getText().toString().trim();
		String eTime = eTimeEdit.getText().toString().trim();
		String price = priceEdit.getText().toString().trim();
		String fee = feeEdit.getText().toString().trim();
		String intro = introEdit.getText().toString().trim();
		if (TextUtils.isEmpty(name)) {
			T.showShort(getActivity(), "店名不能为空");
			return;
		}
		if (TextUtils.isEmpty(phone)) {
			T.showShort(getActivity(), "联系方式不能为空");
			return;
		}
		if (TextUtils.isEmpty(addr)) {
			T.showShort(getActivity(), "地址不能为空");
			return;
		}
		if (TextUtils.isEmpty(sTime) || TextUtils.isEmpty(eTime)) {
			T.showShort(getActivity(), "营业时间不能为空");
			return;
		}
		if (TextUtils.isEmpty(price)) {
			price = "0";
		}
		if (TextUtils.isEmpty(fee)) {
			fee = "0";
		}

		if (loadingDialog == null) {
			loadingDialog = new LoadingDialog(getActivity(), "正在修改...");
			loadingDialog.show();
		}
		try {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("id", String.valueOf(id)));
			params.add(new BasicNameValuePair("name", URLEncoder.encode(name,
					"utf-8")));
			params.add(new BasicNameValuePair("phone", phone));
			params.add(new BasicNameValuePair("addr", URLEncoder.encode(addr,
					"utf-8")));
			params.add(new BasicNameValuePair("sTime", sTime));
			params.add(new BasicNameValuePair("eTime", eTime));
			params.add(new BasicNameValuePair("diliverPrice", price));
			params.add(new BasicNameValuePair("diliverFee", fee));
			params.add(new BasicNameValuePair("intro", URLEncoder.encode(intro,
					"utf-8")));

			XwContext.getInstance().getmRequestAPI()
					.shopUpdateBasicInfo(params, new IApiCallback() {

						@Override
						public void onError(int errorCode) {
							loadingDialog.dismiss();
							T.showShort(getActivity(), "修改失败");
						}

						@Override
						public void onComplete(Object object) {
							loadingDialog.dismiss();
							T.showShort(getActivity(), "修改成功");
							updateShopInfo();
							setEditEnable(false);
							((ShopMainActivity) getActivity()).finishEdit();
						}
					});

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void refreshContext(Object obj) {
		// TODO Auto-generated method stub
		
	}
}
