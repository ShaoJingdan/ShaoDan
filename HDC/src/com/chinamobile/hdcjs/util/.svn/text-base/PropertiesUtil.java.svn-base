package com.chinamobile.hdcjs.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;

/**
 * 配置文件读取工具
 */
public class PropertiesUtil {
	
	private Context context;
	
	public PropertiesUtil(Context context){
		this.context = context;
	}
	
	public String get(String fileName, String key){
		
		try {
			Properties prop = new Properties();
			
			InputStream in = context.getAssets().open(fileName);
			
			prop.load(in);
			
			return prop.get(key).toString();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}

}
