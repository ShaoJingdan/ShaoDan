package com.chinamobile.hdcjs.view;

import com.chinamobile.hdcjs.R;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MyRoundProgressBar {
	Dialog dialog;
	Context context;
	// 声明ProgressBar对象
	private ProgressBar pro1;
	int mPerValue;
	int maxValue = 100;
//	TextView textView;
	boolean quitAuto=false;

	/**
	 * 构造
	 */
	public MyRoundProgressBar(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		dialog = new Dialog(context, R.style.CustomDialog);
		dialog.setOnCancelListener(onCancelListener);
	}

	/**
	 * 初始化进度对话框
	 */
	public void initDialog() {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.myround_progressbar, null);
		dialog.setContentView(view);
		pro1 = (ProgressBar) dialog.findViewById(R.id.progressBar2);
//		// 设置进度条是否自动旋转,即设置其不确定模式,false表示不自动旋转
		pro1.setIndeterminate(true);
//		// 设置ProgressBar的最大值
//		pro1.setMax(maxValue);
//
//		// 设置ProgressBar的当前值
//		pro1.setProgress(0);

		//调整对话框的位置，解决不同手机显示不一样的问题
		Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics dm = new DisplayMetrics(); 
        ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        dialogWindow.setGravity(Gravity.CENTER);
        //lp.x = 100; // 新位置X坐标
        //lp.y = 100; // 新位置Y坐标
        lp.width =(int) (dm.widthPixels*0.9);// DensityUtil.dip2px(context, 290); // 宽度
        //lp.height = DensityUtil.dip2px(context, 300); // 高度
        lp.alpha = 1.0f; // 透明度
        dialogWindow.setAttributes(lp);
		
		dialog.show();
	}
	
//	public void setText(String text) {
//		textView.setText(text);	
//	}
	
	public void setMaxValue(int value) {
		maxValue = value;
		pro1.setMax(maxValue);
	}

	public void setProgress(int progressValue) {
		Log.i("MyRoundProgressBar", "progressValue:"+progressValue);
		quitAuto=true;		
		if(progressValue<maxValue){
			pro1.setProgress(progressValue);
		}else {
			pro1.setProgress(maxValue);
			colseDialog();
		}		
	}
	
	/**
	 * 进度条自动增长
	 * @param perValue:每次增加的进度
	 * @param duration：每次增加进度用时
	 * @param toValue：增加到的最大值
	 */
	public boolean setProgress(int perValue,final long duration,int toValue) {
		quitAuto=false;
		final int currentProsess = pro1.getProgress();
		if (perValue<=0||duration<=0||toValue<=0||toValue<currentProsess||perValue>maxValue||toValue>maxValue) {
			return false;
		}
		mPerValue = perValue;
		final int nCount=(toValue-currentProsess)/perValue;
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = currentProsess; i <= nCount; i++) {					
					if (quitAuto) {
						break;
					}
					handler.sendEmptyMessage(0);
					try {
						Thread.sleep(duration);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
		return false;
	}
		
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// 改变ProgressBar的当前值
			int intCounter = getCurrentProsess();
			if (intCounter < maxValue) {
				pro1.setProgress(intCounter+mPerValue);	
			}else {
				pro1.setProgress(maxValue);
				colseDialog();
			}
		}

	};
	
	public int getCurrentProsess() {
		return pro1.getProgress();
	}
	
	public int getMax() {
		return maxValue;
	}
	
	public void colseDialog() {
		Log.i("MyRoundProgressBar", "dialog:"+dialog+"isShowing:"+isShowing());
		if (dialog!=null&&isShowing()) {
			quitAuto = true;
			dialog.dismiss();
		}		
	}

	public boolean isShowing() {
		if (dialog.isShowing()) {
			return true;
		} else {
			return false;
		}
	}

	OnCancelListener onCancelListener = new OnCancelListener() {
		@Override
		public void onCancel(DialogInterface dialog) {
			// TODO Auto-generated method stub
			dialog.dismiss();
		}
	};
}
