package com.smilexi.sx.adapter;

import java.util.List;

import com.smilexi.sx.R;
import com.smilexi.sx.activity.DynamicDetail;
import com.smilexi.sx.protocol.SxDynamics.Dynamic;
import com.smilexi.sx.protocol.SxNotiReply.NotiReply;
import com.smilexi.sx.protocol.SxNotiZan.NotiZan;
import com.smilexi.sx.request.domain.ZanDynamic;
import com.smilexi.sx.util.Tool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class NotifacationAdapter extends BaseAdapter {

	public static final int TYPE_DYNAMIC_REPLY = 0;
	public static final int TYPE_DYNAMIC_ZAN = 1;

	private Context mContext;
	private LayoutInflater mInflater;
	private int type;

	private List<NotiReply> notiReplyList;
	private NotiReply item;

	private List<NotiZan> notiZanList;
	private NotiZan itemZan;

	public NotifacationAdapter(Context mContext, List<NotiReply> notiReplyList,
			int type) {
		super();
		this.mContext = mContext;
		this.notiReplyList = notiReplyList;
		this.type = type;
		mInflater = LayoutInflater.from(mContext);
	}

	public NotifacationAdapter(Context mContext, int type,
			List<NotiZan> notiZanList) {
		super();
		this.mContext = mContext;
		this.type = type;
		this.notiZanList = notiZanList;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		if (type == TYPE_DYNAMIC_REPLY)
			return notiReplyList.size();
		else if (type == TYPE_DYNAMIC_ZAN)
			return notiZanList.size();
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		if (type == TYPE_DYNAMIC_REPLY)
			return notiReplyList.get(arg0);
		else if (type == TYPE_DYNAMIC_ZAN)
			return notiZanList.get(arg0);
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {

		if (mContext == null)
			return convertView;

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.adapter_notifacation_item,
					null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (type == TYPE_DYNAMIC_REPLY) {
			item = notiReplyList.get(position);

			String path = item.getfPortrait();
			holder.userImage.setTag(item.getfPortrait());
			if (holder.userImage.getTag() != null
					&& holder.userImage.getTag().equals(path))
				Tool.imageLoader(mContext, holder.userImage, path, null);
			holder.userName.setText(item.getfName());

			long times = Tool.getTime(item.getReplyTime());
			String timeIntro = times > 0 ? Tool.getChatTime(mContext, times)
					: "";
			holder.time.setText(timeIntro);

			Dynamic dynamic = item.getDynamic();

			String dPath = "";
			if (dynamic.getDpics().size() == 0) {
				dPath = dynamic.getUportrait();
				holder.dynamicImage.setTag(dynamic.getUportrait());
			} else {
				dPath = dynamic.getDpics().get(0).getDpic();
				holder.dynamicImage.setTag(dynamic.getDpics().get(0).getDpic());
			}

			if (holder.dynamicImage.getTag() != null
					&& holder.dynamicImage.getTag().equals(dPath))
				Tool.imageLoader(mContext, holder.dynamicImage, dPath, null);
			holder.dynamicUsername.setText(dynamic.getUname());
			holder.dynamicContent.setText(dynamic.getDcontent());

			if (item.getReplyType() == 0
					|| TextUtils.isEmpty(item.getLastContent())) {
				holder.dynamicReplyContent.setVisibility(View.GONE);
				holder.dynamicReplyLl.setBackgroundColor(mContext
						.getResources().getColor(R.color.white));
				holder.dynamicLl.setBackgroundDrawable(mContext.getResources()
						.getDrawable(R.drawable.line_bg_deep));

				if (item.getReplyType() == 0)
					holder.replyContent.setText(item.getContent());
				else if (item.getReplyType() == 1) {
					String newContent = "";
					newContent = item.getfName() + " 回复 我 :"
							+ item.getContent();
					holder.replyContent.setText(newContent);
				}
			} else if (item.getReplyType() == 1
					&& !TextUtils.isEmpty(item.getLastContent())) {

				if (TextUtils.isEmpty(item.getLastContent()))

					holder.dynamicLl.setBackgroundDrawable(mContext
							.getResources().getDrawable(
									R.drawable.line_bg_light));
				holder.dynamicReplyContent.setVisibility(View.VISIBLE);
				holder.dynamicReplyLl.setBackgroundDrawable(mContext
						.getResources().getDrawable(R.drawable.line_bg_deep));

				String lastContent = "";
				lastContent = "我 回复 " + item.getfName() + ":"
						+ item.getLastContent();
				holder.dynamicReplyContent.setText(lastContent);

				String newContent = "";
				newContent = item.getfName() + " 回复 我 :" + item.getContent();
				holder.replyContent.setText(newContent);

			}
			holder.dynamicLl.setOnClickListener(new DynamicClickLis(item
					.getDynamic().getDid(), position));
		} else if (type == TYPE_DYNAMIC_ZAN) {
			
			
			
			itemZan = notiZanList.get(position);

			String path = itemZan.getfPortrait();
			holder.userImage.setTag(itemZan.getfPortrait());
			if (holder.userImage.getTag() != null
					&& holder.userImage.getTag().equals(path))
				Tool.imageLoader(mContext, holder.userImage, path, null);
			holder.userName.setText(itemZan.getfName());

			long times = Tool.getTime(itemZan.getZanTime());
			String timeIntro = times > 0 ? Tool.getChatTime(mContext, times)
					: "";
			holder.time.setText(timeIntro);

			Dynamic dynamic = itemZan.getDynamic();

			String dPath = "";
			if (dynamic.getDpics().size() == 0) {
				dPath = dynamic.getUportrait();
				holder.dynamicImage.setTag(dynamic.getUportrait());
			} else {
				dPath = dynamic.getDpics().get(0).getDpic();
				holder.dynamicImage.setTag(dynamic.getDpics().get(0).getDpic());
			}

			if (holder.dynamicImage.getTag() != null
					&& holder.dynamicImage.getTag().equals(dPath))
				Tool.imageLoader(mContext, holder.dynamicImage, dPath, null);
			holder.dynamicUsername.setText(dynamic.getUname());
			holder.dynamicContent.setText(dynamic.getDcontent());

			holder.dynamicReplyContent.setVisibility(View.GONE);
			holder.dynamicReplyLl.setBackgroundColor(mContext.getResources()
					.getColor(R.color.white));
			holder.dynamicLl.setBackgroundDrawable(mContext.getResources()
					.getDrawable(R.drawable.line_bg_deep));

			String newContent = "";
			newContent = itemZan.getfName() + " 赞了你的动态";
			holder.replyContent.setText(newContent);

			holder.dynamicLl.setOnClickListener(new DynamicClickLis(itemZan
					.getDynamic().getDid(), position));
		}

		return convertView;
	}

	private class DynamicClickLis implements OnClickListener {
		int did;
		int position;

		public DynamicClickLis(int did, int position) {
			super();
			this.did = did;
			this.position = position;
		}

		@Override
		public void onClick(View arg0) {
			Dynamic dynamic = null;
			if (type == TYPE_DYNAMIC_REPLY)
				dynamic = notiReplyList.get(position).getDynamic();
			else if (type == TYPE_DYNAMIC_ZAN)
				dynamic = notiZanList.get(position).getDynamic();
			Intent intent = new Intent(mContext, DynamicDetail.class);
			Bundle b = new Bundle();
			b.putSerializable("dynamic", dynamic);
			b.putInt("detailType", DynamicDetail.DETAIL_LIFE);
			intent.putExtras(b);
			((Activity) mContext).startActivity(intent);
		}
	}

	private class ViewHolder {
		private ImageView userImage, dynamicImage;
		private TextView userName, time, replyBtn, replyContent,
				dynamicReplyContent, dynamicUsername, dynamicContent;
		private LinearLayout dynamicReplyLl, dynamicLl;

		public ViewHolder(View v) {
			userImage = (ImageView) v.findViewById(R.id.ani_user_image);
			dynamicImage = (ImageView) v.findViewById(R.id.ani_dynamic_image);
			userName = (TextView) v.findViewById(R.id.ani_username);
			time = (TextView) v.findViewById(R.id.ani_reply_time);
			replyBtn = (TextView) v.findViewById(R.id.ani_reply_btn);
			replyContent = (TextView) v.findViewById(R.id.ani_reply_content);
			dynamicReplyContent = (TextView) v
					.findViewById(R.id.ani_dynamic_reply_content);
			dynamicUsername = (TextView) v
					.findViewById(R.id.ani_dynamic_username);
			dynamicContent = (TextView) v
					.findViewById(R.id.ani_dynamic_content);
			dynamicReplyLl = (LinearLayout) v
					.findViewById(R.id.ani_dynamic_reply_ll);
			dynamicLl = (LinearLayout) v.findViewById(R.id.ani_dynamic_ll);
		}

	}

}
