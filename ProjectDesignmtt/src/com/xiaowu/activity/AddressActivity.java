package com.xiaowu.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaowu.adapter.AddressAdapter;
import com.xiaowu.db.dao.AddressBean;
import com.xiaowu.db.dao.AddressDao;
import com.xiaowu.projectdesign.R;
import com.xiaowu.widget.SelectDialog;

public class AddressActivity extends Activity {
	public static final int TYPE_ADD_ADDRESS = 1;

	public static final int LOAD_INIT = 0;
	public static final int LOAD_AFTER_ADD = 1;

	/*
	 * titlebar
	 */
	private RelativeLayout titleLeftRel, titleRightRel_1, titleRightRel_2;
	private LinearLayout titleLeftLl;
	private TextView titleLeftText;
	private ImageView titleRightImage_1, titleRightImage_2;

	private ListView listview;

	List<AddressBean> beans;
	AddressAdapter adapter;

	int count = 0;

	SelectDialog selectDialog;
	int addId;

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			Bundle b;
			switch (msg.what) {
			case AddressAdapter.OPERATE_ADDRESS_SHORT_CLICK:
				b = (Bundle) msg.obj;
				addId = b.getInt("id", -1);
				if (addId < 0) {
					setResult(RESULT_OK, null);
				} else {
					Intent i = new Intent();
					i.putExtra("addId", addId);
					setResult(RESULT_OK, i);

				}
				finish();
				break;
			case AddressAdapter.OPERATE_ADDRESS_LONG_CLICK:
				b = (Bundle) msg.obj;
				addId = b.getInt("id", -1);
				if (addId < 0)
					return;
				List<String> list = new ArrayList<String>();
				list.add("删除");
				selectDialog = new SelectDialog(AddressActivity.this, mHandler,
						list);
				selectDialog.setTitleText("选择操作");
				break;
			case SelectDialog.DIALOG_SELECT_OPERATE:
				b = (Bundle) msg.obj;
				int selectid = b.getInt("selectid");
				if (selectid == 0) {
					AddressDao dao = new AddressDao(AddressActivity.this);
					dao.del(String.valueOf(addId));

					for (AddressBean bean : beans) {
						if (bean.getId() == addId) {
							beans.remove(bean);
							break;
						}
					}
					adapter.notifyDataSetChanged();

				}
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
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_address);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_ltext_rtwo_btn);

		InitTitle();

		listview = (ListView) findViewById(R.id.address_listview);
		showAllAddress(LOAD_INIT);
	}

	private void showAllAddress(int loadtype) {
		// if (loadtype == LOAD_INIT) {
		AddressDao dao = new AddressDao(AddressActivity.this);
		beans = dao.findAll();
		count = beans.size();
		adapter = new AddressAdapter(AddressActivity.this, beans, mHandler);
		listview.setAdapter(adapter);
		// } else if (loadtype == LOAD_AFTER_ADD) {
		// AddressDao dao = new AddressDao(AddressActivity.this);
		// beans = dao.findAll();
		// adapter.notifyDataSetChanged();
		// }
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
				R.drawable.add));
		titleLeftRel.setOnClickListener(titleLeftRelClickLis);
		titleLeftText.setText("选择收货地址");

		titleRightRel_2.setOnClickListener(addAddressClickLis);
	}

	private OnClickListener addAddressClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(AddressActivity.this,
					AddReceiverAddress.class);
			intent.putExtra("count", count);
			startActivity(intent); 
//			startActivityForResult(intent, TYPE_ADD_ADDRESS);
		}
	};
	private OnClickListener titleLeftRelClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			onBackPressed();
		}
	};

	/*protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK)
			return;
		switch (requestCode) {

		case TYPE_ADD_ADDRESS:
			showAllAddress(LOAD_AFTER_ADD);
			break;

		default:
			break;
		}
	};*/
}
