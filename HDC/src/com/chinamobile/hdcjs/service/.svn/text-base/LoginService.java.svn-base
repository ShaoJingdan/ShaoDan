package com.chinamobile.hdcjs.service;

import hop.client.service.rest.HopTerminalClient;
import hop.client.service.xmlobject.AuthenticationResponse;
import hop.client.service.xmlobject.AuthenticationTokenResponse;
import hop.client.service.xmlobject.ServiceException;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.SystemInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.chinamobile.hdcjs.R;
import com.chinamobile.hdcjs.database.LoginInfoDBHelper;
import com.chinamobile.hdcjs.interfaces.LoginTaskResponse;
import com.chinamobile.hdcjs.provider.UserInfoDBhelper;
import com.chinamobile.hdcjs.receiver.BootReceiver;
import com.chinamobile.hdcjs.ui.AuthorizeActivity;
import com.chinamobile.hdcjs.util.Constants;
import com.chinamobile.hdcjs.util.ShortCut;
import com.cmcc.sso.sdk.auth.AuthnHelper;
import com.cmcc.sso.sdk.auth.TokenListener;
import com.cmcc.sso.sdk.util.SsoSdkConstants;

/**
 * 首次、定时自动登录服务
 */
public class LoginService extends Service {

	public static Context loginServiceContext = null;
	private static final String TAG = "LoginService";

	private static final String SSO_APPID = Constants.APPID;
	private static final String SSO_APPKEY = Constants.APPKEY;
	
	private Handler uiHandler = null;
	
	private String username;
	private String password;
	private int type;
	
	private Handler handler;
	
	private static int failedCounter = 0;
	
	private AuthnHelper authnHelper;
	
