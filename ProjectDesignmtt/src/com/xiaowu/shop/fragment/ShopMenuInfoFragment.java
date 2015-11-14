package com.xiaowu.shop.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.xiaowu.common.RequestAPI.IApiCallback;
import com.xiaowu.common.XwContext;
import com.xiaowu.finals.ServerFinals;
import com.xiaowu.projectdesign.R;
import com.xiaowu.protocol.XwShopMenu;
import com.xiaowu.protocol.XwShopMenu.ClassifyMenu.ShopMenu;
import com.xiaowu.shop.activity.ShopEditShopMenuActivity;
import com.xiaowu.shop.activity.ShopMainActivity;
import com.xiaowu.shop.adapter.ShopMenuExpandeAdapter;
import com.xiaowu.shop.interfaces.RefreshFragment;
import com.xiaowu.shop.interfaces.RefreshMenu;
import com.xiaowu.utils.T;

public class ShopMenuInfoFragment extends Fragment implements RefreshFragment,
		RefreshMenu {

	private ExpandableListView shopMenuListView;
	private ShopMenuExpandeAdapter mExpandeAdapter;

	private TextView addBtn;

	private View rootView;// 缓存Fragment view

	XwShopMenu shopMenu;
	Handler mHandler;

	int typ = -1;
	List<NameValuePair> params;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.shop_fra_menu, container,
					false);
		}
		mHandler = ((ShopMainActivity) getActivity()).getmHandler();
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
			return rootView;
		}

		shopMenuListView = (ExpandableListView) rootView
				.findViewById(R.id.shop_fra_menu_listview);
		addBtn = (TextView) rootView.findViewById(R.id.shop_fra_menu_addbtn);

		addBtn.setOnClickListener(addBtnClickLis);

		requestWebInfo();

		return rootView;

	}

	private OnClickListener addBtnClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(getActivity(),
					ShopEditShopMenuActivity.class);
			intent.putExtra("type", ShopEditShopMenuActivity.TYPE_ADD_DISH);
			Bundle b = new Bundle();
			b.putSerializable("shopMenu", shopMenu); 
			intent.putExtras(b);
			startActivity(intent);
		}
	};

	private void requestWebInfo() {
		int sid = XwContext.getInstance().getShopInfo().getId();
		XwContext.getInstance().getmRequestAPI()
				.getShopMenus(sid, new IApiCallback() {

					@Override
					public void onError(int errorCode) {
						T.showShort(getActivity(), "获取菜单失败");
					}

					@Override
					public void onComplete(Object object) {
						shopMenu = (XwShopMenu) object;

						mExpandeAdapter = new ShopMenuExpandeAdapter(
								getActivity(), shopMenu.getClassifyMenu(),
								mHandler);

						shopMenuListView.setAdapter(mExpandeAdapter);

					}
				});
	}

	private void requestShopMenuOperate(List<NameValuePair> params) {
		XwContext
				.getInstance()
				.getmRequestAPI()
				.NoResultRequest(ServerFinals.shop_menu_operate, params,
						new IApiCallback() {

							@Override
							public void onError(int errorCode) {

							}

							@Override
							public void onComplete(Object object) {
								T.showShort(getActivity(), "操作成功");
								requestWebInfo();
							}
						});
	}

	public XwShopMenu getShopMenu() {
		return shopMenu;
	}

	@Override
	public void refreshContext(Object obj) {
		// TODO Auto-generated method stub
		requestWebInfo();
	}

	@Override
	public void refreshMenu(Object obj) {
		// TODO Auto-generated method stub
		Bundle b = (Bundle) obj;
		int oid = b.getInt("oid");
		int mid = b.getInt("mid");
		int sid = XwContext.getInstance().getShopInfo().getId();
		if (oid == ShopMainActivity.MENU_DEL) {
			typ = 3;
			params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("sid", String.valueOf(sid)));
			params.add(new BasicNameValuePair("typ", String.valueOf(typ)));
			params.add(new BasicNameValuePair("mid", String.valueOf(mid)));
			requestShopMenuOperate(params);

		} else if (oid == ShopMainActivity.MENU_EDIT) {
			typ = 2;
			int gPosition = b.getInt("gPosition");
			int cPosition = b.getInt("cPosition");
			ShopMenu dish = shopMenu.getClassifyMenu().get(gPosition)
					.getMenus().get(cPosition);
			Bundle bundle = new Bundle();
			bundle.putSerializable("dish", dish);
			bundle.putInt("typ", ShopEditShopMenuActivity.TYPE_UPDATE_DISH);
			Intent intent = new Intent(getActivity(),
					ShopEditShopMenuActivity.class);
			intent.putExtras(bundle);
			startActivity(intent);
		}
	}

}
