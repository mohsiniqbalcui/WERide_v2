package com.pool.Weride;

import android.app.Fragment;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.pool.Weride.fragments.ProfileFragment;
import com.pool.Weride.fragments.RidesFragment;
import com.pool.Weride.fragments.transactionFragment;


public class driver_drawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
	private DrawerLayout drawer;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_drawer_driver_layout);
		
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		drawer = findViewById(R.id.drawer_layout);
		NavigationView navigationView = findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
		
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
				R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		
		toggle.syncState();
		
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
					new ProfileFragment()).commit();
			navigationView.setCheckedItem(R.id.nav_home);
		}
	}
	
	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		
		Fragment newFragment = null;
		
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		switch (item.getItemId()) {
			case R.id.nav_home:
				getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
						new ProfileFragment()).commit();
				Toast.makeText(this, "Message fragment", Toast.LENGTH_SHORT).show();
				break;
			
			case R.id.nav_rides:
				getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
						new RidesFragment()).commit();
				Toast.makeText(this, "chat fragment", Toast.LENGTH_SHORT).show();
				break;
			case R.id.nav_transaction:
				getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
						new transactionFragment()).commit();
				Toast.makeText(this, "past transaction", Toast.LENGTH_SHORT).show();
				break;
			case R.id.nav_share:
				
				Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
				break;
			case R.id.nav_help:
				Toast.makeText(this, "Help", Toast.LENGTH_SHORT).show();
				break;
			case R.id.nav_settings:
				Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
				break;
			case R.id.nav_Groups:
				Toast.makeText(this, "How Works", Toast.LENGTH_SHORT).show();
				break;
			case R.id.nav_logout:
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
}
