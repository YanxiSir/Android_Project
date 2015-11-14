package com.smilexi.sx.receiver;

import java.util.Set;

import org.json.JSONException;
import org.json.JSONObject;

import com.smilexi.sx.activity.DynamicDetail;
import com.smilexi.sx.activity.MainActivity;
import com.smilexi.sx.activity.NotifacationActivity;
import com.smilexi.sx.activity.SelfMainPageActivity;
import com.smilexi.sx.util.T;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.jpush.android.api.c;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class NotifacationReceiver extends BroadcastReceiver {
	private static final String TAG = "JPush";

	/*
	 * typ: 0 关注用户 {uid,createTime} 1 点赞生活动态 {did,uid,createTime} 2 赞答案
	 * {aid,uid,createTime} 3 回复生活动态 {did,uid,createTime} 4 回答问题
	 * {qid,uid,createTime} 5 评论答案 {aid,uid,createTime}
	 */
	public final static int TYPE_ATTENTION_USER = 0;
	public final static int TYPE_ZAN_DYNAMIC = 1;
	public final static int TYPE_ZAN_ANSWER = 2;
	public final static int TYPE_REPLY_DYNAMIC = 3;
	public final static int TYPE_ANSWER_QUESTION = 4;
	public final static int TYPE_REPLY_ANSWER = 5;

	public static int reply_count = 0;
	public static int zan_count = 0;

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction()
				+ ", extras: " + printBundle(bundle));

		if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
			String regId = bundle
					.getString(JPushInterface.EXTRA_REGISTRATION_ID);
			Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
			// send the Registration Id to your server...

		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG,
					"[MyReceiver] 接收到推送下来的自定义消息: "
							+ bundle.getString(JPushInterface.EXTRA_MESSAGE));
			processCustomMessage(context, bundle);

		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent
				.getAction())) {
			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
			int notifactionId = bundle
					.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
//
			// String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
			// try {
			// JSONObject json = new JSONObject(extra);
			// int typ = json.getInt("typ");
			// if (typ == TYPE_REPLY_DYNAMIC) {
			// reply_count++;
			// } else if (typ == TYPE_ZAN_DYNAMIC) {
			// zan_count++;
			// }
			// } catch (JSONException e) {
			// e.printStackTrace();
			// } finally {
			//
			// }

			Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent
				.getAction())) {
			Log.d(TAG, "[MyReceiver] 用户点击打开了通知");

			String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
			try {
				JSONObject json = new JSONObject(extra);
				int typ = json.getInt("typ");
				String createTime = json.getString("createTime");

				int uid, qid, aid, did;

				Intent i = null;
				switch (typ) {
				case TYPE_ATTENTION_USER:
					uid = json.getInt("uid");
					i = new Intent(context, MainActivity.class);
					break;
				case TYPE_ZAN_DYNAMIC:
					// did = json.getInt("did");
					// uid = json.getInt("uid");

					i = new Intent(context, NotifacationActivity.class);
					i.putExtra("notifacateType", NotifacationActivity.TYPE_ZAN);

					break;
				case TYPE_ZAN_ANSWER:
					// aid = json.getInt("aid");
					// uid = json.getInt("uid");
					i = new Intent(context, MainActivity.class);
					break;
				case TYPE_REPLY_DYNAMIC:
					// did = json.getInt("did");
					// uid = json.getInt("uid");
					i = new Intent(context, NotifacationActivity.class);
					i.putExtra("notifacateType",
							NotifacationActivity.TYPE_REPLY);
					// i.putExtra("detailType", DynamicDetail.DETAIL_LIFE);
					break;
				case TYPE_ANSWER_QUESTION:
					// qid = json.getInt("qid");
					// uid = json.getInt("uid");
					// i = new Intent(context,DynamicDetail.class);
					// i.putExtra("qid", qid);
					// i.putExtra("detailType", DynamicDetail.DETAIL_QUESTION);
					i = new Intent(context, MainActivity.class);
					break;
				case TYPE_REPLY_ANSWER:
					// aid = json.getInt("aid");
					// uid = json.getInt("uid");
					// i = new Intent(context,DynamicDetail.class);
					// i.putExtra("aid", aid);
					// i.putExtra("detailType",
					// DynamicDetail.SHOW_ANSWER_REPLY);

					i = new Intent(context, MainActivity.class);
					break;

				default:
					break;
				}
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
						| Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(i);
				JPushInterface.reportNotificationOpened(context,
						bundle.getString(JPushInterface.EXTRA_MSG_ID));
			} catch (JSONException e) {
				e.printStackTrace();
			}

			// 打开自定义的Activity
			// Intent i = new Intent(context, TestActivity.class);
			// i.putExtras(bundle);
			// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			// i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
			// | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			// context.startActivity(i);
			// JPushInterface.reportNotificationOpened(context,
			// bundle.getString(JPushInterface.EXTRA_MSG_ID));

		} else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent
				.getAction())) {
			Log.d(TAG,
					"[MyReceiver] 用户收到到RICH PUSH CALLBACK: "
							+ bundle.getString(JPushInterface.EXTRA_EXTRA));
			// 在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity，
			// 打开一个网页等..

		} else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent
				.getAction())) {
			boolean connected = intent.getBooleanExtra(
					JPushInterface.EXTRA_CONNECTION_CHANGE, false);
			Log.w(TAG, "[MyReceiver]" + intent.getAction()
					+ " connected state change to " + connected);
		} else {
			Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
		}
	}

	// 打印所有的 intent extra 数据
	private static String printBundle(Bundle bundle) {
		StringBuilder sb = new StringBuilder();
		for (String key : bundle.keySet()) {
			if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
			} else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
				sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
			} else {
				sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
			}
		}
		return sb.toString();
	}

	// send msg to MainActivity
	private void processCustomMessage(Context context, Bundle bundle) {
		if (MainActivity.isForeground) {
			String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
			String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
			Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
			msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
			if (!ExampleUtil.isEmpty(extras)) {
				try {
					JSONObject extraJson = new JSONObject(extras);
					if (null != extraJson && extraJson.length() > 0) {
						msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
					}
				} catch (JSONException e) {

				}

			}
			context.sendBroadcast(msgIntent);
		}
	}
}
