package com.pool.Weride;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class ViewPagerAdapter extends PagerAdapter {
	
	private Context context;
	private LayoutInflater layoutInflater;
	private Integer[] images = {R.drawable.a, R.drawable.b, R.drawable.c, R.drawable.d};
	
	public ViewPagerAdapter(Context context) {
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return images.length;
	}
	
	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}
	
	@Override
	public Object instantiateItem(ViewGroup container, final int position) {
		layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.custom_layout, null);
		ImageView imageView = view.findViewById(R.id.imageView);
		imageView.setImageResource(images[position]);
		ViewPager vp = (ViewPager) container;
		vp.addView(view, 0);
		return view;
		
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		
		ViewPager vp = (ViewPager) container;
		View view = (View) object;
		vp.removeView(view);
		
	}
}
