package com.smilexi.sx.app;


import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.smilexi.sx.common.SXContext;

import android.app.Activity;
import android.app.Application;


public class AppManager extends Application {
	/** 应用实例 **/
	private static AppManager instance;
	
	/** 打开的activity **/
	private List<Activity> activities = new ArrayList<Activity>();
	
	/**
	 * 获得实例
	 * 
	 * @return
	 */
	public static AppManager getInstance() {
		if (instance == null) {
			instance = new AppManager();
		}
		return instance;
	}
	
	/**
	 * 新建了一个activity
	 * 
	 * @param activity
	 */
	public void addActivity(Activity activity) {
		activities.add(activity);
	}

	/**
	 * 结束指定的Activity
	 * 
	 * @param activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			this.activities.remove(activity);
			activity.finish();
			activity = null;
		}
	}
	
	/**
	 * 应用退出，结束所有的activity
	 */
	public void exit() {
		for (Activity activity : activities) {
			if (activity != null) {
				activity.finish();
			}
		}
		System.exit(0);
	}
	
	@Override
	public void onCreate() {
		instance = this;
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				instance)
				//
				.threadPriority(Thread.NORM_PRIORITY - 2)
				//
				// .denyCacheImageMultipleSizesInMemory()//
				.memoryCache(new WeakMemoryCache())
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())//
				.tasksProcessingOrder(QueueProcessingType.LIFO)//
				// .writeDebugLogs()//
				.build();
		ImageLoader.getInstance().init(config);

		SXContext sxContext = SXContext.getInstance();
		if (sxContext != null) {
			sxContext.init(this);
		}
		
		 JPushInterface.setDebugMode(true);
         JPushInterface.init(this);

	}
}
