package com.pool.Weride;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.Timer;
import java.util.TimerTask;

public class property_details extends AppCompatActivity {
	
	ViewPager viewPager;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_property_details);
		
		viewPager = findViewById(R.id.viewPager);
		ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this);
		viewPager.setAdapter(viewPagerAdapter);
		
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(new MyTimerTask(), 1000, 4000);
		
		TextView tv1 = findViewById(R.id.title);
		
		TextView tv2 = findViewById(R.id.address);
		/*TextView tv3 = findViewById(R.id.purpose);
		TextView tv4 = findViewById(R.id.type);
		
		TextView tv5 = findViewById(R.id.unit);
		*/
		TextView tv6 = findViewById(R.id.area);
		
		TextView tv7 = findViewById(R.id.price);
		TextView tv8 = findViewById(R.id.city);
		
		TextView tv9 = findViewById(R.id.location);
		TextView tv10 = findViewById(R.id.description);
		
		TextView tv11 = findViewById(R.id.phone11);
		TextView tv12 = findViewById(R.id.email);
		
		TextView tv13 = findViewById(R.id.bedroom);
		TextView tv14 = findViewById(R.id.bathroom);
		TextView tv15 = findViewById(R.id.kitchen);
		
		String st1 = tv1.getText().toString();
		String st2 = tv2.getText().toString();
	/*	String st3=tv3.getText().toString();
		String st4=tv4.getText().toString();
		String st5=tv5.getText().toString();
	*/
		String st6 = tv6.getText().toString();
		String st7 = tv7.getText().toString();
		String st8 = tv8.getText().toString();
		String st9 = tv9.getText().toString();
		String st10 = tv10.getText().toString();
		String st11 = tv11.getText().toString();
		String st12 = tv12.getText().toString();
		String st13 = tv13.getText().toString();
		
		String st14 = tv14.getText().toString();
		String st15 = tv15.getText().toString();
		
		
		Bundle gt = getIntent().getExtras();
		String str1 = gt.getString("1");
		String str2 = gt.getString("2");
	/*	String str3=gt.getString("3");
		String str4=gt.getString("4");
		String str5=gt.getString("5");
	*/
		String str6 = gt.getString("6");
		String str7 = gt.getString("7");
		String str8 = gt.getString("8");
		String str9 = gt.getString("9");
		String str10 = gt.getString("10");
		String str11 = gt.getString("11");
		String str12 = gt.getString("12");
		String str13 = gt.getString("13");
		String str14 = gt.getString("14");
		String str15 = gt.getString("15");
		
		
		st1 = st1.concat(str1);
		st2 = st2.concat(str2);
	/*	st3=st3.concat(str3);
		st4=st4.concat(str4);
		st5=st5.concat(str5);
	*/
		st6 = st6.concat(str6);
		st7 = st7.concat(str7);
		st8 = st8.concat(str8);
		st9 = st9.concat(str9);
		st10 = st10.concat(str10);
//		st11=st11.concat(str11);
		st12 = st12.concat(str12);
		st13 = st13.concat(str13);
		st14 = st14.concat(str14);
		st15 = st15.concat(str15);
		
		
		tv1.setText(st1);
		tv2.setText(st2);
	/*	tv3.setText(st3);
		tv4.setText(st4);
		tv5.setText(st5);
	*/
		tv6.setText(st6);
		tv7.setText(st7);
		tv8.setText(st8);
		tv9.setText(st9);
		tv10.setText(st10);
		tv11.setText(st11);
		tv12.setText(st12);
		tv13.setText(st13);
		tv14.setText(st14);
		tv15.setText(st15);
		
		
	}
	
	public class MyTimerTask extends TimerTask {
		
		@Override
		public void run() {
			property_details.this.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					if (viewPager.getCurrentItem() == 0) {
						viewPager.setCurrentItem(1);
					} else if (viewPager.getCurrentItem() == 1) {
						viewPager.setCurrentItem(2);
					} else if (viewPager.getCurrentItem() == 2) {
						viewPager.setCurrentItem(3);
					} else {
						viewPager.setCurrentItem(0);
					}
				}
			});
		}
	}
}
