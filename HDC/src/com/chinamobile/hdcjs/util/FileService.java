package com.chinamobile.hdcjs.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.util.EncodingUtils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

/**
 * @author LiMo
 * 主要对SD卡进行了文件的读写操作，用于缓存机制的使用
 */
public class FileService {

	// private static String bootPath = "/sdcard/OutOrder/File/";

	/**
	 * 检查SD卡剩余空间
	 * 
	 * @return
	 */
	public static boolean checkSDCard(int sizeMb) {
		boolean ishasSpace = false;
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			String sdcard = Environment.getExternalStorageDirectory().getPath();
			StatFs statFs = new StatFs(sdcard);
			long blockSize = statFs.getBlockSize();
			long blocks = statFs.getAvailableBlocks();
			long availableSpare = (blocks * blockSize) / (1024 * 1024);
			Log.d("剩余空间", "availableSpare = " + availableSpare);
			if (availableSpare > sizeMb) {
				ishasSpace = true;
			}
		}
		return ishasSpace;
	}

	//若有SD卡则放在SD卡，无则放在程序目录
	public static String getSdPath(Context ct) {
		if (!checkSDCard(5)) { // 不存在SD卡或者SD卡空间不足
			String filePath = ct.getApplicationContext().getFilesDir()
					.getAbsolutePath();
			Log.d("getSdPath", filePath);
			return filePath;
		}
		return android.os.Environment.getExternalStorageDirectory().toString();
	}

	public static String getBootPathPath(Context ct) {
		return FileService.getSdPath(ct) + "/HomeGalary/File/CacheFiles";
	}

	public static String getPicPath(Context ct) {
		return FileService.getSdPath(ct) + "/HomeGalary/Pic/";
	}

	public static String getCachePath(Context ct) {
		return FileService.getSdPath(ct) + "/HomeGalary/Pic/CachePictures/";
	}

	public static String getFilePath(Context ct) {
		return FileService.getSdPath(ct) + "/OutOrder/Pic/File/";
	}

	// public static String getSavePath(){
	// return FileService.getSdPath() + "/OutOrder/Pic/SavePictures/";
	// }
	//
	// public static String getSharePath(){
	// return FileService.getSdPath() + "/OutOrder/Pic/SharePictures/";
	// }

	/**
	 * 删除缓存
	 */
	public static void deleteCache(Context ct) {
		File bootDir = new File(getBootPathPath(ct));
		if (!bootDir.exists()) {
			return;
		}
		File[] files = bootDir.listFiles();
		for (int i = 0; i < files.length; i++) {
			files[i].delete();
		}
	}

	/**
	 * 检查路径是否存在
	 */
	public static void checkDir(Context ct) {
		if (!checkSDCard(5)) {
			return;
		}
		File bootFile = new File(getBootPathPath(ct));
		if (!bootFile.exists()) {
			boolean result = bootFile.mkdirs();
		}
		File picFile = new File(getPicPath(ct));
		if (!picFile.exists()) {
			picFile.mkdirs();
		}
		// File saveFile = new File(getSavePath());
		// if (!saveFile.exists()) {
		// saveFile.mkdirs();
		// }
		File file = new File(getFilePath(ct));
		if (!file.exists()) {
			file.mkdirs();
		}
		// File shareFile = new File(getSharePath());
		// if (!shareFile.exists()) {
		// shareFile.mkdirs();
		// }

	}

	// 写数据到SD中的文件
	public static void writeFileSdcardFile(String fileName, String write_str,
			Context ct) throws IOException {
		try {
			if (!checkSDCard(5)) {
				Log.v("FileService", "SD 卡不存在");
				return;
			}
			checkDir(ct);
			File file = new File(getBootPathPath(ct) + fileName + ".mj");
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fout = new FileOutputStream(getBootPathPath(ct)
					+ fileName + ".mj");
			byte[] bytes = write_str.getBytes();

			fout.write(bytes);
			fout.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 读SD中的文件
	public static String readFileSdcardFile(String fileName, Context ct)
			throws IOException {
		String res = "";
		try {
			if (!checkSDCard(5)) {
				Log.v("FileService", "SD 卡不存在");
				return "";
			}
			FileInputStream fin = new FileInputStream(getBootPathPath(ct)
					+ fileName + ".mj");

			int length = fin.available();

			byte[] buffer = new byte[length];
			fin.read(buffer);

			res = EncodingUtils.getString(buffer, "UTF-8");

			fin.close();
		}

		catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

}
