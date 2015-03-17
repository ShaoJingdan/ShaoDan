package com.chinamobile.hdcjs.util;

import hop.client.service.rest.HopTerminalClientImpl;

import android.app.SystemInfo;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

public class ShortCut {
	private static final String TAG = ShortCut.class.getSimpleName();
	
	public static ImageView OldView;
	
	private static HopTerminalClientImpl client = null;

	
	public static HopTerminalClientImpl getHopTerminalClientImplInstance(Context mContext) {
		Log.d(TAG, "HopTerminalClientImplSingleTonHolder()");
		if(client == null){
			client = new HopTerminalClientImpl();
			client.setApiVersion("1.0");
			client.setAppId("199000000000000002"); //接入编码
			client.setAppKey("9990ED52D0742EE4C20BE2E3C2599C5F"); //接入秘钥
			client.setServerUrl(SystemInfo.getHDCServiceURL(mContext));
			client.setIdentity(SystemInfo.getSTBSN(mContext));//设备编号，获取本地deviceId
			client.terminalReg();
		}

		return client;
	}
	
	public static String updateImageUrl = "";
}
