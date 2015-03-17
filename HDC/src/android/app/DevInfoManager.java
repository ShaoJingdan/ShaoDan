package android.app;

/**
 * 系统参数获取类 create on 2014-03-16
 * 
 * @author L.jinzhu@EB
 * @version 1.00
 * 
 */
public abstract interface DevInfoManager {
	/**
	 * 获取机顶盒的id代码参考如下： DevInfoManager mDevInfoManager = (DevInfoManager)
	 * getSystemService(DevInfoManager. DATA_SERVER); String stbId =
	 * mDevInfoManager.getValue("STBSN"); 以上stbId即为机顶盒的唯一id。
	 * 
	 * Client.setIdentity(stbId); 这样appId和identity能唯一确认一组鉴权值
	 */
	
	// MobilePhoneNumber : 用户手机号
	public static final String PHONE = "MobilePhoneNumber";
	// ServicePassword : 用户手机号服务密码
	public static final String SPASSWORD = "ServicePassword";
	// Account : 用户业务账号
	public static final String ACCOUNT = "Account";
	// AccountPassword : 用户业务账号密码
	public static final String APASSWORD = "AccountPassword";
	// STBMAC : 设备MAC地址
	public static final String STB_MAC = "STBMAC";
	// STBSN : 设备唯一序列号
	public static final String STB_SN = "STBSN";
	// PlatformURL : 开放平台主地址；
	public static final String PLATFORM_URL = "PlatformURL";
	// PlatformURLBackup : 放平台备地址；
	public static final String PLATFORM_URL_BACKUP = "PlatformURLBackup";
	// CMSURL ：终端管理主地址
	public static final String CMS_URL = "CMSURL";
	// CMSURLBackup：终端管理备地址
	public static final String CMS_URL_BACKUP = "CMSURLBackup";
	// HDCRURL ： HDC地址
	public static final String HDCR_URL = "HDCRURL";

	public static final int Default_Attribute = 0;
	public static final String DATA_SERVER = "data";

	public abstract String getValue(String paramString);

	public abstract int update(String paramString1, String paramString2,
			int paramInt);
}
