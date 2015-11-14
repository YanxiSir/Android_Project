package com.neu.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.neu.exception.UnkownNetWorkException;
import com.neu.tools.constant.ConstantUtils;

import android.content.Context;
import android.os.Environment;

public class StoreBancheInfoUtils {
	

	public static InputStream getBancheInfoInputStream(Context context,String fileName) throws UnkownNetWorkException{
		InputStream is = null;
		
		try{
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				// 获得存储卡的路径
				String sdpath = Environment.getExternalStorageDirectory()
						+ "/";
				String mSavePath = sdpath + "neuhelper";
				
	
				File file = new File(mSavePath);
				// 判断文件目录是否存在
				if (!file.exists()) {
					file.mkdir();
				}
				File infoFile = new File(mSavePath,fileName);
				if(!infoFile.exists())
					storeInfoFromNet(context);
				is = new FileInputStream(infoFile);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return is;
		
	}
	public static void storeInfoFromNet(Context context) throws IOException, UnkownNetWorkException{ 
		
		if (!NetUtils.isHaveInternet(context)) {
			throw new UnkownNetWorkException("网络未连接");
			
		}
		URL url = new URL(ConstantUtils.teaBancheInfo);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.connect();
		InputStream is = (InputStream) connection
				.getContent();
		StoreBancheInfoUtils.storeBancheInfo(is,ConstantUtils.teaInfoStr );
		
		url = new URL(ConstantUtils.stuBancheInfo);
		connection = (HttpURLConnection) url.openConnection();
		connection.connect();
		is = (InputStream) connection.getContent();
		StoreBancheInfoUtils.storeBancheInfo(is, ConstantUtils.stuInfoStr);
	}
	
	public static boolean storeBancheInfo(InputStream is,String fileName){
		try{
			if (Environment.getExternalStorageState().equals(
					Environment.MEDIA_MOUNTED)) {
				// 获得存储卡的路径
				String sdpath = Environment.getExternalStorageDirectory()
						+ "/";
				String mSavePath = sdpath + "neuhelper";
				
	
				File file = new File(mSavePath);
				// 判断文件目录是否存在
				if (!file.exists()) {
					file.mkdir();
				}
				File infoFile = new File(mSavePath,fileName);
				if(!infoFile.exists())
					infoFile.createNewFile();
				FileOutputStream fos = new FileOutputStream(infoFile);
				byte buf[] = new byte[1024];
				int len = -1;
				// 写入到文件中
				while((len = is.read(buf))!=-1){
					fos.write(buf, 0, len);
				}
				fos.close();
				is.close();
				return true;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}
