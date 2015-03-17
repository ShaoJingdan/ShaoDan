package com.chinamobile.hdcjs.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.http.util.EncodingUtils;

import android.util.Log;

/**
 * 文件读取工具
 */
public class FileUtil {
	private static final String TAG = "FileUtil";

	// 写数据到SD中的文件
	public static void writeFile(String fileName, String write_str) {
		try {
			// 文件已存在且不为空，则先删除
			File file = new File(fileName);
			if (file.isFile() && file.exists()) {
				file.delete();
			}

			FileOutputStream fout = new FileOutputStream(fileName);
			byte[] bytes = write_str.getBytes();
			fout.write(bytes);
			fout.close();
		} catch (Exception e) {
			Log.e(TAG, "保存文件异常：" + e.getMessage());
		}
	}

	// 读SD中的文件
	public static String readFile(String fileName) {
		String res = "";
		try {
			FileInputStream fin = new FileInputStream(fileName);
			int length = fin.available();
			byte[] buffer = new byte[length];
			fin.read(buffer);
			res = EncodingUtils.getString(buffer, "UTF-8");
			fin.close();
		} catch (Exception e) {
			Log.e(TAG, "保存读取异常：" + e.getMessage());
		}
		return res;
	}
}
