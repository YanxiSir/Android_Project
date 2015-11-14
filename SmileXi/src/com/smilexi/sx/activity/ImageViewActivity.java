package com.smilexi.sx.activity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.smilexi.sx.R;
import com.smilexi.sx.finals.ServerFinals;
import com.smilexi.sx.fragment.GlideImageFragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ImageViewActivity extends FragmentActivity {
	public static final String IMAGE_URLS_KEY = "ImageURLs";
	public static final String SELECT_INDEX_KEY = "SelectIndex";
	private LinearLayout loadingLine;
	private ImageView loadingImage;

	private GlideImageFragment glideImages;

	private Animation loadingAnim;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.common_image_view);

		String path = "";
		String thirdKey = "http://7teb2g.com2.z0.glb.qiniucdn.com";
		String replaceStr = "?imageMogr2/auto-orient/gravity/center/quality/80/format/jpg/interlace/1";
		glideImages = new GlideImageFragment();
		Bundle bundle = getIntent().getExtras();
		List<String> imageUrls = new ArrayList<String>();
		List<String> smallImageUrls = (List<String>) bundle.get(IMAGE_URLS_KEY);
//		for (int i = 0; i < smallImageUrls.size(); i++) {
//			if (smallImageUrls.get(i).startsWith(ServerFinals.WS_DOMAIN)) {
//				path = smallImageUrls.get(i).replace("thumbnail", "large");
//			} else if (smallImageUrls.get(i).startsWith(thirdKey)) {
//				if (smallImageUrls.get(i).indexOf("?") > 0) {
//					path = smallImageUrls.get(i).substring(0,
//							smallImageUrls.get(i).indexOf("?"))
//							+ replaceStr;
//				} else {
//					path = smallImageUrls.get(i).substring(0,
//							smallImageUrls.get(i).indexOf("?"));
//				}
//			} else {
//				path = smallImageUrls.get(i).substring(0,
//						smallImageUrls.get(i).indexOf("?"));
//			}
//			imageUrls.add(path);
//		}

		glideImages.init(smallImageUrls, (Integer) bundle.get(SELECT_INDEX_KEY));
		// glideImages.init(photoUrls, currentSelected);
		FragmentTransaction ft = getFragmentTransaction();
		ft.add(R.id.glideimage, glideImages).commit();

		// imgControl = (ImageControl) findViewById(R.id.imageview_control);
		loadingLine = (LinearLayout) findViewById(R.id.loading_line);
		loadingImage = (ImageView) findViewById(R.id.loading_inner_image);

		// imgControl.setVisibility(View.GONE);
		loadingLine.setVisibility(View.GONE);

		if (loadingAnim == null) {
			loadingAnim = AnimationUtils.loadAnimation(this, R.anim.loading_3);
			LinearInterpolator lin = new LinearInterpolator();
			loadingAnim.setInterpolator(lin);
			if (loadingAnim != null) {
				loadingImage.startAnimation(loadingAnim);
				loadingAnim.startNow();
			}
		} else {
			loadingImage.startAnimation(loadingAnim);
			loadingAnim.startNow();
		}

		// titleLeftRel.setOnClickListener(titleLeftImageClickLis);
	}

	private FragmentTransaction getFragmentTransaction() {
		FragmentManager fragmentMgr = getSupportFragmentManager();
		FragmentTransaction ft = fragmentMgr.beginTransaction();
		return ft;
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
	}

	private OnClickListener titleLeftImageClickLis = new OnClickListener() {

		@Override
		public void onClick(View v) {
			ImageViewActivity.this.finish();
		}
	};

	private void request(String... params) {
		new AsyncTask<String, Integer, Bitmap>() {

			@Override
			protected Bitmap doInBackground(String... params) {
				Bitmap bm = null;
				try {

					URL myurl = new URL(params[0]);
					// �������?
					HttpURLConnection conn = (HttpURLConnection) myurl
							.openConnection();
					conn.setConnectTimeout(6000);// ���ó�ʱ
					conn.setDoInput(true);
					conn.setUseCaches(false);// ������
					conn.connect();
					InputStream is = conn.getInputStream();// ���ͼƬ��������?
					bm = BitmapFactory.decodeStream(is);
					is.close();
				} catch (Exception e) {
					return null;
				}
				return bm;
			}

			@Override
			protected void onPostExecute(Bitmap result) {
				if (result != null) {
					loadingLine.setVisibility(View.GONE);
					// imgControl.setVisibility(View.VISIBLE);

					Bitmap bmp = result;

					Rect frame = new Rect();
					getWindow().getDecorView().getWindowVisibleDisplayFrame(
							frame);
					int statusBarHeight = frame.top;
					int screenW = ImageViewActivity.this.getWindowManager()
							.getDefaultDisplay().getWidth();
					int screenH = ImageViewActivity.this.getWindowManager()
							.getDefaultDisplay().getHeight()
							- statusBarHeight;
				}
			}
		}.execute(params);
	}

	@Override
	public void onBackPressed() {
		finish();
		overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out);
	}

}
