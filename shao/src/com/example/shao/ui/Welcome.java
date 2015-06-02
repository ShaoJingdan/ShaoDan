package com.example.shao.ui;

import cn.jpush.android.api.JPushInterface;

import com.example.shao.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class Welcome extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		//�������ر������ĺ���
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_welcome);
		new Handler().postDelayed(new Runnable()
		{
			@Override
			public void run() 
			{
				startActivity(new Intent(getApplication(),MainActivity.class));
				Welcome.this.finish();
			}			
		}, 5000);
	}
	
	@Override
	protected void onPause()
	{
		JPushInterface.onPause(this);
        super.onPause();
	}
	
	@Override
	protected void onResume()
	{
		JPushInterface.onResume(this);
		super.onResume();
		
	}
}
