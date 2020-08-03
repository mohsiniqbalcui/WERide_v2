package com.pool.Weride.Activities;


import android.Manifest;
import android.Manifest.permission;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.pool.Weride.Activities.Auth.LoginRegister.signin_activity;
import com.pool.Weride.R;
import com.pool.Weride.Users.Driver.NavHome.driver_nav_drawer;
import com.pool.Weride.Users.Rider.NavHome.rider_nav_layout;
import com.pool.Weride.Util.Utils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

public class userType extends AppCompatActivity {
	
	Button driverbtn;
	Button riderbtn;
	Intent intent;
	Editor editor;
	LocationManager locationManager;
	SharedPreferences mSharedPreferences;
	FirebaseAuth firebaseAuth;
	private FirebaseAnalytics mFirebaseAnalytics;
	String TAG = "userType";
	KonfettiView viewKonfetti;
	View view;
	Context context = userType.this;


	/*two operation are validated internet connection checking in app opening and second location permission*/
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_user_type);
			
			mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
			firebaseAuth = FirebaseAuth.getInstance();
			
			Dexter.withContext(this)
					.withPermissions(
							Manifest.permission.ACCESS_FINE_LOCATION,
							Manifest.permission.CAMERA,
							permission.ACCESS_NETWORK_STATE,
							permission.ACCESS_WIFI_STATE,
							Manifest.permission.CALL_PHONE)
					.withListener(new MultiplePermissionsListener() {
						@Override
						public void onPermissionsChecked(final MultiplePermissionsReport pMultiplePermissionsReport) {
						
						}
						
						@Override
						public void onPermissionRationaleShouldBeShown(final List<PermissionRequest> pList, final PermissionToken pPermissionToken) {
						
						}
					}).check();
			
			viewKonfetti = (KonfettiView)findViewById(R.id.viewKonfetti);
			
			
			viewKonfetti.build()
					.addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
					.setDirection(80, 359.0)
					.setSpeed(1f, 5f)
					.setFadeOutEnabled(true)
					.setTimeToLive(2000L)
					.addShapes(Shape.RECT, Shape.CIRCLE)
					.addSizes(new Size(12, 5f))
					.setPosition(-50f, viewKonfetti.getWidth() + 50f, -50f, -50f)
					.streamFor(300, 7000L);


			KonfettiView viewKonfetti1 = (KonfettiView) findViewById(R.id.viewKonfetti);
			
			
			viewKonfetti1.build()
					.addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
					.setDirection(80, 359.0)
					.setSpeed(1f, 5f)
					.setFadeOutEnabled(true)
					.setTimeToLive(2000L)
					.addShapes(Shape.RECT, Shape.CIRCLE)
					.addSizes(new Size(12, 5f))
					.setPosition(-50f, viewKonfetti.getWidth() + 50f, -50f, -50f)
					.streamFor(300, 7000L);
			
			
			
			//	context = userType.this;
		//	isLocationEnabled(); // location setting enabling

			if (Utils.isNetworkAvailable(context)) {
				buildDialog(userType.this).show();
			}
		else {
			Toast.makeText(userType.this, "Welcome", Toast.LENGTH_SHORT).show();
		}
		
		driverbtn = findViewById(R.id.driverUser);
			driverbtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(final View v) {
					driverClick();
				}
			});
		riderbtn = findViewById(R.id.riderUser);
			riderbtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(final View v) {
					riderClick();
				}
			});
			
		checkUserType();
		
		
		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
					== PackageManager.PERMISSION_GRANTED) {
				
			} else {
				//checkLocationPermission();
				/*locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
						2000,
						10, locationListenerGPS);*/
				
			}
		}
			
			
			mSharedPreferences = getSharedPreferences("type", MODE_PRIVATE); // 0 - for private mode
		    editor = mSharedPreferences.edit();
		
		
		} catch (Exception pE) {
			//Toast.makeText(context, ""+pE, Toast.LENGTH_LONG).show();
			pE.printStackTrace();
			Log.e("------------error------",""+pE.getMessage());
			
		}
	}// oncreate
	
	private void checkUserType()
	{
		//havePermission();
		if (firebaseAuth.getCurrentUser() != null) {
			
			SharedPreferences mSharedPreferences1 = getSharedPreferences("type", MODE_PRIVATE);
			String type = mSharedPreferences1.getString("type", "");
			
			try {
				
				/* in case driver and rider both are already */
				assert type != null;
				if (type.equals("driver")) {
					Log.d(TAG, "checkUserType: DriverMapActivity opened");
					startActivity(new Intent(getApplicationContext(), driver_nav_drawer.class));
					finish();
				}
				if (type.equals("rider")) {
					Log.d(TAG, "checkUserType: RiderMapActivity opened");
					startActivity(new Intent(getApplicationContext(), rider_nav_layout.class));
					finish();
				}
				
			} catch (Exception e) {
				Toast.makeText(this, "shared Preference storage", Toast.LENGTH_SHORT).show();
			}
			

		}
		
	}


	public boolean hasActiveInternetConnection()
	{
		try
		{
			HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
			urlc.setRequestProperty("User-Agent", "Test");
			urlc.setRequestProperty("Connection", "close");
			urlc.setConnectTimeout(4000);
			urlc.setReadTimeout(4000);
			urlc.connect();
			//int networkcode2 = urlc.getResponseCode();
			return (urlc.getResponseCode() == 200);
		} catch (IOException e)
		{
			Log.i("warning", "Error checking internet connection", e);
			return false;
		}

	}


	public AlertDialog.Builder buildDialog(Context c)
	{
		
		AlertDialog.Builder builder = new AlertDialog.Builder(c);
		builder.setTitle("No Internet Connection");
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setMessage("You need to have Mobile Data or wifi to access this. Press ok to Exit");
		
		builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				finish();
			}
		});
		
		return builder;
	}
	
	public boolean havePermission()
	{
		if (Build.VERSION.SDK_INT >= 23) {
			if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
				return true;
			} else {
				ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
				return false;
			}
		} else {
			return true;
		}
	}
	private void checkLocationPermission()
	{
		if (ContextCompat.checkSelfPermission(this,
				android.Manifest.permission.ACCESS_FINE_LOCATION) !=
				PackageManager.PERMISSION_GRANTED) {
			if (ActivityCompat.shouldShowRequestPermissionRationale(
					this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
				new android.app.AlertDialog.Builder(this)
						.setTitle("give permission")
						.setMessage("give permission message")
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								ActivityCompat.requestPermissions(userType.this,
										new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
							}
						})
						.create()
						.show();
			} else {
				ActivityCompat.requestPermissions(userType.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
			}
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
	{
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		switch (requestCode) {
			case 1: {
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					if (ContextCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
					
					}
				} else {
					//locationsetting();
					Toast.makeText(getApplicationContext(), "Please provide the permission", Toast.LENGTH_LONG).show();
				}
				break;
			}
		}
	}
	
	
	public void driverClick()
	{
		
		editor.putString("type", "driver"); // Storing string
		editor.apply();
		
		intent = new Intent(this, signin_activity.class);

		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

		startActivity(intent);
		finish();
		
	}
	
	public void riderClick()
	{
		
		editor.putString("type", "rider"); // Storing string
		editor.apply();
		
		intent = new Intent(this, signin_activity.class);

		
		startActivity(intent);
		finish();
	}
	
	
	public void locationsetting()
	{
		LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		boolean gps_enabled = false;
		boolean network_enabled = false;
		
		try {
			gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
		} catch (Exception ex) {
		}
		
		try {
			network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		} catch (Exception ex) {
		}
		
		if (!gps_enabled && !network_enabled) {
			// notify user
			new AlertDialog.Builder(context)
					.setTitle("Enable Location")
					.setMessage("Your locations setting is not enabled. " +
							"Please enabled it in settings menu.")
					.setPositiveButton("Location Settings", new DialogInterface.
							OnClickListener() {
						@Override
						public void onClick(DialogInterface paramDialogInterface, int paramInt) {
							context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
						}
					})
					.setNegativeButton("Cancel", null)
					.show();
		}
	}

	public boolean isOnline() {
		ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

		if(netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()){
			Toast.makeText(context, "No Internet connection!", Toast.LENGTH_LONG).show();
			return false;
		}
		return true;
	}

}


