package com.pool.Weride;

import android.Manifest;
import android.Manifest.permission;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.Place.Field;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/*import com.google.android.gms.location.places.Place;
import com.google.android.libraries.places.api.Places;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;*/


public class RiderMapActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    String TAG="RiderMapActivity.this";

    private FusedLocationProviderClient mFusedLocationClient;

    private Button mLogout, mRequest, mSettings, mHistory;
//customer current location that is pick up location
    private LatLng pickupLocation;

    private Boolean requestBol = false;
// customer current location marker
    private Marker pickupMarker;

    private SupportMapFragment mapFragment;
// location where rider want to travel
    private String destination;
    // service car or rickshaw
		String requestService;

    private LatLng destinationLatLng;

    private LinearLayout mDriverInfo;

    private ImageView mDriverProfileImage;

    private TextView mDriverName, mDriverPhone, mDriverCar;

    private RadioGroup mRadioGroup;

    private RatingBar mRatingBar;ImageButton imageCall;
	
	private FirebaseAnalytics mFirebaseAnalytics;
	Editor type;
	SharedPreferences mSharedPreferences1;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rider_map);
	
		try {
	
		buttonsCall();

		if (!Places.isInitialized()) {
		Places.initialize(getApplicationContext(), getString(R.string.google_maps_key), Locale.US);
		}
	
		AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
				getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
		PlacesClient placesClient = Places.createClient(this);
		
		autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Field.LAT_LNG)).setCountry("pk");
	
		autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
			@Override
			public void onPlaceSelected(Place place) {
				// TODO: Get info about the selected place.
				destination = place.getName();
				destinationLatLng = place.getLatLng();
				Log.i("customer", "onError: ");
			
				Toast.makeText(RiderMapActivity.this, "destination" + destination, Toast.LENGTH_SHORT).show();
			}
		
			@Override
			public void onError(Status status) {
				// TODO: Handle the error.
				Log.i("customer", "onError: ");
				Toast.makeText(RiderMapActivity.this, "excetion in fetching the autocompelete", Toast.LENGTH_SHORT).show();
				
			}
		});
		
		
		}catch (Exception pE){
			Log.e(TAG, "onCreate: "+pE.getMessage());
			Toast.makeText(this, "customer map activity ", Toast.LENGTH_SHORT).show();
		}
	}// oncreate
	
	
	
	
	
	
	
	private void buttonsCall()
	{
		
		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
		
		mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
		
		mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
		
		destinationLatLng = new LatLng(0.0, 0.0);
		
		mDriverInfo = findViewById(R.id.driverInfo);
		
		mDriverProfileImage = findViewById(R.id.driverProfileImage);
		
		mDriverName = findViewById(R.id.driverName);
		mDriverPhone = findViewById(R.id.driverPhone);
		mDriverCar = findViewById(R.id.driverCar);
		
		mRatingBar = findViewById(R.id.ratingBar1);
		mRadioGroup = findViewById(R.id.radioGroup1);
		
		mLogout = findViewById(R.id.logout1);
		mRequest = findViewById(R.id.callride);
		mSettings = findViewById(R.id.settings1);
		mHistory = findViewById(R.id.history1);
		/*
		 * ============offline
		 * 						FirebaseDatabase.getInstance().setPersistenceEnabled(true);
		 * */
		FirebaseDatabase.getInstance().setPersistenceEnabled(true);
		imageCall = findViewById(R.id.imageButton);
		
		mSharedPreferences1 = getSharedPreferences("type", MODE_PRIVATE);
		type = mSharedPreferences1.edit();
		mLogout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				FirebaseAuth.getInstance().signOut();
				Intent intent = new Intent(RiderMapActivity.this, userType.class);
				type.clear();
				startActivity(intent);
				finish();
			}
		});
		
		
		imageCall.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				makePhoneCall();
			}
		});
		
		mRequest.setOnClickListener(new View.OnClickListener() {// call uber driver
			@Override
			public void onClick(View v) {
				try {
					callDriver();
				} catch (Exception pE) {
					Toast.makeText(RiderMapActivity.this, "Exception in getting driver location" + pE, Toast.LENGTH_SHORT).show();
					pE.printStackTrace();
				}
				
			}
		});
		
		mSettings.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RiderMapActivity.this, RiderSettingsActivity.class);
				startActivity(intent);
				
			}
		});
		
		mHistory.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RiderMapActivity.this, HistoryActivity.class);
				intent.putExtra("customerOrDriver", "Customers");
				startActivity(intent);
				
			}
		});
	}
	
	
	
	
	
	
	private void callDriver()
	{
		if (requestBol) {// only be true in case of ride completion or onCancelled
			Log.d(TAG, "callDriver: requestBol rider pressed two times request button to cancel ride");
			endRide();
			
		} else {
			// if requestbol false
			Log.d(TAG, "callDriver: finding driver");
			int selectId = mRadioGroup.getCheckedRadioButtonId();
			final RadioButton radioButton = findViewById(selectId);
			
			if (radioButton.getText() == null) {
				Toast.makeText(this, "user not selected any service", Toast.LENGTH_SHORT).show();
				Log.e(TAG, "callDriver: user not selected any service");
				return;
			}
			
			requestService = radioButton.getText().toString().trim();// is car or rickshaw.
			
			requestBol = true;
			
			String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();//  current user id
			
			
			DatabaseReference ref = FirebaseDatabase.getInstance().getReference("customerRequest");
			
			GeoFire geoFire = new GeoFire(ref);
			
			
			geoFire.setLocation(userId, new GeoLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude()));
			// only user locaton stored with userkey in firbase geofire method
			
			pickupLocation = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
			pickupMarker = mMap.addMarker(new MarkerOptions()
					.position(pickupLocation).title("Pickup me Here").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_pickup)));
			//  user current location is denoted by the icon and title.
			
			mRequest.setText("Getting your Driver....");// button test updated from call driver to getting the drriver
			
			getClosestDriver();// getting the colsest driver near to you
		}
	}

	
	private int radius = 1; //from 1
    private Boolean driverFound = false;
    private String driverFoundID;

    GeoQuery geoQuery;
	
	/*
	*   =========getClosestDriver()
	*
	/*this methios pick nesarest driver and save in fireabse realtime databvase by geo query implelemntation
	*
	*
	* */
	
	
	
	
	
	
	private void getClosestDriver()
	{
		Log.d(TAG, "getClosestDriver: ");
		
		DatabaseReference driverLocation = FirebaseDatabase.getInstance().getReference().child("driversAvailable");
		
		GeoFire geoFire = new GeoFire(driverLocation);
		geoQuery = geoFire.queryAtLocation(new GeoLocation(pickupLocation.latitude, pickupLocation.longitude), radius);
		if (geoQuery != null) {
			
			geoQuery.removeAllListeners();
		}
									/*we not have the driver location in tghe firebase on current time this will be emotyy
									fiednt in every query and calusing the query failed in
									* 9in every button pressed */
		// masla geo query ma ha
		Log.d(TAG,"before finding the closest driver ");
		
		geoQuery.addGeoQueryEventListener(new GeoQueryEventListener()
		{
			
			@Override
			public void onKeyEntered(String key, GeoLocation location) {
				if (!driverFound && requestBol) {
					DatabaseReference mCustomerDatabase = FirebaseDatabase.getInstance().
							getReference().child("Users").child("Drivers").child(key);
					//Users/Driver.child(key)
					Log.d(TAG,"before finding the closest driver by reading the drivers");
					
					mCustomerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
						@Override
						public void onDataChange(DataSnapshot dataSnapshot) {
							if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
								Map<String, Object> driverMap = (Map<String, Object>) dataSnapshot.getValue();
								/*driver information from firbase */
								if (driverFound) {
									Log.d(TAG,"driver is already found that customer demanding the service from drivers");
									
									return;
								}
								
								if (driverMap.get("service").equals(requestService)) {
									driverFound = true;
									driverFoundID = dataSnapshot.getKey(); // neareast driver id
									
									Log.d(TAG,"Driver car service matched to rider demanding car service");
									
									DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference()
											.child("Users").child("Drivers").child(driverFoundID).child("customerRequest");
									
									String customerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
									HashMap map = new HashMap();
									map.put("customerRideId", customerId);
									map.put("destination", destination);
									map.put("destinationLat", destinationLatLng.latitude);
									map.put("destinationLng", destinationLatLng.longitude);
									driverRef.updateChildren(map);
									
									Log.d(TAG,"rider service matched to driver service");
									Log.d(TAG, "getClosestDriver: customer request is created in driver database");
									Log.d(TAG, "getClosestDriver: customerRequest contain hash map values in firebase realtime database");
									getDriverLocation();
									getDriverInfo();
									getHasRideEnded();
									mRequest.setText("Looking for Driver Location....");
								}
							}
						}
						
						@Override
						public void onCancelled(DatabaseError databaseError) {
						}
					});
				}
			}
			
			@Override
			public void onKeyExited(String key) {
			
			}
			
			@Override
			public void onKeyMoved(String key, GeoLocation location) {
			
			}
			
			@Override
			public void onGeoQueryReady() {
				if (!driverFound) {
					radius++;
					getClosestDriver();
				}
			}
			
			@Override
			public void onGeoQueryError(DatabaseError error) {
			
			}
		});
	}
	
	
	
	
	
	
	
	
	private Marker mDriverMarker;
	private DatabaseReference driverLocationRef;
	private ValueEventListener driverLocationRefListener;
	
    /*-------------------------------------------- Map specific functions -----
    |  Function(s) getDriverLocation
    |
    |  Purpose:  Get's most updated driver location and it's always checking for movements.
    |
    |  Note:
    |	   Even tho we used geofire to push the location of the driver we can use a normal
    |      Listener to get it's location with no problem.
    |
    |      0 -> Latitude
    |      1 -> Longitudde
    |
    *-------------------------------------------------------------------*/
	
    
    
    
    
	/*this method will be only called when */
	private void getDriverLocation()
	{
		
		driverLocationRef = FirebaseDatabase.getInstance().getReference().child("driversWorking").child(driverFoundID).child("l");
		
		
		Log.d(TAG, "getDriverLocation: finding driver location(latidue,longtidue) that are working");
		
		driverLocationRefListener = driverLocationRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && requestBol){
                    List<Object> map = (List<Object>) dataSnapshot.getValue();
                    double locationLat = 0;
                    double locationLng = 0;
	
					if (map.get(0) != null) {
						locationLat = Double.parseDouble(map.get(0).toString().trim());
						//getting driver latitude that is found
                    }
                    if(map.get(1) != null){
						locationLng = Double.parseDouble(map.get(1).toString().trim());
						//getting driver longitude that is found
	
					}
                    // driver location from the firebase database that is available...
                    LatLng driverLatLng = new LatLng(locationLat,locationLng);
                    
                    if(mDriverMarker != null){
                        mDriverMarker.remove();
                        // to updte the driver marker from the privous location to new one so we remove the driver marker from the past place to new one
                    }
                    
                    Location loc1 = new Location("");
                    loc1.setLatitude(pickupLocation.latitude);
                    loc1.setLongitude(pickupLocation.longitude);

                    
                    Location loc2 = new Location("");
                    loc2.setLatitude(driverLatLng.latitude);
                    loc2.setLongitude(driverLatLng.longitude);
					Log.d(TAG, "onDataChange: driverlocation finding");
                    float distance = loc1.distanceTo(loc2);

                    if (distance<100){
                        mRequest.setText("Driver's Here");
                    }else{
						mRequest.setText("Driver Found: " + distance);
                    }
	
					/*please remove this comment*/
					mDriverMarker = mMap.addMarker(new MarkerOptions()
							.position(driverLatLng).title("your driver")
							.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_car)));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    /*-------------------------------------------- getDriverInfo -----
    |  Function(s) getDriverInfo
    |
    |  Purpose:  Get all the user information that we can get from the user's database.
    |
    |  Note:= this method read the driver infomation from the firebase
    |
    *-------------------------------------------------------------------*/
	
    
    
    
    
    
    
    
    
    
    
    
	private void getDriverInfo()
	{
        mDriverInfo.setVisibility(View.VISIBLE);
		DatabaseReference mCustomerDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").child(driverFoundID);
		Log.d(TAG, "getDriverInfo: started");
		mCustomerDatabase.addListenerForSingleValueEvent(new ValueEventListener()
		{
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				
				if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
					if (dataSnapshot.child("name") != null) {
						mDriverName.setText(dataSnapshot.child("name").getValue().toString());
					}
					if (dataSnapshot.child("phone") != null) {
						mDriverPhone.setText(dataSnapshot.child("phone").getValue().toString());
					}
					if (dataSnapshot.child("car") != null) {
						mDriverCar.setText(dataSnapshot.child("car").getValue().toString());
					}
					
					if (dataSnapshot.child("profileImageUrl").getValue() != null) {
						Glide.with(getApplication()).load(dataSnapshot.child("profileImageUrl").getValue().toString()).into(mDriverProfileImage);
					}
					
					int ratingSum = 0;
					float ratingsTotal = 0;
					float ratingsAvg = 0;
					for (DataSnapshot child : dataSnapshot.child("rating").getChildren()) {
						ratingSum = ratingSum + Integer.valueOf(child.getValue().toString());
						ratingsTotal++;
					}
					if (ratingsTotal != 0) {
						ratingsAvg = ratingSum / ratingsTotal;
						mRatingBar.setRating(ratingsAvg);
					}
				}
			}
			
			@Override
			public void onCancelled(DatabaseError databaseError) {
				Log.d(TAG, "error Users-Drivers-info onCancelled: ");
				Log.e(TAG, "error's Users-Drivers-info onCancelled: ");
				Toast.makeText(RiderMapActivity.this, "error Users-Drivers-rating", Toast.LENGTH_SHORT).show();
			}
        });
    }

    private DatabaseReference driveHasEndedRef;
    private ValueEventListener driveHasEndedRefListener;
	
	/*-------------------------------------------- getHasRideEnde -----
 |  Function(s) getHasRideEnde
 |
 |  Purpose:  this method will end the ride in case of ride is
 |
 |  Note: --
 |
 *-------------------------------------------------------------------*/
	
	
	
	
	
	
	private void getHasRideEnded()
	{
		driveHasEndedRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Drivers").
				child(driverFoundID).child("customerRequest").child("customerRideId");
		driveHasEndedRefListener = driveHasEndedRef.addValueEventListener(new ValueEventListener() {
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				if (dataSnapshot.exists()) {
				
				} else {
					endRide();
				}
			}
			
			@Override
			public void onCancelled(DatabaseError databaseError) {
			}
		});
	}
	
	
	
	
	
	
	
	
	private void endRide()
	{
		requestBol = false;
		geoQuery.removeAllListeners();
		if (driverLocationRef != null) {
			driverLocationRef.removeEventListener(driverLocationRefListener);
		}
		
		if (driveHasEndedRef != null) {
			driveHasEndedRef.removeEventListener(driveHasEndedRefListener);
		}
		
		
		if (driverFoundID != null){
			//driver is exist
			DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference()
					.child("Users").child("Drivers").child(driverFoundID).child("customerRequest");
			driverRef.removeValue();
			driverFoundID = null;
		}
		/// screen not fetshing driver information
		
		
		driverFound = false;
		radius = 1;
		String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();// customer id
		
		DatabaseReference ref = FirebaseDatabase.getInstance().getReference("customerRequest");
		GeoFire geoFire = new GeoFire(ref); // geo fire is used to maintin the firebase location services
		geoFire.removeLocation(userId);
		
		if(pickupMarker != null){
			pickupMarker.remove();
		}
		if (mDriverMarker != null){
			mDriverMarker.remove();
		}
		mRequest.setText("call Driver");
		
		mDriverInfo.setVisibility(View.GONE);
		mDriverName.setText("");
		mDriverPhone.setText("");
		mDriverCar.setText("Destination: --");
		mDriverProfileImage.setImageResource(R.mipmap.ic_default_user);
	}

    /*-------------------------------------------- Map specific functions -----
    |  Function(s) onMapReady, buildGoogleApiClient, onLocationChanged, onConnected
    |
    |  Purpose:  Find and update user's location.
    |
    |  Note:
    |	   The update interval is set to 1000Ms and the accuracy is set to PRIORITY_HIGH_ACCURACY,
    |
    |
    *-------------------------------------------------------------------*/
	
	//=====================================called on start===============================================================================================
   
   
   
   
   
   
   
   
   
   
   
    @Override
    public void onMapReady(GoogleMap googleMap)
	{
		mMap = googleMap;
	
		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            }else{
                checkLocationPermission();
            }
        }
	
		mLocationRequest = LocationRequest.create(); // new keyword removed and .create added due to current playservices api >12
		mLocationRequest.setInterval(30000); //10 sec
		mLocationRequest.setFastestInterval(15000);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	
		/*mMap.getUiSettings().setZoomControlsEnabled(true);
		mMap.getUiSettings().setMyLocationButtonEnabled(true);*/
		
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        mMap.setMyLocationEnabled(true);
    }
	
    
    
    
    
    
    
    
	
	LocationCallback mLocationCallback = new LocationCallback()
	{
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for(Location location : locationResult.getLocations()){
				if (getApplicationContext() != null) { // tjis line will handle is activity facing any error befiore opeing it like permission
                    mLastLocation = location;
					try {
					LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
					
					mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
						mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
					Log.d(TAG,"LIVE LOCATION UPDATED");
					
					if (!getDriversAroundStarted)
                        getDriversAround();
				
					} catch (Exception mException) {
						mException.printStackTrace();
					}
				}
			}
		}
		
		
	};


	
	/*
	 * deal with with all permission related features dont see this
	 *
	 * */
	
	//=============================permissiosn===========================================================================================================
	
	
	
	
	
	private void checkLocationPermission()
	{
		if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
				PackageManager.PERMISSION_GRANTED) {
			if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
				new android.app.AlertDialog.Builder(this)
						.setTitle("give permission")
						.setMessage("give permission message")
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								ActivityCompat.requestPermissions(RiderMapActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
							}
						})
						.create()
						.show();
			} else {
				ActivityCompat.requestPermissions(RiderMapActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
			}
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
	
    
    
    
    
    
    
    
	@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
	{
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case 1:{
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
					if (ContextCompat.checkSelfPermission(this, permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mMap.setMyLocationEnabled(true);
                    }
				} else {
					Toast.makeText(getApplicationContext(), "Please provide the permission", Toast.LENGTH_LONG).show();
				}
				break;
			}
			case 2: {
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					if (ContextCompat.checkSelfPermission(this, permission.CALL_PHONE) ==
							PackageManager.PERMISSION_GRANTED) {
				
						makePhoneCall();
					}
				} else {
					Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
				}
				break;
		
			}
	
			case 3: {
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					if (ContextCompat.checkSelfPermission(this, permission.CALL_PHONE) ==
							PackageManager.PERMISSION_GRANTED) {
						makePhoneCallToWeride();
					}
				} else {
					Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
				}
				break;
			}
		}
	}
	
	
	
	
	//============================================================================================================================================================
	
	
	/*
	 ** deal with with all permission related features dont see this
	 *
	 *
	 *                   upper part only for permision
	 * */
	
	
	/*
	 *
	 * ================getDriversAround()
	 *
	 * deal with all types of drivers  that are around the Rider
	 *
	 *
	 * */
	
	/*works properly */
	boolean getDriversAroundStarted = false;
    List<Marker> markers = new ArrayList<Marker>();
    
    
    
    
    
    
    private void getDriversAround()
	{
        getDriversAroundStarted = true;
        DatabaseReference driverLocation = FirebaseDatabase.getInstance().getReference().child("driversAvailable");
		/* geo fire to store the user location reference in firebase*/
        GeoFire geoFire = new GeoFire(driverLocation);
		/*geo firease store the driver location*/
		GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(mLastLocation.getLongitude(),
						mLastLocation.getLatitude()),
				10000);//10000km
	
		geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                for(Marker markerIt : markers){
					if (markerIt.getTag() != null) {
						if (markerIt.getTag().equals(key))
							return;
					}
				}

                LatLng driverLocation = new LatLng(location.latitude, location.longitude);
	
				Marker mDriverMarker = mMap.addMarker(new MarkerOptions()
						.position(driverLocation).title(key).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_car)));
                mDriverMarker.setTag(key);
                markers.add(mDriverMarker);


            }

            @Override
            public void onKeyExited(String key) {
					for (Marker markerIt : markers) {
						if (markerIt.getTag() != null) {
                    if(markerIt.getTag().equals(key)){
                        markerIt.remove();
                    }
						}
					}
				
            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
					for (Marker markerIt : markers) {
						if (markerIt.getTag() != null) {
                    if(markerIt.getTag().equals(key)){
                        markerIt.setPosition(new LatLng(location.latitude, location.longitude));
                    }
						}
					}
            }

            @Override
            public void onGeoQueryReady() {
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
				Toast.makeText(RiderMapActivity.this, "problem in driver location query", Toast.LENGTH_LONG).show();
            }
        });
    }
    
	
	
	@Override
	protected void onStop() {
		super.onStop();
		System.exit(0);
	}
	
	int REQUEST_CALL = 2;
	
	
 
 
 
 
 
 
 
	private void makePhoneCall()
	{
		//String number ="+923424475733";
		String number = "1122";
		if (number.trim().length() >= 1) {
			
			if (ContextCompat.checkSelfPermission(RiderMapActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(RiderMapActivity.this,
						new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
			} else {
				String dial = "tel:" + number;
				startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
			}
			
		} else {
			Toast.makeText(RiderMapActivity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
		}
	}
	
	int REQUEST_Support_CALL = 3;
	
	
	
	
	
	
	
	
	
	
	
	
	
	private void makePhoneCallToWeride()
	{
		String number = "+923424475733";
		//String number ="1122";
		if (number.trim().length() > 9) {
			
			if (ContextCompat.checkSelfPermission(RiderMapActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(RiderMapActivity.this,
						new String[]{Manifest.permission.CALL_PHONE}, REQUEST_Support_CALL);
			} else {
				String dial = "tel:" + number;
				startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
			}
			
		} else {
			Toast.makeText(RiderMapActivity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
		}
	}


	
	
	
	
	
	@Override
	public void onBackPressed()
	{
		new AlertDialog.Builder(this)
				.setTitle("Really Exit?")
				.setMessage("Are you sure you want to exit?")
				.setNegativeButton(android.R.string.no, null)
				.setPositiveButton(android.R.string.yes, new OnClickListener() {
					
					public void onClick(DialogInterface arg0, int arg1) {
//						RiderMapActivity.super.onBackPressed();
						finish();
						System.exit(0);
					}
				}).create().show();
	}
	
	@Override
	public void onLocationChanged(final Location pLocation) {
	
	
	}
	
	
	
	
	
	
	
	private boolean isLocationEnabled()
	{
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
				LocationManager.NETWORK_PROVIDER
		);
	}
	
}
	/*
	int AUTOCOMPLETE_REQUEST_CODE = 1;
	
	public void onSearchCalled() {
		// Set the fields to specify which types of place data to return.
		List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);
		// Start the autocomplete intent.
		Intent intent = new Autocomplete.IntentBuilder(
				AutocompleteActivityMode.FULLSCREEN, fields).setCountry("PK") //pk
				.build(this);
		startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);
	}
	*/
/*
	@Override
	protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
			if (resultCode == RESULT_OK) {
				Place place = Autocomplete.getPlaceFromIntent(data);
				Log.i("TAG", "Place: " + place.getName() + ", " + place.getId() + ", " + place.getAddress());
				Toast.makeText(getApplicationContext(), "ID: " + place.getId() + "address:" + place.getAddress() + "Name:" + place.getName() + " latlong: " + place.getLatLng(), Toast.LENGTH_LONG).show();
				String address = place.getAddress();
				// do query with address
				
			} else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
				// TODO: Handle the error.
				Status status = Autocomplete.getStatusFromIntent(data);
				Toast.makeText(getApplicationContext(), "Error: " + status.getStatusMessage(), Toast.LENGTH_LONG).show();
				Log.i("TAG", status.getStatusMessage());
			} else if (resultCode == RESULT_CANCELED) {
				// The user canceled the operation.
			}
		}
	}*/