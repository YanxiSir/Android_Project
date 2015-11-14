package com.smilexi.sx.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smilexi.sx.R;
import com.smilexi.sx.activity.DynamicDetail;
import com.smilexi.sx.activity.SelfMainPageActivity;
import com.smilexi.sx.common.SXContext;
import com.smilexi.sx.common.SmileXiApi.IApiCallback;
import com.smilexi.sx.protocol.SxDynamics.Dynamic;
import com.smilexi.sx.protocol.SxDynamics.Dynamic.Dynamic_Photos;
import com.smilexi.sx.request.domain.ZanDynamic;
import com.smilexi.sx.util.T;
import com.smilexi.sx.util.Tool;
import com.smilexi.sx.widget.CGridView;
import com.smilexi.sx.widget.ConfirmDialog;

public class DynamicAdapter extends BaseAdapter {
	
	public static final int REQUEST_LIFE_DYNAMIC_DETAIL = 10;

	private Context mContext;
	private LayoutInflater mInflater;
	private List<Dynamic> dynamicList;

	Dynamic item;
	int uid;

	public DynamicAdapter(Context mContext, List<Dynamic> dynamicList) {
		super();
		this.mContext = mContext;
		this.dynamicList = dynamicList;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return dynamicList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return dynamicList.get(arg0);
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
			convertView = mInflater
					.inflate(R.layout.adapter_dynamic_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		item = dynamicList.get(position);

		holder.content.setText(item.getDcontent());
		holder.nickName.setText(item.getUname());

		long times = Tool.getTime(item.getDtime());
		String timeIntro = times > 0 ? Tool.getChatTime(mContext, times) : "";
		holder.publishTime.setText("发布于:" + timeIntro);

		if (item.getReplysize() != 0)
			holder.replyText.setText(item.getReplysize() + "");
		else
			holder.replyText.setText("");
		if (item.getZanscount() != 0)
			holder.zanText.setText(item.getZanscount() + "");
		else
			holder.zanText.setText("");
		if (item.getIszan() == 0) {
			holder.zanImage.setImageDrawable(mContext.getResources()
					.getDrawable(R.drawable.zan_gray));
		} else {
			holder.zanImage.setImageDrawable(mContext.getResources()
					.getDrawable(R.drawable.zan_red));
		}

		String path = item.getUportrait();
		holder.userImage.setTag(item.getUportrait());
		if (holder.userImage.getTag() != null
				&& holder.userImage.getTag().equals(path))
			Tool.imageLoader(mContext, holder.userImage, path, null);

		List<String> photos = new ArrayList<String>();
		for (Dynamic_Photos dp : item.getDpics()) {
			photos.add(dp.getDpic());
		}

		if (photos.size() == 0) {
			holder.gridViewLl.setVisibility(View.GONE);
		} else {
			holder.gridViewLl.setVisibility(View.VISIBLE);
		}
		DynamicPhotoAdapter adapter = new DynamicPhotoAdapter(mContext,
				holder.gridView, photos, position);
		holder.gridView.setAdapter(adapter);

		uid = SXContext.getInstance().getUserInfo().getUserid();
		if (uid == item.getUid()) {
			holder.delLl.setVisibility(View.VISIBLE);
			holder.delImage.setOnClickListener(new DynamicDelClickLis(item
					.getDid(), position));
		} else {
			holder.delLl.setVisibility(View.GONE);
		}

		holder.nickName.setOnClickListener(new UserClickLis(item.getUid())); 
		holder.userImage.setOnClickListener(new UserClickLis(item.getUid()));
		holder.zanBtn.setOnClickListener(new zanClickLis(uid, item.getDid(),
				item.getIszan(), position));
		holder.replyBtn.setOnClickListener(new replyClickLis(item.getDid(),
				position));
		return convertView;
	}

	private class replyClickLis implements OnClickListener {
		int did;
		int position;

		public replyClickLis(int did, int position) {
			super();
			this.did = did;
			this.position = position;
		}

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(mContext, DynamicDetail.class);
			Bundle b = new Bundle();
			b.putSerializable("dynamic", dynamicList.get(position));
			b.putInt("detailType", DynamicDetail.DETAIL_LIFE);
			intent.putExtras(b);
			((Activity)mContext).startActivityForResult(intent,REQUEST_LIFE_DYNAMIC_DETAIL );
		}
	}

