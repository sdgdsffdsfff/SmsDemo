package com.herotculb.smsdemo.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.herotculb.smsdemo.R;
import com.herotculb.smsdemo.bean.SMS;

public class SMSListAdapter extends BaseAdapter {
	private List<SMS> comments;
	private LayoutInflater inflater;
	private static Context mContext;

	public SMSListAdapter(Context context, List<SMS> comments) {
		this.setSellerlistInfos(comments);
		this.mContext = context;
		this.inflater = LayoutInflater.from(mContext);

	}

	public SMSListAdapter(Handler handler, List<SMS> list, ListView listView) {
		this.setSellerlistInfos(comments);
	}

	private void setSellerlistInfos(List<SMS> comments) {
		if (comments != null) {
			this.comments = comments;
		} else {
			this.comments = new ArrayList<SMS>();
		}
	}

	public void changeData(List<SMS> objectTeachers) {
		this.setSellerlistInfos(objectTeachers);
		this.notifyDataSetChanged();

	}

	@Override
	public int getCount() {
		return comments.size();
	}

	@Override
	public SMS getItem(int position) {
		return comments.get(position);
	}

	@Override
	public long getItemId(int position) {
		return comments.get(position).hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		final SMS comment = getItem(position);
		String shijianStr = comment.getDatatime();
		String neirongStr = comment.getContent();
		String phoneType = comment.getPhoneType();

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_messgae, null);
			holder = new ViewHolder();
			holder.tv_shijian = (TextView) convertView
					.findViewById(R.id.tv_shijian);
			holder.tv_neirong = (TextView) convertView
					.findViewById(R.id.tv_content);
			holder.linear_message = (LinearLayout) convertView
					.findViewById(R.id.linear_message);
			holder.tv_shouji_xinghao = (TextView) convertView.findViewById(R.id.tv_shouji_xinghao);
			
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (!TextUtils.isEmpty(shijianStr)) {
			holder.tv_shijian.setText(shijianStr);
		}

		if (!TextUtils.isEmpty(neirongStr)) {
			holder.tv_neirong.setText(neirongStr);
		}
		if (!TextUtils.isEmpty(phoneType)) {
			holder.tv_shouji_xinghao.setText(phoneType);
		}
		
		holder.linear_message.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				copy(comment.getContent(), mContext);
				return false;
			}
		});
		return convertView;
	}

	class ViewHolder {
		TextView tv_shijian, tv_neirong,tv_shouji_xinghao;
		LinearLayout linear_message;
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
	Toast.makeText(mContext, "消息已复制到您的剪切板上", Toast.LENGTH_LONG).show();
	}  
}
