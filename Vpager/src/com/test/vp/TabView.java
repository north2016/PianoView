package com.test.vp;

import java.util.Random;

import com.test.vpager.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

/**
 * 
 * @author 白小康 自定义View
 */
public class TabView extends FrameLayout {
	private RoundImageView image;// 头像
	int[] Ids = { R.drawable.s, R.drawable.a, R.drawable.e, R.drawable.bai,
			R.drawable.huang };

	public TabView(Context context, AttributeSet attrs) {
		super(context, attrs);
		LayoutInflater.from(context).inflate(R.layout.tab_item, this);
		image = (RoundImageView) findViewById(R.id.image);
		int n = new Random().nextInt(Ids.length);
		image.setImageResource(Ids[n]);
	}

}
