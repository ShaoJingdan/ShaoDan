package com.chinamobile.hdcjs.aidl;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.chinamobile.hdcjs.aidl.IUserInfoService.Stub;
import com.chinamobile.hdcjs.database.LoginInfoDBHelper;
import com.chinamobile.hdcjs.util.Constants;
import com.cmcc.sso.sdk.auth.AuthnHelper;
import com.cmcc.sso.sdk.auth.TokenListener;
import com.cmcc.sso.sdk.util.SsoSdkConstants;


public class UserInfoService extends Service {
	
	private String TAG = "UserInfoService";
	private final String TOKEN_ACTION = "com.chinamobile.hdcjs.aidl.token";
	private AuthnHelper mAuthnHelper;
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		mAuthnHelper = new AuthnHelper(getApplicationContext());
		mAuthnHelper.setDefaultUI(false);
		
		/******************************************
		 * 开发调试时不验证apk签名，正式发布版本时需要验证签名
		 * true为测试模式，不验证签名
		 * 正式发布时要设为false
		 * ****************************************/
		//测试环境，不验证签名
		//mAuthnHelper.setTest(true);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}
		
	private IUserInfoService.Stub stub = new IUserInfoService.Stub() {
		
		@Override
		public void getToken(String packageName) throws RemoteException {
			Log.d(TAG, "getToken"+packageName);
			
			int isLogin = LoginInfoDBHelper.getCurrentLoginStatus(getApplicationContext());
			//如果未登录，直接将数据库置空
			if(isLogin==LoginInfoDBHelper.LOGINSTATUS_OFF){			
				Log.d(TAG, "未登录");
				
				sendTokenBroadcast("");			
				
			//如果已登录，并且不是服务密码登录，则读取sso存储的临时token存到数据库
			}else if(isLogin==LoginInfoDBHelper.LOGINSTATUS_ON && 
					LoginInfoDBHelper.getCurrentLoginType(getApplicationContext())!=LoginInfoDBHelper.LOGINTYPE_SERVICE){
				Log.d(TAG, "sso 登录");
				getSSOToken();	
				
			}else{
				Log.d(TAG, "service 登录");
				getServiceToken();
			}
			
		}
	};
	
	
	private void getServiceToken(){
		
		String token = UserInfoUtil.getUserToken(this);
		Log.i(TAG, "token="+token);
		
		sendTokenBroadcast(token);
		
	}
	
	private void getSSOToken(){
				
		String appid = Constants.APPID;
		String appkey = Constants.APPKEY;
		String username = LoginInfoDBHelper.getLastLoginUsername(this);
		Log.i(TAG, "getSSOToken appid="+appid+",appkey="+appkey+",username="+username);
		
//		mAuthnHelper.getAccessTokenByCondition(appid, appkey, SsoSdkConstants.AUTHN_ACCOUNT_PASSWORD, username, "23822359@ltsh", new TokenListener() {
//			
//			@Override
//			public void onGetTokenComplete(JSONObject result) {
//				Log.i(TAG, "onGetTokenComplete result="+result);
//				int resultCode;
//				String token = "";
//				try {
//					resultCode = result.getInt("resultCode");
//					if (resultCode == 102000) {
//						token = result.getString("token");
//		
//					} 					
//					
//				} catch (JSONException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}finally{
//					sendTokenBroadcast(token);
//				}
//
//			}
//		});

		
		
		mAuthnHelper.getAccessToken(appid, appkey, username, SsoSdkConstants.LOGIN_TYPE_DEFAULT, new TokenListener() {
			
			@Override
			public void onGetTokenComplete(JSONObject result) {
				Log.i(TAG, "onGetTokenComplete result="+result);
				int resultCode;
				String token = "";
				try {
					resultCode = result.getInt("resultCode");
					if (resultCode == 102000) {
						token = result.getString("token");
		
					} 					
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					sendTokenBroadcast(token);
				}

			}
		});

	}
	
	private void sendTokenBroadcast(String token){
		Log.i(TAG, "--------------sendTokenBroadcast----------- token" + token);
		Intent intent = new Intent(TOKEN_ACTION);
		//通过Intent携带消息 
		intent.putExtra("token", token);
		//发送广播消息：
		sendBroadcast(intent);
	}
	

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub

		Log.i(TAG, "onBind" + (stub == null ?"null":"not null"));
		return stub;
		
		
	}

}
