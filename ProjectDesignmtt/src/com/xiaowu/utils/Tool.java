package com.xiaowu.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import android.app.Activity;
import android.content.Context;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.xiaowu.projectdesign.R;


public class Tool {
	public static boolean isOnMainThread() {
		return Looper.myLooper() == Looper.getMainLooper();
	}

	public static String decodeUTF_8(String str) {
		String s = "";
		try {
			s = URLDecoder.decode(str, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return s;
	}
	public static String encodeUTF_8(String str) {
		String s = "";
		try {
			s = URLEncoder.encode(str, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return s;
	}

	public static void imageLoader(Context context, ImageView view,
			String imageUrl, Callback callback) {// ImageLoadingListener
		// callback

		if (imageUrl == null) {
			return;
		}

		if (imageUrl.startsWith("http://7teb2g.com2.z0.glb.qiniucdn.com")) {
			imageUrl.replace(
					"imageMogr/auto-orient/thumbnail/!140x140r/gravity/center/crop/!140x140/quality/80",
					"imageMogr/auto-orient/thumbnail/!140x140r/gravity/center/crop/!140x140/quality/80/format/jpg");
		}
		if (callback == null)
			Picasso.with(context).load(imageUrl)
					.placeholder(R.drawable.defaultheader)
					.error(R.drawable.defaultheader).into(view);
		else
			Picasso.with(context).load(imageUrl)
					.error(R.drawable.defaultheader).into(view, callback);// .into(view);
	}
	
	public static void hideSoft(Context context) {
		try {
			InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
			if (imm.isActive()) {
				imm.hideSoftInputFromWindow(((Activity) context).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

	}
	


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if(listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for(int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }
	
}
