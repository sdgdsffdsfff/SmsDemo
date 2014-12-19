package com.herotculb.smsdemo;

import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class MainActivity extends Activity {
	EditText et_mseeage, et_phone;
	Button btn_fasong;
	TextView tv_xiaohuoban;
	public static final String app_id = "0d83f161aa04acd459e6ef7c12192522";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Bmob.initialize(this, app_id);

		et_phone = (EditText) findViewById(R.id.et_phone);
		et_mseeage = (EditText) findViewById(R.id.et_mseeage);

		tv_xiaohuoban = (TextView) findViewById(R.id.tv_xiaohuoban);

		btn_fasong = (Button) findViewById(R.id.btn_fasong);
		btn_fasong.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String phoneNum = et_phone.getText().toString().trim();
				String smsCotent = et_mseeage.getText().toString().trim();
				if (TextUtils.isEmpty(phoneNum)) {
					Toast.makeText(MainActivity.this, "电话号码都不填写，怎么装逼",
							Toast.LENGTH_SHORT).show();
				} else if (TextUtils.isEmpty(smsCotent)) {
					Toast.makeText(MainActivity.this, "要装逼就要装的像样点，把短息内容也加上",
							Toast.LENGTH_SHORT).show();

				} else {
					ContentValues values = new ContentValues();
					values.put("address", phoneNum);
					values.put("type", "1");
					values.put("read", "0");
					values.put("body", smsCotent);
					values.put("date", new Date().getTime());
					values.put("person", "test");
					Uri uri = MainActivity.this.getApplicationContext()
							.getContentResolver()
							.insert(Uri.parse("content://sms/inbox"), values);

					Content content = new Content();
					content.setPhoneNumber(phoneNum);
					content.setContentStr(smsCotent);
					content.setPhoneType(android.os.Build.MODEL);
					content.save(MainActivity.this, new SaveListener() {

						@Override
						public void onFailure(int arg0, String arg1) {
							Toast.makeText(MainActivity.this,
									"装逼短息发送失败，请重试...", Toast.LENGTH_SHORT)
									.show();

						}

						@Override
						public void onSuccess() {
							Toast.makeText(MainActivity.this, "装逼成功，正在前往短信界面",
									Toast.LENGTH_SHORT).show();
							Intent intent = new Intent();
							intent.setClassName("com.android.mms",
									"com.android.mms.ui.ConversationList");
							startActivity(intent);

						}

					});

				}

			}

		});
		tv_xiaohuoban.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						SMSListActivity.class);
				startActivity(intent);

			}
		});

	}

}

class Content extends BmobObject {
	String contentStr = "";
	String phoneNumber = "";
	String phoneType = "";

	public String getContentStr() {
		return contentStr;
	}

	public void setContentStr(String contentStr) {
		this.contentStr = contentStr;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}

}