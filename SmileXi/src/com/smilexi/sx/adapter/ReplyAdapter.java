package com.smilexi.sx.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smilexi.sx.R;
import com.smilexi.sx.activity.SelfMainPageActivity;
import com.smilexi.sx.protocol.SxDynamicReply.Reply;
import com.smilexi.sx.util.Tool;
import com.smilexi.sx.widget.RoundImageView;

public class ReplyAdapter extends BaseAdapter {

	List<Reply> replys;
	private Context mContext;
	private Handler mHandler;

	private LayoutInflater mInflater;

	Reply item;

	public ReplyAdapter(List<Reply> replys, Context mContext,Handler handler) {
		super();
		this.replys = replys;
		this.mContext = mContext;
		this.mHandler = handler;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return replys.size();
	}

	@Override
	public Object getItem(int arg0) {
		return replys.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (mContext == null)
			return convertView;

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.adapter_reply_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		item = replys.get(position);

		holder.username.setText(item.getFromUserName());

		if (item.getReplyTime() != null) {
			long times = Tool.getTime(item.getReplyTime());
			String timeIntro = times > 0 ? Tool.getChatTime(mContext, times)
					: "";
			holder.time.setText(timeIntro);
		}
		if (TextUtils.isEmpty(item.getToUserName())) {
			holder.toUserLl.setVisibility(View.GONE);
		} else {
			holder.toUserLl.setVisibility(View.VISIBLE);
			holder.toUsername.setText(item.getToUserName());
		}
		holder.content.setText(item.getContent());

		String path = item.getFromUserPortrait();
		holder.userImage.setTag(item.getFromUserPortrait());
		if (holder.userImage.getTag() != null
				&& holder.userImage.getTag().equals(path))
			Tool.imageLoader(mContext, holder.userImage, path, null);

		
		holder.userImage.setOnClickListener(new UserClickLis(item.getFromUserId()));
		holder.username.setOnClickListener(new UserClickLis(item.getFromUserId()));
		holder.replyBtn.setOnClickListener(new replyClickLis(item.getFromUserId(), item.getFromUserName()));
		
		return convertView;
	}
	private class replyClickLis implements OnClickListener{
		
		private int toUserId;
		private String toUserName;
		public replyClickLis(int toUserId, String toUserName) {
			super();
			this.toUserId = toUserId;
			this.toUserName = toUserName;
		}
		
		@Override
		public void onClick(View arg0) {
			int SEND_TO_SOMEONE = 10;
			Bundle b = new Bundle();
			b.putInt("uid", toUserId);
			b.putString("uname", toUserName);
			mHandler.obtainMessage(SEND_TO_SOMEONE, b).sendToTarget();
		}
	}
	private class UserClickLis implements OnClickListener {
		int uid;

		public UserClickLis(int uid) {
			super();
			this.uid = uid;
		}

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(mContext, SelfMainPageActivity.class);
			intent.putExtra("uid", uid);
			mContext.startActivity(intent);
		}
	}

	private static class ViewHolder {
		RoundImageView userImage;
		TextView username;
		TextView time;
		TextView replyBtn;
		LinearLayout toUserLl;
		TextView toUsername;
		TextView content;

		ViewHolder(View v) {
			userImage = (RoundImageView) v.findViewById(R.id.irp_userimage);
			username = (TextView) v.findViewById(R.id.irp_username);
			time = (TextView) v.findViewById(R.id.irp_time);
			replyBtn = (TextView) v.findViewById(R.id.irp_reply);
			toUserLl = (LinearLayout) v.findViewById(R.id.irp_touser_ll);
			toUsername = (TextView) v.findViewById(R.id.irp_touser_name);
			content = (TextView) v.findViewById(R.id.irp_content);
		}
	}

}
