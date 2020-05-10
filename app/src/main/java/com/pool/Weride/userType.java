package com.pool.Weride;


import android.Manifest;
import android.Manifest.permission;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
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

import java.util.List;

public class userType extends AppCompatActivity {
	
	Button driverbtn;
	Button riderbtn;
	Intent intent;
	Editor editor;
	LocationManager locationManager;
	Context context;
	SharedPreferences mSharedPreferences;
	FirebaseAuth firebaseAuth;
	private FirebaseAnalytics mFirebaseAnalytics;
	
	/*two operation are validated internet connection checking in app opening and second location permission*/
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_user_type);
			
			
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
			mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
			
		//	context = userType.this;
		//	isLocationEnabled(); // location setting enabling
			if (!isConnected(userType.this)) {
				buildDialog(userType.this).show();
			}
		else {
			Toast.makeText(userType.this, "Welcome", Toast.LENGTH_SHORT).show();
		}
		
		
		driverbtn = findViewById(R.id.driverUser);
			driverbtn.setOnClickListener(v -> driverClick());
		riderbtn = findViewById(R.id.riderUser);
			riderbtn.setOnClickListener(v -> riderClick());
		firebaseAuth = FirebaseAuth.getInstance();
			//havePermission();
		if (firebaseAuth.getCurrentUser() != null) {
			
			SharedPreferences mSharedPreferences1 = getSharedPreferences("type", MODE_PRIVATE);
			String type = mSharedPreferences1.getString("type", "");
			
			try {
				
				/* in case driver and rider both are already */
				assert type != null;
				if (type.equals("driver")) {
					startActivity(new Intent(getApplicationContext(), DriverMapActivity.class));
					finish();
				}
				if (type.equals("rider")) {
					startActivity(new Intent(getApplicationContext(), CustomerMapActivity.class));
					finish();
				}
				
			} catch (Exception e) {
				Toast.makeText(this, "shared Preference storage", Toast.LENGTH_SHORT).show();
			}
			
			
			startActivity(new Intent(userType.this, CustomerMapActivity.class));
			finish();
		}
			/* permisio */
		
		
		
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
			Toast.makeText(context, ""+pE, Toast.LENGTH_LONG).show();
			pE.printStackTrace();
			
		}
	}// oncreate
	
	
	public boolean isConnected(Context context) {
		
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netinfo = cm.getActiveNetworkInfo();
		
		if (netinfo != null && netinfo.isConnectedOrConnecting()) {
			android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			
			return (mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting());
		} else {
			return false;
		}
	}
	
	
	public AlertDialog.Builder buildDialog(Context c) {
		
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
	
	public boolean havePermission() {
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
	private void checkLocationPermission() {
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
								ActivityCompat.requestPermissions(userType.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
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
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
	
	
	public void driverClick() {
		
		editor.putString("type", "driver"); // Storing string
		editor.apply();
		
		intent = new Intent(this, signin_activity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		
		startActivity(intent);
		finish();
		
	}
	
	public void riderClick() {
		
		editor.putString("type", "rider"); // Storing string
		editor.apply();
		
		intent = new Intent(this, signin_activity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
		
		startActivity(intent);
		finish();
	}
	
	
	public void locationsetting() {
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
	
	
	
	
	// boolean online = hostAvailable("www.google.com", 80);
		
		/*if (!online){
		
		AlertDialog alertDialog = new Builder(this).create();
		alertDialog.setTitle("no internet");
		alertDialog.setMessage("no internet");
		alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
		    new OnClickListener() {
		        public void onClick(DialogInterface dialog, int which) {
		        //	finish(); // closee app
		            dialog.dismiss();
		        }
		    });
		alertDialog.show();
		}
		*/
	
	/*public boolean hostAvailable(String host, int port) {
		try (Socket socket = new Socket()) {
			socket.connect(new InetSocketAddress(host, port), 2000);
			return true;
		} catch (IOException e) {
			// Either we have a timeout or unreachable host or failed DNS lookup
			Toast.makeText(context, "no internet"+e, Toast.LENGTH_SHORT).show();			return false;
		}
	}*/
	

	
		/*
		LocationListener locationListenerGPS = new LocationListener() {
			@Override
			public void onLocationChanged(android.location.Location location) {
				double latitude = location.getLatitude();
				double longitude = location.getLongitude();
				String msg = "New Latitude: " + latitude + "New Longitude: " + longitude;
				//Toast.makeText(mContext,msg,Toast.LENGTH_SHORT).show();
			}
			
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras) {
			
			}
			
			@Override
			public void onProviderEnabled(String provider) {
			
			}
			
			@Override
			public void onProviderDisabled(String provider) {
			
			}
		};
		*/
		
		
			/*
	private void isLocationEnabled() {
		// forace user to turn on locatioin from phone
		if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
		AlertDialog.Builder alertDialog=new AlertDialog.Builder(context);
		alertDialog.setTitle("Enable Location");
		alertDialog.setMessage("Your locations setting is not enabled. Please enabled it in settings menu.");
		alertDialog.setPositiveButton("Location Settings", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which){
				Intent intent=new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				startActivity(intent);
			}
		});
		alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int which){
				dialog.cancel();
			}
		});
		AlertDialog alert=alertDialog.create();
		alert.show();
	}
		else{
		//location enabled
	}
}*/

}

