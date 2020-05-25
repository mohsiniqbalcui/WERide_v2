package com.pool.Weride;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.pool.Weride.fragments.helpSupport;


public class rider_nav_layout extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
	private DrawerLayout drawer;
	NavigationView navigationView;
	Editor type;
	SharedPreferences mSharedPreferences1;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drawer_rider_layout);
		
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		drawer = findViewById(R.id.drawer_layout);
		 navigationView = findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
		
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		
		toggle.syncState();
		
		if (savedInstanceState == null) {
			
			startActivity(new Intent(rider_nav_layout.this, RiderMapActivity.class));
			navigationView.setCheckedItem(R.id.nav_home);
			
		}
		
		mSharedPreferences1 = getSharedPreferences("type", MODE_PRIVATE);
		type = mSharedPreferences1.edit();
	}
	
	
	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		
		switch (item.getItemId()) {
			case R.id.nav_home:
				startActivity(new Intent(rider_nav_layout.this, RiderMapActivity.class));
				navigationView.setCheckedItem(R.id.nav_home);
				Toast.makeText(this, "customer map activity is opened", Toast.LENGTH_SHORT).show();
				break;
			
			case R.id.nav_rides:
				startActivity(new Intent(rider_nav_layout.this,HistorySingleActivity.class));
				navigationView.setCheckedItem(R.id.nav_Driver_History);
				Toast.makeText(this, "Rides History", Toast.LENGTH_SHORT).show();
				break;
			case R.id.nav_transaction:
				startActivity(new Intent(rider_nav_layout.this, HistoryActivity.class));
				navigationView.setCheckedItem(R.id.nav_home);
				Toast.makeText(this, "rides history", Toast.LENGTH_SHORT).show();
				break;
			case R.id.nav_share:
				
				shareAppLink();
				navigationView.setCheckedItem(R.id.nav_share);
				Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
				break;
				
			case R.id.nav_packages:
				getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
						new Rider_packages()).commit();
				Toast.makeText(this, "packages", Toast.LENGTH_SHORT).show();
				break;
			case R.id.nav_help:
				getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
						new helpSupport()).commit();
				Toast.makeText(this, "Help ", Toast.LENGTH_SHORT).show();
				break;
			case R.id.nav_settings:
				
				startActivity(new Intent(rider_nav_layout.this, RiderSettingsActivity.class));
				navigationView.setCheckedItem(R.id.nav_settings);
				
				Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
				break;
			case R.id.nav_Groups:
				
				startActivity(new Intent(rider_nav_layout.this, RiderMapActivity.class));
				navigationView.setCheckedItem(R.id.nav_Groups);
				
				Toast.makeText(this, "How Works", Toast.LENGTH_SHORT).show();
				break;
			case R.id.nav_logout:
				FirebaseAuth.getInstance().signOut();
				Intent intent = new Intent(rider_nav_layout.this, userType.class);
				type.clear();
				startActivity(intent);
				finish();
				System.exit(1);
				Toast.makeText(this, "Log Out", Toast.LENGTH_SHORT).show();
				break;
		}
		
		
		/*if (newFragment != null) {
			transaction.replace(R.id.fragment_container, newFragment);
			transaction.addToBackStack(null);
			transaction.commit();
		}*/
		
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}
	
	@Override
	public void onBackPressed() {
		
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}
	
	
	public void shareAppLink(){
		try {
			
			Intent shareIntent = new Intent(Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			shareIntent.putExtra(Intent.EXTRA_SUBJECT, "WE Ride");
			String shareMessage= "\nLet me recommend you this application\n\n";
			shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
			shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
			startActivity(Intent.createChooser(shareIntent, "choose one"));
		} catch(Exception e) {
			e.toString();
		}
	}
}
