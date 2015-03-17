package android.app;

import android.content.Context;

/**
 * 系统信息提供类 create on 2014-03-16
 * 
 * @author L.jinzhu@EB
 * @version 1.00
 * 
 */
public class SystemInfo {

	public static DevInfoManager mDevInfoManager = null;

	/*
	 * 获取用户手机号
	 */
	public static String getMobilePhoneNumber(Context context) {
		mDevInfoManager = (DevInfoManager) context
				.getSystemService(DevInfoManager.DATA_SERVER);
		String mobilePhoneNumber = mDevInfoManager
				.getValue("MobilePhoneNumber");
		return mobilePhoneNumber;
	}

	/*
	 * 获取用户手机号服务密码
	 */
	public static String getServicePassword(Context context) {
		mDevInfoManager = (DevInfoManager) context
				.getSystemService(DevInfoManager.DATA_SERVER);
		String servicePassword = mDevInfoManager.getValue("ServicePassword");
		return servicePassword;
	}

	/*
	 * 获取用户业务账号
	 */
	public static String getAccount(Context context) {
		mDevInfoManager = (DevInfoManager) context
				.getSystemService(DevInfoManager.DATA_SERVER);
		String account = mDevInfoManager.getValue("Account");
		return account;
	}

	/*
	 * 获取用户业务账号密码
	 */
	public static String getAccountPassword(Context context) {
		mDevInfoManager = (DevInfoManager) context
				.getSystemService(DevInfoManager.DATA_SERVER);
		String accountPassword = mDevInfoManager.getValue("AccountPassword");
		return accountPassword;
	}

	/*
	 * 获取设备MAC地址
	 */
	public static String getSTBMAC(Context context) {
		mDevInfoManager = (DevInfoManager) context
				.getSystemService(DevInfoManager.DATA_SERVER);
		String stbMac = mDevInfoManager.getValue("STBMAC");
		return stbMac;
	}

	/*
	 * 获取设备唯一序列号
	 */
	public static String getSTBSN(Context context) {
		mDevInfoManager = (DevInfoManager) context
				.getSystemService(DevInfoManager.DATA_SERVER);
		String stbSn = mDevInfoManager.getValue("STBSN");
		return stbSn;
	}

	/*
	 * 获取开放平台主地址
	 */
	public static String getPlatformURL(Context context) {
		mDevInfoManager = (DevInfoManager) context
				.getSystemService(DevInfoManager.DATA_SERVER);
		String platformUrl = mDevInfoManager.getValue("PlatformURL");
		return platformUrl;
	}

	/*
	 * 获取开放平台备地址
	 */
	public static String getPlatformURLBackup(Context context) {
		mDevInfoManager = (DevInfoManager) context
				.getSystemService(DevInfoManager.DATA_SERVER);
		String platformUrlBackup = mDevInfoManager
				.getValue("PlatformURLBackup");
		return platformUrlBackup;
	}

	/*
	 * 获取终端管理主地址
	 */
	public static String getCMSURL(Context context) {
		mDevInfoManager = (DevInfoManager) context
				.getSystemService(DevInfoManager.DATA_SERVER);
		String cmsUrl = mDevInfoManager.getValue("CMSURL");
		return cmsUrl;
	}

	/*
	 * 获取终端管理备地址
	 */
	public static String getCMSURLBackup(Context context) {
		mDevInfoManager = (DevInfoManager) context
				.getSystemService(DevInfoManager.DATA_SERVER);
		String cmsUrlBackup = mDevInfoManager.getValue("CMSURLBackup");
		return cmsUrlBackup;
	}

	/*
	 * 获取HDC基本地址
	 */
	public static String getHDCBasicURL(Context context) {
		// 形如http://112.4.17.182:8080/
		mDevInfoManager = (DevInfoManager) context
				.getSystemService(DevInfoManager.DATA_SERVER);
		String hdcBasicUrl = mDevInfoManager.getValue("HDCRURL");
		return hdcBasicUrl;
	}

	/********** 以下方法返回的服务器路径，由nginx服务器分发指向真实服务请求地址 **********/
	/*
	 * 获取HDC服务地址
	 */
	public static String getHDCServiceURL(Context context) {
		// 形如http://112.4.17.182:8080/hdc-service/
		String hdcServiceUrl = getHDCBasicURL(context) + "hdc-service/";
		return hdcServiceUrl;
	}

	/*
	 * 获取应用远程服务地址
	 */
	public static String getHopAppRemoteURL(Context context) {
		// 形如http://112.4.17.182:8080/hop-app-remote/
		String hopAppRemoteURL = getHDCBasicURL(context) + "hop-app-remote/";
		return hopAppRemoteURL;
	}

	/*
	 * 获取屏幕配置文件服务地址
	 */
	public static String getLauncherXmlURL(Context context) {
		// 形如http://112.4.17.182:8080/launcher-xml/
		String launcherXml = getHDCBasicURL(context) + "launcher-xml/";
		return launcherXml;
	}
}
