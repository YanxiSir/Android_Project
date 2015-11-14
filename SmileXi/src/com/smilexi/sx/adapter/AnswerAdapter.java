package com.smilexi.sx.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smilexi.sx.R;
import com.smilexi.sx.activity.AnswerDetailActivity;
import com.smilexi.sx.protocol.SxAnswers.Answer;
import com.smilexi.sx.util.Tool;

public class AnswerAdapter extends BaseAdapter {
	
	public final static int TYPE_ANSWERS = 0;
	public final static int TYPE_COLLECTED_ANSWERS = 1;
	
	
	private Context mContext;
	private List<Answer> answerList;
	private LayoutInflater mInflater;
	private int typ;
	
	Answer item;
	
	

	public AnswerAdapter(Context mContext, List<Answer> answerList,int typ) {
		super();
		this.mContext = mContext;
		this.answerList = answerList;
		this.typ = typ;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return answerList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return answerList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (mContext == null)
			return convertView;

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater
					.inflate(R.layout.adapter_answer_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		item = answerList.get(position);
		
		if(typ == TYPE_ANSWERS){
			holder.answerTitle.setVisibility(View.GONE);
		}else if(typ == TYPE_COLLECTED_ANSWERS){
			holder.answerTitle.setVisibility(View.VISIBLE);
			holder.answerTitle.setText(item.getqTitle());
		}
		
		String path = item.getUserPortrait();
		holder.userImage.setTag(item.getUserPortrait());
		if (holder.userImage.getTag() != null
				&& holder.userImage.getTag().equals(path))
			Tool.imageLoader(mContext, holder.userImage, path, null);
		holder.zanCount.setText(item.getZanCount()+"");
		holder.username.setText(item.getUserName());
		holder.answerTxt.setText(item.getContent());
		
		
		return convertView;
	}
	
	private class AnswerClickLis implements OnClickListener{
		
		private int aid;
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			
		}
	}
	
	private OnClickListener itemClickLis = new OnClickListener() {
		
		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(mContext,AnswerDetailActivity.class);
			Bundle b = new Bundle();
			b.putSerializable("answer", item); 
			intent.putExtras(b);
			mContext.startActivity(intent); 
		}
	};
	
	private static class ViewHolder {
		private ImageView userImage;
		private TextView zanCount;
		private TextView username;
		private TextView answerTxt;
		private TextView answerTitle;
		
		private LinearLayout mainLl;

		ViewHolder(View v) {
			userImage = (ImageView) v.findViewById(R.id.ians_image);
			zanCount = (TextView) v.findViewById(R.id.ians_zan_count);
			username = (TextView) v.findViewById(R.id.ians_name);
			answerTxt = (TextView) v.findViewById(R.id.ians_answer);
			mainLl = (LinearLayout) v.findViewById(R.id.ians_main_ll); 
			answerTitle = (TextView) v.findViewById(R.id.ians_title);
		}
	}

}
