package com.chinamobile.hdcjs.util;


import java.io.File;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class ImageUtil {
	// 放大缩小图片
	public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Matrix matrix = new Matrix();
		float scaleWidht = ((float) w / width);
		float scaleHeight = ((float) h / height);
		matrix.postScale(scaleWidht, scaleHeight);
		Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
				matrix, true);
		return newbmp;
	}

	// 将Drawable转化为Bitmap
	public static Bitmap drawableToBitmap(Drawable drawable) {
		int width = drawable.getIntrinsicWidth();
		int height = drawable.getIntrinsicHeight();
		Bitmap bitmap = Bitmap.createBitmap(width, height, drawable
				.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
				: Bitmap.Config.RGB_565);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, width, height);
		drawable.draw(canvas);
		return bitmap;

	}

	// 获得圆角图片的方法
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	// 获得带倒影的图片方法
	public static Bitmap getReflectionBitmap(Bitmap bitmap) {
		// 原始图片和反射图片中间的间距
		final int reflectionGap = 4;
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		// 反转
		Matrix matrix = new Matrix();
		// 第一个参数为1表示x方向上以原比例为准保持不变，正数表示方向不变。
		// 第二个参数为-1表示y方向上以原比例为准保持不变，负数表示方向取反。
		matrix.preScale(1, -0.75f);
		// reflectionImage就是下面透明的那部分,可以设置它的高度为原始的3/4,这样效果会更好些
		Bitmap reflectionImage = Bitmap.createBitmap(bitmap, 0, 0, width,
				height * 1 / 4, matrix, false);
		// 创建一个新的bitmap,高度为原来的两倍
		Bitmap bitmapWithReflection = Bitmap.createBitmap(width,
				(height + height * 3 / 4), Config.ARGB_8888);
		// 其宽*高 = width * (height + height * 3 / 4)
		Canvas canvasRef = new Canvas(bitmapWithReflection);
		// 先画原始的图片
		canvasRef.drawBitmap(bitmap, 0, 0, null);
		// 画间距
		Paint deafaultPaint = new Paint();
		// defaultPaint不能为null，否则会有空指针异常。
		canvasRef.drawRect(0, height, width, height + reflectionGap,
				deafaultPaint);
		// 画被反转以后的图片
		canvasRef.drawBitmap(reflectionImage, 0, height + reflectionGap, null);
		// 创建一个渐变的蒙版放在下面被反转的图片上面
		Paint paint = new Paint();
		LinearGradient shader = new LinearGradient(200, bitmap.getHeight(), 10,
				bitmapWithReflection.getHeight() + reflectionGap, 0x80ffffff,
				0x00ffffff, TileMode.CLAMP);
		paint.setShader(shader);
		// Set the Transfer mode to be porter duff and destination in
		paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
		// 将蒙板画上
		canvasRef.drawRect(0, height, width, bitmapWithReflection.getHeight()
				+ reflectionGap, paint);
		// 调用ImageView中的setImageBitmap
		return bitmapWithReflection;
	}

	// 图片平铺
	public static Bitmap createRepeater(int width, Bitmap src) {
		int count = (width + src.getWidth() - 1) / src.getWidth();
		Bitmap bitmap = Bitmap.createBitmap(width, src.getHeight(),
				Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		for (int idx = 0; idx < count; ++idx) {
			canvas.drawBitmap(src, idx * src.getWidth(), 0, null);
		}
		return bitmap;
	}

//	// 获得一张图片,从三个地方获取,首先是内存缓存,然后是文件缓存,最后从网络获取
//	public static Bitmap getBitmap(String url, Context context) {
//		ImageFileCache fileCache = new ImageFileCache();
//		ImageMemoryCache memoryCache = new ImageMemoryCache(context);
//		// 从内存缓存中获取图片
//		Bitmap result = memoryCache.getBitmapFromCache(url);
//		if (result == null) {
//			// 文件缓存中获取
//			result = fileCache.getImage(url);
//			if (result == null) {
//				// 从网络获取
//				result = ImageGetFromHttp.downloadBitmap(url);
//				if (result != null) {
//					// 保存到文件缓存
//					fileCache.saveBitmap(result, url);
//					// 保存到内存缓存
//					memoryCache.addBitmapToCache(url, result);
//				}
//			} else {
//				// 保存到内存缓存
//				memoryCache.addBitmapToCache(url, result);
//			}
//		}
//		// 所有方式，均未下载成功,则替换默认图片
//		if (result == null) {
//			Drawable d = AppManager.mainActivity.getResources().getDrawable(
//					R.drawable.picture_error);
//			result = ImageUtil.drawableToBitmap(d);
//		}
//		return result;
//	}
//
//	// 初始化图片处理过程
//	public static void initImageLoader(Context context) {
//		// 设置缓存地址
//		File cacheDir = new File(SDPath.getCachDirectory());
//
//		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
//				context).threadPriority(Thread.NORM_PRIORITY - 2)
//				.denyCacheImageMultipleSizesInMemory()
//				.discCache(new UnlimitedDiscCache(cacheDir))
//				.discCacheFileNameGenerator(new Md5FileNameGenerator())
//				.tasksProcessingOrder(QueueProcessingType.LIFO).build();
//		ImageLoader.getInstance().init(config);
//	}
//
//	// 获取图片信息
//	public static DisplayImageOptions getImageOptions() {
//		DisplayImageOptions options = null;
//		options = new DisplayImageOptions.Builder()
//				.showStubImage(R.drawable.picture_loading)
//				.showImageForEmptyUri(R.drawable.picture_error)
//				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
//				.showImageOnFail(R.drawable.picture_error).cacheInMemory(true)
//				.cacheOnDisc(true).build();
//		return options;
//	}
}