package com.xiaowu.service;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class BaiduMapLocationService extends Service {
	// 定位相关
	private LocationClient mLocationClient;
	private MyLocationClientListener mLocationListener;
	StringBuffer sb;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		InitLocation();
		new Thread(new Runnable() {
		
			@Override
			public void run() {
				// TODO Auto-generated method stub
				
				// mBaiduMap.setMyLocationEnabled(true);
				Intent intent = new Intent();			
				intent.putExtra("LocationAddr", sb.toString());
				intent.setAction("com.xiaowu.LocationAddr");
				sendBroadcast(intent);
			}
		}).start();

	}

	private void InitLocation() {
		// TODO Auto-generated method stub
		mLocationClient = new LocationClient(this);
		mLocationListener = new MyLocationClientListener();
		mLocationClient.registerLocationListener(mLocationListener);

		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll");
		option.setIsNeedAddress(true);
		option.setOpenGps(true);
		option.setScanSpan(1000);
		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	class MyLocationClientListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub

			sb=new StringBuffer(256);
		    sb.append(location.getProvince());
		    sb.append(location.getCity());
		    sb.append(location.getStreet());
		    System.out.println(sb.toString());
		
		}

	}

}
