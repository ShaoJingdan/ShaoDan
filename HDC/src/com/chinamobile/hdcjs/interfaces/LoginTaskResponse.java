package com.chinamobile.hdcjs.interfaces;

/**
 * 登录线程执行完毕后
 * @author CJL
 *
 */
public interface LoginTaskResponse {
	public static final String LOGIN_SUCCESS = "login_success";
	public static final String LOGIN_FAILED = "login_failed";
	public static final String MSG_ACCOUNT_ERROR = "9104";
	public static final String MSG_PASSWORD_ERROR_BOX = "9105";	// 机顶盒密码错误
	public static final String MSG_PASSWORD_ERROR_PHONE = "9203";		// 手机密码错误
	public static final String MSG_CONNECT_EXCPTION = "-90003";//连接建立异常
	public static final String MSG_CONNECTTIMEOUT_EXCPTION = "-90006";//请求超时
	public static final String MSG_SOCKETTIMEOUT_EXCEPTION = "-90007";//响应超时
	public static final String MSG_SOCKET_EXCEPTION = "-90005";//连接已关闭
	public static final String MSG_IO_EXCEPTION = "-90001";//IO异常
	public static final String MSG_GENERAL_SECURITY_EXCPTION= "-90002";//加密异常
	public static final String MSG_PARAMNOTINIT_EXCPTION= "-90004";//参数缺失异常
	public static final String MSG_ACCOUNT_LOCKED = "9211"; // 账号被锁
	void processFinish(String result,String msg);
}
