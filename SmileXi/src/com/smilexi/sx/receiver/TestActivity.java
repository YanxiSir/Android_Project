package com.smilexi.sx.receiver;

import com.smilexi.sx.app.BaseActivity;

import cn.jpush.android.api.JPushInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public class TestActivity extends BaseActivity {
	/*
	 * typ:	0 关注用户 {uid,createTime}
			1 点赞生活动态 {did,uid,createTime}
			2 赞答案 {aid,uid,createTime}
			3 回复生活动态 {did,uid,createTime}
			4 回答问题 {qid,uid,createTime}
			5 评论答案 {aid,uid,createTime}
	 * */
	public final static int TYPE_ATTENTION_USER = 0;
	public final static int TYPE_ZAN_DYNAMIC = 1;
	public final static int TYPE_ZAN_ANSWER = 2;
	public final static int TYPE_REPLY_DYNAMIC = 3;
	public final static int TYPE_ANSWER_QUESTION = 4;
	public final static int TYPE_REPLY_ANSWER = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tv = new TextView(this);
        tv.setText("鐢ㄦ埛鑷畾涔夋墦寮?鐨凙ctivity");
        Intent intent = getIntent();
        if (null != intent) {
	        Bundle bundle = getIntent().getExtras();
	        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
	        String content = bundle.getString(JPushInterface.EXTRA_ALERT);
	        String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
	        tv.setText("Title : " + title + "  " + "Content : " + content+"  \n"+"Extra:"+extra);
        }
        addContentView(tv, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
    }

}
