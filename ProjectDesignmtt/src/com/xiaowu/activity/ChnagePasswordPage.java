package com.xiaowu.activity;

import com.xiaowu.projectdesign.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ChnagePasswordPage extends Activity {
	private RelativeLayout change_titlebarRel;
	private TextView titlebar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.chnage_password_page);	
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.change_titlebar);
	    Init();
		change_titlebarRel.setOnClickListener(change_titlebarRelClickLis);

	}
    private void Init()
    {
    	titlebar=(TextView) findViewById(R.id.change_text);
    	titlebar.setText("ÐÞ¸ÄÃÜÂë");
    }
    
	private OnClickListener change_titlebarRelClickLis=new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			onBackPressed();
		}
	};
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chnage_password_page, menu);
		return true;
	}

}
