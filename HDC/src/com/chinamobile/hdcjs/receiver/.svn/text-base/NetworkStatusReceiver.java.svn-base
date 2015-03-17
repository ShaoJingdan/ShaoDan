package com.chinamobile.hdcjs.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.util.Log;
import android.widget.Toast;

import com.chinamobile.hdcjs.database.LoginInfoDBHelper;
import com.chinamobile.hdcjs.service.LoginService;

/**
 * 网络状态广播，开启登录服务
 * 
 */
public class NetworkStatusReceiver extends BroadcastReceiver {

	public static final String TAG = NetworkStatusReceiver.class.getSimpleName();

//	private PreferencesUtil preferencesUtil = null;
//	/* 设备信息 */
//	private DevInfoManager mDevInfoManager;
	
	public static long timeStamp = 0;

	@Override
	public void onReceive(Context context, Intent mintent) {
		Log.i(TAG, "[登录服务]网络监听...");

//		preferencesUtil = new PreferencesUtil(context);
//		mDevInfoManager = (DevInfoManager) context
//				.getSystemService(DevInfoManager.DATA_SERVER);

		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager != null) {

			NetworkInfo[] networkInfos = connectivityManager
					.getAllNetworkInfo();
			for (int i = 0; i < networkInfos.length; i++) {
				State state = networkInfos[i].getState();
				if (NetworkInfo.State.CONNECTED == state) {
					Log.i(TAG, "网络连接正常");
					
					long tempTime = System.currentTimeMillis();
					Log.d(TAG, "当前时间：" + tempTime);
					if (tempTime - timeStamp <1000) {
						Log.d(TAG, "密集网络抖动事件！");
						timeStamp = tempTime;
						return;
					}
					
					timeStamp = tempTime;
					
					// Toast.makeText(context, "网络连接成功", Toast.LENGTH_SHORT).show();
//					if (isFactorySetting(context)) {
//						//如果恢复出厂设置了
//						Log.i(TAG, "恢复出场设置流程");
//						Toast.makeText(context, "恢复出场设置流程", Toast.LENGTH_SHORT).show();
						
						// 开启自动登录服务
						Intent intent = new Intent(context, LoginService.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_CLEAR_TOP);
						intent.putExtra("operation", "login");
						intent.putExtra("login_type", LoginInfoDBHelper.getLastLoginType(context));
						context.startService(intent);
//					} else {
//						if (isAutoLogin(context)) {
//							Log.i(TAG, "非恢复出厂设置自动登录流程");
////							Toast.makeText(context, "非恢复出厂设置自动登录流程", Toast.LENGTH_SHORT).show();
//							if (!LoginService.isLoginServiceWorked(context
//									.getApplicationContext())) {
//								// 开启自动登录服务
//								Intent intent = new Intent(context,
//										LoginService.class);
//								intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK
//										| Intent.FLAG_ACTIVITY_CLEAR_TOP);
//								intent.putExtra("startNow", true);
//								context.startService(intent);
//							} else {
//								LoginTask loginTask = new LoginTask(
//										LoginService.loginServiceContext,
//										LoginTask.FROM_SERVICE);
//								loginTask.execute();
//							}
//						} else {
//							// 如果是未勾选自动登录，则将空的登录信息写入到本地文件
//							Log.i(TAG, "非恢复出厂设置手动登录流程");
////							Toast.makeText(context, "非恢复出厂设置手动登录流程!!!", Toast.LENGTH_SHORT).show();
//							UserInfoDBhelper.save(context, "", "", "", "");
//						}
//					}

					return;
				} else {
					Log.e(TAG, "网络连接失败");
					// Toast.makeText(context, "网络连接失败",
					// Toast.LENGTH_SHORT).show();
				}
			}
		} else {
			Toast.makeText(context, "网络连接异常", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 判断是否是出厂设置
	 * 
	 * @param context
	 * @return
	 */
//	public boolean isFactorySetting(Context context) {
//		// 获取机顶盒的用户信息内容
//		String isAutoLogin = preferencesUtil
//				.readSharedPreferencesString("isFactorySetting");
//		Log.d(TAG, "isFactorySetting==" + isAutoLogin);
////		isAutoLogin在银河的盒子上取出来的值在恢复出厂设置的情况下为""
//		if (isAutoLogin != null&&!isAutoLogin.equals("")) {
//			return false;
//		}
//		return true;
//	}

	/**
	 * 判断是否自动登录
	 * 
	 * @param context
	 * @return
	 */
//	public boolean isAutoLogin(Context context) {
//		// 获取机顶盒的用户信息内容
//		String isAutoLogin = preferencesUtil
//				.readSharedPreferencesString("isAutoLogin");
//		Log.d(TAG, "isAutoLogin==" + isAutoLogin);
//		if (isAutoLogin != null && isAutoLogin.equals("1")) {
//			return true;
//		}
//		return false;
//	}
}
