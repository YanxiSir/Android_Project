package com.smilexi.sx.widget;


import com.smilexi.sx.util.CountDownTimer;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.TextView;

public class CountDownButton extends CountDownTimer {

	public static final int TIME_COUNT_FUTURE = 60000;
	public static final int TIME_COUNT_INTERVAL = 1000;

	private Context mContext;
	private TextView mButton;

	private String mOriginalText;
	private Drawable mOriginalBackground;
	private int mOriginalTextColor;

	private String mTickText;
	private Drawable mTickBackground;
	private int mTickTextColor;

	public CountDownButton() {
		super(TIME_COUNT_FUTURE, TIME_COUNT_INTERVAL);
	}

	public CountDownButton(long millisInFuture, long countDownInterval) {
		super(millisInFuture, countDownInterval);
	}

	public void init(Context context, TextView button, String tickText,
			Drawable tickBackground, int tickTextColor) {
		this.mContext = context;
		this.mButton = button;

		this.mOriginalText = String.valueOf(mButton.getText());
		this.mOriginalBackground = mButton.getBackground();
		this.mOriginalTextColor = mButton.getCurrentTextColor();

		this.mTickText = tickText;
		this.mTickBackground = tickBackground;
		this.mTickTextColor = tickTextColor;
	}

	public void init(Context context, Button button) {
		this.mContext = context;
		this.mButton = button;
		this.mOriginalText = String.valueOf(mButton.getText());
		this.mOriginalBackground = mButton.getBackground();
		this.mOriginalTextColor = mButton.getCurrentTextColor();
		this.mTickText = this.mOriginalText;
		this.mTickBackground = this.mOriginalBackground;
		this.mTickTextColor = this.mOriginalTextColor;
	}

	public void setTickDrawable(Drawable tickDrawable) {
		this.mTickBackground = tickDrawable;
	}

	@Override
	public void onFinish() {
		if (mContext != null && mButton != null) {
			mButton.setText(mOriginalText);
			mButton.setTextColor(mOriginalTextColor);
			mButton.setBackgroundDrawable(mOriginalBackground);
			mButton.setClickable(true);
			mButton.setText(mOriginalText);
		}
	}

	@Override
	public void onTick(long millisUntilFinished) {
		if (mContext != null && mButton != null) {
			mButton.setClickable(false);
			mButton.setBackgroundDrawable(mTickBackground);
			mButton.setTextColor(mTickTextColor);
			mButton.setText(mTickText + "(" + millisUntilFinished / 1000 + "s)");
		}
	}
}
