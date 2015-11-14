package com.xiaowu.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AddressDao {

	AddressDBOpenHelper helper;

	public AddressDao(Context context) {
		helper = new AddressDBOpenHelper(context);
	}

	public boolean add(AddressBean bean) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("id", bean.getId());
		values.put("addr", bean.getAddr());
		values.put("username", bean.getUsername());
		values.put("phone", bean.getPhone());

		long row_id = db.insert("address", null, values);
		db.close();
		if (row_id > 0)
			return true;
		return false;
	}

	public boolean del(String id) {

		SQLiteDatabase db = helper.getWritableDatabase();
		int result = db.delete("address", "id=?", new String[] { id });
		db.close();
		if (result > 0)
			return true;

		return false;
	}

	public List<AddressBean> findAll() {

		SQLiteDatabase db = helper.getReadableDatabase();
		List<AddressBean> beans = new ArrayList<AddressBean>();

		Cursor cursor = db.query("address", new String[] { "id", "addr",
				"username", "phone" }, null, null, null, null, null);
		while (cursor.moveToNext()) {
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			String addr = cursor.getString(cursor.getColumnIndex("addr"));
			String username = cursor.getString(cursor
					.getColumnIndex("username"));
			String phone = cursor.getString(cursor.getColumnIndex("phone"));

			AddressBean bean = new AddressBean(id, addr, username, phone);
			beans.add(bean);
			bean = null;
		}
		db.close();
		return beans;
	}
}
