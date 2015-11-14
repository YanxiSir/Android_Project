package com.smilexi.sx.db.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NoticeDBOpenHelper extends SQLiteOpenHelper{
	
	public NoticeDBOpenHelper(Context context) {
		super(context, "smilexi.db", null, 1);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table notice (id int primary key,content varchar(50),did int,aid int,qid int,uid int,createTime varchar(30))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
}
