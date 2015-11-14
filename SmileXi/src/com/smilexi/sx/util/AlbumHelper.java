package com.smilexi.sx.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;





import com.smilexi.sx.entity.ImageBucket;
import com.smilexi.sx.entity.ImageItem;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.MediaStore.Audio.AlbumColumns;
import android.provider.MediaStore.Audio.Albums;
import android.provider.MediaStore.Images.ImageColumns;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.provider.MediaStore.MediaColumns;

public class AlbumHelper {
	Context context;
	ContentResolver cr;

	HashMap<String, String> thumbnailList = new HashMap<String, String>();

	List<HashMap<String, String>> albumList = new ArrayList<HashMap<String, String>>();
	HashMap<String, ImageBucket> bucketList = new HashMap<String, ImageBucket>();

	private static AlbumHelper instance;

	private AlbumHelper() {
	}

	public static AlbumHelper getHelper() {
		if (instance == null) {
			instance = new AlbumHelper();
		}
		return instance;
	}

	public void init(Context context) {
		if (this.context == null) {
			this.context = context;
			cr = context.getContentResolver();
		}
	}

	private void getThumbnail() {
		String[] projection = { BaseColumns._ID, Thumbnails.IMAGE_ID,
				Thumbnails.DATA };
		Cursor cursor = null;
		cursor = cr.query(Thumbnails.EXTERNAL_CONTENT_URI, projection, null,
				null, null);
		getThumbnailColumnData(cursor);
		if (cursor != null) {
			cursor.close();
		}

	}

	// private void getImages(){
	// String[] projection = { BaseColumns._ID, Thumbnails.IMAGE_ID,
	// Thumbnails.DATA };
	// Cursor cursor = cr.query(Images.ImageColumns,);
	//
	// }

	private void getThumbnailColumnData(Cursor cur) {
		if (cur.moveToFirst()) {
			int _id;
			int image_id;
			String image_path;
			int _idColumn = cur.getColumnIndex(BaseColumns._ID);
			int image_idColumn = cur.getColumnIndex(Thumbnails.IMAGE_ID);
			int dataColumn = cur.getColumnIndex(Thumbnails.DATA);
			// int lastModifyColumn = cur.getColumnIndex(Thumbnails);

			do {
				// Get the field values
				_id = cur.getInt(_idColumn);
				image_id = cur.getInt(image_idColumn);
				image_path = cur.getString(dataColumn);

				// Do something with the values.
				// Log.i(TAG, _id + " image_id:" + image_id + " path:"
				// + image_path + "---");
				// HashMap<String, String> hash = new HashMap<String, String>();
				// hash.put("image_id", image_id + "");
				// hash.put("path", image_path);
				// thumbnailList.add(hash);
				thumbnailList.put("" + image_id, image_path);
			} while (cur.moveToNext());
		}
	}

	void getAlbum() {
		String[] projection = { BaseColumns._ID, AlbumColumns.ALBUM,
				AlbumColumns.ALBUM_ART, AlbumColumns.ALBUM_KEY,
				AlbumColumns.ARTIST, AlbumColumns.NUMBER_OF_SONGS };
		Cursor cursor = cr.query(Albums.EXTERNAL_CONTENT_URI, projection, null,
				null, null);
		getAlbumColumnData(cursor);
		if (cursor != null)
			cursor.close();
	}

	private void getAlbumColumnData(Cursor cur) {
		if (cur.moveToFirst()) {
			int _id;
			String album;
			String albumArt;
			String albumKey;
			String artist;
			int numOfSongs;

			int _idColumn = cur.getColumnIndex(BaseColumns._ID);
			int albumColumn = cur.getColumnIndex(AlbumColumns.ALBUM);
			int albumArtColumn = cur.getColumnIndex(AlbumColumns.ALBUM_ART);
			int albumKeyColumn = cur.getColumnIndex(AlbumColumns.ALBUM_KEY);
			int artistColumn = cur.getColumnIndex(AlbumColumns.ARTIST);
			int numOfSongsColumn = cur
					.getColumnIndex(AlbumColumns.NUMBER_OF_SONGS);

			do {
				// Get the field values
				_id = cur.getInt(_idColumn);
				album = cur.getString(albumColumn);
				albumArt = cur.getString(albumArtColumn);
				albumKey = cur.getString(albumKeyColumn);
				artist = cur.getString(artistColumn);
				numOfSongs = cur.getInt(numOfSongsColumn);

				// Do something with the values.
				L.i(_id + " album:" + album + " albumArt:" + albumArt
						+ "albumKey: " + albumKey + " artist: " + artist
						+ " numOfSongs: " + numOfSongs + "---");
				HashMap<String, String> hash = new HashMap<String, String>();
				hash.put("_id", _id + "");
				hash.put("album", album);
				hash.put("albumArt", albumArt);
				hash.put("albumKey", albumKey);
				hash.put("artist", artist);
				hash.put("numOfSongs", numOfSongs + "");
				albumList.add(hash);

			} while (cur.moveToNext());

		}
	}

