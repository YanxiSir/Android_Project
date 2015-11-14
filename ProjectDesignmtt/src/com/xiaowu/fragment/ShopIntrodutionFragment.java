package com.xiaowu.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaowu.activity.ShopPage;
import com.xiaowu.projectdesign.R;
import com.xiaowu.protocol.XwShopList.ShopInfos;
import com.xiaowu.utils.Tool;
import com.xiaowu.widget.RoundImageView;

public class ShopIntrodutionFragment extends Fragment{
	private TextView IntroShopName;
	private TextView IntroCollect;
	private RoundImageView IntroShopImage;
	private TextView IntroReviewGrade,IntroArriveTime,IntroDiliverPrice,IntroDiliverFee;
	private TextView Intro_shop_time,Intro_shop_Introduction,Intro_shop_address,Intro_shop_phone;
	private ShopInfos shopInfos;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.shop_introduction, container,false);
		
		shopInfos = ((ShopPage)getActivity()).getShopInfo();
		
		InitView(view);
		return view;
	}
	private void InitView(View v)
	{
		IntroShopName=(TextView) v.findViewById(R.id.Intro_shop_name);
		IntroCollect=(TextView) v.findViewById(R.id.Intro_collect);
		IntroReviewGrade = (TextView) v.findViewById(R.id.Intro_review_grade);
		IntroArriveTime = (TextView) v.findViewById(R.id.Intro_arrive_time);
		IntroDiliverPrice = (TextView) v.findViewById(R.id.Intro_diliver_price);
		IntroDiliverFee = (TextView) v.findViewById(R.id.Intro_diliver_fee);
		
		Intro_shop_time=(TextView) v.findViewById(R.id.Intro_shop_time);
		Intro_shop_Introduction=(TextView) v.findViewById(R.id.Intro_shop_Introduction);
		Intro_shop_address=(TextView) v.findViewById(R.id.Intro_shop_address);
		Intro_shop_phone=(TextView) v.findViewById(R.id.Intro_shop_phone);

		
		
		IntroShopImage = (RoundImageView) v.findViewById(R.id.Intro_shop_pic);
		
		IntroShopName.setMovementMethod(ScrollingMovementMethod.getInstance());
		
		if(shopInfos!=null){
			Tool.imageLoader(getActivity(), IntroShopImage, shopInfos.getShopPicUrl(), null);
			IntroShopName.setText(shopInfos.getShopName());
			IntroArriveTime.setText(shopInfos.getDiliverTime()+"");
			IntroReviewGrade.setText(shopInfos.getReviewGrade()+"");
			IntroDiliverPrice.setText(shopInfos.getDiliverPrice()+"");
			IntroDiliverFee.setText(shopInfos.getDiliverFee()+"");
			Intro_shop_time.setText(shopInfos.getStartTime()+"---"+shopInfos.getEndTime());
			Intro_shop_Introduction.setText(shopInfos.getIntro());
			Intro_shop_address.setText(shopInfos.getAddr());
			Intro_shop_phone.setText(shopInfos.getPhone());
			
		}
		Intro_shop_phone.setOnClickListener(phoneClickLis); 
	}
	private OnClickListener phoneClickLis = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			String phone = shopInfos.getPhone();
			
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_DIAL);
			intent.setData(Uri.parse("tel:"+phone));
			startActivity(intent); 
		}
	};
}

