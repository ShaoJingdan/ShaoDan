package com.chinamobile.hdcjs.util;

import java.io.File;

import android.os.Environment;

/**
 * SDPath获取SD卡路径的相关操作 create on 2014-03-16
 * 
 * @author L.jinzhu@EB
 * @version 1.00
 * 
 */
public class SDPath {
	private static final String CACHDIR = "Launcher/ResourceCache";
	private static final String XMLDIR = "Launcher";

	/*
	 * 获得SD卡路径
	 */
	public static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory(); // 获取根目录
		}
		if (sdDir != null) {
			return sdDir.toString();
		} else {
			return "";
		}
	}

	/*
	 * 获得缓存目录
	 */
	public static String getCachDirectory() {
		String dir = getSDPath() + "/" + CACHDIR;
		return dir;
	}

	/*
	 * 获得配置文件目录
	 */
	public static String getXmlDirectory() {
		String dir = getSDPath() + "/" + XMLDIR;
		return dir;
	}

}