package com.test.vp;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.HorizontalScrollView;

public class SlowScrollView extends HorizontalScrollView {

	public SlowScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public SlowScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public SlowScrollView(Context context) {
		super(context);
	}

	/**
	 * 滑动事件
	 */
	@Override
	public void fling(int velocityX) {
		super.fling(velocityX / 4);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			return false;
		} else {
			float absX = Math.abs(ev.getX());
			float absY = Math.abs(ev.getY());
			if (absX > absY) {// 水平

				return false;
			}
		}

		return super.onInterceptTouchEvent(ev);
	}

}
