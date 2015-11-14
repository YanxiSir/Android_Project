package com.xiaowu.shop.activity;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaowu.activity.ChangeInfoActivity;
import com.xiaowu.common.RequestAPI.IApiCallback;
import com.xiaowu.common.XwContext;
import com.xiaowu.finals.ServerFinals;
import com.xiaowu.projectdesign.R;
import com.xiaowu.protocol.XwShopMenu;
import com.xiaowu.protocol.XwShopMenu.ClassifyMenu;
import com.xiaowu.shop.adapter.ShopMenuClassifyAdapter;
import com.xiaowu.utils.Tool;
import com.xiaowu.widget.ConfirmDialog;
import com.xiaowu.widget.LoadingDialog;

public class ShopMenuEditActivity extends Activity {

	public static final int TYPE_ADD_CLASSIFY = 100;
	public static final int TYPE_UPDATE_CLASSIFY = 101;
	public static final int TYPE_DEL_CLASSIFY = 102;

	/*
	 * titlebar
	 */
	private RelativeLayout titleLeftRel, titleRightRel;
	private ImageView titleLeftImage, titleRightImage;
	private TextView titleLeftText, titleRightText;

	private LinearLayout menuEditAddLl;
	private ListView listview;
	ShopMenuClassifyAdapter adapter;

	ConfirmDialog confirmDialog;

	XwShopMenu shopMenu;
	ArrayList<String> classifyList;

	private LoadingDialog loadingDialog;

	List<NameValuePair> params;

	private int typ = -1;// // typ:1,增加菜 2,修改菜 3,删除菜 4,增加分类 5,修改分类名称6,删除分类

	int pid = -1;

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Bundle b;
			switch (msg.what) {
			case TYPE_UPDATE_CLASSIFY:
				b = (Bundle) msg.obj;
				pid = b.getInt("pid");
				Intent intent = new Intent(ShopMenuEditActivity.this,
						ChangeInfoActivity.class);
				intent.putExtra("operateId", TYPE_UPDATE_CLASSIFY);
				intent.putExtra("operateStr", classifyList.get(pid).toString());
				startActivityForResult(intent, TYPE_UPDATE_CLASSIFY);
				break;
			case TYPE_DEL_CLASSIFY:
				typ = 6;
				b = (Bundle) msg.obj;
				pid = b.getInt("pid");
				params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("sid", String
						.valueOf(XwContext.getInstance().getShopInfo().getId())));
				params.add(new BasicNameValuePair("typ", String.valueOf(typ)));
				params.add(new BasicNameValuePair("cid", String
						.valueOf(shopMenu.getClassifyMenu().get(pid).getId())));
				menuClassifyEditRequest(params, null);
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.shop_act_editmenu);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_lbtn_text_rbtn_text);
		Bundle b = getIntent().getExtras();
		shopMenu = (XwShopMenu) b.getSerializable("shopMenu");
		if (shopMenu == null)
			return;
		classifyList = new ArrayList<String>();
		for (ClassifyMenu cm : shopMenu.getClassifyMenu()) {
			classifyList.add(cm.getClassifyName());
		}

		initTitle();
		initUI();
	}

	private void menuClassifyEditRequest(List<NameValuePair> params,
			final String name) {
		loadingDialog = new LoadingDialog(ShopMenuEditActivity.this, "正在操作...");
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
								if (typ == 4) {
									classifyList.add(name);
								} else if (typ == 5) {
									shopMenu.getClassifyMenu().get(pid)
											.setClassifyName(name);
									classifyList.clear();
									for (ClassifyMenu cm : shopMenu
											.getClassifyMenu()) {
										classifyList.add(cm.getClassifyName());
									}
								} else if (typ == 6) {
									classifyList.remove(pid);
								}
								adapter.notifyDataSetChanged();
							}
						});
	}

	private void initUI() {
		menuEditAddLl = (LinearLayout) findViewById(R.id.shop_menu_edit_add_ll);
		listview = (ListView) findViewById(R.id.shop_menu_edit_listview);

		adapter = new ShopMenuClassifyAdapter(ShopMenuEditActivity.this,
				classifyList, mHandler);
		listview.setAdapter(adapter);

		menuEditAddLl.setOnClickListener(menuEditLlClick);

	}

	private OnClickListener menuEditLlClick = new OnClickListener() {

		@Override
		public void onClick(View arg0) {

			Intent intent = new Intent(ShopMenuEditActivity.this,
					ChangeInfoActivity.class);
			intent.putExtra("operateId", TYPE_ADD_CLASSIFY);
			startActivityForResult(intent, TYPE_ADD_CLASSIFY);

		}
	};

	private void initTitle() {
		titleLeftRel = (RelativeLayout) findViewById(R.id.titlebar_lrbt_left_rel);
		titleLeftImage = (ImageView) findViewById(R.id.titlebar_lrbt_left_image);
		titleLeftText = (TextView) findViewById(R.id.titlebar_lrbt_left_text);
		titleRightRel = (RelativeLayout) findViewById(R.id.titlebar_lrbt_right_rel);
		titleRightImage = (ImageView) findViewById(R.id.titlebar_lrbt_right_image);
		titleRightText = (TextView) findViewById(R.id.titlebar_lrbt_right_text);

		titleLeftRel.setVisibility(View.VISIBLE);
		titleLeftImage.setImageDrawable(getResources().getDrawable(
				R.drawable.back));
		titleRightImage.setVisibility(View.GONE);
		titleRightText.setVisibility(View.VISIBLE);
		titleRightRel.setClickable(true);
		titleRightText.setText("保存");

		titleLeftText.setText("菜单分组管理");
		
		titleLeftRel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				onBackPressed();
			}
		});
	}

	@Override
	public void onBackPressed() {
		finish();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		String content = "";
		if (resultCode != RESULT_OK)
			return;
		switch (requestCode) {
		case TYPE_ADD_CLASSIFY:
			typ = 4;
			content = data.getStringExtra("content");
			params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("sid", String.valueOf(XwContext
					.getInstance().getShopInfo().getId())));
			params.add(new BasicNameValuePair("name", Tool.encodeUTF_8(content)));
			params.add(new BasicNameValuePair("typ", String.valueOf(typ)));

			menuClassifyEditRequest(params, content);
			break;
		case TYPE_UPDATE_CLASSIFY:
			typ = 5;
			content = data.getStringExtra("content");
			params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("sid", String.valueOf(XwContext
					.getInstance().getShopInfo().getId())));
			params.add(new BasicNameValuePair("name", Tool.encodeUTF_8(content)));
			params.add(new BasicNameValuePair("typ", String.valueOf(typ)));
			params.add(new BasicNameValuePair("cid", String.valueOf(shopMenu
					.getClassifyMenu().get(pid).getId())));

			menuClassifyEditRequest(params, content);
			break;

		default:
			break;
		}
	}
}
