package com.jsmcc.sso;

import com.chinamobile.hdcjs.aidl.IUserInfoService;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

public class CallSSO{
	
	private String TAG = "CallSSO";
	private Handler mHandler;
	private Context mContext;
	private String mPackageName;
	private IUserInfoService mService;
	private TokenReceiver mTokenReceiver;
	private final String TOKEN_ACTION = "com.chinamobile.hdcjs.aidl.token";
	
    ServiceConnection mConnection = new ServiceConnection() {  
        
        @Override  
        public void onServiceDisconnected(ComponentName name) {  
            // TODO Auto-generated method stub  
        	Log.i(TAG, "onServiceDisconnected");
            mService = null;  
        }  
          
        @Override  
        public void onServiceConnected(ComponentName name, IBinder service) {  
            // TODO Auto-generated method stub  
        	Log.i(TAG, "onServiceConnected");
            mService = IUserInfoService.Stub.asInterface(service);  
            
            if(mService!=null){
            	Log.i(TAG, " mService!=null");
            	 
    			try {
    				mService.getToken(mPackageName);
    				
    			} catch (RemoteException e) {
    				// TODO Auto-generated catch block
    				Log.i(TAG, "RemoteException"+e.getMessage());
    				e.printStackTrace();
    			}
    			
    		}else{
    			
    			Log.i(TAG, "onServiceConnected失败 mService=null");
    		}
        }  
    }; 
	
	public CallSSO(Context c,Handler h){
		mContext = c;
		mHandler = h;
				
		mTokenReceiver = new TokenReceiver();
		mPackageName = c.getPackageName();
		System.out.println(mPackageName);
	}
	
	
	public void prepare(){
		Log.i(TAG, "prepare");
				
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(TOKEN_ACTION);   //为BroadcastReceiver指定action，使之用于接收同action的广播		
		mContext.registerReceiver(mTokenReceiver, intentFilter);
		Log.i(TAG, "registerReceiver");
		
		Intent bindIntent = new Intent("com.chinamobile.hdcjs.aidl.UserInfoService");
		mContext.bindService(bindIntent, mConnection, Context.BIND_AUTO_CREATE);
		Log.i(TAG, "bindService");
				
	}

	
	public class TokenReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("intent"+intent.getAction());
			System.out.println("fsssssssssssssssssssssssssssssssssss");
			// TODO Auto-generated method stub
			/*if(intent.getAction().equals(TOKEN_ACTION)){
				
				String token = intent.getStringExtra("token");
				Log.i(TAG, "onReceive token"+token);				
				
				Message msg = new Message();
				msg.what=CallAIDL.SUCCESS;
				msg.obj= new SSOManager(token);		
				mHandler.sendMessage(msg);
				
				mContext.unbindService(mConnection);
				mContext.unregisterReceiver(mTokenReceiver);
				
			}*/
		}
	}
	
	
	
}