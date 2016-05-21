package com.panzhiming.viewpage;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
	private ViewPager viewPager;
	private TextView tvDesc;
	private int[] imageResIds;
	private List<ImageView> imageList;
	private String[] contextDescs;
	private LinearLayout llPointContainer;
	
	private boolean isRunning;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initViews();

		initData();

		initAdapter();
		
		
		//自动轮询
		new Thread(){
			public void run() {
				isRunning = true;
				while(isRunning){
					try {
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					runOnUiThread(new Runnable() {
						public void run() {
							viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
						}
					});
				}
			};
		}.start();

	}

	private void initViews() {
		viewPager = (ViewPager) findViewById(R.id.view_pager);
		
		//位viewpager设置监听器
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				
				int newPosition = position % imageList.size();
				
				//选中时调用
				tvDesc.setText(contextDescs[newPosition]);
				setEnable(newPosition);
				
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				//滚动时调用
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				//滚动状态发生变化是调用
				
			}
		});
		tvDesc = (TextView) findViewById(R.id.tv_desc);
		llPointContainer = (LinearLayout) findViewById(R.id.ll_point_container);
	}

	private void initData() {
		
		imageResIds = new int[] { R.drawable.a, R.drawable.b, R.drawable.c,
				R.drawable.d, R.drawable.e };
		// 添加图片。圆点
		ImageView imageView;
		imageList = new ArrayList<ImageView>();
		View point;
		
		for (int i = 0; i < imageResIds.length; i++) {
			imageView = new ImageView(this);
			imageView.setBackgroundResource(imageResIds[i]);
			imageList.add(imageView);
			
			point = new View(MainActivity.this);
			point.setBackgroundResource(R.drawable.selector_bg_point);
			
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(5, 5);
			if(i != 0){
				params.leftMargin=10;
				point.setEnabled(false);
			}
			llPointContainer.addView(point, params);
		}

		//添加文字
		contextDescs = new String[] { "爱情呼叫转移", "爱你一万你", "可惜不是你", "任瑟韩国", "谁是爱情里的主角"
				
		};
		
		
		
	}

	private void initAdapter() {
		
		
		tvDesc.setText(contextDescs[0]);
		viewPager.setAdapter(new MyAdapter());
		
//		int pos = Integer.MAX_VALUE/2-Integer.MAX_VALUE/2%5;
//		Log.i("edu", "pos"+pos);
		
		viewPager.setCurrentItem(5000000);
	}

	private class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {

			return Integer.MAX_VALUE;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {

			return view == object;
		}

		// 添加要显示 的条目 增加条目
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			
			int newPosition = position % imageList.size();
			
			ImageView imageView = imageList.get(newPosition);
			container.addView(imageView);
			return imageView;
		}

		// 删除条目
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((ImageView) object);
		}

	}
	
	private void setEnable(int position){
		for (int i = 0; i < llPointContainer.getChildCount(); i++) {
			llPointContainer.getChildAt(i).setEnabled(false);
		}
		
		llPointContainer.getChildAt(position).setEnabled(true);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		isRunning=false;
	}
}
