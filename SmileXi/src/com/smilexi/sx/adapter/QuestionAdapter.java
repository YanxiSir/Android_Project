package com.smilexi.sx.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smilexi.sx.R;
import com.smilexi.sx.activity.DynamicDetail;
import com.smilexi.sx.protocol.SxQuestions.Question;

public class QuestionAdapter extends BaseAdapter {
	
	public static final int REQUEST_QUESTION_DYNAMIC_DETAIL = 11;

	private Context mContext;
	private List<Question> questionList;

	private LayoutInflater mInflater;

	public QuestionAdapter(Context mContext, List<Question> questionList) {
		super();
		this.mContext = mContext;
		this.questionList = questionList;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return questionList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return questionList.get(arg0);
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
			convertView = mInflater.inflate(R.layout.adapter_question_item,
					null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Question item = questionList.get(position);

		holder.typeText.setText("来自 " + item.getQtypename());
		holder.title.setText(item.getQtitle());
		holder.attendCount.setText(item.getAttencount() + "人关注了该问题");
		holder.username.setText(item.getUname() + " 提问");
		
		holder.title.setOnClickListener(new quesTitleClickLis(item.getQid(), position));

		return convertView;
	}
	private class quesTitleClickLis implements OnClickListener {
		int qid;
		int position;

		public quesTitleClickLis(int qid, int position) {
			super();
			this.qid = qid;
			this.position = position;
		}

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(mContext, DynamicDetail.class);
			Bundle b = new Bundle();
			b.putSerializable("dynamic", questionList.get(position));
			b.putInt("detailType", DynamicDetail.DETAIL_QUESTION);
			intent.putExtras(b);
			((Activity)mContext).startActivityForResult(intent,REQUEST_QUESTION_DYNAMIC_DETAIL);
		}
	}

	private static class ViewHolder {

		private LinearLayout typeLl;
		private TextView typeText;
		private TextView title;
		private TextView attendCount;
		private TextView username;

		ViewHolder(View v) {
			typeLl = (LinearLayout) v.findViewById(R.id.iqa_type_ll);
			typeText = (TextView) v.findViewById(R.id.iqa_type);
			title = (TextView) v.findViewById(R.id.iqa_title);
			attendCount = (TextView) v.findViewById(R.id.iqa_count);
			username = (TextView) v.findViewById(R.id.iqa_username);
		}
	}

}
