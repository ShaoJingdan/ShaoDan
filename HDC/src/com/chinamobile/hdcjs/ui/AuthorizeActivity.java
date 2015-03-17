package com.chinamobile.hdcjs.ui;

import hop.client.service.exception.ParamNotInitException;
import hop.client.service.rest.HopTerminalClient;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.DevInfoManager;
import android.app.SystemInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chinamobile.hdcjs.R;
import com.chinamobile.hdcjs.aidl.UserInfoUtil;
import com.chinamobile.hdcjs.database.LoginInfoDBHelper;
import com.chinamobile.hdcjs.interfaces.LoginTaskResponse;
import com.chinamobile.hdcjs.provider.DevInfoDBhelper;
import com.chinamobile.hdcjs.service.LoginService;
import com.chinamobile.hdcjs.util.ShortCut;
import com.chinamobile.hdcjs.view.MyRoundProgressBar;

/**
 * 登录
 */
public class AuthorizeActivity extends Activity{
	public static final String TAG = AuthorizeActivity.class.getSimpleName();
	// public static final int PHONE_NUMBER_LENGTH = 11;// 手机号长度
	/**
	 * 登录类型（1:服务密码登录 2:统一认证业务密码登录 3:统一认证动态密码登录） 服务密码登录,即机顶盒密码登录
	 */
	public static final int LOGIN_BY_SERVICE = 1;
	/**
	 * 登录类型（1:服务密码登录 2:统一认证业务密码登录 3:统一认证动态密码登录） 统一认证业务密码登录，即手机密码登录
	 */
	public static final int LOGIN_BY_SSO = 2;
	/**
	 * 登录类型（1:服务密码登录 2:统一认证业务密码登录 3:统一认证动态密码登录） 统一认证动态密码登录，即短信密码登录
	 */
	public static final int LOGIN_BY_PASSWORD = 3;
	
	public static final int LOGIN_BY_SMSCODE = 4;
	
	// 控件
	private EditText userEdit;
	private EditText passwordEdit;
	private Button serviceBtn;// 互联网电视账号登录,即机顶盒密码登录
	private Button ssoBtn;    //和通行证登录，即统一认证服务密码登录
	
	private RelativeLayout andLayout;
	private Button passBtn; //固定密码登陆
	private Button smsBtn;  //短信密码登陆
	private TextView timeTv; //显示倒计时秒数
	private Button loginBtn;// 登录按钮
	private Button logoutBtn;// 注销按钮
	private MyRoundProgressBar myRoundProgressBar;// loading框
	private Button getPassBtn;// 获取动态密码
	private Button kongbaiBtn;// 获取动态密码
	private DevInfoManager mDevInfoManager;// 设备信息

	private int currentLoginMode = LOGIN_BY_SERVICE;   //登录方式选择，默认为互联网电视账号登录
	private int currentPasswordMode = LOGIN_BY_PASSWORD; //登录密码选择，默认为固定密码登录
	private String username = "";// 账号，电话号
	private String password = "";// 密码
	
	private TimeLimitThread mGetPassThread = null;
	private boolean TimeLimitExit = true;
	/* 计时器 */
	public int timeNo = 30;// 倒计时长"秒"
	public static final int TIMECHECK = 600;
//	private Timer mTimer;
//	private TimerTask mTimerTask;


	// Reference to the service
	private LoginService serviceBinder;
	 
	// Handles the connection between the service and activity
	private ServiceConnection mConnection = new ServiceConnection()
	{
		public void onServiceConnected(ComponentName className, IBinder service) {
			// Called when the connection is made.
			serviceBinder = ((LoginService.MyBinder)service).getService();
			serviceBinder.setHandler(mLoginServiceHandler);
		}
	 
		public void onServiceDisconnected(ComponentName className) {
			// Received when the service unexpectedly disconnects.
			serviceBinder = null;
		}
	};
		

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		init();
		setWidgetListenerAction();
		
