package com.xiaowu.common.net;

import com.xiaowu.utils.FileUtils;
import com.xiaowu.utils.L;
import com.xiaowu.utils.Tool;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;




public class CWebService {
	private static String tag = "WebHandlerUtils";

	public interface INtNetCallback {
		public void OnError(int errorCode);

		public void OnSuccess(Object obj);
	}

	// ����3���̵߳��̳߳�
	private static final ExecutorService executorService = Executors
			.newFixedThreadPool(3);

	/**
	 * 
	 * @param url
	 *            WebService��������ַ
	 * @param methodName
	 *            WebService�ĵ��÷�����
	 * @param properties
	 *            WebService�Ĳ���
	 * @param webHandlerCallBack
	 *            �ص��ӿ�
	 */
	public static void callWebHandler(String market_uri,
			List<NameValuePair> nameValuePairs,
			final WebHandlerCallBack webHandlerCallBack) {

		if (null == market_uri || "" == market_uri) {
			return;
		}
		final HttpPost request = new HttpPost(market_uri);
		try {
			request.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		// �������߳������߳�ͨ�ŵ�Handler
		final Handler mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				// ������ֵ�ص���callBack�Ĳ�����
				webHandlerCallBack.callBack((String) msg.obj);
			}

		};

		// �����߳�ȥ����web handler
		executorService.submit(new Runnable() {

			@Override
			public void run() {
				String result = null;
				try {
					HttpResponse httpResponse = new DefaultHttpClient()
							.execute(request);
					int statusCode = httpResponse.getStatusLine()
							.getStatusCode();
					if (HttpStatus.SC_OK == statusCode) {
						result = EntityUtils.toString(httpResponse.getEntity());
						L.i(tag, "results=" + result);
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					// ����ȡ����Ϣ����Handler���͵����߳�
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					mHandler.sendMessage(mHandler.obtainMessage(0, result));
				}
			}
		});
	}

