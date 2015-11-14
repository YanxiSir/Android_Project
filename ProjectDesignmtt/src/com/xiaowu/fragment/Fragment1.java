package com.xiaowu.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xiaowu.adapter.ShopListAdapter;
import com.xiaowu.common.RequestAPI.IApiCallback;
import com.xiaowu.common.XwContext;
import com.xiaowu.projectdesign.R;
import com.xiaowu.protocol.XwShopList;
import com.xiaowu.protocol.XwShopList.ShopInfos;
import com.xiaowu.utils.T;
import com.xiaowu.widget.LoadingDialog;
import com.xiaowu.widget.SelectPopup;

public class Fragment1 extends Fragment {
	private final static int LOADTYPE_INIT = 0;
	private final static int LOADTYPE_REFRESH = 1;
	private final static int LOADTYPE_MORE = 2;

	private PullToRefreshListView listView;
	private List<ShopInfos> shopList;
	private ShopListAdapter adapter;

	// private TextView order1, order2, order3;
	private LinearLayout order1, order2, order3,selectConditionLl;

	private ImageView order1_arrowhead, order2_arrowhead, order3_arrowhead;

	private List<String> list;

	private SelectPopup selectPopup;
	private LoadingDialog loadingDialog;

	int sid = 0;
	int count = 8;

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SelectPopup.SELECT_ID:
				Bundle b = (Bundle) msg.obj;
				int id = b.getInt("oid");
				T.showShort(getActivity(), "当前点击的第" + id + "个");
				break;

			default:
				break;
			}
		};
	};

	private View rootView;// 缓存Fragment view

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment1, container, false);
		}
		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null) {
			parent.removeView(rootView);
			return rootView;
		}

		initUI(rootView);

		requestWebInfo(LOADTYPE_INIT);
		return rootView;
	}

	private void initUI(View v) {
		listView = (PullToRefreshListView) v.findViewById(R.id.menu_listview);

		order1 = (LinearLayout) v.findViewById(R.id.fra1_order_1);
		order2 = (LinearLayout) v.findViewById(R.id.fra1_order_2);
		order3 = (LinearLayout) v.findViewById(R.id.fra1_order_3);
		
		selectConditionLl = (LinearLayout) v.findViewById(R.id.fra1_select_condition_ll);
		selectConditionLl.setVisibility(View.GONE);

		order1_arrowhead = (ImageView) v.findViewById(R.id.order1_arrowhead);
		order2_arrowhead = (ImageView) v.findViewById(R.id.order2_arrowhead);
		order3_arrowhead = (ImageView) v.findViewById(R.id.order3_arrowhead);

		order1.setOnClickListener(order1ClickLis);
		order2.setOnClickListener(order2ClickLis);
		order3.setOnClickListener(order3ClickLis);

	}

	private OnClickListener order1ClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (selectPopup != null && selectPopup.isShow()) {
				selectPopup.dismiss();
				return;
			}
			list = new ArrayList<String>();
			list.add("mtt");
			list.add("ma da pang");
			list.add("nimei");
			selectPopup = new SelectPopup(getActivity(), mHandler, list, order1);

		}
	};
	private OnClickListener order2ClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (selectPopup != null && selectPopup.isShow()) {
				selectPopup.dismiss();
				return;
			}
			list = new ArrayList<String>();
			list.add("mtt");
			list.add("ma da pang");
			list.add("nimei");
			selectPopup = new SelectPopup(getActivity(), mHandler, list, order2);

		}
	};
	private OnClickListener order3ClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			if (selectPopup != null && selectPopup.isShow()) {
				selectPopup.dismiss();
				return;
			}
			list = new ArrayList<String>();
			list.add("mtt");
			list.add("ma da pang");
			list.add("nimei");
			selectPopup = new SelectPopup(getActivity(), mHandler, list, order3);

		}
	};

	private OnRefreshListener2<ListView> refreshLis = new PullToRefreshBase.OnRefreshListener2<ListView>() {

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
			requestWebInfo(LOADTYPE_REFRESH);
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
			requestWebInfo(LOADTYPE_MORE);
		}
	};

	private void requestWebInfo(final int loadType) {

		if (loadType == LOADTYPE_INIT || loadType == LOADTYPE_REFRESH) {
			sid = 0;
			count = 8;
		} else if (loadType == LOADTYPE_MORE) {
			sid += count;
		}
		if (loadType == LOADTYPE_INIT) {
			if (loadingDialog == null)
				loadingDialog = new LoadingDialog(getActivity(), "正在加载...");
			loadingDialog.show();
		}
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("sid", String.valueOf(sid)));
		params.add(new BasicNameValuePair("count", String.valueOf(count)));
		XwContext.getInstance().getmRequestAPI()
				.shopList(params, new IApiCallback() {

					@Override
					public void onError(int errorCode) {
						if (loadingDialog != null)
							loadingDialog.dismiss();
						T.showShort(getActivity(), "请求失败...");
					}

					@Override
					public void onComplete(Object object) {
						if (loadingDialog != null)
							loadingDialog.dismiss();

						XwShopList xwShop = (XwShopList) object;
						if (xwShop == null) {
							T.showShort(getActivity(), "没有内容");
							return;
						}
						if (loadType == LOADTYPE_MORE
								&& xwShop.getShops().size() == 0) {
							T.showShort(getActivity(), "没有数据了");
						}
						if (loadType == LOADTYPE_INIT
								|| loadType == LOADTYPE_REFRESH) {
							if (shopList == null)
								shopList = new ArrayList<ShopInfos>();
							else
								shopList.clear();
						}
						shopList.addAll(xwShop.getShops());

						if (loadType == LOADTYPE_INIT) {
							adapter = new ShopListAdapter(shopList,
									getActivity());
							listView.setAdapter(adapter);

							listView.setOnRefreshListener(refreshLis);

							/*listView.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
								

								}
							});*/
						} else if (loadType == LOADTYPE_REFRESH) {
							adapter.notifyDataSetChanged();
							listView.onRefreshComplete();
						} else if (loadType == LOADTYPE_MORE) {
							adapter.notifyDataSetChanged();
							listView.onRefreshComplete();
						}

					}
				});
	}

}
