package com.test.vp;

import java.util.ArrayList;
import java.util.List;

import com.test.vpager.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

@SuppressLint("NewApi")
public class MainActivity extends Activity {
	public static ViewPager mPager;
	private ArrayList<View> views;

	public int currIndex;// 当前页卡编号
	private int screenW;// 屏幕宽度
	LayoutInflater mInflater;
	List<String> mTabs;// 标签列表
	LinearLayout ll_tabs;// 标签容器

	private List<TabView> mTabViews;// 标签视图列表
	private List<Integer> mHeights;// 每个tab现在的高度
	private List<Integer> mOldHeights;// 每个tab原来的高度

	SlowScrollView hsrcoll;// 水平滚动
	int width = 0;// 应该滚动的距离
	int oldposition = 0;// 上一次停止时的位置
	int oldwidth = 0;// 上一次的滚动的距离
	boolean isRight = true;// 是否是向右滚动
	int speed = 1;// 滚动时的速度
	float oldx = 0;// 上一次手指触摸的位置

	private Handler handler = new Handler();
	private Runnable runnable = new Runnable() {
		public void run() {
			if (isRight) {// 向右滚
				oldwidth = oldwidth + speed;
				if (oldwidth <= width) {
					hsrcoll.smoothScrollTo(oldwidth, 0);
					handler.postDelayed(this, 10);// 更新
				} else {
					oldwidth = width;
				}

			} else {// 向左滚
				oldwidth = oldwidth - speed;
				if (oldwidth >= width) {
					hsrcoll.smoothScrollTo(oldwidth, 0);
					handler.postDelayed(this, 10);// 更新
				} else {
					oldwidth = width;
				}
			}

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		init();// 初始化
		InitTabViews();// 标签初始化
		InitViewPager();// viewpager初始化
	}

	private void init() {
		mInflater = LayoutInflater.from(MainActivity.this);
		ll_tabs = (LinearLayout) findViewById(R.id.ll_tabs);
		hsrcoll = (SlowScrollView) findViewById(R.id.rl_bottom);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		screenW = dm.widthPixels;

		String[] mStrings = { "我的", "关于", "发表", "空间", "关于", "发表", "空间", "关于",
				"发表", "空间", "关于", "发表", "空间", "关于", "发表", "空间", "关于", "发表",
				"发表", "空间", "关于", "发表", "空间", "关于", "发表", "空间", "关于", "发表",
				"发表", "空间", "关于", "发表", "空间", "关于", "发表", "空间", "关于", "发表",
				"空间", "关于", "关于", "发表", "空间", "关于", "发表", "空间", "关于", "发表" };
		mTabs = new ArrayList<String>();
		for (String s : mStrings) {
			mTabs.add(s);
		}
	}

	/**
	 * 初始化标签名
	 */
	public void InitTabViews() {
		mTabViews = new ArrayList<TabView>();
		mOldHeights = new ArrayList<Integer>();
		mHeights = new ArrayList<Integer>();
		for (int i = 0; i < mTabs.size(); i++) {
			mTabViews.add(setTabView());
			ll_tabs.addView(mTabViews.get(i));
			mOldHeights.add(65);
			mHeights.add(65);
			// 触摸事件
			mTabViews.get(i).setOnTouchListener(new MyTouchListener(i));
		}

	}

	/**
	 * 标签的触摸事件
	 * 
	 * @param index
	 */
	public void Touched(int index) {
		currIndex = index;
		mHeights = new ArrayList<Integer>();
		for (int i = 0; i < mTabViews.size(); i++) {
			if (i == index) {
				Animation translateAnimation = new TranslateAnimation(0, 0,
						mOldHeights.get(i), 0);
				translateAnimation.setFillAfter(true);
				translateAnimation
						.setInterpolator(AnimationUtils
								.loadInterpolator(
										this,
										android.R.anim.accelerate_decelerate_interpolator));
				// 设置动画时间
				translateAnimation.setDuration(150);
				mTabViews.get(i).startAnimation(translateAnimation);
				mHeights.add(0);
			} else if (Math.abs(i - currIndex) < 7) {
				mHeights.add(5 + 9 * Math.abs(i - currIndex));
				Animation translateAnimation = new TranslateAnimation(0, 0,
						mOldHeights.get(i), mHeights.get(i));
				translateAnimation.setFillAfter(true);
				translateAnimation.setInterpolator(AnimationUtils
						.loadInterpolator(this,
								android.R.anim.overshoot_interpolator));
				// 设置动画时间
				translateAnimation.setDuration(150);
				mTabViews.get(i).startAnimation(translateAnimation);
			} else {
				mHeights.add(65);
			}
		}
		mOldHeights = mHeights;
	}

	/**
	 * 设置选中tab
	 * 
	 * @param arg0
	 */
	public void Selected(int index) {// 设置当前tab
		currIndex = index;
		mPager.setCurrentItem(index, true);
		for (int i = 0; i < mTabViews.size(); i++) {
			if (i == index) {
				Animation translateAnimation = new TranslateAnimation(0, 0,
						mOldHeights.get(i), 0);
				translateAnimation.setFillAfter(true);
				translateAnimation
						.setInterpolator(AnimationUtils
								.loadInterpolator(
										this,
										android.R.anim.accelerate_decelerate_interpolator));
				// 设置动画时间
				translateAnimation.setDuration(150);
				mTabViews.get(i).startAnimation(translateAnimation);
			} else if (Math.abs(i - currIndex) < 7) {

				Animation translateAnimation = new TranslateAnimation(0, 0,
						mOldHeights.get(i), 65);
				translateAnimation.setFillAfter(true);
				translateAnimation.setInterpolator(AnimationUtils
						.loadInterpolator(this,
								android.R.anim.overshoot_interpolator));
				// 设置动画时间
				translateAnimation.setDuration(150);
				mTabViews.get(i).startAnimation(translateAnimation);
				mHeights.add(65);
			}else{
				mHeights.add(65);
			}
		}

		scrollTo();

	}

	/**
	 * 开始滚动
	 */
	private void scrollTo() {
		width = (screenW / 7) * (currIndex - 3);
		if (width > oldposition) {
			speed = (width - oldposition) / (screenW / 7) + 1;
			isRight = true;
			oldposition = width;
		} else if (width < oldposition) {
			speed = (oldposition - width) / (screenW / 7) + 1;
			isRight = false;
			oldposition = width;
		}
		handler.post(runnable);
	}

	/**
	 * 设置viewpager的item界面
	 * 
	 * @return view
	 */
	private View setView() {
		View view = mInflater.inflate(R.layout.viewpager_item, null);
		return view;
	}

	/**
	 * 设置标签
	 */
	private TabView setTabView() {
		TabView mTabView = new TabView(this, null);
		LinearLayout.LayoutParams lp;
		lp = new LinearLayout.LayoutParams(screenW / 7, 120);
		mTabView.setLayoutParams(lp);
		return mTabView;
	}

	/**
	 * 初始化ViewPager
	 */
	public void InitViewPager() {
		mPager = (ViewPager) findViewById(R.id.viewpager);
		views = new ArrayList<View>();
		for (int i = 0; i < mTabs.size(); i++) {
			views.add(setView());
		}

		// 给ViewPager设置适配器
		mPager.setAdapter(new MyViewPagerAdapter(views));
		mPager.setCurrentItem(0);// 设置当前显示标签页为第一页
		Selected(0);
		mPager.setOnPageChangeListener(new MyOnPageChangeListener());// 页面变化时的监听器
	}

	/**
	 * 滚动监听
	 * 
	 * @author Administrator
	 * 
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPx) {

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageSelected(int arg0) {
			Selected(arg0);

		}
	}

	/**
	 * 触摸事件
	 * 
	 * @author Administrator
	 * 
	 */

	public class MyTouchListener implements OnTouchListener {
		private int index = 0;

		public MyTouchListener(int i) {
			index = i;
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				oldx = event.getX();
				if (oldx % (screenW / 7) != (screenW / 7) / 2) {// 触摸位置偏移调整
					oldx = oldx - oldx % (screenW / 7) + (screenW / 7) / 2;
				}
				currIndex = index;
				Touched(currIndex);
				return true;

			case MotionEvent.ACTION_MOVE:
				float newx = event.getX();
				currIndex = index + (int) (newx - oldx) / (screenW / 7);
				Touched(currIndex);
				return true;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				Selected(currIndex);

				Vibrator mVibrator01 = (Vibrator) getSystemService(Service.VIBRATOR_SERVICE);
				mVibrator01.vibrate(new long[] { 10, 10 }, -1);
				return true;
			default:
				return false;
			}

		}
	}
}