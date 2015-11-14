package com.xiaowu.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.xiaowu.activity.OrderDetailActivity;
import com.xiaowu.adapter.OrderListAdapter;
import com.xiaowu.common.RequestAPI.IApiCallback;
import com.xiaowu.common.XwContext;
import com.xiaowu.projectdesign.R;
import com.xiaowu.protocol.XwOrderList;
import com.xiaowu.protocol.XwOrderList.OrderInfo;
import com.xiaowu.utils.T;

public class Fragment2 extends Fragment {

	public static final int LOADTYPE_INIT = 0;
	public static final int LOADTYPE_REFRESH = 1;
	public static final int LOADTYPE_MORE = 2;

	private PullToRefreshListView orderListView;

	XwOrderList orderList;
	List<OrderInfo> orders;
	OrderListAdapter adapter;
	int typ = 1;

	private View rootView;// ����Fragment view

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if (rootView == null) {
			rootView = inflater.inflate(R.layout.fragment2, container, false);
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
		orderListView = (PullToRefreshListView) v
				.findViewById(R.id.order_listview);
	}

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

	int sid = 0;
	int count = 8;
	
	private void requestWebInfo(final int loadType) {
		
		if (loadType == LOADTYPE_INIT || loadType == LOADTYPE_REFRESH) {
			sid = 0;
			count = 8;
		} else if (loadType == LOADTYPE_MORE) {
			sid += count;
		}
		
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("id", String.valueOf(XwContext
				.getInstance().getUserInfo().getUid())));
		params.add(new BasicNameValuePair("typ", String.valueOf(1)));
		params.add(new BasicNameValuePair("startId", String.valueOf(sid)));
		params.add(new BasicNameValuePair("count", String.valueOf(count)));

		XwContext.getInstance().getmRequestAPI()
				.getOrderList(params, new IApiCallback() {

					@Override
					public void onError(int errorCode) {
						T.showShort(getActivity(), "����ʧ��");
					}

					@Override
					public void onComplete(Object object) {

						orderList = (XwOrderList) object;

						if (loadType == LOADTYPE_INIT
								|| loadType == LOADTYPE_REFRESH) {
							if (orders == null) {
								orders = new ArrayList<XwOrderList.OrderInfo>();
							} else {
								orders.clear();
							}
						}
						orders.addAll(orderList.getOrders());

						if (loadType == LOADTYPE_INIT) {
							adapter = new OrderListAdapter(orders,
									getActivity(),typ);
							orderListView.setAdapter(adapter);
							orderListView.setOnRefreshListener(refreshLis); 
							orderListView.setOnItemClickListener(orderItemClickLis); 
						} else if (loadType == LOADTYPE_REFRESH) {
							adapter.notifyDataSetChanged();
							orderListView.onRefreshComplete();
						} else if (loadType == LOADTYPE_MORE) {
							adapter.notifyDataSetChanged();
							orderListView.onRefreshComplete();
						}
					}
				});
	}
	private OnItemClickListener orderItemClickLis = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			Intent i = new Intent(getActivity(),OrderDetailActivity.class);
			Bundle b=new Bundle();
			b.putSerializable("orderDetail", orders.get(position));
			b.putInt("type", OrderDetailActivity.TYPE_USER);
			i.putExtras(b);
			startActivity(i); 
		}
	};
}
