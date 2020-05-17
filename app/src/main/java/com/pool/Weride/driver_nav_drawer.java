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
import com.pool.Weride.fragments.transactionFragment;


public class driver_nav_drawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
	
	private DrawerLayout drawer;
	NavigationView navigationView;
	ActionBarDrawerToggle toggle;
	Editor type;
	SharedPreferences mSharedPreferences1;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_drawer_driver_layout);
		
		Toolbar toolbar = findViewById(R.id.driver_toolbar);
		setSupportActionBar(toolbar);
		drawer = findViewById(R.id.drawer_driver_layout);
		
		navigationView = findViewById(R.id.nav_driver_view);
		navigationView.setNavigationItemSelectedListener(this);
		
		 toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		
		toggle.syncState();
		
		if (savedInstanceState == null) {
			startActivity(new Intent(driver_nav_drawer.this,DriverMapActivity.class));
			navigationView.setCheckedItem(R.id.nav_home1);
			
		}
		mSharedPreferences1 = getSharedPreferences("type", MODE_PRIVATE);
		type = mSharedPreferences1.edit();
		
	}
	
	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		
		
		switch (item.getItemId()) {
			
			case R.id.nav_home1:
				startActivity(new Intent(driver_nav_drawer.this,DriverMapActivity.class));
				navigationView.setCheckedItem(R.id.nav_home1);
				Toast.makeText(this, "Message fragment", Toast.LENGTH_SHORT).show();
				break;
				
			case R.id.nav_Driver_History:
				
				startActivity(new Intent(driver_nav_drawer.this,HistorySingleActivity.class));
				navigationView.setCheckedItem(R.id.nav_Driver_History);
				Toast.makeText(this, "Driver History", Toast.LENGTH_SHORT).show();
				break;
			case R.id.nav_Driver_Wallet:// earning
				getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
						new transactionFragment()).commit();
				
				Toast.makeText(this, "Driver Wallet", Toast.LENGTH_SHORT).show();
				break;
			case R.id.nav_share1:
				shareAppLink();
				Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
				break;
			case R.id.nav_help1:
				getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
						new helpSupport()).commit();
				Toast.makeText(this, "Help", Toast.LENGTH_SHORT).show();
				break;
				
			case R.id.nav_settings1:
				startActivity(new Intent(driver_nav_drawer.this,DriverSettingsActivity.class));
				navigationView.setCheckedItem(R.id.nav_settings1);
				Toast.makeText(this, "driver Settings", Toast.LENGTH_SHORT).show();
				break;
				
			case R.id.nav_logout1:
				
				FirebaseAuth.getInstance().signOut();
				Intent intent = new Intent(driver_nav_drawer.this, userType.class);
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
			shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
			String shareMessage= "\nLet me recommend you this application\n\n";
			shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
			shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
			startActivity(Intent.createChooser(shareIntent, "choose one"));
		} catch(Exception e) {
			e.toString();
		}
	}
}
