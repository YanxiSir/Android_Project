package com.xiaowu.fragment;

import java.util.List;

import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import com.nhaarman.listviewanimations.appearance.StickyListHeadersAdapterDecorator;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;
import com.nhaarman.listviewanimations.util.StickyListHeadersListViewWrapper;
import com.xiaowu.activity.ConfirmFoodPage;
import com.xiaowu.activity.ShopPage;
import com.xiaowu.adapter.ShopMenuAdapter;
import com.xiaowu.common.RequestAPI.IApiCallback;
import com.xiaowu.common.XwContext;
import com.xiaowu.projectdesign.R;
import com.xiaowu.protocol.XwShopList.ShopInfos;
import com.xiaowu.protocol.XwShopMenu;
import com.xiaowu.protocol.XwShopMenu.ClassifyMenu;
import com.xiaowu.utils.OrderDetail;
import com.xiaowu.utils.T;
import com.xiaowu.utils.OrderDetail.DishInfo;
import com.xiaowu.widget.LoadingDialog;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class ClassifyMenuFragment extends Fragment {

	public static final int CHANGE_ORDER_INFO = 0;

	private StickyListHeadersListView listView;

	private View rootView;
	private TextView submitOrder;
	private TextView totalPrice;
	private ShopInfos shopInfo;
	private int sid;
	private int uid;

	private List<ClassifyMenu> classifyMenuList;
	ShopMenuAdapter adapter;

	private LoadingDialog loadingDialog;
	int count;
	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CHANGE_ORDER_INFO:
				count = 0;
				for (DishInfo di : OrderDetail.dishList) {
					count += di.getDishCount();
				}
				totalPrice.setText(count + "份 ￥" + OrderDetail.totalPrice);
				break;

			default:
				break;
			}
		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		sid = ((ShopPage) getActivity()).getSid();
		uid = XwContext.getInstance().getUserInfo().getUid();
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.classify_menu, container,
					false);
		}
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
			return rootView;
		}

		listView = (StickyListHeadersListView) rootView
				.findViewById(R.id.stickheader_listview);
		listView.setFitsSystemWindows(true);

		requestWebInfo();
		shopInfo = ((ShopPage) getActivity()).getShopInfo();
		submitOrder = (TextView) rootView
				.findViewById(R.id.classify_menu_submit);
		totalPrice = (TextView) rootView
				.findViewById(R.id.classify_menu_total_price);

		count = 0;
		for (DishInfo di : OrderDetail.dishList) {
			count += di.getDishCount();
		}
		totalPrice.setText(count + "份 ￥" + OrderDetail.totalPrice);

		submitOrder.setOnClickListener(settlementCliclis);
		return rootView;
	}

	private OnClickListener settlementCliclis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent = new Intent(getActivity(), ConfirmFoodPage.class);
			startActivity(intent);
		}
	};

	private void requestWebInfo() {
		if (loadingDialog == null)
			loadingDialog = new LoadingDialog(getActivity(), "正在加载...");
		loadingDialog.show();
		XwContext.getInstance().getmRequestAPI()
				.getShopMenus(sid, new IApiCallback() {

					@Override
					public void onError(int errorCode) {
						loadingDialog.dismiss();
						T.showShort(getActivity(), "获取菜单失败");
					}

					@Override
					public void onComplete(Object object) {
						loadingDialog.dismiss();
						XwShopMenu shopMenu = (XwShopMenu) object;

						adapter = new ShopMenuAdapter(getActivity(), shopMenu
								.getClassifyMenu(), mHandler);
						AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(
								adapter);
						StickyListHeadersAdapterDecorator stickyListHeadersAdapterDecorator = new StickyListHeadersAdapterDecorator(
								animationAdapter);
						stickyListHeadersAdapterDecorator
								.setListViewWrapper(new StickyListHeadersListViewWrapper(
										listView));

						assert animationAdapter.getViewAnimator() != null;
						animationAdapter.getViewAnimator()
								.setInitialDelayMillis(500);

						assert stickyListHeadersAdapterDecorator
								.getViewAnimator() != null;
						stickyListHeadersAdapterDecorator.getViewAnimator()
								.setInitialDelayMillis(500);

						listView.setAdapter(stickyListHeadersAdapterDecorator);

					}
				});
	}

}
