package com.xiaowu.baidumap;


import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.xiaowu.projectdesign.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class BaiduMapActivity  extends Activity{
	private MapView baidu_mapView;
	private BaiduMap mBaiduMap;
    private boolean isFirstIn=true;
    
    //定位相关
    private LocationClient mLocationClient;
    private MyLocationClientListener mLocationListener;
    private BDLocation mLocation;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 在使用SDK各组件之前初始化context信息，传入ApplicationContext
		// 注意该方法要再setContentView方法之前实现
		SDKInitializer.initialize(getApplicationContext());
		setContentView(R.layout.baidu_map);
		InitView();
		InitLocation();
	}
   
	
	
	private void InitLocation() {
		// TODO Auto-generated method stub
		mLocationClient=new LocationClient(this);
		mLocationListener=new MyLocationClientListener();
		mLocationClient.registerLocationListener(mLocationListener);
		
		LocationClientOption option=new LocationClientOption();
		option.setCoorType("bd09ll");
		option.setIsNeedAddress(true);
		option.setOpenGps(true);
		option.setScanSpan(1000);
		mLocationClient.setLocOption(option);
	}


	private void InitView() {
		// TODO Auto-generated method stub
		baidu_mapView = (MapView) findViewById(R.id.baidu_mapView);
		mBaiduMap=baidu_mapView.getMap();
		MapStatusUpdate msu=MapStatusUpdateFactory.zoomTo(15.0f);
		mBaiduMap.setMapStatus(msu);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		baidu_mapView.onDestroy(); 
	}
    @Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    	mBaiduMap.setMyLocationEnabled(true);
    	if(!mLocationClient.isStarted())
    	{
    		mLocationClient.start();
    	}
    }
    @Override
    protected void onStop() {
    	// TODO Auto-generated method stub
    	super.onStop();
    	mBaiduMap.setMyLocationEnabled(false);
    	mLocationClient.stop();
    }
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		 //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理  
		baidu_mapView.onPause();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		baidu_mapView.onResume();
	}
	
	class MyLocationClientListener implements BDLocationListener{
		

		@Override
		public void onReceiveLocation(BDLocation location) {
			// TODO Auto-generated method stub
			
			setmLocation(location);
			MyLocationData data=new MyLocationData.Builder()//
			                     .accuracy(location.getRadius())//
			                     .latitude(location.getLatitude())//
			                     .longitude(location.getLongitude()).build();
			
			mBaiduMap.setMyLocationData(data);
			
			if(isFirstIn)
			{
				LatLng latLn=new LatLng(location.getLatitude(),location.getLongitude());
				MapStatusUpdate msu=MapStatusUpdateFactory.newLatLng(latLn);
				mBaiduMap.animateMapStatus(msu);
				isFirstIn=false;
				System.out.println(location.getAddrStr());
			}
		}
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.baidu_map, menu);
		return true;
	}



	public BDLocation getmLocation() {
		return mLocation;
	}



	public void setmLocation(BDLocation mLocation) {
		this.mLocation = mLocation;
	}


}
