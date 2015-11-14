package com.xiaowu.activity;

import com.xiaowu.projectdesign.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class SearchPage extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_page);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_page, menu);
		return true;
	}

}
