package com.chinamobile.hdcjs.database;

import android.app.DevInfoManager;
import android.app.SystemInfo;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class LoginInfoDBHelper extends SQLiteOpenHelper {
	
	private static String TAG = "LoginInfoDBHelper";
	public static final int LOGINTYPE_LOGOUT = 0;				// 未登录
	public static final int LOGINTYPE_SERVICE = 1;				// 业务密码登录
	public static final int LOGINTYPE_SSO_PASSWORD = 2;		// 全网统一认证固定密码登录
	public static final int LOGINTYPE_SSO_SMS = 3;				// 全网统一认证短信动态密码登录
	
	public static final int LOGINSTATUS_ON = 1;					// 已登录
	public static final int LOGINSTATUS_OFF = 0;				// 未登录

	public LoginInfoDBHelper(Context context) {
		super(context, "LoginInfoDBHelper", null, 1);
	}
	
	public LoginInfoDBHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table login_info (current_login_type INTEGER, current_login_status INTEGER)");
		
		ContentValues values = new ContentValues();
		values.put("current_login_type", LoginInfoDBHelper.LOGINTYPE_LOGOUT);
		values.put("current_login_status", LoginInfoDBHelper.LOGINSTATUS_OFF);
		db.insert("login_info", "current_login_type", values);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 获取当前登录类型
	 * @return LOGINTYPE_LOGOUT 未登录|LOGINTYPE_SERVICE 业务密码登录|LOGINTYPE_SSO_PASSWORD 全网统一认证固定密码登录|LOGINTYPE_SSO_SMS 全网统一认证短信动态密码登录
	 */
	public static int getCurrentLoginType(Context context) {
		LoginInfoDBHelper helper = new LoginInfoDBHelper(context);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor result = db.rawQuery("select current_login_type from login_info", null);
		result.moveToFirst();
		int res = result.getInt(0);
		result.close();
		db.close();
		helper.close();
		return res;
	}
	
	/**
	 * 获取当前登录状态
	 * @return LOGINSTATUS_OFF 未登录|LOGINSTATUS_ON 已登录
	 */
	public static int getCurrentLoginStatus(Context context) {
		LoginInfoDBHelper helper = new LoginInfoDBHelper(context);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor result = db.rawQuery("select current_login_status from login_info", null);
		result.moveToFirst();
		int res = result.getInt(0);
		result.close();
		db.close();
		helper.close();
		return res;
	}
	
	/**
	 * 获取上次成功登录类型
	 * @return LOGINTYPE_SERVICE 业务密码登录|LOGINTYPE_SSO_PASSWORD 全网统一认证固定密码登录|LOGINTYPE_SSO_SMS 全网统一认证短信动态密码登录
	 */
	public static int getLastLoginType(Context context) {
		
		String ssoUsername = SystemInfo.getMobilePhoneNumber(context);
		if (ssoUsername == null || "".equals(ssoUsername)) {
			// 非业务密码登录
			String serviceUsername = SystemInfo.getAccount(context);
			if (serviceUsername == null || "".equals(serviceUsername)) {
				// 数据区无数据，属于非登录状态
				return LOGINTYPE_LOGOUT;
			} else {
				String ssoPassword = SystemInfo.getAccountPassword(context);
				if (ssoPassword == null || "".equals(ssoPassword)) {
					// 没有保存密码，为短信动态密码登录方式
					Log.d(TAG, "getLastLoginType: 短信动态密码登录方式");
					return LOGINTYPE_SSO_SMS;
				} else {
					// 保存了密码，为sso固定密码方式
					Log.d(TAG, "getLastLoginType: sso固定密码方式");
					return LOGINTYPE_SSO_PASSWORD;
				}
			}
		}
		
		// 数据区业务账户密码保存，为业务密码登录方式
		return LOGINTYPE_SERVICE;
	}
	
	/**
	 * 获取上次成功登录的用户名
	 * @return
	 */
	public static String getLastLoginUsername(Context context) {
		int type = getLastLoginType(context);
		if (LOGINTYPE_SERVICE == type) {
			return SystemInfo.getMobilePhoneNumber(context);
		} else if (LOGINTYPE_LOGOUT == type) {
			return "";
		} else {
			return SystemInfo.getAccount(context);
		}
	}
	
	/**
	 * 获取上次成功登录的密码
	 * @return
	 */
	public static String getLastLoginPassword(Context context) {
		int type = getLastLoginType(context);
		if (LOGINTYPE_SERVICE == type) {
			return SystemInfo.getServicePassword(context);
		} else if (LOGINTYPE_SSO_PASSWORD == type) {
			return SystemInfo.getAccountPassword(context);
		} else {
			return "";
		}
	}
	
	/**
	 * 设置当前登录类型
	 * @param type LOGINSTATUS_OFF 未登录|LOGINTYPE_SERVICE 业务密码登录|LOGINTYPE_SSO_PASSWORD 全网统一认证固定密码登录|LOGINTYPE_SSO_SMS 全网统一认证短信动态密码登录
	 */
	public static void setCurrentLoginType(Context context, int type) {
		LoginInfoDBHelper helper = new LoginInfoDBHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("current_login_type", type);
		db.update("login_info", values, null, null);
		db.close();
		helper.close();
	}
	
	/**
	 * 设置当前登录状态
	 * @param status LOGINSTATUS_OFF 未登录|LOGINSTATUS_ON 已登录
	 */
	public static void setCurrentLoginStatus(Context context, int status) {
		LoginInfoDBHelper helper = new LoginInfoDBHelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("current_login_status", status);
		db.update("login_info", values, null, null);
		db.close();
		helper.close();
	}

	/**
	 * 清理所有的用户名密码
	 * @param context
	 */
	public static void clearUserAndPassword(Context context) {
		DevInfoManager devInfoManager = (DevInfoManager)context.getSystemService(DevInfoManager.DATA_SERVER);
		devInfoManager.update(DevInfoManager.PHONE, "",
				DevInfoManager.Default_Attribute);
		devInfoManager.update(DevInfoManager.SPASSWORD, "",
				DevInfoManager.Default_Attribute);
		devInfoManager.update(DevInfoManager.ACCOUNT, "",
				DevInfoManager.Default_Attribute);
		devInfoManager.update(DevInfoManager.APASSWORD, "",
				DevInfoManager.Default_Attribute);
	}
	
	/**
	 * 保存统一认证平台登录的用户名密码到数据区
	 * @param context
	 * @param username
	 * @param password
	 */
	public static void saveSSOUserPassword(Context context, String username, String password) {
		DevInfoManager devInfoManager = (DevInfoManager)context.getSystemService(DevInfoManager.DATA_SERVER);
		
		devInfoManager.update(DevInfoManager.ACCOUNT, username,
				DevInfoManager.Default_Attribute);
		devInfoManager.update(DevInfoManager.APASSWORD,password,
				DevInfoManager.Default_Attribute);
		devInfoManager.update(DevInfoManager.PHONE, "",
				DevInfoManager.Default_Attribute);
		devInfoManager.update(DevInfoManager.SPASSWORD, "",
				DevInfoManager.Default_Attribute);
	}
	
	/**
	 * 保存业务密码登录的用户名密码到数据区
	 * @param context
	 * @param username
	 * @param password
	 */
	public static void saveServiceUserPassword(Context context, String username, String password) {
		DevInfoManager devInfoManager = (DevInfoManager)context.getSystemService(DevInfoManager.DATA_SERVER);
		
		devInfoManager.update(DevInfoManager.ACCOUNT, "",
				DevInfoManager.Default_Attribute);
		devInfoManager.update(DevInfoManager.APASSWORD, "",
				DevInfoManager.Default_Attribute);
		devInfoManager.update(DevInfoManager.PHONE, username,
				DevInfoManager.Default_Attribute);
		devInfoManager.update(DevInfoManager.SPASSWORD, password,
				DevInfoManager.Default_Attribute);
	}
	
}