	/**
	 * 
	 * @param url
	 *            WebService��������ַ
	 * @param methodName
	 *            WebService�ĵ��÷�����
	 * @param properties
	 *            WebService�Ĳ���
	 * @param webHandlerCallBack
	 *            �ص��ӿ�
	 */
	public static void reqSessionHandlerGet(final Context context,
			String market_uri, 
			final WebHandlerCallBack webHandlerCallBack) {

		if (null == market_uri || "" == market_uri) {
			return;
		}
		final HttpGet request = new HttpGet(market_uri);
		final Handler mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				webHandlerCallBack.callBack((String) msg.obj);
			}

		};
		executorService.submit(new Runnable() {

			@Override
			public void run() {
				String result = null;
				try {
					DefaultHttpClient httpClient = new DefaultHttpClient();
					httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000);
					
					PersistentCookieStore cookieStore = new PersistentCookieStore(
							context);
					if (cookieStore != null)
						httpClient.setCookieStore(cookieStore);

					HttpResponse httpResponse = httpClient.execute(request);

					int statusCode = httpResponse.getStatusLine()
							.getStatusCode();
					if (HttpStatus.SC_OK == statusCode) {
						result = EntityUtils.toString(httpResponse.getEntity());

						// List<Cookie> cookies = httpClient.getCookieStore()
						// .getCookies();
						// for (Cookie cookie : cookies) {
						// cookieStore.addCookie(cookie);
						// }

						L.i(tag, "results=" + result);
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					// ����ȡ����Ϣ����Handler���͵����߳�
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					mHandler.sendMessage(mHandler.obtainMessage(0, result));
				}
			}
		});
	}
	public static void reqSessionHandler(final Context context,
			String market_uri, List<NameValuePair> nameValuePairs,
			final WebHandlerCallBack webHandlerCallBack) {

		if (null == market_uri || "" == market_uri) {
			return;
		}
		final HttpPost request = new HttpPost(market_uri);
		try {
			request.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		final Handler mHandler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				webHandlerCallBack.callBack((String) msg.obj);
			}

		};
		executorService.submit(new Runnable() {

			@Override
			public void run() {
				String result = null;
				try {
					
					DefaultHttpClient httpClient = new DefaultHttpClient();
					httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000);
					
					PersistentCookieStore cookieStore = new PersistentCookieStore(
							context);
					if (cookieStore != null)
						httpClient.setCookieStore(cookieStore);

					HttpResponse httpResponse = httpClient.execute(request);

					int statusCode = httpResponse.getStatusLine()
							.getStatusCode();
					if (HttpStatus.SC_OK == statusCode) {
						result = EntityUtils.toString(httpResponse.getEntity());

						// List<Cookie> cookies = httpClient.getCookieStore()
						// .getCookies();
						// for (Cookie cookie : cookies) {
						// cookieStore.addCookie(cookie);
						// }

						L.i(tag, "results=" + result);
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					// ����ȡ����Ϣ����Handler���͵����߳�
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					mHandler.sendMessage(mHandler.obtainMessage(0, result));
				}
			}
		});
	}

	public static class IoOnMainThreadExecption extends Exception {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	}

	public static String synReqSessionHandler(final Context context,
			String market_uri, List<NameValuePair> nameValuePairs)
			throws IoOnMainThreadExecption {

		if (Tool.isOnMainThread()) {
			throw new IoOnMainThreadExecption();
		}

		if (null == market_uri || "" == market_uri) {
			return null;
		}
		final HttpPost request = new HttpPost(market_uri);
		try {
			request.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		String result = null;
		try {
			DefaultHttpClient httpClient = new DefaultHttpClient();

			PersistentCookieStore cookieStore = new PersistentCookieStore(
					context);
			if (cookieStore != null)
				httpClient.setCookieStore(cookieStore);

			HttpResponse httpResponse = httpClient.execute(request);

			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (HttpStatus.SC_OK == statusCode) {
				result = EntityUtils.toString(httpResponse.getEntity());
				// L.i(tag, "results=" + result);
				return result;
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;

	}

	public static void loginWebHandler(final Context context,
			String market_uri, List<NameValuePair> nameValuePairs,
			final WebHandlerCallBack webHandlerCallBack) {

		if (null == market_uri || "" == market_uri) {
			return;
		}
		final HttpPost request = new HttpPost(market_uri);
		try {
			request.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		// �������߳������߳�ͨ�ŵ�Handler
		final Handler mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				// ������ֵ�ص���callBack�Ĳ�����
				webHandlerCallBack.callBack((String) msg.obj);
			}

		};

		// �����߳�ȥ����web handler
		executorService.submit(new Runnable() {

			@Override
			public void run() {
				String result = null;
				try {
					DefaultHttpClient httpClient = new DefaultHttpClient();

					HttpResponse httpResponse = httpClient.execute(request);

					int statusCode = httpResponse.getStatusLine()
							.getStatusCode();
					if (HttpStatus.SC_OK == statusCode) {
						PersistentCookieStore cookieStore = new PersistentCookieStore(
								context);//
						List<Cookie> cookies = httpClient.getCookieStore()
								.getCookies();
						for (Cookie cookie : cookies) {
							cookieStore.addCookie(cookie);
						}

						result = EntityUtils.toString(httpResponse.getEntity());
						L.i(tag, "results=" + result);
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					// ����ȡ����Ϣ����Handler���͵����߳�
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					mHandler.sendMessage(mHandler.obtainMessage(0, result));
				}
			}
		});
	}

	/*
	 * */
	public static void savaBitmapHandler(final Context context,
			final Bitmap bitmap, final Object param,
			final WebHandlerCallBack webHandlerCallBack) {

		/*
		 * if (null == bitmap) { return; }
		 */

		final Handler mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				webHandlerCallBack.callBack((String) msg.obj);
			}

		};

		// �����߳�ȥ����web handler
		executorService.submit(new Runnable() {

			@Override
			public void run() {
				String filePath = FileUtils.savaBitmap(context, bitmap, param);
				mHandler.sendMessage(mHandler.obtainMessage(0, filePath));
			}
		});
	}

	public static void getURLBitmap(final Context context, final Object param,
			final WebHandlerCallBack2 webHandlerCallBack2) {

		/*
		 * if (null == bitmap) { return; }
		 */

		final String urlStr = (String) param;
		if (TextUtils.isEmpty(urlStr)) {
			webHandlerCallBack2.callBack(null);
			return;
		}

		final Handler mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				webHandlerCallBack2.callBack(msg.obj);
			}

		};

		// �����߳�ȥ����web handler
		executorService.submit(new Runnable() {

			@Override
			public void run() {

				HttpURLConnection conn;
				try {
					URL url = new URL(urlStr);
					conn = (HttpURLConnection) url.openConnection();

					conn.setDoInput(true);
					conn.connect();
					int statusCode = conn.getResponseCode();

					if (HttpStatus.SC_OK == statusCode) {

						InputStream is = conn.getInputStream();
						Bitmap bitmap = BitmapFactory.decodeStream(is);
						mHandler.obtainMessage(1, bitmap).sendToTarget();
						is.close();

					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 
	 * 
	 * @author xiaanming
	 * 
	 */
	public interface WebHandlerCallBack {
		public void callBack(String result);
	}
	public interface WebHandlerCallBack2{
		public void callBack(Object obj);
	}

	private static HashMap<String, String> CookieContiner = new HashMap<String, String>();

	/**
	 * ����Cookie
	 * 
	 * @param resp
	 */
	public void SaveCookies(HttpResponse httpResponse) {
		Header[] headers = httpResponse.getHeaders("Set-Cookie");
		String headerstr = headers.toString();
		if (headers == null)
			return;

		for (int i = 0; i < headers.length; i++) {
			String cookie = headers[i].getValue();
			String[] cookievalues = cookie.split(";");
			for (int j = 0; j < cookievalues.length; j++) {
				String[] keyPair = cookievalues[j].split("=");
				String key = keyPair[0].trim();
				String value = keyPair.length > 1 ? keyPair[1].trim() : "";
				CookieContiner.put(key, value);
			}
		}
	}

	/**
	 * ����Cookie
	 * 
	 * @param request
	 */
	public void AddCookies(HttpPost request) {
		StringBuilder sb = new StringBuilder();
		Iterator iter = CookieContiner.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = entry.getKey().toString();
			String val = entry.getValue().toString();
			sb.append(key);
			sb.append("=");
			sb.append(val);
			sb.append(";");
		}
		request.addHeader("cookie", sb.toString());
	}

}
