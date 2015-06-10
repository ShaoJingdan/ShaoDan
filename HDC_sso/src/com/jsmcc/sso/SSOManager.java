package com.jsmcc.sso;

import android.os.RemoteException;
import android.util.Log;

public class SSOManager{
	
	private String TAG = "SSOManager";
	private String token = null;
	
	public SSOManager(String ticket){
		token = ticket;
	}
	
	public void saveTicket(String ticket,String packageName){
		
	}
	
	public String accessTicket(String packageName) throws RemoteException {
				
		Log.i(TAG, "accessTicket packageName"+packageName);
		return token;
	    	
	}
	    
	public void clearTicket(String packageName){
		
	}
	    
	public boolean checkUpdatedVersion(){
		return false;
	    	
	}
}