		//绑定loginservice
		Intent bindIntent = new Intent(this, LoginService.class);
		bindService(bindIntent, mConnection, Context.BIND_AUTO_CREATE);
		
	}
	

	/**
	 * 初始化控件
	 */
	public void init() {
		// 初始化控件
		userEdit = (EditText) findViewById(R.id.phone_login);		
		passwordEdit = (EditText) findViewById(R.id.password_login);		
		loginBtn = (Button) findViewById(R.id.login_bt);
		logoutBtn = (Button) findViewById(R.id.logout_bt);
		serviceBtn = (Button) findViewById(R.id.btn_by_itvpassport);		
		ssoBtn = (Button) findViewById(R.id.btn_by_andpassport);
		andLayout = (RelativeLayout) findViewById(R.id.for_andpassport);
		passBtn = (Button) findViewById(R.id.passord);
		smsBtn = (Button) findViewById(R.id.smscode);
		timeTv = (TextView) findViewById(R.id.time);
		timeTv.setTag(timeNo);  //设置倒计时初始值
		myRoundProgressBar = new MyRoundProgressBar(AuthorizeActivity.this);
		getPassBtn = (Button) findViewById(R.id.getpwd);
		kongbaiBtn = (Button) findViewById(R.id.kongbai);
	
		mDevInfoManager = (DevInfoManager) getSystemService(DevInfoManager.DATA_SERVER);// 初始化

		// 设备数据写入到共享接口
		DevInfoDBhelper devInfoDBhelper = new DevInfoDBhelper(this);
		devInfoDBhelper.insert(SystemInfo.getSTBMAC(this),
				SystemInfo.getSTBSN(this), SystemInfo.getPlatformURL(this),
				SystemInfo.getPlatformURLBackup(this),
				SystemInfo.getCMSURL(this), SystemInfo.getCMSURLBackup(this),
				SystemInfo.getHDCBasicURL(this));
		
		serviceBtn.setSelected(true);
		passBtn.setSelected(true);
		serviceBtn.requestFocus();
			

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//重置倒计时初始值
		timeTv.setTag(timeNo);
		
		loadData();
		
		if(serviceBinder!=null){
			serviceBinder.setHandler(mLoginServiceHandler);
		}
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//停止倒计时线程
		TimeLimitExit = true; 	
		
		//暂停接收绑定的service的消息
		if(serviceBinder!=null){
			serviceBinder.setHandler(null);
		}
	}
	

	/**
	 * 载入数据
	 */
	public void loadData() {
		Log.i(TAG, "---------loadData---------");
		setTabStatus();
		
		// 判断是否允许"注销"按钮可点,已登录的情况
		if (LoginInfoDBHelper.getCurrentLoginStatus(this)==LoginInfoDBHelper.LOGINSTATUS_ON) {
			// Enable
			Log.i(TAG, "已登录，注销按钮可用");
			logoutBtn.setFocusable(true);
			logoutBtn.setEnabled(true);
			loginBtn.setNextFocusRightId(R.id.logout_bt);
		
		//未登录的情况
		} else {
			// Disable
			Log.i(TAG, "未登录，注销按钮不可用");
			logoutBtn.setFocusable(false);
			logoutBtn.setEnabled(false);
			loginBtn.setNextFocusRightId(R.id.login_bt);
		}

		userEdit.requestFocus();
		hideSoftInput(userEdit);// 隐藏软键盘
		userEdit.setSelection(userEdit.getText().length());// 光标移到末尾

	}

	/**
	 * tab栏背景及选中状态
	 */
	public void setTabStatus() {
		// 初始的tab显示
		if (currentLoginMode == LOGIN_BY_SERVICE) {

			serviceBtn.setSelected(true);
			ssoBtn.setSelected(false);

			andLayout.setVisibility(View.GONE);
			kongbaiBtn.setVisibility(View.INVISIBLE);
			passwordEdit.setHint(R.string.login_business_pass_hint);
			
			userEdit.setNextFocusDownId(R.id.password_login);
			passwordEdit.setNextFocusUpId(R.id.phone_login);
			passwordEdit.setNextFocusDownId(R.id.login_bt);
			loginBtn.setNextFocusUpId(R.id.password_login);
			logoutBtn.setNextFocusUpId(R.id.password_login);
			
			if(LoginInfoDBHelper.getLastLoginType(this)==LoginInfoDBHelper.LOGINTYPE_SERVICE){
				userEdit.setText(LoginInfoDBHelper.getLastLoginUsername(this));
				passwordEdit.setText(LoginInfoDBHelper.getLastLoginPassword(this));
			}else{
				// 清空用户名密码窗口
				userEdit.setText("");
				passwordEdit.setText("");
			}
						
			
		} else if (currentLoginMode == LOGIN_BY_SSO) {

			serviceBtn.setSelected(false);
			ssoBtn.setSelected(true);
			
			kongbaiBtn.setVisibility(View.GONE);
			andLayout.setVisibility(View.VISIBLE);

			passBtn.setSelected(true);
//			timeTv.setVisibility(View.INVISIBLE);
//			getPassBtn.setVisibility(View.VISIBLE);
			passwordEdit.setHint(R.string.login_sso_pass_hint);
						
			userEdit.setNextFocusDownId(R.id.passord);
			getPassBtn.setNextFocusUpId(R.id.phone_login);
			passwordEdit.setNextFocusDownId(R.id.login_bt);
			
			if(LoginInfoDBHelper.getLastLoginType(this)==LoginInfoDBHelper.LOGINTYPE_SSO_PASSWORD){
				userEdit.setText(LoginInfoDBHelper.getLastLoginUsername(this));
				passwordEdit.setText(LoginInfoDBHelper.getLastLoginPassword(this));
			}else if(LoginInfoDBHelper.getLastLoginType(this)==LoginInfoDBHelper.LOGINTYPE_SSO_SMS){
				userEdit.setText(LoginInfoDBHelper.getLastLoginUsername(this));
				passwordEdit.setText("");
			}else{
				// 清空用户名密码窗口
				userEdit.setText("");
				passwordEdit.setText("");
			}

			setBoxStatus();
		} 

	}
	
	/**
	 * 密码类型选中状态
	 */
	public void setBoxStatus() {
		Log.i(TAG, "setBoxStatus currentPasswordMode"+currentPasswordMode);
		// 选择动态验证码方式登陆
		if (currentPasswordMode == LOGIN_BY_SMSCODE) {
			Log.i(TAG, "currentPasswordMode == LOGIN_BY_SMSCODE");
			smsBtn.setSelected(true);
			passBtn.setSelected(false);
			passwordEdit.setHint(R.string.login_sso_sms_hint);
			
			userEdit.setNextFocusDownId(R.id.smscode);
			passwordEdit.setNextFocusUpId(R.id.smscode);
							
			int time = (Integer) timeTv.getTag();
			Log.i(TAG,"time="+time);
			Log.i(TAG,"TimeLimitExit="+TimeLimitExit);
			if(TimeLimitExit){
				Log.i(TAG,"time==timeNo&&TimeLimitExit");
//				TimeLimitExit = false;
//				timeTv.setTag(timeNo);
//				timeTv.setText("( "+timeNo+"s )");
//				mGetPassThread = new TimeLimitThread();
//				mGetPassThread.start();				
//				serviceBinder.getSmsCode(username);
				
				getPassBtn.setVisibility(View.VISIBLE);
				timeTv.setVisibility(View.INVISIBLE);
				
			}else{
				Log.i(TAG, "短信验证码已发，请等待"+time+"s");
				getPassBtn.setVisibility(View.INVISIBLE);
				timeTv.setVisibility(View.VISIBLE);
			}
		
			
			
		// 选择固定密码方式登陆	
		} else if (currentPasswordMode == LOGIN_BY_PASSWORD) {
			Log.i(TAG, "currentPasswordMode == LOGIN_BY_PASSWORD");
			smsBtn.setSelected(false);
			passBtn.setSelected(true);
			passwordEdit.setHint(R.string.login_sso_pass_hint);	
			
			userEdit.setNextFocusDownId(R.id.passord);
			passwordEdit.setNextFocusUpId(R.id.passord);
			
			timeTv.setVisibility(View.INVISIBLE);		
			getPassBtn.setVisibility(View.INVISIBLE);
			
			//****************测试账号***********************
			//userEdit.setText("18256940717");
			//passwordEdit.setText("23822359@ltsh");
			//***************************************
			
		} 

	}



	/**
	 * 提示信息
	 * 
	 * @param msg
	 */
	public void showToast(String msg) {
		Toast.makeText(AuthorizeActivity.this, msg, Toast.LENGTH_SHORT).show();
	}


	/**
	 * 各个控件的监听事件
	 */
	public void setWidgetListenerAction() {
		//机顶盒密码登录监听
		serviceBtn.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					if(currentLoginMode!= LOGIN_BY_SERVICE){
						currentLoginMode = LOGIN_BY_SERVICE;					
						setTabStatus();
					}
					
				}
			}
		});
		//统一认证服务密码登录监听
		ssoBtn.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					
					if(currentLoginMode != LOGIN_BY_SSO){
						currentLoginMode = LOGIN_BY_SSO;
						setTabStatus();
					}
					
				}
			}
		});
		//短信密码登陆监听
		smsBtn.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					
