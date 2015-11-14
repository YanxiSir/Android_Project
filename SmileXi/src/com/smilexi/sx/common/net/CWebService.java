package com.smilexi.sx.common.net;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.smilexi.sx.util.L;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

public class CWebService {
	private static String tag = "WebHandlerUtils";
	private static int THREAD_COUNT = 3;

	private static final ExecutorService executorService = Executors
			.newFixedThreadPool(THREAD_COUNT);

	public static void reqSessionHandler(final Context context,
			final String market_uri, final SoapObject request,
			final WebHandlerCallBack webHandlerCallBack) {

		if (request == null) {
			return;
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
					HttpTransportSE ht = new HttpTransportSE(market_uri);
					ht.debug = false;
					SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
							SoapEnvelope.VER11);
					envelope.bodyOut = ht;
					envelope.dotNet = true;
					envelope.setOutputSoapObject(request);

					ht.call(null, envelope);

					// SoapObject soapObject =
					// (SoapObject)envelope.getResponse();
					
					Object obj = (Object) envelope.bodyIn; 
					SoapObject soapObject = (SoapObject) obj;
					// result = obj.toString();
					result = (String) soapObject.getPropertyAsString(0);
					L.d(result);

				} catch (IOException e) {
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				} finally {
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					mHandler.obtainMessage(0, result).sendToTarget();
//					mHandler.sendMessage(mHandler.obtainMessage(0, result));
				}
			}
		});

	}

	public interface WebHandlerCallBack {
		public void callBack(String result);
	}
}
