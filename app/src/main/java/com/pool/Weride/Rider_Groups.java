package com.pool.Weride;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Rider_Groups extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rider__groups);
		
		getSupportActionBar().setTitle("Rider Groups");
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		
	}
}
