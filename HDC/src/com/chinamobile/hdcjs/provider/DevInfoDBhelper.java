package com.chinamobile.hdcjs.provider;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DevInfoDBhelper extends SQLiteOpenHelper {
	public static final String TAG = DevInfoDBhelper.class.getSimpleName();

	private static final String CREATE_TABLE = "create table " + DevInfo.TNAME + "(" + DevInfo.TID
			+ " integer primary key autoincrement not null,"
			+ DevInfo.STB_MAC + " text," + DevInfo.STB_SN + " text,"
			+ DevInfo.PLATFORM_URL + " text," + DevInfo.PLATFORM_URL_BACKUP
			+ " text," + DevInfo.CMS_URL + " text,"
			+ DevInfo.CMS_URL_BACKUP +  " text,"
			+ DevInfo.HDCR_URL +" text);";
	private static final String DELETE_TABLE = "delete from " + DevInfo.TNAME;
	private static String GET_ALL_ITEM = "select * from " + DevInfo.TNAME;
	
	public DevInfoDBhelper(Context context) {
		super(context, DevInfo.DBNAME, null, DevInfo.VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}

	public void query(int id) {

	}

	/**
	 * 插入一条记录
	 * 
	 * @param STBMAC
	 * @param STBSN
	 * @param PlatformURL
	 * @param PlatformURLBackup
	 * @param CMSURL
	 * @param CMSURLBackup
	 * @param HDCRURL
	 */
	public void insert(String STBMAC, String STBSN, String PlatformURL,
			String PlatformURLBackup, String CMSURL, String CMSURLBackup,String HDCRURL) {
		SQLiteDatabase db = getWritableDatabase();
		String[] args = { String.valueOf("1") };
		Cursor c = db.query(DevInfo.TNAME, new String[] { DevInfo.STB_MAC,
				DevInfo.STB_SN, DevInfo.PLATFORM_URL,
				DevInfo.PLATFORM_URL_BACKUP, DevInfo.CMS_URL,
				DevInfo.CMS_URL_BACKUP ,DevInfo.HDCR_URL}, DevInfo.TID + "=?", args, null, null,
				null, null);

		if (c == null || c.getCount() == 0) {
			Log.d(TAG, "cursor is null,insert item...");
			ContentValues values = new ContentValues();
			values.put(DevInfo.STB_MAC, STBMAC);
			values.put(DevInfo.STB_SN, STBSN);
			values.put(DevInfo.PLATFORM_URL, PlatformURL);
			values.put(DevInfo.PLATFORM_URL_BACKUP, PlatformURLBackup);
			values.put(DevInfo.CMS_URL, CMSURL);
			values.put(DevInfo.CMS_URL_BACKUP, CMSURLBackup);
			values.put(DevInfo.HDCR_URL, HDCRURL);
			db.insert(DevInfo.TNAME, "", values);
		} else {
			Log.d(TAG, "cursor is not null,update item...");
			if (c.moveToFirst()) {
				Log.d(TAG,
						"cursor.STBSN="
								+ c.getString(c.getColumnIndex(DevInfo.STB_SN)));
			}
			update(STBMAC, STBSN, PlatformURL, PlatformURLBackup, CMSURL,
					CMSURLBackup,HDCRURL);
		}
		db.close();
	}

	/**
	 * 更新数据
	 * 
	 * @param STBMAC
	 * @param STBSN
	 * @param PlatformURL
	 * @param PlatformURLBackup
	 * @param CMSURL
	 * @param CMSURLBackup
	 */
	public void update(String STBMAC, String STBSN, String PlatformURL,
			String PlatformURLBackup, String CMSURL, String CMSURLBackup, String HDCRURL) {
		Log.d(TAG, "update()...");
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DevInfo.STB_MAC, STBMAC);
		values.put(DevInfo.STB_SN, STBSN);
		values.put(DevInfo.PLATFORM_URL, PlatformURL);
		values.put(DevInfo.PLATFORM_URL_BACKUP, PlatformURLBackup);
		values.put(DevInfo.CMS_URL, CMSURL);
		values.put(DevInfo.CMS_URL_BACKUP, CMSURLBackup);
		values.put(DevInfo.HDCR_URL, HDCRURL);
		String[] args = { String.valueOf("1") };
		db.update(DevInfo.TNAME, values, DevInfo.TID + "=?", args);
		db.close();
	}
}