//					username = userEdit.getText().toString().trim();
//					if (username == null || username.equals("")) {
//						showToast(getResources().getString(
//								R.string.alert_input_phonenum));
//					} else if (!username.matches("\\d{11}")) {
//						Log.i(TAG, "!username.matches(\\d{11}");
//						showToast(getResources().getString(
//								R.string.alert_phonenum_illegal));
//					} else {
//						Log.i(TAG, "点击发送验证码");
//						currentPasswordMode = LOGIN_BY_SMSCODE;
//						setBoxStatus();
//					}
					
					currentPasswordMode = LOGIN_BY_SMSCODE;
					setBoxStatus();
									
					
				}
			}
		});
		//固定密码登陆监听
		passBtn.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if (hasFocus) {
					currentPasswordMode = LOGIN_BY_PASSWORD;
					setBoxStatus();
				}
			}
		});
		
//		smsBtn.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				// TODO Auto-generated method stub
//				if (hasFocus) {
//					currentLoginMode = LOGIN_BY_SMS_PASS;
//					setTabStatus();
//				}
//			}
//		});
		
		loginBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				login();
			}
		});
		
		logoutBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				logout();
			}
		});

		final Button to_register = (Button) findViewById(R.id.to_register);
		to_register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setClass(getApplicationContext(), RegisterActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

				startActivity(intent);
			}
		});
		//固定密码登陆监听
		getPassBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				username = userEdit.getText().toString().trim();
				if (username == null || username.equals("")) {
					showToast(getResources().getString(
							R.string.alert_input_phonenum));
				} else if (!username.matches("\\d{11}")) {
					Log.i(TAG, "!username.matches(\\d{11}");
					showToast(getResources().getString(
							R.string.alert_phonenum_illegal));
				} else {
					Log.i(TAG, "点击发送验证码");
										
					int time = (Integer) timeTv.getTag();
					Log.i(TAG,"time="+time);
					Log.i(TAG,"TimeLimitExit="+TimeLimitExit);
					if(TimeLimitExit){
						Log.i(TAG,"time==timeNo&&TimeLimitExit");
						TimeLimitExit = false;
						timeTv.setTag(timeNo);
						timeTv.setText("( "+timeNo+"s )");
						mGetPassThread = new TimeLimitThread();
						mGetPassThread.start();
						
						serviceBinder.getSmsCode(username);
						
						passwordEdit.requestFocus();
						timeTv.setVisibility(View.VISIBLE);
						getPassBtn.setVisibility(View.INVISIBLE);
												
					}else{
						Log.i(TAG, "短信验证码已发，请等待"+time+"s");
					}
				
					
					
				}
				
			}
		});
		
		userEdit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				username = s.toString();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		
		passwordEdit.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				password = s.toString();
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
	}



	/**
	 * 登录
	 */
	public void login() {

		username = userEdit.getText().toString().trim();
		password = passwordEdit.getText().toString().trim();
		if (username == null || username.equals("")) {
			showToast(getResources().getString(R.string.alert_input_phonenum));
		} else if (password == null || password.equals("")) {
			showToast(getResources().getString(R.string.alert_input_password));
		} else if (!username.matches("\\d{11}")) {
			showToast(getResources().getString(R.string.alert_phonenum_illegal));
		} else {

			myRoundProgressBar.initDialog();// 显示loading框
			
			int loginType;
			switch(currentLoginMode){
			
			case LOGIN_BY_SERVICE:
				loginType = LoginInfoDBHelper.LOGINTYPE_SERVICE;
				break;

			default:			
				if(currentPasswordMode==LOGIN_BY_PASSWORD){
					loginType = LoginInfoDBHelper.LOGINTYPE_SSO_PASSWORD;
				}else{
					loginType = LoginInfoDBHelper.LOGINTYPE_SSO_SMS;
				}
			}
						
			if(serviceBinder!=null){
				serviceBinder.login(loginType,username, password);
			}else{
				showToast(getString(R.string.alert_login_bindservice_error));
			}
			
		}
	}
	
	/**
	 * 注销
	 */
	public void logout() {
		
		if(serviceBinder!=null){
			
			serviceBinder.logout();
			
			// 注销按钮变不可用
			logoutBtn.setEnabled(false);
			logoutBtn.setFocusable(false);
			loginBtn.setNextFocusRightId(R.id.login_bt);
			
			// 账户输入框获取焦点
			userEdit.requestFocus();
			
			// 弹出Toast提示
			showToast(getResources().getString(R.string.alert_logout_success));
			
		}else{
			showToast(getString(R.string.alert_login_bindservice_error));
		}		
				
	}


	/**
	 * 显示软键盘
	 */
	private void showSoftInput() {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (inputMethodManager != null) {
			inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED,
					0);
		}
	}

	/**
	 * 隐藏软键盘
	 * 
	 * @param view
	 */
	private void hideSoftInput(View view) {
		InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_UP) {
			String sessionID = UserInfoUtil.getUserSessionId(this);
			if (sessionID == null || "".equals(sessionID)) {
				this.setResult(0);// 返回失败code
			}
			finish();
			return true;
		}

		if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
			// 按向上键,解决TAB焦点问题
			Log.d(TAG, "KeyEvent.KEYCODE_DPAD_UP");
			if (this.getCurrentFocus() == userEdit) {
				Log.d(TAG, "focus from edit to tab...");
				if (currentLoginMode == LOGIN_BY_SERVICE) {
					serviceBtn.requestFocus();
				} else if (currentLoginMode == LOGIN_BY_SSO) {
					ssoBtn.requestFocus();
				}
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/**
	 * 实现handleMessage()方法，用于接收LoginService反馈的Message，刷新UI
	 */
	public Handler mLoginServiceHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			//登录成功
			case 0:
				myRoundProgressBar.colseDialog();
				Log.i(TAG, "——————————————登录成功————————————————");				
				//loadData();				
				finish();				
				break;
			//登录失败
			case 1:
				myRoundProgressBar.colseDialog();
				Log.i(TAG, "——————————————登录失败————————————————");
				String errorString = msg.getData().getString("errorcode");
				Log.i(TAG, "errorString"+errorString);
				
				if(errorString==null||errorString.equals("")){
					
					
				}else if (errorString.equals(LoginTaskResponse.MSG_ACCOUNT_ERROR)) {
					// 用户账号输入错误
					userEdit.requestFocus();
					
				} else if (errorString.equals(LoginTaskResponse.MSG_PASSWORD_ERROR_BOX)
						|| errorString.equals(LoginTaskResponse.MSG_PASSWORD_ERROR_PHONE)) {
					// 用户密码输入错误，提示密码错误，然后自动清空密码，焦点定在密码输入框。
					passwordEdit.setText("");
					passwordEdit.requestFocus();
					
				} else if (errorString.equals(LoginTaskResponse.MSG_CONNECT_EXCPTION)) {
					// 网络连接失败
					
				} else if (errorString.equals(LoginTaskResponse.MSG_CONNECTTIMEOUT_EXCPTION)) {
					// 网络连接超时

				} else if (errorString.equals(LoginTaskResponse.MSG_ACCOUNT_LOCKED)) {
					// 用户账户被锁

				} else {
					// 其他错误
					
				}
				
				break;
			default:
				
			}

			super.handleMessage(msg);

		}
	};


	/**
	 * 实现handleMessage()方法，用于接收Message，刷新UI
	 */
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			
			Log.i(TAG, "mHandler msg.what="+msg.what);
			switch (msg.what) {
			//刷新倒计时计数
			case 1:
				refreshGetPassText();
				break;
//			//获取短信密码失败
//			case 2:
//				getPassBtn.setText("获取动态密码");
//				if (mGetPassThread != null) {
//					mGetPassThread.exit = true; // 终止线程
//				}
//				getPassBtn.setTag(R.id.tag_btngetpass_is_click, "click");
//				break;
//			//获取短信密码成功
//			case 3:
//				passwordEdit.requestFocus();// 密码框获取焦点
//				passwordEdit.setSelection(passwordEdit.getText().length());// 光标移到末尾
//				break;

			default:
				
			}

			super.handleMessage(msg);

		}
	};
	
	/**
	 * 刷新获取密码的文字显示
	 */
	public void refreshGetPassText() {
		
		if(TimeLimitExit){
			return;
		}
		
		int time = (Integer) timeTv.getTag();
		time--;
		Log.i(TAG, "refreshGetPassText time = "+time);
		timeTv.setTag(time);

		if (time <= 0) {
//			getPassBtn.setText("获取动态密码");
			if (mGetPassThread != null) {
				TimeLimitExit = true; // 终止线程
			}			
			timeTv.setVisibility(View.INVISIBLE);
			getPassBtn.setVisibility(View.VISIBLE);
			
			
		} else {

			String timeToStr = time + "s";
			String str = "<font color='#F0FF00'>( " + timeToStr + " )</font>";
			timeTv.setText(Html.fromHtml(str));
		}
	}


	
	/**
	 * 获取密码文字倒计时的线程
	 * 
	 * @author CJL
	 * 
	 */
	public class TimeLimitThread extends Thread {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			// 实现run()方法，每1秒发送一条Message给Handler
			while (!TimeLimitExit) {
				Log.i(TAG, "TimeLimitExit="+TimeLimitExit);
				try {
					Thread.sleep(1000);// 休息1s
					android.os.Message msg = new android.os.Message();
					msg.what = 1;
					mHandler.sendMessage(msg);
					Log.i(TAG, "mHandler.sendMessage(msg) msg.what = 1;");
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * 给手机下发密码
	 * 
	 * @author CJL
	 * 
	 */
	public class SendPassToPhoneTask extends AsyncTask<Object, Integer, String> {
		private String phone = "";
		private Context context;

		public SendPassToPhoneTask(Context c) {
			this(c, "");
		}

		public SendPassToPhoneTask(Context c, String phone) {
			this.phone = phone;
			this.context = c;
		}

		@Override
		protected String doInBackground(Object... arg0) {
			// TODO Auto-generated method stub
			// 读取配置文件，获取token过期时间
			String result = null;
			try {
				String terminal_server_url = SystemInfo
						.getHDCServiceURL(context);
				// 调用终端客户端服务
				HopTerminalClient client = ShortCut
						.getHopTerminalClientImplInstance(context);

				client.setServerUrl(terminal_server_url);

				if (client.getIdentity() == null
						|| client.getIdentity().equals("")) {
					client.setIdentity(mDevInfoManager
							.getValue(DevInfoManager.STB_SN));
				}

				result = (String) client.userRegister(phone);
				Log.d("SendPassToPhoneTask", "result=" + result);

			} catch (ParamNotInitException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return result;
		}

		@Override
		protected void onProgressUpdate(Integer... progress) {
		}

		@Override
		protected void onPostExecute(String result) {
			android.os.Message msg = new android.os.Message();
			if (result != null && result.equals("0")) {
				showToast(getResources().getString(
						R.string.alert_have_send_pass));
				msg.what = 3;// 成功
				mHandler.sendMessage(msg);
			} else {
				msg.what = 2;// 失败
				mHandler.sendMessage(msg);
			}
		}
	}


	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
		if(serviceBinder!=null){
			serviceBinder.setHandler(null);
			unbindService(mConnection);
		}
		
		
	}


}