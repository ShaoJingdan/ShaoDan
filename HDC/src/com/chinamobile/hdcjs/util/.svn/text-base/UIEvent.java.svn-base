package com.chinamobile.hdcjs.util;



import android.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.widget.ImageView;

public class UIEvent {

	private static final String TAG = "Launcher_LauncherEvent";
	private static int shadowSize = 40;// 边框阴影尺寸
	private static int extendWidth = 35;// 总放大长度
	private static int extendHeight = 35;// 总放大高度
	private static int extendWidthShadow = 50;// 总放大长度(含阴影边框)
	private static int extendHeightShadow = 50;// 总放大高度(含阴影边框)
	private static WindowManager wm = null;
	static WindowManager.LayoutParams wmParams = null;
	private static boolean flag = false;
	private static ImageView view;
	private static Bitmap bp;

	/*
	 * ImageView的zoom图进入
	 */
	public static void imageViewZoomIn(Context context, View view) {
		wm = (WindowManager) context.getSystemService("window");
		drawZoomImage(context, view);
	}

	/*
	 * ImageSLideView的zoom图进入
	 */
	// public static void imageSlideViewZoomIn(Context context, View view) {
	// // 当前不是图片轮播的焦点，则切换成其焦点
	// if (ShortCut.isImageSlideZoom == false) {
	// drawZoomImage(context, view);
	// ShortCut.isImageSlideZoom = true;
	// }
	// }

	/*
	 * 显示ZoomImage
	 */
	public static void drawZoomImage(Context context, View view) {
		// 获取图像
		int[] location = new int[2];
		if (view == null) {
			Log.i(TAG, "view:null");
		}
		view.getLocationInWindow(location);
		int x = location[0];
		int y = location[1];
		// Log.i(TAG, "x:"+x+"y:"+y);
		bp = null;
		view.setDrawingCacheEnabled(true);
		// 图片为空，则跳出方法
		if (view.getDrawingCache() == null)
			return;
		bp = Bitmap.createBitmap(view.getDrawingCache());
		view.setDrawingCacheEnabled(false);

		// 放大后图像(图像(含阴影边框)总尺寸)
		Bitmap bpBigShadow = ImageUtil.zoomBitmap(bp, bp.getWidth()
				+ extendWidthShadow, bp.getHeight() + extendHeightShadow);

		// 放大后图像(图像单独尺寸)
		Bitmap bpBig = ImageUtil.zoomBitmap(bp, bp.getWidth() + extendWidth,
				bp.getHeight() + extendHeight);

		// 加圆角
		bpBig = ImageUtil.getRoundedCornerBitmap(bpBig, 2f);

		// 开始加阴影效果
		BlurMaskFilter blurFilter = new BlurMaskFilter(shadowSize,
				BlurMaskFilter.Blur.NORMAL);
		Paint shadowPaint = new Paint();
		shadowPaint.setMaskFilter(blurFilter);
		int[] offsetXY = new int[2];
		Bitmap shadowBitmap = bpBigShadow.extractAlpha(shadowPaint, offsetXY);
		Bitmap shadowImage32 = shadowBitmap.copy(Bitmap.Config.ARGB_8888, true);
		Canvas c = new Canvas(shadowImage32);
		c.drawBitmap(bpBig, shadowSize + (extendWidthShadow - extendWidth) / 2,
				shadowSize + (extendHeightShadow - extendHeight) / 2, null);
		BitmapDrawable bd = new BitmapDrawable(shadowImage32);
		Bitmap bm = bd.getBitmap();
		// ShortCut.zoomImageView.setVisibility(0);// 可见
		// ShortCut.zoomImageView.setImageResource(bd);
		// //设置晃动效果

		int afterX, afterY;
		afterX = x - shadowSize - extendWidthShadow / 2;
		afterY = y - shadowSize - extendHeightShadow / 2;
		// Log.i(TAG,
		// "  afterX:"+afterX+"  afterY:"+afterY+"  shadowSize:"+shadowSize+"extendWidthShadow:"+extendWidthShadow);
		// 设定位置尺寸=原来尺寸-边框阴影尺寸-总放大尺寸的一半
		// ShortCut.zoomImageView.setX(x - shadowSize - extendWidthShadow / 2);
		// ShortCut.zoomImageView.setY(y - shadowSize - extendHeightShadow / 2);
		createView(context, bm, afterX, afterY);
		// Animation an = AnimationUtils.loadAnimation(context,
		// R.anim.x1_to_x2);
		// ShortCut.zoomImageView.startAnimation(an);

		// AppManager.endX = ;
		// AppManager.endY = ;
		// TranslateAnimation an = new TranslateAnimation(AppManager.startX,
		// AppManager.endX, AppManager.startY, AppManager.endY);
		// an.setDuration(1000);
		// an.setFillAfter(true);
		// AppManager.zoomImageView.startAnimation(an);

	}

	private static void createView(Context context, Bitmap bm, float locx,
			float locy) {
		if (!flag) {
//			if(wmParams!=null){
//				
//			}else{
				wm = (WindowManager) context
						.getSystemService(Context.WINDOW_SERVICE);
				wmParams = new WindowManager.LayoutParams();
				// 设置LayoutParams(全局变量）相关参数
				wmParams.type = LayoutParams.TYPE_PHONE;
				wmParams.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
						| LayoutParams.FLAG_NOT_FOCUSABLE;
				wmParams.gravity = Gravity.LEFT | Gravity.TOP; // 调整悬浮窗口至中间
				DisplayMetrics dm = new DisplayMetrics();
				wm.getDefaultDisplay().getMetrics(dm);
				// wmParams.width = (int) (dm.widthPixels*0.93);
				wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
				wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
				wmParams.format = PixelFormat.RGBA_8888;
				view = new ImageView(context);
//			}
			flag = true;
			
			// wmParams.x = wm.getDefaultDisplay().getWidth()/2;
			// wmParams.y = -wm.getDefaultDisplay().getHeight()/4;
			wmParams.x = (int) locx;
			wmParams.y = (int) locy;
			// 设置悬浮窗口长宽数据
			
//			wmParams.windowAnimations = R.style.mywindowAnimation
			
			// Bitmap mDragBitmap = getBitmapByPath(R.drawable.blk_drag_bg+"");

			// BitmapDrawable bd_bg = new BitmapDrawable(mDragBitmap);
			view.setImageBitmap(bm);
			wm.addView(view, wmParams);
			ShortCut.OldView = view;
		}
	}

	public static void DestroyView() {
		if (flag && ShortCut.OldView != null) {
			wm.removeView(ShortCut.OldView);
			flag = false;

		}
	}

}
