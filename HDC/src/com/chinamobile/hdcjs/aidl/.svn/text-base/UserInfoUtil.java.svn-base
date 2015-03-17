package com.chinamobile.hdcjs.aidl;


import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

/**
 * 用户信息工具
 */
public class UserInfoUtil {
	private static final String TAG = "UserInfoUtil";

	/**
	 * 查询userinfo表中数据
	 * 
	 * @param context
	 * @param columnIndex
	 * @return
	 */
	public static String query(Context context, String columnIndex) {
		ContentResolver contentResolver;
		// 通过contentResolver进行查找
		contentResolver = context.getContentResolver();
		Cursor cursor = contentResolver.query(
				Uri.parse("content://com.chinamobile.hdcjs/userinfo"),
				new String[] { "token", "phone", "sessionId", "imageUrl" },
				null, null, null);
		String result = "";
		if (cursor != null && cursor.moveToFirst()) {
			result = cursor.getString(cursor.getColumnIndex(columnIndex));
			cursor.close(); // 查找后关闭游标
		} else {
			Log.d(TAG, "cursor is null...");
		}
		return result;
	}

	/**
	 * 获取用户token
	 * 
	 * @return
	 */
	public static String getUserToken(Context context) {
		return query(context, "token");
	}

	/**
	 * 获取用户imageUrl
	 * 
	 * @return
	 */
	public static String getImageUrl(Context context) {
		return query(context, "imageUrl");
	}

	/**
	 * 获取用户电话号码
	 * 
	 * @return
	 */
	public static String getUserPhone(Context context) {

		return query(context, "phone");
	}

	/**
	 * 获取sessionId
	 * 
	 * @return
	 */
	public static String getUserSessionId(Context context) {
		return query(context, "sessionId");
	}
}
