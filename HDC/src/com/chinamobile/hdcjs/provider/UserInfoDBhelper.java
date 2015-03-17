package com.chinamobile.hdcjs.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserInfoDBhelper extends SQLiteOpenHelper {
	public static final String TAG = UserInfoDBhelper.class.getSimpleName();
	
	/**
	 * 判断是否处于登录状态
	 */
//	public static boolean ISLOGIN = false;
	
	public UserInfoDBhelper(Context context) {
		super(context, UserInfo.DBNAME, null, UserInfo.VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(TAG, "onCreate");
		db.execSQL("create table " + UserInfo.TNAME + "(" + UserInfo.TID
				+ " integer primary key autoincrement not null,"
				+ UserInfo.TOKEN + " text," + UserInfo.PHONE
				+ " text not null," + UserInfo.SESSION_ID + " text,"
				+ UserInfo.IMAGE_URL + " text);");
		
		ContentValues values = new ContentValues();
		values.put(UserInfo.TOKEN, "");
		values.put(UserInfo.PHONE, "");
		values.put(UserInfo.SESSION_ID, "");
		values.put(UserInfo.IMAGE_URL, "");
		db.insert(UserInfo.TNAME, "", values);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onUpgrade");
	}

	/**
	 * 插入一条记录
	 * 
	 * @param token
	 * @param phone
	 * @param sessionid
	 * @param imageurl
	 */
//	public synchronized void insert(String token, String phone, String sessionid,
//			String imageurl) {
//		SQLiteDatabase db = getWritableDatabase();
//		String[] args = { String.valueOf("1") };
//		Cursor c = db.query(UserInfo.TNAME, new String[] { UserInfo.TOKEN,
//				UserInfo.PHONE, UserInfo.SESSION_ID, UserInfo.IMAGE_URL },
//				UserInfo.TID+"=?", args, null, null, null, null);
//        
//		if (c == null || c.getCount()==0) {
//			Log.d(TAG,"cursor is null,insert item...");
//			ContentValues values = new ContentValues();
//			values.put(UserInfo.TOKEN, token);
//			values.put(UserInfo.PHONE, phone);
//			values.put(UserInfo.SESSION_ID, sessionid);
//			values.put(UserInfo.IMAGE_URL, imageurl);
//			db.insert(UserInfo.TNAME, "", values);
//		} else {
//			Log.d(TAG,"cursor is not null,update item...");
//			if(c.moveToFirst()){
//			Log.d(TAG,"cursor.phone="+c.getString(c.getColumnIndex(UserInfo.PHONE)));}
//			update(token, phone,sessionid, imageurl);
//		}
//		db.close();
//		
//		if (phone == null || "".equals(phone))
//			UserInfoDBhelper.ISLOGIN = false;
//		else
//			UserInfoDBhelper.ISLOGIN = true;
//	}

	/**
	 * 更新数据
	 * @param token
	 * @param phone
	 * @param sessionid
	 * @param imageurl
	 */
//	public void update( String token, String phone, String sessionid,
//			String imageurl) {
//		Log.d(TAG,"update()...");
//		SQLiteDatabase db = getWritableDatabase();
//		ContentValues values = new ContentValues();
//		values.put(UserInfo.TOKEN, token);
//		values.put(UserInfo.PHONE, phone);
//		values.put(UserInfo.SESSION_ID, sessionid);
//		values.put(UserInfo.IMAGE_URL, imageurl);
//		String[] args = { String.valueOf("1") };
//		db.update(UserInfo.TNAME, values, UserInfo.TID + "=?", args);
//		db.close();
//	}
	
	
	public synchronized static void save(Context context, String token, String phone, String sessionid, String imageurl) {
		Log.i(TAG, "save token="+token+",phone="+phone);
		UserInfoDBhelper helper = new UserInfoDBhelper(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(UserInfo.TOKEN, token);
		values.put(UserInfo.PHONE, phone);
		values.put(UserInfo.SESSION_ID, sessionid);
		values.put(UserInfo.IMAGE_URL, imageurl);
		String[] args = { String.valueOf("1") };
		db.update(UserInfo.TNAME, values, UserInfo.TID + "=?", args);
		db.close();
		helper.close();
	}
}
