package com.chinamobile.hdcjs.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.chinamobile.hdcjs.database.LoginInfoDBHelper;

public class UserInfoProvider extends ContentProvider {
	public static final String TAG = UserInfoProvider.class.getSimpleName();

	private UserInfoDBhelper  mUserInfoDBhelper;
	private SQLiteDatabase db;

	private static final UriMatcher sMatcher;
	static {
		sMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sMatcher.addURI(UserInfo.AUTOHORITY, UserInfo.TNAME, UserInfo.ITEM);
		sMatcher.addURI(UserInfo.AUTOHORITY, UserInfo.TNAME + "/#",
				UserInfo.ITEM_ID);

	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		db =  mUserInfoDBhelper.getWritableDatabase();
		int count = 0;
		switch (sMatcher.match(uri)) {
		case UserInfo.ITEM:
			count = db.delete(UserInfo.TNAME, selection, selectionArgs);
			break;
		case UserInfo.ITEM_ID:
			String id = uri.getPathSegments().get(1);
			 count = db.delete(UserInfo.TID,
			 UserInfo.TID+"="+id+(!TextUtils.isEmpty(UserInfo.TID="?")?"AND("+selection+')':""),
			 selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI" + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		switch (sMatcher.match(uri)) {
		case UserInfo.ITEM:
			return UserInfo.CONTENT_TYPE;
		case UserInfo.ITEM_ID:
			return UserInfo.CONTENT_ITEM_TYPE;
		default:
			throw new IllegalArgumentException("Unknown URI" + uri);
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub

		db =  mUserInfoDBhelper.getWritableDatabase();
		long rowId;
		if (sMatcher.match(uri) != UserInfo.ITEM) {
			throw new IllegalArgumentException("Unknown URI" + uri);
		}
		rowId = db.insert(UserInfo.TNAME, UserInfo.TID, values);
		if (rowId > 0) {
			Uri noteUri = ContentUris.withAppendedId(UserInfo.CONTENT_URI,
					rowId);
			getContext().getContentResolver().notifyChange(noteUri, null);
			return noteUri;
		}
		throw new IllegalArgumentException("Unknown URI" + uri);
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		this. mUserInfoDBhelper = new UserInfoDBhelper(this.getContext());
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
//		Log.d(TAG, "*********query***************");		
//		
//		int isLogin = LoginInfoDBHelper.getCurrentLoginStatus(getContext());
//		//如果未登录，直接将数据库置空
//		if(isLogin==LoginInfoDBHelper.LOGINSTATUS_OFF){			
//			//Log.d(TAG, "未登录");
//			
//		//如果已登录，并且不是服务密码登录，则读取sso存储的临时token存到数据库
//		}else if(isLogin==LoginInfoDBHelper.LOGINSTATUS_ON && 
//				LoginInfoDBHelper.getCurrentLoginType(getContext())!=LoginInfoDBHelper.LOGINTYPE_SERVICE){
//						
//			String token = null;
//			//UserInfoDBhelper.save(getContext(),token, "", "", "");	
//			Log.d(TAG, "token="+token);
//		}		
						
		db =  mUserInfoDBhelper.getWritableDatabase();	
		Cursor c;
		Log.d(TAG, String.valueOf(sMatcher.match(uri)));
		switch (sMatcher.match(uri)) {
		case UserInfo.ITEM:
			c = db.query(UserInfo.TNAME, projection, selection, selectionArgs,
					null, null, sortOrder); 

			break;
		case UserInfo.ITEM_ID:
			String id = uri.getPathSegments().get(1);
			c = db.query(UserInfo.TNAME, projection, UserInfo.TID
					+ "="
					+ id
					+ (!TextUtils.isEmpty(selection) ? "AND(" + selection + ')'
							: ""), selectionArgs, null, null, sortOrder);
			break;
		default:
			Log.d(TAG, "Unknown URI = " + uri);
			throw new IllegalArgumentException("Unknown URI" + uri);
		}
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}
}
