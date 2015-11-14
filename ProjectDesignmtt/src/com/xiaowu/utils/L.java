package com.xiaowu.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

/**
 * Logͳһ������
 * 
 * @author way
 * 
 */
public class L {
	public static boolean isDebug = true;// �Ƿ���Ҫ��ӡbug��������application��onCreate���������ʼ��
	private static final String TAG = "Neitao";
	private static boolean isReport = true;

	// �����ĸ���Ĭ��tag�ĺ���
	public static void i(String msg) {
		if (isDebug)
			Log.i(TAG, msg);
	}

	public static void d(String msg) {
		if (isDebug)
			Log.d(TAG, msg);
	}

	public static void e(String msg) {
		// if ("service = null".equalsIgnoreCase(msg)) {
		// System.out.println(msg);
		// }
		if (isDebug)
			Log.e(TAG, msg);
	}

	public static void v(String msg) {
		if (isDebug)
			Log.v(TAG, msg);
	}

	// �����Ǵ���������ӡlog
	public static void i(Class<?> _class, String msg) {
		if (isDebug)
			Log.i(_class.getName(), msg);
	}

	public static void d(Class<?> _class, String msg) {
		if (isDebug)
			Log.i(_class.getName(), msg);
	}

	public static void e(Class<?> _class, String msg) {
		if (isDebug)
			Log.i(_class.getName(), msg);
	}

	public static void v(Class<?> _class, String msg) {
		if (isDebug)
			Log.i(_class.getName(), msg);
	}

	// �����Ǵ����Զ���tag�ĺ���
	public static void i(String tag, String msg) {
		if (isDebug)
			Log.i(tag, msg);
	}

	public static void d(String tag, String msg) {
		if (isDebug)
			Log.i(tag, msg);
	}

	public static void e(String tag, String msg) {
		// if ("service = null".equalsIgnoreCase(msg)) {
		// System.out.println(msg);
		// }
		// if ("StubController".equalsIgnoreCase(tag)) {
		// System.out.println(msg);
		// }

		if (isDebug)
			Log.i(tag, msg);
	}

	public static void v(String tag, String msg) {
		if (isDebug)
			Log.i(tag, msg);
	}

	// ��¼������־������
	public static void reportErrorToUMeng(Context mcontext, String message) {
		if (isReport) {
			if (!TextUtils.isEmpty(message)) {
				// MobclickAgent.reportError(mcontext, message);
			}
		}
	}

	// ��¼������־������
	public static void reportErrorToUMeng(Context mcontext, Throwable ex) {
		if (isReport) {
			if (ex != null) {
				// MobclickAgent.reportError(mcontext, ex);
			}
		}
	}
}
