package com.herotculb.smsdemo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

import com.herotculb.smsdemo.adapter.SMSListAdapter;
import com.herotculb.smsdemo.bean.SMS;
import com.herotculb.smsdemo.xlist.XListView;
import com.herotculb.smsdemo.xlist.XListView.IXListViewListener;
import com.nhaarman.listviewanimations.swinginadapters.AnimationAdapter;
import com.nhaarman.listviewanimations.swinginadapters.prepared.SwingBottomInAnimationAdapter;

public class SMSListActivity extends Activity implements IXListViewListener {
	protected static final int REFRESHING = 0;
	protected static final int LOADMOREING = 1;
	private XListView lv;

	List<SMS> list = null;
	private SMSListAdapter adapter;
	SMS sms = null;
	ImageButton btn_black;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sms_list);

		adapter = new SMSListAdapter(SMSListActivity.this, list);
		initView();
		lv.setAdapter(adapter);
		list = new ArrayList<SMS>();
		getUpdata();

	}

	int pageNum = 0;
	int pageSize = 10;

	/**
	 * 加载数据
	 * 
	 */
	private void getUpdata() {
		BmobQuery<Content> query = new BmobQuery<Content>();
		query.setLimit(pageSize); // 限制最多10条数据结果作为一页
		query.setSkip(pageSize * (pageNum++)); // 忽略前10条数据（即第一页数据结果）
		query.findObjects(SMSListActivity.this, new FindListener<Content>() {
			@Override
			public void onSuccess(List<Content> data) {

				if (data.size() != 0) {
					if (pageNum == 1)
						list.clear();
					for (int i = 0; i < data.size(); i++) {
						sms = new SMS();
						sms.setContent(data.get(i).getContentStr());
						sms.setDatatime(data.get(i).getCreatedAt());
						sms.setPhoneType(data.get(i).getPhoneType());

						System.out.println("---------"
								+ data.get(i).getContentStr() + "---------"
								+ data.get(i).getCreatedAt());
						list.add(sms);
						Collections.reverse(list);
					}
					adapter.changeData(list);
				}

				lv.stop();

			}

			@Override
			public void onError(int code, String msg) {
				lv.stop();
			}
		});
	}

	/**
	 * 下拉刷新
	 */

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		pageNum = 0;
		getUpdata();
	}

	/**
	 * 加载更多
	 */
	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		getUpdata();
	}

	private void initView() {
		lv = (XListView) findViewById(R.id.lv);
		btn_black = (ImageButton) findViewById(R.id.btn_black);
		lv.setPullRefreshEnable(true);// 开启下拉刷新
		lv.setPullLoadEnable(true);// 开启上滑加载更多
		lv.setXListViewListener(SMSListActivity.this);// 设置监听器
		
		btn_black.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			finish();
				
			}
		});

		AnimationAdapter animAdapter = new SwingBottomInAnimationAdapter(
				adapter);
		animAdapter.setAbsListView(lv);
		lv.setAdapter(animAdapter);
		
		
	}
	
	/** 
	* 实现文本复制功能 
	* add by wangqianzhou 
	* @param content 
	*/  
	public static void copy(String content, Context context)  
	{  
	// 得到剪贴板管理器  
	ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);  
	cmb.setText(content.trim());  
	}  
	/** 
	* 实现粘贴功能 
	* add by wangqianzhou 
	* @param context 
	* @return 
	*/  
	public static String paste(Context context)  
	{  
	// 得到剪贴板管理器  
	ClipboardManager cmb = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);  
	return cmb.getText().toString().trim();  
	}  
}