	@Override
	public void onCreate() {
		authnHelper = new AuthnHelper(LoginService.this);
		authnHelper.setDefaultUI(false);
		
		/******************************************
		 * 开发调试时不验证apk签名，正式发布版本时需要验证签名
		 * true为测试模式，不验证签名
		 * 正式发布时要设为false
		 * ****************************************/
		//authnHelper.setTest(false);
		//authnHelper.setTest(true);
		
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 0) {
					
					showLongToast(msg.getData().getString("toast_string"));
				}
				super.handleMessage(msg);
			}
		};
		super.onCreate();
		
		
	}

	private final IBinder binder = new MyBinder();	
	
	@Override
	public IBinder onBind(Intent arg0) {
		return binder;
	}
	
	public class MyBinder extends Binder {
		public LoginService getService()
		{
			return LoginService.this;
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "LoginService onStartCommand...");
//
//		loginServiceContext = LoginService.this;
//
//		// 读取配置文件，获取token过期时间
//		String expire = new PropertiesUtil(LoginService.this).get(
//				"config.properties", "token_expire");
//
//		// 启动周期任务，执行周期登陆
//		ScheduledExecutorService service = Executors
//				.newSingleThreadScheduledExecutor();
//		Runnable command = new Runnable() {
//			@Override
//			public void run() {
//				LoginTask loginTask = new LoginTask(LoginService.this,
//						LoginTask.FROM_SERVICE);
//				loginTask.execute();
//			}
//		};
//
//		// 开始执行周期任务
//		long initialDelay = 0;
//		if (intent == null) {
//			initialDelay = 0;
//		} else if (intent.getExtras() == null) {
//			initialDelay = 0;
//		} else 	if (intent.getExtras().getBoolean("startNow")){
//			initialDelay = 0;
//		} else {
//			initialDelay = Integer.valueOf(expire);
//		}
//		service.scheduleAtFixedRate(command, initialDelay, Integer.valueOf(expire),
//				TimeUnit.MINUTES);
		
		if (intent != null) {
			String operation = intent.getExtras().getString("operation");

			if ("logout".equals(operation)) {
				// 注销流程
				logout();
			} else if ("login".equals(operation)) {
				// 登录流程
				int type = intent.getExtras().getInt("login_type");
				String tmpUsername = intent.getExtras().getString("username");
				String tmpPassword = intent.getExtras().getString("password");
				if (tmpUsername == null || "".equals(tmpUsername)) {
					// 用户名密码不变更的自动登录流程
					login(type);
				} else {
					// 用户名密码变更的手动登录流程
					login(type, tmpUsername, tmpPassword);
				}
			}
		}

		return START_REDELIVER_INTENT;
	}

	@Override
	public void onDestroy() {
//		Intent service = new Intent(this, LoginService.class);
//		startService(service);
//		Log.i(TAG, "服务Destroy后自动重启...");
		super.onDestroy();
	}

	public void login(int type) {
		Log.i(TAG, "login type="+type);
		LoginInfoDBHelper.setCurrentLoginType(this, type);
		
		// 数据区获取用户名密码数据
		this.username = LoginInfoDBHelper.getLastLoginUsername(this);
		this.password = LoginInfoDBHelper.getLastLoginPassword(this);
		this.type = type;
		Log.i(TAG, "login username="+username+",password="+password);
		
		switch (type) {
		case LoginInfoDBHelper.LOGINTYPE_SERVICE:
			BootReceiver.needClean = false;
			new Thread(new ServiceLoginRunnable()).start();
			break;
		case LoginInfoDBHelper.LOGINTYPE_SSO_PASSWORD:
			BootReceiver.needClean = false;
			new Thread(new SsoPasswordLoginRunnable()).start();
			break;
		case LoginInfoDBHelper.LOGINTYPE_SSO_SMS:
			BootReceiver.needClean = false;
			new Thread(new SsoSMSLoginRunnable()).start();
			break;
		case LoginInfoDBHelper.LOGINTYPE_LOGOUT:
			break;
		}
	}
	
	public void login(int type, String username, String password) {
		
		Log.i(TAG, "login type="+type+",username="+username+",password="+password);
		
		LoginInfoDBHelper.setCurrentLoginType(this, type);
		
		int status = LoginInfoDBHelper.getCurrentLoginStatus(this);
		if (status == LoginInfoDBHelper.LOGINSTATUS_ON) {
			logout();
		}
		
		this.username = username;
		this.password = password;
		this.type = type;
		
		switch (type) {
		case LoginInfoDBHelper.LOGINTYPE_SERVICE:
			BootReceiver.needClean = false;
			new Thread(new ServiceLoginRunnable()).start();
			break;
		case LoginInfoDBHelper.LOGINTYPE_SSO_PASSWORD:
			BootReceiver.needClean = false;
			new Thread(new SsoPasswordLoginRunnable()).start();
			break;
		case LoginInfoDBHelper.LOGINTYPE_SSO_SMS:
			BootReceiver.needClean = false;
			new Thread(new SsoSMSLoginRunnable()).start();
			break;
		case LoginInfoDBHelper.LOGINTYPE_LOGOUT:
			break;
		}
	}
	
	public void getSmsCode(String username){
		Log.i(TAG, "getSmsCode username="+username);
		authnHelper.getSmsCode(SSO_APPID, SSO_APPKEY, username, SsoSdkConstants.BUSI_TYPE_SMSLOGIN, smsCodeListener);
	}
	
	private TokenListener smsCodeListener = new TokenListener() {
		
		@Override
		public void onGetTokenComplete(JSONObject result) {
			Log.i(TAG, "smsCodeListener onGetTokenComplete result="+result);

			int resultCode;
			try {
				resultCode = result.getInt("resultCode");
				if (resultCode == 102000) {
					Log.i(TAG, "----------getsmscode成功-----------");
					smsCodeHandler.sendEmptyMessage(0);
				}else{
					Log.i(TAG, "----------getsmscode失败-----------");
					smsCodeHandler.sendEmptyMessage(1);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	};
	
	/**
	 * 实现handleMessage()方法，用于接收Message，刷新UI
	 */
	private Handler smsCodeHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			
			Log.i(TAG, "smsCodeHandler msg.what="+msg.what);
			switch (msg.what) {
			//刷新倒计时计数
			case 0:
				Toast.makeText(getApplicationContext(), "短信密码已发送", Toast.LENGTH_SHORT).show();
				break;
			case 1:
				Toast.makeText(getApplicationContext(), "短信密码发送失败，请稍后重试", Toast.LENGTH_SHORT).show();
				break;

			default:
				
			}

			super.handleMessage(msg);

		}
	};
	
	private TokenListener cleanSSOListener = new TokenListener() {
		
		@Override
		public void onGetTokenComplete(JSONObject result) {
			Log.i(TAG, "cleanSSO onGetTokenComplete result="+result);

			int resultCode;
			try {
				resultCode = result.getInt("resultCode");
				if (resultCode == 102000) {
					Log.i(TAG, "----------cleanSSO成功-----------");
					
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	};
	
	public void logout() {
		
		int loginType = LoginInfoDBHelper.getLastLoginType(this);
		if (loginType == LoginInfoDBHelper.LOGINTYPE_SSO_PASSWORD ||
				loginType == LoginInfoDBHelper.LOGINTYPE_SSO_SMS) {
			// 调用sso中间件的注销接口
			authnHelper.cleanSSO(cleanSSOListener);
			
		}
		
		closeLoginTimer();
		
		// 删除本地数据库的相关数据
		UserInfoDBhelper.save(this, "", "", "", "");
		
		// 删除数据区的数据
		LoginInfoDBHelper.clearUserAndPassword(this);
		
		// 标记当前为未登录状态
		LoginInfoDBHelper.setCurrentLoginStatus(this, LoginInfoDBHelper.LOGINSTATUS_OFF);
		
		// 发送系统注销事件
		Intent bintent = new Intent("cn.10086.action.HDC_LOGIN_STATUS_UPDATE");
		bintent.putExtra("status", "offline"); // 填写online或offline表示登录成功和下线。
		LoginService.this.sendBroadcast(bintent);
		
	}
	
	/**
	 * 登录Activity传入handler，用于线程中必要事件反馈给UI
	 * @param handler
	 */
	public void setHandler(Handler handler) {
		uiHandler = handler;
	}
	
	
	/**
	 * 业务密码登录线程
	 */
	class ServiceLoginRunnable implements Runnable {
		
		@Override
		public void run() {
			try {
				// 调用终端客户端服务
				HopTerminalClient client = ShortCut.getHopTerminalClientImplInstance(LoginService.this);
				Object resultObj = client.stbLogin(
						username,
						password,
						3 + "",
						SystemInfo.getSTBSN(LoginService.this));
				
				if (resultObj != null) {
					
					Log.i(TAG, "resultObj != null");
					if (resultObj instanceof AuthenticationResponse) {
						Log.i(TAG, "resultObj instanceof AuthenticationResponse");
						// 失败重试计数器归零
						failedCounter = 0;
						
						// Token信息保存到数据库，供第三方调用
						// sessionID等信息保存到数据库，供ContentProvider调用
						AuthenticationResponse response = (AuthenticationResponse) resultObj;
						String token = response.getCmtokenId();
						String phone = response.getPhoneNumber();
						String sessionId = response.getSessionId();
						String imageUrl = response.getAvatarUrl();
						Log.i(TAG, "token="+token+",phone="+phone);
						UserInfoDBhelper.save(LoginService.this, token, phone, sessionId, imageUrl);
						
						// 记录当前登录状态为已登录状态
						LoginInfoDBHelper.setCurrentLoginStatus(LoginService.this, LoginInfoDBHelper.LOGINSTATUS_ON);
						
						// 通知登录窗体登录成功
						if (uiHandler != null) {
							LoginInfoDBHelper.saveServiceUserPassword(LoginService.this, username, password);

							Message msg = uiHandler.obtainMessage();
							msg.what = 0;
							uiHandler.sendMessage(msg);
						}
						
						// 发送登录成功广播
						Intent bintent = new Intent("cn.10086.action.HDC_LOGIN_STATUS_UPDATE");
						bintent.putExtra("status", "online"); // 填写online或offline表示登录成功和下线。
						LoginService.this.sendBroadcast(bintent);
						
						// 开启报活定时器
						openLoginTimer();
					} else if (resultObj instanceof ServiceException) {
						Log.i(TAG, "resultObj instanceof ServiceException");
						if (failedCounter == 3) {
							Log.i(TAG, "failedCounter"+failedCounter);
							failedCounter = 0;
							String errorCode = ((ServiceException)resultObj).getErrorcode();
							handleHopErrorResult(errorCode);
						} else {
							failedCounter++;
							new Thread(new ServiceLoginRunnable()).start();
						}
						
					} else {
						Log.d(TAG, "登录返回对象类型异常！");
					}
				}
			} catch (Exception e) {
				Log.e(TAG, "登录失败:" + e.getMessage());
			}
		}

	};
	
	/**
	 * 统一认证平台固定密码登录线程
	 */
	class SsoPasswordLoginRunnable implements Runnable {
		
				
		@Override
		public void run() {
			if (password != null && !"".equals(password)) {
				Log.i(TAG, "---------getAccessTokenByCondition-----------");
				authnHelper.getAccessTokenByCondition(SSO_APPID, SSO_APPKEY, SsoSdkConstants.AUTHN_ACCOUNT_PASSWORD, username, password, getAccessTokenByConditionListener);
			} else {
				Log.i(TAG, "----------getAccessToken------------");
				authnHelper.getAccessToken(SSO_APPID, SSO_APPKEY, username, SsoSdkConstants.LOGIN_TYPE_DEFAULT, getAccessTokenListener);
			}
		}
		
		TokenListener getAccessTokenListener = new TokenListener() {
			
			@Override
			public void onGetTokenComplete(JSONObject result) {
				Log.i(TAG, "getAccessTokenListener onGetTokenComplete result="+result);

				int resultCode;
				try {
					resultCode = result.getInt("resultCode");
					if (resultCode == 102000) {
						String token = result.getString("token");
						getTokenSuccess(token);
					} else {
						authnHelper.getAccessTokenByCondition(SSO_APPID, SSO_APPKEY, SsoSdkConstants.AUTHN_ACCOUNT_PASSWORD, username, password, getAccessTokenByConditionListener);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};
		
		private TokenListener getAccessTokenByConditionListener = new TokenListener() {
			
			@Override
			public void onGetTokenComplete(JSONObject result) {
				Log.i(TAG, "getAccessTokenByConditionListener onGetTokenComplete result="+result);

				int resultCode;
				try {
					resultCode = result.getInt("resultCode");
					if (resultCode == 102000) {
						String token = result.getString("token");
						getTokenSuccess(token);
					} else {
						// 记录当前登录状态为未登录状态
						LoginInfoDBHelper.setCurrentLoginStatus(LoginService.this, LoginInfoDBHelper.LOGINSTATUS_OFF);
						
						handleTokenErrorResult(result);
						
						// 非UI发起的登录
						if (uiHandler == null) {
							// 开启报活定时器
							openLoginTimer();
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		};
		


	}
	

	/**
	 * 统一认证平台短信动态密码登录线程
	 */
	class SsoSMSLoginRunnable implements Runnable {
		
		@Override
		public void run() {
			if (password != null && !"".equals(password)) {
				authnHelper.getAccessTokenByCondition(SSO_APPID, SSO_APPKEY, SsoSdkConstants.AUTHN_ACCOUNT_SMSCODE, username, password, getAccessTokenByConditionListener);
			} else {
				authnHelper.getAccessToken(SSO_APPID, SSO_APPKEY, username, SsoSdkConstants.LOGIN_TYPE_DEFAULT, getAccessTokenListener);
			}
		}
		
		private TokenListener getAccessTokenListener = new TokenListener() {
			
			@Override
			public void onGetTokenComplete(JSONObject result) {
				int resultCode;
				try {
					resultCode = result.getInt("resultCode");
					if (resultCode == 102000) {
						String token = result.getString("token");
						getTokenSuccess(token);
					} else {
						
						authnHelper.getAccessToken(SSO_APPID, SSO_APPKEY, username, SsoSdkConstants.LOGIN_TYPE_DEFAULT, getAccessTokenByConditionListener);
						//authnHelper.getAccessTokenByCondition(SSO_APPID, SSO_APPKEY, SsoSdkConstants.AUTHN_ACCOUNT_SMSCODE, username, password, getAccessTokenByConditionListener);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		
		private TokenListener getAccessTokenByConditionListener = new TokenListener() {
			
			@Override
			public void onGetTokenComplete(JSONObject result) {
				
				int resultCode;
				try {
					resultCode = result.getInt("resultCode");
					if (resultCode == 102000) {
						String token = result.getString("token");
						getTokenSuccess(token);
					} else {
						// 记录当前登录状态为未登录状态
						LoginInfoDBHelper.setCurrentLoginStatus(LoginService.this, LoginInfoDBHelper.LOGINSTATUS_OFF);
						
						handleTokenErrorResult(result);
						
						// 非UI发起的登录
						if (uiHandler == null) {
							// 开启报活定时器
							openLoginTimer();
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};

	}
	
	/**
	 * 通过SSO获取失败时统一逻辑处理方法，实现Toast和UI上报
	 * @param result
	 */
	private void handleTokenErrorResult(JSONObject result) {
		Log.i(TAG, "result"+result);
		String errorString = "";
		try {
			int resultCode = result.getInt("resultCode");
			errorString = result.getString("resultString");
			
			if(resultCode!=102000){
				authnHelper.cleanSSO(cleanSSOListener);
			}
			
			switch (resultCode) {
//			case 102000:
//				// 正确返回
//				break;
				
			case 102101:
				// 无网络
				errorString = this.getResources().getString(R.string.alert_login_network_error);
				break;
				
			case 102102:
				// 网络连接超时（根据Server设置变化）
				errorString = this.getResources().getString(R.string.alert_login_network_timeout);
				break;
				
			case 102201:
				// 自动登陆失败，用户选择自定义界面时，需要继续处理手动登陆流程，比如弹出登陆界面。
				errorString = this.getResources().getString(R.string.alert_login_sso_failed);
				break;
				
			case 102202:
				// APP签名验证不通过
				errorString = this.getResources().getString(R.string.alert_login_sso_sig_error);
				break;
				
			case 102203:
				// 接口入参错误
				errorString = this.getResources().getString(R.string.alert_login_sso_para_error);
				break;
				
			case 102204:
				// 正在进行GetToken动作，请稍后
				errorString = this.getResources().getString(R.string.alert_login_sso_try_later);
				break;
			
			case 102205:
				// 当前环境不支持指定的登陆方式
				errorString = this.getResources().getString(R.string.alert_login_sso_type_notsupport);
				break;
				
			case 102206:
				// 选择用户登陆时，本地不存在指定的用户
				errorString = this.getResources().getString(R.string.alert_login_sso_user_not_exsit);
				break;
				
			case 102299:
				// 其他错误
				errorString = this.getResources().getString(R.string.alert_login_sso_other_failed);
				break;
				
			case 102301:
				// 用户取消
				errorString = this.getResources().getString(R.string.alert_login_sso_user_cancelled);
				break;
			}
			
			Message msg = handler.obtainMessage();
			msg.what = 0;
			Bundle bundle = new Bundle();
			bundle.putString("toast_string", errorString);
			//Log.i(TAG, "errorString"+errorString);
			msg.setData(bundle);
			handler.sendMessage(msg);
			
			if (uiHandler != null) {
				Message msg1 = uiHandler.obtainMessage();
				msg1.what = 1;
				Bundle bundle1 = new Bundle();
				bundle1.putString("errorcode", "" + resultCode);
				//Log.i(TAG, "errorcode"+resultCode);
				msg1.setData(bundle1);
				uiHandler.sendMessage(msg1);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 通过Hop接口处理登录逻辑失败时统一处理，实现Toast和UI上报
	 * @param result
	 */
	private void handleHopErrorResult(String result) {
		
		String errorString = "";
		
		if (result.equals(LoginTaskResponse.MSG_ACCOUNT_ERROR)) {
			// 用户账号输入错误
			errorString = this.getResources().getString(
					R.string.alert_login_phonenum_error);
		} else if (result.equals(LoginTaskResponse.MSG_PASSWORD_ERROR_BOX)
				|| result.equals(LoginTaskResponse.MSG_PASSWORD_ERROR_PHONE)) {
			// 用户密码输入错误
			if (uiHandler == null) {
				errorString = this.getResources().getString(
						R.string.alert_login_pass_changed);
			} else {
				errorString = this.getResources().getString(
						R.string.alert_login_pass_error);
			}
		} else if (result
				.equals(LoginTaskResponse.MSG_CONNECT_EXCPTION)) {
			// 网络连接失败
			errorString = this.getResources().getString(R.string.alert_login_network_error);
		} else if (result
				.equals(LoginTaskResponse.MSG_CONNECTTIMEOUT_EXCPTION)) {
			// 网络连接超时
			errorString = this.getResources().getString(R.string.alert_login_network_timeout);
		} else if (result
				.equals(LoginTaskResponse.MSG_ACCOUNT_LOCKED)) {
			// 用户账户被锁
			errorString = this.getResources().getString(R.string.alert_login_account_locked);
		} else {
			// 其他错误
			errorString = this.getResources().getString(R.string.alert_login_failed);
		}
		
		Message msg = handler.obtainMessage();
		msg.what = 0;
		Bundle bundle = new Bundle();
		bundle.putString("toast_string", errorString);
		msg.setData(bundle);
		//Log.i(TAG, "msg.getData()="+msg.getData());
		handler.sendMessage(msg);
		
		if (uiHandler != null) {
			Message msg1 = uiHandler.obtainMessage();
			msg1.what = 1;
			Bundle bundle1 = new Bundle();
			bundle1.putString("errorcode", result);
			//Log.i(TAG, "errorcode"+result);
			msg1.setData(bundle1);
			//Log.i(TAG, "msg1.getData()="+msg1.getData());
			
			uiHandler.sendMessage(msg1);
		}
	}
	
	/**
	 * 通过统一认证平台成功获取Token后的处理逻辑
	 * @param token
	 */
	private void getTokenSuccess(final String token) {
		Log.i(TAG, "getTokenSuccess:"+token);
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
							
				// 调用Hop接口获取SessionID
				HopTerminalClient client = ShortCut.getHopTerminalClientImplInstance(LoginService.this);
				Object resultObj = client.getUserInfo(token);
				
				if (resultObj instanceof AuthenticationTokenResponse) {
					//Log.i(TAG,"resultObj instanceof AuthenticationTokenResponse");
					// 如果成功保存SessionID等信息
					AuthenticationTokenResponse response = (AuthenticationTokenResponse) resultObj;
//					String token = response.getCmtokenId();
					String phone = response.getPhoneNumber();
					String sessionId = response.getSessionId();
					String imageUrl = response.getAvatarUrl();
					
					UserInfoDBhelper.save(LoginService.this, token, phone, sessionId, imageUrl);
					
					// 记录当前登录状态为已登录状态
					LoginInfoDBHelper.setCurrentLoginStatus(LoginService.this, LoginInfoDBHelper.LOGINSTATUS_ON);
					
					// 通知登录窗体登录成功
					if (uiHandler != null) {
						
						if(type==LoginInfoDBHelper.LOGINTYPE_SSO_PASSWORD){
							LoginInfoDBHelper.saveSSOUserPassword(LoginService.this, username, password);
						}else{
							LoginInfoDBHelper.saveSSOUserPassword(LoginService.this, username, "");
						}
												
						Message msg = uiHandler.obtainMessage();
						msg.what = 0;
						uiHandler.sendMessage(msg);
					}
					
					// 发送登录成功广播
					Intent bintent = new Intent("cn.10086.action.HDC_LOGIN_STATUS_UPDATE");
					bintent.putExtra("status", "online"); // 填写online或offline表示登录成功和下线。
					LoginService.this.sendBroadcast(bintent);
					
					// 开启报活定时器
					openLoginTimer();
				} else if (resultObj instanceof ServiceException) {
					//Log.i(TAG,"resultObj instanceof ServiceException");
					// 记录当前登录状态为未登录状态
					LoginInfoDBHelper.setCurrentLoginStatus(LoginService.this, LoginInfoDBHelper.LOGINSTATUS_OFF);
					
					String errorCode = ((ServiceException)resultObj).getErrorcode();
					handleHopErrorResult(errorCode);
					
					// 非UI发起的登录
					if (uiHandler == null) {
						// 开启报活定时器
						openLoginTimer();
					}
				}
				
				//Log.i(TAG,"resultObj"+resultObj);
				
			}
		}).start();
		
		

	
	}
	
	/**
	 * 弹出Toast提示
	 * @param msg
	 */
	private void showLongToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}
	
	/**
	 * 开启报活定时器
	 */
	private void openLoginTimer() {
		Intent intent = new Intent("com.chinamobile.hdcjs.service.LoginService");
		intent.putExtra("operation", "login");
		intent.putExtra("login_type", LoginInfoDBHelper.getCurrentLoginType(this));
		PendingIntent pi = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
		am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 60*60*1000, pi);
	}
	
	/**
	 * 关闭报活定时器
	 */
	private void closeLoginTimer() {
		Intent intent = new Intent("com.chinamobile.hdcjs.service.LoginService");
		intent.putExtra("operation", "login");
		intent.putExtra("login_type", LoginInfoDBHelper.getCurrentLoginType(this));
		PendingIntent pi = PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
		am.cancel(pi);
	}

	/**
	 * 本方法判断com.chinamobile.hdcjs.service.LoginService是否已经运行
	 * 
	 * @return
	 */
	public static boolean isLoginServiceWorked(Context context) {
		ActivityManager myManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
				.getRunningServices(30);
		for (int i = 0; i < runningService.size(); i++) {
			if (runningService.get(i).service.getClassName().toString()
					.equals("com.chinamobile.hdcjs.service.LoginService")) {
				return true;
			}
		}
		return false;
	}

}
