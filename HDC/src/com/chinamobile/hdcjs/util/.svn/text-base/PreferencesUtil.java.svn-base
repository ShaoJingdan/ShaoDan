package com.chinamobile.hdcjs.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesUtil {

	private Context mContext;
	private String table = "EB";
	public static String modPwdKey = "MODPWD";

	public PreferencesUtil(Context context) {
		super();
		mContext = context;
	}

	public String readSharedPreferencesString(String key) {
		// String strName,strPassword;
		SharedPreferences user = mContext.getSharedPreferences(table, 0);
		/*
		 * SharedPreferences支持string,int,float,long等
		 * 模式为私有(Context.MODE_PRIVATE)值0， 公开可读(Context.MODE_WORLD_READABLE)值1，
		 * 公开可写（Context.MODE_WORLD_WRITEABLE）值2 或者某种组合 追加形式(Context.MODE_APPEND)
		 */
		return user.getString(key, "");
		// strPassword = user.getString("PASSWORD","");
	}

	public int readSharedPreferencesInt(String key) {
		SharedPreferences user = mContext.getSharedPreferences(table, 0);
		return user.getInt(key, 0);
	}
	
	public int readSharedPreferencesInt(String key,int defalutValue) {
		SharedPreferences user = mContext.getSharedPreferences(table, 0);
		return user.getInt(key, defalutValue);
	}

	public long readSharedPreferencesLong(String key) {
		SharedPreferences user = mContext.getSharedPreferences(table, 0);
		return user.getLong(key, 0);
	}

	public void writeSharedPreferences(String strKey, String keyValue) {
		SharedPreferences.Editor user = mContext.getSharedPreferences(table, 0)
				.edit();
		user.putString(strKey, keyValue);
		user.commit();
	}

	public void writeSharedPreferences(String strKey, int keyValue) {
		SharedPreferences.Editor user = mContext.getSharedPreferences(table, 0)
				.edit();
		user.putInt(strKey, keyValue);
		user.commit();
	}

	public void writeSharedPreferences(String strKey, long keyValue) {
		SharedPreferences.Editor user = mContext.getSharedPreferences(table, 0)
				.edit();
		user.putLong(strKey, keyValue);
		user.commit();
	}

	public void clearSharedPreferences() {
		SharedPreferences.Editor user = mContext.getSharedPreferences(table, 0)
				.edit();
		user.clear();
		user.commit();
	}

	public void removeSharedPreferences(String strKey) {
		SharedPreferences.Editor user = mContext.getSharedPreferences(table, 0)
				.edit();
		// user.remove(strKey);
		user.putString(strKey, "");
		user.commit();
	}

}
