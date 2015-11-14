package com.xiaowu.db.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AddressDBOpenHelper extends SQLiteOpenHelper {

	public AddressDBOpenHelper(Context context) {
		super(context, "addressdb.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table address (id int primary key,addr varchar(200),username varchar(20),phone varchar(20))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

	}
}
