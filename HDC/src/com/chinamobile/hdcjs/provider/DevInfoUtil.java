package com.chinamobile.hdcjs.provider;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

/**
 * 供其他用户获取机顶盒信息的类，HDC不会调用
 * @author CJL
 *
 */
public class DevInfoUtil {
	private static final String TAG = "DevInfoUtil";

	/**
	 * 查询devinfo表中数据
	 * 
	 * @param context
	 * @param columnIndex
	 * @return
	 */
	public static String query(Context context, String columnIndex) {
		String result = "";
		
		if(context==null){
			Log.e(TAG, "context is null...");
			return result;
		}
		
		ContentResolver contentResolver;
		// 通过contentResolver进行查找
		contentResolver = context.getContentResolver();
		Cursor cursor = contentResolver.query(
				Uri.parse("content://com.chinamobile.hdcjs.devinfoprovider/devinfo"),
				new String[] { "STBMAC", "STBSN", "PlatformURL",
						"PlatformURLBackup", "CMSURL", "CMSURLBackup","HDCRURL" }, null,
				null, null);
		
		if (cursor != null && cursor.moveToFirst()) {
			result = cursor.getString(cursor.getColumnIndex(columnIndex));
			cursor.close(); // 查找后关闭游标
		} else {
			Log.d(TAG, "cursor is null...");
		}
		return result;
	}

	/**
	 * 获取STBMAC
	 * 
	 * @return
	 */
	public static String getDevSTBMAC(Context context) {
		return query(context, "STBMAC");
	}

	/**
	 * 获取STBSN
	 * 
	 * @return
	 */
	public static String getDevSTBSN(Context context) {
		return query(context, "STBSN");
	}

	/**
	 * 获取PlatformURL
	 * 
	 * @return
	 */
	public static String getDevPlatformURL(Context context) {

		return query(context, "PlatformURL");
	}

	/**
	 * 获取PlatformURLBackup
	 * 
	 * @return
	 */
	public static String getDevPlatformURLBackup(Context context) {
		return query(context, "PlatformURLBackup");
	}

	/**
	 * 获取CMSURL
	 * 
	 * @return
	 */
	public static String getDevCMSURL(Context context) {
		return query(context, "CMSURL");
	}

	/**
	 * 获取CMSURLBackup
	 * 
	 * @return
	 */
	public static String getDevCMSURLBackup(Context context) {
		return query(context, "CMSURLBackup");
	}
	
	/**
	 * 获取CMSURLBackup
	 * 
	 * @return
	 */
	public static String getDevHDCRURL(Context context) {
		return query(context, "HDCRURL");
	}
	
	/********** 以下方法返回的服务器路径，由nginx服务器分发指向真实服务请求地址 **********/
	/*
	 * 获取HDC服务地址
	 */
	public static String getHDCServiceURL(Context context) {
		// 形如http://112.4.17.182:8080/hdc-service/
		String hdcServiceUrl = getDevHDCRURL(context) + "hdc-service/";
		return hdcServiceUrl;
	}

	/*
	 * 获取应用远程服务地址
	 */
	public static String getHopAppRemoteURL(Context context) {
		// 形如http://112.4.17.182:8080/hop-app-remote/
		String hopAppRemoteURL = getDevHDCRURL(context) + "hop-app-remote/";
		return hopAppRemoteURL;
	}

	/*
	 * 获取屏幕配置文件服务地址
	 */
	public static String getLauncherXmlURL(Context context) {
		// 形如http://112.4.17.182:8080/launcher-xml/
		String launcherXml = getDevHDCRURL(context) + "launcher-xml/";
		return launcherXml;
	}
}
