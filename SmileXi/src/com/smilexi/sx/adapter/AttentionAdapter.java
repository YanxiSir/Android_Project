package com.smilexi.sx.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.smilexi.sx.R;
import com.smilexi.sx.protocol.SxAttentioned.Attentioned;
import com.smilexi.sx.util.Tool;

public class AttentionAdapter extends BaseAdapter {

	private Context mContext;
	private List<Attentioned> atts;

	private LayoutInflater mInflater;

	Attentioned item;

	public AttentionAdapter(Context mContext, List<Attentioned> atts) {
		super();
		this.mContext = mContext;
		this.atts = atts;
		mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return atts.size();
	}

	@Override
	public Object getItem(int arg0) {
		return atts.get(arg0);
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
			convertView = mInflater.inflate(R.layout.adapter_attention_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		item = atts.get(position);

		String path = item.getPortrait();
		holder.userImage.setTag(item.getPortrait());
		if (holder.userImage.getTag() != null
				&& holder.userImage.getTag().equals(path))
			Tool.imageLoader(mContext, holder.userImage, path, null);
		holder.username.setText(item.getUsername());
		if(!TextUtils.isEmpty(item.getSign())){
			holder.signText.setText(item.getSign());
		}

		return convertView;
	}

	private static class ViewHolder {
		private ImageView userImage;
		private TextView username, signText;

		ViewHolder(View v) {
			userImage = (ImageView) v.findViewById(R.id.iat_userimage);
			username = (TextView) v.findViewById(R.id.iat_username);
			signText = (TextView) v.findViewById(R.id.iat_usersign);
		}
	}

}
