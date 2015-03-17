package com.chinamobile.hdcjs.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.chinamobile.hdcjs.R;

public class RegisterActivity extends Activity {

	private static final String TAG = "Register";

	private EditText phone;
	private Button captcha;
	private EditText captcha_et;
	private EditText pwd;
	private Button register;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		phone = (EditText) findViewById(R.id.reg_phone);
		captcha = (Button) findViewById(R.id.captcha);
		captcha_et = (EditText) findViewById(R.id.captcha_et);
		pwd = (EditText) findViewById(R.id.pwd);
		register = (Button) findViewById(R.id.register);

		captcha.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.i(TAG, phone.getText().toString());
				if (phone.getText().toString().equals("")) {
					Toast.makeText(RegisterActivity.this, "请填写手机号", Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(RegisterActivity.this,
							"正在发送验证码到" + phone.getText().toString(),
							Toast.LENGTH_SHORT).show();
				}
			}
		});

		register.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (phone.getText().toString().equals("")) {
					Toast.makeText(RegisterActivity.this, "请填写手机号", Toast.LENGTH_SHORT)
							.show();
				} else if (captcha_et.getText().toString().equals("")) {
					Toast.makeText(RegisterActivity.this, "请填写验证码", Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(RegisterActivity.this, "点击注册", Toast.LENGTH_SHORT)
							.show();
					// TODO 调用终端客户端服务
					// 执行注册流程
				}

			}
		});

		register.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View arg0, boolean arg1) {
				if (arg1) {
					register.setBackgroundResource(R.drawable.bg_btn1_focus);
				} else {
					register.setBackgroundResource(R.drawable.bg_btn1_normal);
				}
			}
		});

	}

}
