package com.pool.Weride.Activities.Auth.PhoneNumber;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pool.Weride.Users.Driver.NavHome.driver_nav_drawer;
import com.pool.Weride.R;
import com.pool.Weride.Users.Rider.NavHome.rider_nav_layout;


public class PhoneNumberActivity extends AppCompatActivity {
	
	
	private EditText editTextMobile;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone_number);
		
		editTextMobile = findViewById(R.id.editTextMobile);
		
		SharedPreferences mSharedPreferences = getSharedPreferences("phone", MODE_PRIVATE);
		String verfied = "";
		String type = "";
		verfied = mSharedPreferences.getString("phone", "");
		
		SharedPreferences mSharedPreferences1 = getSharedPreferences("type", MODE_PRIVATE);
		type = mSharedPreferences1.getString("type", "");
		
		try {
			
			/* in case driver and rider both are already */
//			if (verfied == "verify" && type == "driver") {
			if (type.equals("driver")) {
				Log.e("PhoneNumberActivity", "opening DriverMapActivity" );
				startActivity(new Intent(getApplicationContext(), driver_nav_drawer.class));
				finish();
			}

			if (type.equals("rider")) {
				Log.d("PhoneNumberActivity", "opening RiderMapActivity ");
				startActivity(new Intent(getApplicationContext(), rider_nav_layout.class));
				finish();
			}
			
		} catch (Exception e) {
			Toast.makeText(this, "shared Preference storage", Toast.LENGTH_SHORT).show();
		}
		
		findViewById(R.id.buttonContinue).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				String mobile = editTextMobile.getText().toString().trim();
				
				if (mobile.isEmpty() || mobile.length() < 11) {
					editTextMobile.setError("Enter a valid mobile Number");
					editTextMobile.requestFocus();
					return;
				}
				
				Intent intent = new Intent(PhoneNumberActivity.this, verifyPhoneActivity.class);
				intent.putExtra("mobile", mobile);
				startActivity(intent);
			}
		});
	}
	
}