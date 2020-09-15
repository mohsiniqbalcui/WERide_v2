package com.pool.Weride.Users.Rider.NavHome;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.pool.Weride.Activities.userType;
import com.pool.Weride.BuildConfig;
import com.pool.Weride.History.HistoryActivity;
import com.pool.Weride.History.HistorySingleActivity;
import com.pool.Weride.Users.Rider.Groups.riderGroups;
import com.pool.Weride.Users.Rider.Map.RiderHomeMapFragment;
import com.pool.Weride.Users.Rider.Packages.Rider_packages;
import com.pool.Weride.R;
import com.pool.Weride.Users.Rider.RiderSetting.RiderSettingsActivity;
import com.pool.Weride.fragments.helpSupport;


public class rider_nav_layout extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    NavigationView navigationView;
    Editor type;
    SharedPreferences mSharedPreferences1;
    Toolbar toolbar;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_drawer_rider_layout);

        toolbar = findViewById(R.id.toolbar11);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new RiderHomeMapFragment())
                    .addToBackStack(null)
                    .commit();
            toolbar.setTitle("Home");
            navigationView.setCheckedItem(R.id.nav_home);
        }

        mSharedPreferences1 = getSharedPreferences("type", MODE_PRIVATE);
        type = mSharedPreferences1.edit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new RiderHomeMapFragment())
                        .addToBackStack(null)
                        .commit();
                toolbar.setTitle("Home");
                navigationView.setCheckedItem(R.id.nav_home);
                break;

            case R.id.nav_callSupport:
                makePhoneCallToWeride(); // cal to mohsin iqbak
                break;

            case R.id.nav_rides:
                startActivity(new Intent(rider_nav_layout.this, HistorySingleActivity.class));
                navigationView.setCheckedItem(R.id.nav_Driver_History);
                toolbar.setTitle("Home");
                Toast.makeText(this, "Rides History", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_transaction:
                startActivity(new Intent(rider_nav_layout.this, HistoryActivity.class));
                navigationView.setCheckedItem(R.id.nav_transaction);
                toolbar.setTitle("pay bill");
                Toast.makeText(this, "rides history", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_share:
                shareAppLink();
                navigationView.setCheckedItem(R.id.nav_share);
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_packages:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Rider_packages())
                        .addToBackStack(null)
                        .commit();
                toolbar.setTitle(R.string.packages);

                Toast.makeText(this, "packages", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_help:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new helpSupport()).commit();
                toolbar.setTitle("Help");

                Toast.makeText(this, "Help ", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_settings:
                startActivity(new Intent(rider_nav_layout.this, RiderSettingsActivity.class));
                navigationView.setCheckedItem(R.id.nav_settings);
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                break;

/*            case R.id.nav_Groups:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new riderGroups())
                        .addToBackStack(null)
                        .commit();
                toolbar.setTitle("Rider Groups");
                navigationView.setCheckedItem(R.id.nav_Groups);
                Toast.makeText(this, "rider Groups", Toast.LENGTH_SHORT).show();
                break;*/

            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "Log Out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(rider_nav_layout.this, userType.class));
                type.clear();
                finish();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new RiderHomeMapFragment())
                    .addToBackStack(null)
                    .commit();
            toolbar.setTitle("Home");
        }

    }


    public void shareAppLink() {
        try {

            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "WE Ride");
            String shareMessage = "\nLet me recommend you this application\n\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            e.toString();
        }
    }


    private void makePhoneCallToWeride() {
        int REQUEST_Support_CALL = 3;

        String number = "+923424475733";
        //String number ="1122";
        if (number.trim().length() > 9) {

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CALL_PHONE}, REQUEST_Support_CALL);
            } else {
                String dial = "tel:" + number;
                startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
            }
        } else {
            Toast.makeText(this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
        }
    }

	/*-------------------------------------------- onRequestPermissionsResult -----


    |  Function onRequestPermissionsResult
    |
    |  Purpose:  Get permissions for our app if they didn't previously exist.
    |
    |  Note:
    |	requestCode: the nubmer assigned to the request that we've made. Each
    |                request has it's own unique request code.
    |
    *-------------------------------------------------------------------*/


    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    }
                } else {
                    Toast.makeText(this, "Please provide location permission", Toast.LENGTH_LONG).show();
                }
                break;
            }
            case 2: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

//						makePhoneCall(); call to 1122 pakistan
                    }
                } else {
                    Toast.makeText(this, "Phone Call Permission DENIED", Toast.LENGTH_SHORT).show();
                }
                break;
            }

            case 3: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) ==
                            PackageManager.PERMISSION_GRANTED) {
                        makePhoneCallToWeride();
                        Toast.makeText(this, "Phone Call to weride Developer Respected Mohsin Iqbal", Toast.LENGTH_SHORT).show();

                    }
                } else {
                    Toast.makeText(this, "Phone Call Permission DENIED", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }


}