	boolean hasBuildImagesBucketList = false;

	void buildImagesBucketList() {
		long startTime = System.currentTimeMillis();

		getThumbnail();

		String columns[] = new String[] { BaseColumns._ID,
				ImageColumns.BUCKET_ID, ImageColumns.PICASA_ID,
				MediaColumns.DATA, MediaColumns.DISPLAY_NAME,
				MediaColumns.TITLE, MediaColumns.SIZE,
				ImageColumns.BUCKET_DISPLAY_NAME };

		Cursor cur = cr.query(Media.EXTERNAL_CONTENT_URI, columns, null, null,
				"_id DESC");
		if (cur.moveToFirst()) {
			int photoIDIndex = cur.getColumnIndexOrThrow(BaseColumns._ID);
			int photoPathIndex = cur.getColumnIndexOrThrow(MediaColumns.DATA);
			int photoNameIndex = cur
					.getColumnIndexOrThrow(MediaColumns.DISPLAY_NAME);
			int photoTitleIndex = cur.getColumnIndexOrThrow(MediaColumns.TITLE);
			int photoSizeIndex = cur.getColumnIndexOrThrow(MediaColumns.SIZE);
			int bucketDisplayNameIndex = cur
					.getColumnIndexOrThrow(ImageColumns.BUCKET_DISPLAY_NAME);
			int bucketIdIndex = cur
					.getColumnIndexOrThrow(ImageColumns.BUCKET_ID);
			int picasaIdIndex = cur
					.getColumnIndexOrThrow(ImageColumns.PICASA_ID);

			int totalNum = cur.getCount();

			do {
				String _id = cur.getString(photoIDIndex);
				String name = cur.getString(photoNameIndex);
				String path = cur.getString(photoPathIndex);
				String title = cur.getString(photoTitleIndex);
				String size = cur.getString(photoSizeIndex);
				String bucketName = cur.getString(bucketDisplayNameIndex);
				String bucketId = cur.getString(bucketIdIndex);
				String picasaId = cur.getString(picasaIdIndex);

				L.i(_id + ", bucketId: " + bucketId + ", picasaId: " + picasaId
						+ " name:" + name + " path:" + path + " title: "
						+ title + " size: " + size + " bucket: " + bucketName
						+ "---");

				ImageBucket bucket = bucketList.get(bucketId);
				if (bucket == null) {
					bucket = new ImageBucket();
					bucketList.put(bucketId, bucket);
					bucket.imageList = new ArrayList<ImageItem>();
					bucket.bucketName = bucketName;
				}
				bucket.count++;
				ImageItem imageItem = new ImageItem();
				imageItem.imageId = _id;
				imageItem.imagePath = path;
				imageItem.thumbnailPath = thumbnailList.get(_id);
				bucket.imageList.add(imageItem);

			} while (cur.moveToNext());
		}
		if (cur != null)
			cur.close();

		Iterator<Entry<String, ImageBucket>> itr = bucketList.entrySet()
				.iterator();
		while (itr.hasNext()) {
			Map.Entry<String, ImageBucket> entry = itr.next();
			ImageBucket bucket = entry.getValue();
			L.d(entry.getKey() + ", " + bucket.bucketName + ", " + bucket.count
					+ " ---------- ");
			for (int i = 0; i < bucket.imageList.size(); ++i) {
				ImageItem image = bucket.imageList.get(i);
				L.d("----- " + image.imageId + ", " + image.imagePath + ", "
						+ image.thumbnailPath);
			}
		}
		hasBuildImagesBucketList = true;
		long endTime = System.currentTimeMillis();
		L.d("use time: " + (endTime - startTime) + " ms");
	}

	public List<ImageBucket> getImagesBucketList(boolean refresh) {
		if (refresh || (!refresh && !hasBuildImagesBucketList)) {
			bucketList.clear();
			albumList.clear();
			thumbnailList.clear();
			buildImagesBucketList();
		}
		List<ImageBucket> tmpList = new ArrayList<ImageBucket>();
		Iterator<Entry<String, ImageBucket>> itr = bucketList.entrySet()
				.iterator();
		while (itr.hasNext()) {
			Map.Entry<String, ImageBucket> entry = itr.next();
			tmpList.add(entry.getValue());
		}
		return tmpList;
	}

	String getOriginalImagePath(String image_id) {
		String path = null;
		L.i("---(^o^)----" + image_id);
		String[] projection = { BaseColumns._ID, MediaColumns.DATA };
		Cursor cursor = cr.query(Media.EXTERNAL_CONTENT_URI, projection,
				BaseColumns._ID + "=" + image_id, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			path = cursor.getString(cursor.getColumnIndex(MediaColumns.DATA));
			cursor.close();
		}
		return path;
	}

}
