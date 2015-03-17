package com.chinamobile.hdcjs.receiver;

import android.app.SystemInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.chinamobile.hdcjs.provider.DevInfoDBhelper;
import com.chinamobile.hdcjs.provider.UserInfoDBhelper;
import com.chinamobile.hdcjs.util.PreferencesUtil;

/**
 * 开机广播，开启登录服务
 * 
 */
public class BootReceiver extends BroadcastReceiver {

	private static final String TAG = "BootBroadcastReceiver";
	
	public static boolean needClean = true;

	@Override
	public void onReceive(Context context, Intent mintent) {
		String action = mintent.getAction();

		if (action.equals("android.intent.action.BOOT_COMPLETED")) {
			Log.d(TAG, "[HDC鉴权服务]开机自动启动...");
			// UserInfoUtil.saveUserInfo("", "", "", "");
			// if (isAutoLogin(context)) {
			// if (!LoginService.isLoginServiceWorked(context
			// .getApplicationContext())) {
			// // 开启自动登录服务
			// Intent intent = new Intent(context, LoginService.class);
			// context.startService(intent);
			// Log.d(TAG, "[HDC鉴权服务]开机自动启动...");
			// }
			// // Toast.makeText(context, "[HDC鉴权服务]开机自动启动...",
			// // Toast.LENGTH_SHORT).show();
			// } else {
			// // 如果是未勾选自动登录，则将空的登录信息写入到本地文件
			// Log.d(TAG, "未自动启动...");
			
			if (needClean) {
				UserInfoDBhelper.save(context, "", "", "", "");
			}

			// 设备数据写入到共享接口
			DevInfoDBhelper devInfoDBhelper = new DevInfoDBhelper(context);
			devInfoDBhelper.insert(SystemInfo.getSTBMAC(context),
					SystemInfo.getSTBSN(context), SystemInfo.getPlatformURL(context),
					SystemInfo.getPlatformURLBackup(context), SystemInfo.getCMSURL(context),
					SystemInfo.getCMSURLBackup(context),SystemInfo.getHDCBasicURL(context));
			// }
		}

	}

	/**
	 * 判断是否自动登录
	 * 
	 * @param context
	 * @return
	 */
	public boolean isAutoLogin(Context context) {
		PreferencesUtil preferencesUtil = new PreferencesUtil(context);
		// 获取机顶盒的用户信息内容
		String isAutoLogin = preferencesUtil
				.readSharedPreferencesString("isAutoLogin");
		Log.d(TAG, "isAutoLogin==" + isAutoLogin);
		if (isAutoLogin != null && isAutoLogin.equals("1")) {
			return true;
		}
		return false;
	}

}
