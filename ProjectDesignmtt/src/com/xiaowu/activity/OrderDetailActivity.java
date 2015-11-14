package com.xiaowu.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaowu.adapter.OrderSubmitAdapter;
import com.xiaowu.common.RequestAPI.IApiCallback;
import com.xiaowu.common.XwContext;
import com.xiaowu.finals.ServerFinals;
import com.xiaowu.projectdesign.R;
import com.xiaowu.protocol.XwOrderList.OrderInfo;
import com.xiaowu.utils.OrderDetail.DishInfo;
import com.xiaowu.utils.T;
import com.xiaowu.utils.Tool;

public class OrderDetailActivity extends Activity {
	public static final int TYPE_USER = 0;
	public static final int TYPE_SHOP = 1;

	public static final int TYPE_REVIEW_ORDER = 200;

	/*
	 * titlebar
	 */
	private RelativeLayout titleLeftRel, titleRightRel_1, titleRightRel_2;
	private LinearLayout titleLeftLl;
	private TextView titleLeftText;
	private ImageView titleRightImage_1, titleRightImage_2;

	private OrderInfo orderInfo;
	private int type = -1;

	private TextView order_detail_noEvaluate, order_detail_explaination,
			order_detail_reviewTv, order_detail_total, order_detail_deliverfee,
			order_detail_shopname;
	private ImageView order_detail_submit, order_detail_shopGetOrder,
			order_detail_delivering, order_detail_received,
			order_detail_finish_image;
	private ListView order_detail_menulist;

