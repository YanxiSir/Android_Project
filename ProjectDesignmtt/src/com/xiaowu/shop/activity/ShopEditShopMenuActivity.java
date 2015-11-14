package com.xiaowu.shop.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaowu.common.RequestAPI.IApiCallback;
import com.xiaowu.common.XwContext;
import com.xiaowu.finals.ServerFinals;
import com.xiaowu.projectdesign.R;
import com.xiaowu.protocol.XwShopMenu;
import com.xiaowu.protocol.XwShopMenu.ClassifyMenu;
import com.xiaowu.protocol.XwShopMenu.ClassifyMenu.ShopMenu;
import com.xiaowu.utils.T;
import com.xiaowu.utils.Tool;
import com.xiaowu.widget.LoadingDialog;
import com.xiaowu.widget.SelectDialog;

public class ShopEditShopMenuActivity extends Activity {
	public static final int TYPE_ADD_DISH = 1;
	public static final int TYPE_UPDATE_DISH = 2;

	/*
	 * titlebar
	 */
	private RelativeLayout titleLeftRel, titleRightRel_1, titleRightRel_2;
	private LinearLayout titleLeftLl;
	private TextView titleLeftText;
	private ImageView titleRightImage_1, titleRightImage_2;

	private EditText nameText, priceText, amountText;
	private TextView submitBtn, dishType;
	private LinearLayout dishTypeLl;

	private int type = -1;

	private int typ = -1;// 菜单操作typ
	private int cid = -1;
	ArrayList<String> classifyList;

	ShopMenu dish;
	int sid;

	List<NameValuePair> params;
	XwShopMenu shopMenu;

	private LoadingDialog loadingDialog;
	private SelectDialog selectDialog;

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SelectDialog.DIALOG_SELECT_OPERATE:
				Bundle b = (Bundle) msg.obj;
				int selectid = b.getInt("selectid");
				dishType.setText(classifyList.get(selectid));
				cid = shopMenu.getClassifyMenu().get(selectid).getId();
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.shop_edit_shop_menu);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_ltext_rtwo_btn);

		Bundle b = getIntent().getExtras();

		type = b.getInt("typ", TYPE_ADD_DISH);
		if (type == TYPE_ADD_DISH) {
			shopMenu = (XwShopMenu) b.getSerializable("shopMenu");
			if (shopMenu == null)
				return;
			classifyList = new ArrayList<String>();
			for (ClassifyMenu cm : shopMenu.getClassifyMenu()) {
				classifyList.add(cm.getClassifyName());
			}
		} else if (type == TYPE_UPDATE_DISH) {
			dish = (ShopMenu) b.getSerializable("dish");
		}
		sid = XwContext.getInstance().getShopInfo().getId();

		InitTitle();
		initUI();
	}

	private void initUI() {
		nameText = (EditText) findViewById(R.id.add_dish_username);
		priceText = (EditText) findViewById(R.id.add_dish_price);
		amountText = (EditText) findViewById(R.id.add_dish_amount);
		submitBtn = (TextView) findViewById(R.id.add_dish_submitbtn);
		dishTypeLl = (LinearLayout) findViewById(R.id.add_dish_type_ll);
		dishType = (TextView) findViewById(R.id.add_dish_type);

		if (type == TYPE_ADD_DISH) {
			submitBtn.setText("确认添加");
			dishTypeLl.setVisibility(View.VISIBLE);
			dishTypeLl.setOnClickListener(dishTypeClickLis);

		} else if (type == TYPE_UPDATE_DISH) {
			nameText.setText(dish.getName());
			nameText.setSelection(nameText.getText().toString().length());
			priceText.setText(dish.getPrice() + "");
			amountText.setText(dish.getCount() + "");
			submitBtn.setText("确认修改");
			dishTypeLl.setVisibility(View.GONE);
		}

		submitBtn.setOnClickListener(titleRightRelClickLis);

	}

	private OnClickListener dishTypeClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			selectDialog = new SelectDialog(ShopEditShopMenuActivity.this,
					mHandler, classifyList);
		}
	};

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
		if (type == TYPE_ADD_DISH)
			titleLeftText.setText("添加新的小菜");
		else if (type == TYPE_UPDATE_DISH)
			titleLeftText.setText("更新小菜信息");
		titleRightRel_2.setOnClickListener(titleRightRelClickLis);

	}

	private void requestShopMenuOperate(List<NameValuePair> params) {
		loadingDialog = new LoadingDialog(ShopEditShopMenuActivity.this,
				"正在操作...");
		loadingDialog.show();
		XwContext
				.getInstance()
				.getmRequestAPI()
				.NoResultRequest(ServerFinals.shop_menu_operate, params,
						new IApiCallback() {

							@Override
							public void onError(int errorCode) {
								loadingDialog.dismiss();
							}

							@Override
							public void onComplete(Object object) {
								loadingDialog.dismiss();
								T.showShort(ShopEditShopMenuActivity.this,
										"操作成功");
								finish();

							}
						});
	}

	private OnClickListener titleRightRelClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			String name = nameText.getText().toString().trim();
			String price = priceText.getText().toString().trim();
			String amount = amountText.getText().toString().trim();
			if (TextUtils.isEmpty(name)) {
				T.showShort(ShopEditShopMenuActivity.this, "名字不能为空");
				return;
			}
			if (dish == null)
				dish = new ShopMenu();
			dish.setName(name);
			if (TextUtils.isEmpty(price)) {
				dish.setPrice(0);
			} else {
				dish.setPrice(Integer.parseInt(price));
			}
			if (TextUtils.isEmpty(amount)) {
				dish.setPrice(0);
			} else {
				dish.setPrice(Integer.parseInt(amount));
			}
			if (type == TYPE_UPDATE_DISH) {
				typ = 2;
				int mid = dish.getId();
				params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("typ", String.valueOf(typ)));
				params.add(new BasicNameValuePair("sid", String.valueOf(sid)));
				params.add(new BasicNameValuePair("mid", String.valueOf(mid)));
				params.add(new BasicNameValuePair("name", Tool
						.encodeUTF_8(name)));
				params.add(new BasicNameValuePair("price", price));
				params.add(new BasicNameValuePair("count", amount));
				requestShopMenuOperate(params);

			} else if (type == TYPE_ADD_DISH) {
				typ = 1;
				if (cid < 0) {
					T.showShort(ShopEditShopMenuActivity.this, "选择菜单分类");
					return;
				}
				params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("typ", String.valueOf(typ)));
				params.add(new BasicNameValuePair("sid", String.valueOf(sid)));
				params.add(new BasicNameValuePair("name", Tool
						.encodeUTF_8(name)));
				params.add(new BasicNameValuePair("price", price));
				params.add(new BasicNameValuePair("count", amount));
				params.add(new BasicNameValuePair("cid", String.valueOf(cid)));
				requestShopMenuOperate(params);
			}
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
