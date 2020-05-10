package com.pool.Weride;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.GoogleMap;


public class splash extends AppCompatActivity {
	Handler handler;
	
	private GoogleMap mMap;
	Location mLastLocation;
	LocationRequest mLocationRequest;
	
	private FusedLocationProviderClient mFusedLocationClient;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		
		handler = new Handler();
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = new Intent(splash.this, userType.class);
				startActivity(intent);
				finish();
			}
		}, 400);
		
	}
	
	
}
