package com.chinamobile.hdcjs.ui;

import java.io.File;

import android.app.Application;
import android.app.DevInfoManager;
import android.content.Context;
import android.util.Log;

import com.chinamobile.hdcjs.util.FileService;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class HDCApplication extends Application {
	public final static String TAG = HDCApplication.class.getSimpleName();
	private DevInfoManager mDevInfoManager;// 设备信息
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		initImageLoader(getApplicationContext());
	}
	
	public static void initImageLoader(Context context) {
		File cacheDir = new File(FileService.getCachePath(context));
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCache(new UnlimitedDiscCache(cacheDir))
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.build();
		ImageLoader.getInstance().init(config);
	}

	/**
	 * 获取stbsn
	 */
	public String getSTBSN() {
		// 获取机顶盒的id代码参考如下
		String stbId = null;
		if (mDevInfoManager != null) {
			stbId = mDevInfoManager.getValue(DevInfoManager.STB_SN);
			Log.d(TAG, "mDevInfoManager.stbId="+stbId);
		} else {
			Log.d(TAG, "mDevInfoManager is null");
		}
		return stbId;
	}
	
}
