package com.xiaowu.activity;

import java.util.ArrayList;

import com.xiaowu.adapter.ReceiverAddressListAdapter;
import com.xiaowu.projectdesign.R;
import com.xiaowu.request.domain.ReceiverInfo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ReceiverAddress extends Activity {
	private ListView listview;
	private ArrayList<ReceiverInfo> receivers=new ArrayList<ReceiverInfo>();
	private RelativeLayout addReceiverRel;
	private RelativeLayout change_titlebarRel;
	
	private TextView titlebar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.receiver_address);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.change_titlebar);

		titlebar=(TextView) findViewById(R.id.change_text);
		titlebar.setText("管理收货人地址");		
		InitUI();
		
     //	测试代码	
		for(int i=0;i<3;i++)
		{
			ReceiverInfo receiver=new ReceiverInfo();
			receiver.setTelnumber("123456543");
			receiver.setAddress("dongbeidaxue");
			receivers.add(receiver);
			
		}
     //	测试代码
		
		listview=(ListView) findViewById(R.id.Receiver_Infolist);
		listview.setAdapter(new ReceiverAddressListAdapter(receivers,ReceiverAddress.this));	
		addReceiverRel.setOnClickListener(addReceiverRelClilis);
		change_titlebarRel.setOnClickListener(change_titlebarRelClickLis);
	}
	
	private void InitUI(){
		addReceiverRel=(RelativeLayout) findViewById(R.id.addReceiverRel);
		change_titlebarRel=(RelativeLayout) findViewById(R.id.change_titlebar);
	}
	
	private OnClickListener change_titlebarRelClickLis=new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			onBackPressed();
		}
	};
	
    private OnClickListener addReceiverRelClilis=new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			Intent intent=new Intent(ReceiverAddress.this,AddReceiverAddress.class);
			startActivity(intent);
			
		}
	};
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.receiver_address, menu);
		return true;
	}

}
