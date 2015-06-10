package com.jsmcc.sso;

import com.jsmcc.sso.R;
import com.jsmcc.sso.CallSSO;
import com.jsmcc.sso.SSOManager;

import android.app.Activity;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	private String TAG = "MainActivity";
	
    /** SSO辅助类 */
	private CallSSO cs;
	/** SSO接口类 */
	private SSOManager ssoManager;
	
	private TextView textView;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		textView = (TextView) findViewById(R.id.token);
		
	}
	
	public void getToken(View view){
		Log.i(TAG, "getToken");
		cs = new CallSSO(MainActivity.this, handler);
        cs.prepare();
                
	}
	
	/**
	 * 实现handleMessage()方法，用于接收Message，刷新UI
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case CallAIDL.SUCCESS:
				
				Log.i(TAG, "CallAIDL.SUCCESS");
				ssoManager = (SSOManager)msg.obj;

				try {
					String packagename = getPackageName();
					Log.i(TAG, "packagename="+packagename);
					String token = ssoManager.accessTicket(packagename);
					Log.e(TAG, "获取token成功:"+token);
					
					// 未登录
					if (null == token || token.equals("")) {
						textView.setText("未登录");				
					}
					// 已登录
					else {
						textView.setText("已登录 "+token);
					}
				} catch (RemoteException e) {
					Log.e(TAG, "获取token失败");
				}
				
			default:
				break;
			}

			super.handleMessage(msg);

		}
	};


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.example_list, menu);
		return true;
	}
	
}