	private TextView order_detail_order_time, order_detail_pay_way,
			order_detail_order_tel, order_detail_order_addr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_order_detail);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.titlebar_ltext_rtwo_btn);
		Bundle b = getIntent().getExtras();
		orderInfo = (OrderInfo) b.getSerializable("orderDetail");
		type = b.getInt("type");

		InitTitle();
		InitView();
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
		titleRightRel_1.setVisibility(View.VISIBLE);
		titleRightImage_1.setImageDrawable(getResources().getDrawable(
				R.drawable.iconfont_fenxiang));
		titleLeftLl.setVisibility(View.VISIBLE);
		titleRightRel_2.setVisibility(View.VISIBLE);
		titleRightImage_2.setImageDrawable(getResources().getDrawable(
				R.drawable.telphone));
		titleLeftText.setText(orderInfo.getsName());

		titleLeftRel.setOnClickListener(titleLeftRelClickLis);
	}

	private void InitView() {
		order_detail_noEvaluate = (TextView) findViewById(R.id.order_detail_noEvaluate);
		order_detail_explaination = (TextView) findViewById(R.id.order_detail_explaination);
		order_detail_reviewTv = (TextView) findViewById(R.id.order_detail_reviewTv);
		order_detail_submit = (ImageView) findViewById(R.id.order_detail_submit);
		order_detail_shopGetOrder = (ImageView) findViewById(R.id.order_detail_shopGetOrder);
		order_detail_delivering = (ImageView) findViewById(R.id.order_detail_delivering);
		order_detail_received = (ImageView) findViewById(R.id.order_detail_received);
		order_detail_menulist = (ListView) findViewById(R.id.order_detail_menulist);
		order_detail_deliverfee = (TextView) findViewById(R.id.order_detail_deliverfee);
		order_detail_total = (TextView) findViewById(R.id.order_detail_total);
		order_detail_shopname = (TextView) findViewById(R.id.order_detail_shopname);

		order_detail_order_time = (TextView) findViewById(R.id.order_detail_order_time);
		order_detail_pay_way = (TextView) findViewById(R.id.order_detail_pay_way);
		order_detail_order_tel = (TextView) findViewById(R.id.order_detail_order_tel);
		order_detail_order_addr = (TextView) findViewById(R.id.order_detail_order_addr);

		order_detail_finish_image = (ImageView) findViewById(R.id.order_detail_finish_image);

		if (orderInfo.getIsReview() == 1) {
			order_detail_noEvaluate.setText("已评价");
			order_detail_reviewTv.setText("已评价");
			order_detail_explaination.setVisibility(View.GONE);
		} else if (orderInfo.getIsReview() == 0) {
			order_detail_noEvaluate.setText("待评价");
			order_detail_reviewTv.setText("评价");
			order_detail_explaination.setVisibility(View.VISIBLE);
		}
		showReviewBtn(orderInfo.getOperateStatus());
		showOrderProcess(orderInfo.getOperateStatus());
		setOrderDetailBasicInfo();
		order_detail_shopname.setText(orderInfo.getsName());
		order_detail_total.setText("￥" + orderInfo.getMoney());
		order_detail_deliverfee.setText("￥" + orderInfo.getDiliverFee());

		List<DishInfo> dishList = getOrderedDishList(orderInfo.getBuyContent());
		OrderSubmitAdapter adapter = new OrderSubmitAdapter(
				OrderDetailActivity.this, dishList);
		order_detail_menulist.setAdapter(adapter);
		Tool.setListViewHeightBasedOnChildren(order_detail_menulist); 

		order_detail_reviewTv.setOnClickListener(valueBtnClickLis);

	}

	private void showRemindContent(boolean finished, String title,
			String content) {
		if (finished) {
			order_detail_finish_image.setImageDrawable(getResources()
					.getDrawable(R.drawable.bigcheck));
		} else {
			order_detail_finish_image.setImageDrawable(getResources()
					.getDrawable(R.drawable.no_finish));
		}
		order_detail_noEvaluate.setText(title);
		order_detail_explaination.setText(content);
	}

	private void showReviewBtn(int status) {
		if (type == TYPE_USER) {
			if (status == 3) {
				order_detail_reviewTv.setText("收货");
				order_detail_reviewTv.setClickable(true);
				showRemindContent(false, "未完成", "饭收到了吗？");
			} else if (status == 4) {
				if (orderInfo.getIsReview() == 1) {
					order_detail_reviewTv.setText("已评价");
					order_detail_reviewTv.setClickable(false);
					showRemindContent(true, "已评价", "下次再光顾");
				} else {
					order_detail_reviewTv.setText("评价");
					order_detail_reviewTv.setClickable(true);
					showRemindContent(true, "未评价", "美食味道怎么样，快来评价下吧");
				}
			} else {
				order_detail_reviewTv.setText("收货");
				order_detail_reviewTv.setClickable(true);
				showRemindContent(false, "未完成", "美食还在路上，耐心等待吧");
			}
		} else if (type == TYPE_SHOP) {
			if (status == 1) {
				order_detail_reviewTv.setText("接单");
				showRemindContent(false, "新订单", "生意来了，赶快接单吧");
			} else if (status == 2) {
				order_detail_reviewTv.setText("配送");
				showRemindContent(false, "新订单,未完成", "订单准备好了，准备配送咯");
			} else if (status == 3) {
				order_detail_reviewTv.setText("正在配送中...");
				order_detail_reviewTv.setClickable(false);
				showRemindContent(false, "正在配送ing。。。", "生意兴隆！！！");
			} else {
				order_detail_reviewTv.setText("订单已完成");
				order_detail_reviewTv.setClickable(false);
				showRemindContent(true, "已完成", "又一单完成咯，感觉萌萌哒...");
			}
		}
	}

	private OnClickListener valueBtnClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			int status = orderInfo.getOperateStatus();
			if (type == TYPE_USER) {
				if (status == 3) {
					shopLoadWebRequestOrderProcess();
				} else if (status == 4) {
					if (orderInfo.getIsReview() == 0) {
						Intent i = new Intent(OrderDetailActivity.this,
								ChangeInfoActivity.class);
						i.putExtra("operateId", TYPE_REVIEW_ORDER);
						i.putExtra("oid", orderInfo.getId());
						startActivity(i);
					} else {
						T.showShort(OrderDetailActivity.this, "已评价");
					}
				} else {
					T.showShort(OrderDetailActivity.this, "外卖还没送出...请等候");
				}
			} else if (type == TYPE_SHOP) {
				if (status == 1 || status == 2) {
					shopLoadWebRequestOrderProcess();
				}
			}
		}
	};

	private void setOrderDetailBasicInfo() {
		order_detail_order_time.setText(orderInfo.getBuyDate());
		if (orderInfo.getPayWay() == 1) {
			order_detail_pay_way.setText("货到付款");
		} else {
			order_detail_pay_way.setText("其他方式");
		}
		String[] orderDetailStr = orderInfo.getAddrStr().split(",");
		order_detail_order_tel.setText(orderDetailStr[1]);
		order_detail_order_addr.setText(orderDetailStr[0]);
	}

	private void setOrderProcessUnfinished() {
		order_detail_submit.setImageDrawable(getResources().getDrawable(
				R.drawable.orderwhitepic));
		order_detail_shopGetOrder.setImageDrawable(getResources().getDrawable(
				R.drawable.ordersubmitwhitepic));
		order_detail_delivering.setImageDrawable(getResources().getDrawable(
				R.drawable.deliveringwhitepic));
		order_detail_received.setImageDrawable(getResources().getDrawable(
				R.drawable.receivedwhitepic));
	}

	private void showOrderProcess(int status) {
		setOrderProcessUnfinished();
		switch (status) {
		case 4:
			order_detail_received.setImageDrawable(getResources().getDrawable(
					R.drawable.receivedpic));
		case 3:
			order_detail_delivering.setImageDrawable(getResources()
					.getDrawable(R.drawable.deliveringpic));
		case 2:
			order_detail_shopGetOrder.setImageDrawable(getResources()
					.getDrawable(R.drawable.ordersubmitpic));
		case 1:
			order_detail_submit.setImageDrawable(getResources().getDrawable(
					R.drawable.orderpic));
			break;

		default:
			break;
		}
	}

	private List<DishInfo> getOrderedDishList(String content) {
		List<DishInfo> dishList = new ArrayList<DishInfo>();

		String[] dishs = content.split(";");
		for (int i = 0; i < dishs.length; i++) {
			DishInfo di = new DishInfo();
			String[] di_detail = dishs[i].split("-");
			di.setDishId(Integer.parseInt(di_detail[0]));
			di.setDishCount(Integer.parseInt(di_detail[1]));
			di.setDishPrice(Integer.parseInt(di_detail[2]));
			di.setDishName(di_detail[3]);
			dishList.add(di);
		}
		return dishList;
	}

	private OnClickListener titleLeftRelClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			onBackPressed();
		}
	};

	private void shopLoadWebRequestOrderProcess() {
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("sid", String.valueOf(orderInfo
				.getSid())));
		params.add(new BasicNameValuePair("oid", String.valueOf(orderInfo
				.getId())));
		XwContext
				.getInstance()
				.getmRequestAPI()
				.NoResultRequest(ServerFinals.shop_order_process, params,
						new IApiCallback() {

							@Override
							public void onError(int errorCode) {

							}

							@Override
							public void onComplete(Object object) {
								T.showShort(OrderDetailActivity.this, "状态更新成功");
								orderInfo.setOperateStatus(orderInfo
										.getOperateStatus() + 1);
								showReviewBtn(orderInfo.getOperateStatus());
								showOrderProcess(orderInfo.getOperateStatus());
							}
						});
	}
}
