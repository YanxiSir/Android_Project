package com.neu.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetUtils {

	public static boolean isHaveInternet(final Context context) { 
            ConnectivityManager manger = (ConnectivityManager) 
                    context.getSystemService(Context.CONNECTIVITY_SERVICE); 
 
            NetworkInfo info = manger.getActiveNetworkInfo(); 
            return (info!=null && info.isConnected()); 
        
    } 
	public static boolean isHaveWIFI(final Context context){
		ConnectivityManager manager = (ConnectivityManager) 
				context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return (info!=null && info.isConnected());
		
	}
}
