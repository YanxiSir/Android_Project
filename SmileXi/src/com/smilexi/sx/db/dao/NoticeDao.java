package com.smilexi.sx.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NoticeDao {
	NoticeDBOpenHelper helper;

	public NoticeDao(Context context) {
		helper = new NoticeDBOpenHelper(context);
	}

	/*
	 * private int id; private String content; private int did; private int aid;
	 * private int qid; private int uid; private String createTime;
	 */
	public boolean add(NoticeBean bean) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("id", bean.getId());
		values.put("content", bean.getContent());
		values.put("did", bean.getDid());
		values.put("aid", bean.getAid());
		values.put("qid", bean.getQid());
		values.put("uid", bean.getUid());
		values.put("createTime", bean.getCreateTime());

		long row_id = db.insert("notice", null, values);
		db.close();
		if (row_id > 0)
			return true;
		return false;
	}

	public List<NoticeBean> findAll() {

		SQLiteDatabase db = helper.getReadableDatabase();
		List<NoticeBean> beans = new ArrayList<NoticeBean>();

		// Cursor cursor = db.query("notice", new String[] { "id", "name",
		// "telphone", "address" }, null, null, null, null, null);

		Cursor cursor = db.query("notice", new String[] { "id", "content",
				"did", "aid", "qid", "uid", "createTime" }, null, null, null,
				null, null);
		while (cursor.moveToNext()) {
			int id = cursor.getInt(cursor.getColumnIndex("id"));
			String content = cursor.getString(cursor.getColumnIndex("content"));
			int did = cursor.getInt(cursor.getColumnIndex("did"));
			int aid = cursor.getInt(cursor.getColumnIndex("aid"));
			int qid = cursor.getInt(cursor.getColumnIndex("qid"));
			int uid = cursor.getInt(cursor.getColumnIndex("uid"));
			String createTime = cursor.getString(cursor
					.getColumnIndex("createTime"));

			NoticeBean bean = new NoticeBean(id, content, did, aid, qid, uid,
					createTime);
			beans.add(bean);
			bean = null;
		}
		db.close();
		return beans;
	}
}