	private class zanClickLis implements OnClickListener {
		int uid;
		int did;
		int oid;
		int position;

		public zanClickLis(int uid, int did, int oid, int position) {
			super();
			this.uid = uid;
			this.did = did;
			this.oid = oid;
			this.position = position;
		}

		@Override
		public void onClick(View arg0) {
			if (oid == 1) {
				T.showShort(mContext, "已经点过了");
			} else {
				ZanDynamic params = new ZanDynamic();
				params.setDid(did);
				params.setUid(uid);
				params.setOid(1);
				SXContext.getInstance().getmSmileXiApi()
						.zanDynamic(params, new IApiCallback() {

							@Override
							public void onError(int errorCode) {
								if (errorCode == -2) {
									T.showShort(mContext, "已经点过了");
								}
							}

							@Override
							public void onComplete(Object object) {
								dynamicList.get(position).setIszan(1);
								int zanCount = dynamicList.get(position)
										.getZanscount();
								dynamicList.get(position).setZanscount(
										zanCount + 1);
								notifyDataSetChanged();
							}
						});
			}

		}
	}

	private class DynamicDelClickLis implements OnClickListener {
		int did;
		int position;

		ConfirmDialog delDialog;

		public DynamicDelClickLis(int did, int position) {
			super();
			this.did = did;
			this.position = position;
		}

		@Override
		public void onClick(View arg0) {

			delDialog = new ConfirmDialog(mContext);
			delDialog.setTitle("提示");
			delDialog.setMessage("确认删除？");
			delDialog.setLeftButton("取消", new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					delDialog.dismiss();
				}
			});
			delDialog.setRightButton("确定", new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					delDialog.dismiss();
					SXContext.getInstance().getmSmileXiApi()
							.delDynamic(did, new IApiCallback() {

								@Override
								public void onError(int errorCode) {
									T.showShort(mContext, "删除失败");
								}

								@Override
								public void onComplete(Object object) {
									T.showShort(mContext, "删除成功");
									dynamicList.remove(position);
									notifyDataSetChanged();
								}
							});
				}
			});

		}
	}

	private class UserClickLis implements OnClickListener {
		int uuid;

		public UserClickLis(int uuid) {
			super();
			this.uuid = uuid;
		}

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(mContext, SelfMainPageActivity.class);
			intent.putExtra("uid", uuid);
			mContext.startActivity(intent);
		}
	}

	private OnClickListener userClickLis = new OnClickListener() {

		@Override
		public void onClick(View arg0) {
			Intent intent = new Intent(mContext, SelfMainPageActivity.class);
			intent.putExtra("uid", item.getUid());
			mContext.startActivity(intent);
		}
	};

	private static class ViewHolder {

		private TextView content;
		private CGridView gridView;
		private ImageView userImage;
		private TextView nickName;
		private TextView publishTime;
		private RelativeLayout replyBtn, zanBtn;
		private LinearLayout gridViewLl;

		private LinearLayout delLl;
		private ImageView delImage, replyImage, zanImage;
		private TextView replyText, zanText;

		ViewHolder(View v) {
			content = (TextView) v.findViewById(R.id.idy_content);
			gridView = (CGridView) v.findViewById(R.id.idy_gridview);
			userImage = (ImageView) v.findViewById(R.id.idy_userImg);
			nickName = (TextView) v.findViewById(R.id.idy_userNick);
			publishTime = (TextView) v.findViewById(R.id.idy_time);
			replyBtn = (RelativeLayout) v.findViewById(R.id.idy_pinglun);
			zanBtn = (RelativeLayout) v.findViewById(R.id.idy_zan);

			gridViewLl = (LinearLayout) v.findViewById(R.id.idy_gridview_ll);

			delLl = (LinearLayout) v.findViewById(R.id.idy_del_ll);
			delImage = (ImageView) v.findViewById(R.id.idy_del_image);

			replyImage = (ImageView) v.findViewById(R.id.idy_pinglun_image);
			zanImage = (ImageView) v.findViewById(R.id.idy_zan_image);
			replyText = (TextView) v.findViewById(R.id.idy_pinglun_text);
			zanText = (TextView) v.findViewById(R.id.idy_zan_text);
		}
	}

}
