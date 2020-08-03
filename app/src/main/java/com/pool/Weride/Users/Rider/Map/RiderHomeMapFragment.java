package com.pool.Weride.Users.Rider.Map;

import android.Manifest;
import android.Manifest.permission;
import android.annotation.SuppressLint;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
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
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.pool.Weride.R;
import com.pool.Weride.Users.Rider.NavHome.rider_nav_layout;
import com.pool.Weride.Util.Utils;
import com.pool.Weride.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;




public class RiderHomeMapFragment extends BaseFragment implements OnMapReadyCallback {

	private GoogleMap mMap;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    String TAG="RiderMapActivity";
    Context mActivity =getActivity();


    private FusedLocationProviderClient mFusedLocationClient;

    private Button  mRequest;
//customer current location that is pick up location
    private LatLng pickupLocation;

    private Boolean requestBol = false;
// customer current location marker
    private Marker pickupMarker;

    private SupportMapFragment mapFragment;
	AutocompleteSupportFragment autocompleteFragment;

// location where rider want to travel
    private String destinationName;
    // service car or rickshaw
		String requestService;
    private LatLng destinationLatLng;
    private LinearLayout mDriverInfo;
    private ImageView mDriverProfileImage;
    private TextView mDriverName, mDriverPhone, mDriverCar;
    private RadioGroup mRadioGroup;
    private RatingBar mRatingBar;
    ImageButton imageCall;
	private FirebaseAnalytics mFirebaseAnalytics;
	Editor type;
	SharedPreferences mSharedPreferences1;
	View mview;

	/*works properly */
	boolean getDriversAroundStarted = false;
	List<Marker> markers = new ArrayList<Marker>();

	private Marker mDriverMarker;
	private DatabaseReference driverLocationRef;
	private ValueEventListener driverLocationRefListener;

	private int radius = 1; //from 1
	private Boolean driverFound = false;
	private String driverFoundID;

	GeoQuery geoQuery;


	private DatabaseReference driveHasEndedRef;
	private ValueEventListener driveHasEndedRefListener;


	@Override
	protected void populateData() {

	}

	@Override
	protected void loadData() {

	}
	@Override
	protected void fragmentBackPressed() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_rider_map, container, false);
		try {
			intiUI(view);

		}catch (Exception pe){
			Log.e(TAG, "onCreateView: "+pe.getMessage() );
			pe.printStackTrace();
		}
		return view;
	}

	private void intiUI(View view) {
		mActivity = getActivity();
		Utils.showLoger(TAG);

		mview = view;// to use the find view by id in aother methods
		mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mActivity);

		 mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

		 autocompleteFragment = (AutocompleteSupportFragment)
				getChildFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

		autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Field.LAT_LNG)).setCountry("pk");



		autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
			@Override
			public void onPlaceSelected(Place place) {
				// TODO: Get info about the selected place.

				Log.d(TAG, "Place: " + place.getName() + ", " + place.getId());
				destinationName = place.getName();
				destinationLatLng = place.getLatLng();
				Utils.showLoger("destination"+ destinationName);
				Toast.makeText(mActivity, "destination" + destinationName, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onError(Status status) {
				// TODO: Handle the error.
				Log.e("customer", "onError: "+status);
				Toast.makeText(mActivity, "error autocompelete"+status, Toast.LENGTH_SHORT).show();

			}
		});

		mapFragment.getMapAsync(this::onMapReady);

		destinationLatLng = new LatLng(0.0, 0.0);

		mDriverInfo = view.findViewById(R.id.driverInfo);

		mDriverProfileImage = view.findViewById(R.id.driverProfileImage);

		mDriverName = view.findViewById(R.id.driverName);
		mDriverPhone = view.findViewById(R.id.driverPhone);
		mDriverCar = view.findViewById(R.id.driverCar);
		mRatingBar = view.findViewById(R.id.ratingBar1);
		mRadioGroup = view.findViewById(R.id.radioGroup1);


		mRequest = view.findViewById(R.id.callride);

		imageCall = view.findViewById(R.id.imageButton);

		mSharedPreferences1 = mActivity.getSharedPreferences("type",Context.MODE_PRIVATE);
		type = mSharedPreferences1.edit();

		imageCall.setOnClickListener(v -> makePhoneCall());

		// call uber driver
		mRequest.setOnClickListener(v -> {
			try {
				callDriver();
			} catch (Exception pE) {
				Utils.showToast(mActivity, "Error " + pE.getMessage());
				Toast.makeText(mActivity, "Exception in getting driver location" + pE, Toast.LENGTH_SHORT).show();
				pE.printStackTrace();
			}

		});
	}


	@Override
      public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try {
			checkLocationPermission();

			if (!Places.isInitialized()) {
					Places.initialize(mActivity, getString(R.string.google_maps_key), Locale.US);
		}

// yha problem ha wwwait

		}catch (Exception pE){
			Log.e(TAG, "onCreate: "+pE.getMessage());
		}
	}// oncreate


	public boolean onOptionsItemSelected(MenuItem item){
		Intent myIntent = new Intent(mActivity, rider_nav_layout.class);
		startActivityForResult(myIntent, 0);
		return true;
	}


	// from this starting request
	private void callDriver()
	{
		if (requestBol) {// only be true in case of ride completion or onCancelled
			Log.d(TAG, "callDriver: requestBol rider pressed two times request button to cancel ride");
			Toast.makeText(mActivity, "callDriver: requestBol rider pressed two times request button to cancel ride", Toast.LENGTH_SHORT).show();
			endRide();
			
		} else {
			// if requestbol false
			Log.d(TAG, "callDriver: finding driver");

			Toast.makeText(mActivity, "callDriver: finding driver", Toast.LENGTH_SHORT).show();

			int selectId = mRadioGroup.getCheckedRadioButtonId();
			final RadioButton radioButton = mview.findViewById(selectId);

			if (radioButton.getText() == null) {
				Toast.makeText(mActivity, "user not selected any service", Toast.LENGTH_SHORT).show();
				Log.e(TAG, "callDriver: user not selected any service");
				return;
			}
			
			requestService = radioButton.getText().toString().trim();// is car or rickshaw.
			Toast.makeText(mActivity, "request"+requestService, Toast.LENGTH_SHORT).show();

			requestBol = true;

			String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();//  current user id

			DatabaseReference ref = FirebaseDatabase.getInstance().getReference("customerRequest");

			if (ref.getKey()!=null){

			}else{
				Toast.makeText(mActivity, "customerRquest not triggered in database", Toast.LENGTH_SHORT).show();
			}
			
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

	

	/*
	*   =========getClosestDriver()
	*
	/*mActivity methios pick nesarest driver and save in fireabse realtime databvase by geo query implelemntation
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

		/*we not have the driver location in tghe firebase on current time mActivity will be emotyy
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
					Toast.makeText(mActivity, "before finding the closest driver by reading the drivers", Toast.LENGTH_SHORT).show();
					mCustomerDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
						@Override
						public void onDataChange(DataSnapshot dataSnapshot) {
							if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
								Map<String, Object> driverMap = (Map<String, Object>) dataSnapshot.getValue();
								/*driver information from firbase */
								if (driverFound) {
									Log.d(TAG,"driver is already found that customer demanding the service from drivers");
									Toast.makeText(mActivity, "driver is already found that customer demanding the service from drivers", Toast.LENGTH_SHORT).show();
									return;
								}
								//these lines not work properly

								if (driverMap.get("service").equals(requestService)) {
									driverFound = true;
									driverFoundID = dataSnapshot.getKey(); // neareast driver id
									
									Log.d(TAG,"Driver car service matched to rider demanding car service");
									Toast.makeText(mActivity, "Driver car service matched to rider demanding car service", Toast.LENGTH_SHORT).show();
									DatabaseReference driverRef = FirebaseDatabase.getInstance().getReference()
											.child("Users").child("Drivers").child(driverFoundID).child("customerRequest");
									
									String customerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
									HashMap map = new HashMap();
									map.put("customerRideId", customerId);
									map.put("destination", destinationName);
									map.put("destinationLat", destinationLatLng.latitude);
									map.put("destinationLng", destinationLatLng.longitude);
									driverRef.updateChildren(map);

									Toast.makeText(mActivity, "rider service matched to driver service", Toast.LENGTH_SHORT).show();

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

    
    
	/*mActivity method will be only called when */
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
	
					/*please remove mActivity comment*/
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
    |  Note:= mActivity method read the driver infomation from the firebase
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
						Glide.with(mActivity).load(dataSnapshot.child("profileImageUrl").getValue().toString()).into(mDriverProfileImage);
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
				Toast.makeText(mActivity, "error Users-Drivers-rating", Toast.LENGTH_SHORT).show();
			}
        });
    }

	/*-------------------------------------------- getHasRideEnde -----
 |  Function(s) getHasRideEnde
 |
 |  Purpose:  mActivity method will end the ride in case of ride is
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

	/*endRide action*/

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

	
    @SuppressLint("MissingPermission")
	@Override
    public void onMapReady(GoogleMap googleMap)
	{
		this.mMap = googleMap;
	
		if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(ContextCompat.checkSelfPermission(mActivity, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            }else{
                checkLocationPermission();
            }
        }

		mLocationRequest = LocationRequest.create(); // new keyword removed and .create added due to current playservices api >12
		mLocationRequest.setInterval(60000); //10 sec
		mLocationRequest.setFastestInterval(30000);
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
	
		mMap.getUiSettings().setZoomControlsEnabled(true);
		mMap.getUiSettings().setMyLocationButtonEnabled(true);
		
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
        mMap.setMyLocationEnabled(true);
    }
	

	
	LocationCallback mLocationCallback = new LocationCallback()
	{
        @Override
        public void onLocationResult(LocationResult locationResult) {
            for(Location location : locationResult.getLocations()){
				if (mActivity != null) {
					// this line will handle is activity facing any error befiore opeing it like permission
                    mLastLocation = location;
					try {
					LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
					
					mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
						mMap.animateCamera(CameraUpdateFactory.zoomTo(16));

						Toast.makeText(mActivity, "live location updated", Toast.LENGTH_SHORT).show();
						Log.d(TAG,"LIVE LOCATION UPDATED");
					
					if (!getDriversAroundStarted)
                        getDriversAround();
				
					} catch (Exception mException) {
						Toast.makeText(mActivity, "Error Message"+mException.getLocalizedMessage(),
								Toast.LENGTH_SHORT).show();

						Log.e(TAG, "onLocationResult: "+mException.getLocalizedMessage()+mException.getCause() );
						mException.printStackTrace();
					}
				}
			}
		}
		
	};
 
	
	/*
	 * deal with with all permission related features dont see mActivity
	 *
	 * */
	
	//=============================permissiosn===========================================================================================================
	
	
	
	private void checkLocationPermission()
	{
		Dexter.withContext(mActivity)
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

		if (ContextCompat.checkSelfPermission(mActivity, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
				PackageManager.PERMISSION_GRANTED) {
			if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION)) {
				new android.app.AlertDialog.Builder(mActivity)
						.setTitle("give permission")
						.setMessage("give permission message")
						.setPositiveButton("OK", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialogInterface, int i) {
								ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
							}
						})
						.create()
						.show();
			} else {
				ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
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
	

	@SuppressLint("MissingPermission")
	@Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
	{
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case 1:{
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
					if (ContextCompat.checkSelfPermission(mActivity, permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                        mMap.setMyLocationEnabled(true);
                    }
				} else {
					Toast.makeText(mActivity, "Please provide the permission", Toast.LENGTH_LONG).show();
				}
				break;
			}
			case 2: {
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					if (ContextCompat.checkSelfPermission(mActivity, permission.CALL_PHONE) ==
							PackageManager.PERMISSION_GRANTED) {
				
						makePhoneCall();
					}
				} else {
					Toast.makeText(mActivity, "Permission DENIED", Toast.LENGTH_SHORT).show();
				}
				break;
		
			}
	
			case 3: {
				if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					if (ContextCompat.checkSelfPermission(mActivity, permission.CALL_PHONE) ==
							PackageManager.PERMISSION_GRANTED) {
						makePhoneCallToWeride();
					}
				} else {
					Toast.makeText(mActivity, "Permission DENIED", Toast.LENGTH_SHORT).show();
				}
				break;
			}
		}
	}
	
	
	
	
	//============================================================================================================================================================

	
	/*
	 *
	 * ================getDriversAround()
	 *
	 * deal with all types of drivers  that are around the Rider
	 *
	 *
	 * */



    private void getDriversAround()
	{
        getDriversAroundStarted = true;
        DatabaseReference driverLocation = FirebaseDatabase.getInstance().getReference().child("driversAvailable");
		/* geo fire to store the user location reference in firebase*/
        GeoFire geoFire = new GeoFire(driverLocation);

        /*geo firease store the driver location*/

		GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(mLastLocation.getLongitude(), mLastLocation.getLatitude()), 10000);//10000km
	
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
				Toast.makeText(mActivity, ""+error.getMessage(),
						Toast.LENGTH_LONG).show();
            }
        });
    }
    

	int REQUEST_CALL = 2;

    // call to 1122
	private void makePhoneCall()
	{

		String number = "1122";
		if (number.trim().length() >= 1) {
			
			if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(getActivity(),
						new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL);
			} else {
				String dial = "tel:" + number;
				startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
			}
			
		} else {
			Toast.makeText(mActivity, "Enter Phone Number", Toast.LENGTH_SHORT).show();
		}
	}
	
	int REQUEST_Support_CALL = 3;

	// call to +923424475733
	private void makePhoneCallToWeride()
	{
		String number = "+923424475733";
		if (number.trim().length() > 9) {
			
			if (ContextCompat.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
				ActivityCompat.requestPermissions(getActivity(),
						new String[]{Manifest.permission.CALL_PHONE}, REQUEST_Support_CALL);
			} else {
				String dial = "tel:" + number;
				startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
			}
			
		} else {
			Toast.makeText(mActivity, "Enter Phone Number", Toast.LENGTH_SHORT).show();
		}
	}


	@SuppressLint("MissingPermission")
	@Override
	public void onAttach(Context context) {
		super.onAttach(context);

//		FirebaseDatabase.getInstance().setPersistenceEnabled(true);
		mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
		mActivity =context;
		Places.initialize(mActivity, getString(R.string.google_maps_key), Locale.US);

	}


	@Override
	public String toString() {
		return "RiderHomeMapFragment{" +
				"mMap=" + mMap +
				", mLastLocation=" + mLastLocation +
				", mLocationRequest=" + mLocationRequest +
				", TAG='" + TAG + '\'' +
				", mActivity=" + mActivity +
				", mFusedLocationClient=" + mFusedLocationClient +
				", mRequest=" + mRequest +
				", pickupLocation=" + pickupLocation +
				", requestBol=" + requestBol +
				", pickupMarker=" + pickupMarker +
				", mapFragment=" + mapFragment +
				", autocompleteFragment=" + autocompleteFragment +
				", destinationName='" + destinationName + '\'' +
				", requestService='" + requestService + '\'' +
				", destinationLatLng=" + destinationLatLng +
				", mDriverInfo=" + mDriverInfo +
				", mDriverProfileImage=" + mDriverProfileImage +
				", mDriverName=" + mDriverName +
				", mDriverPhone=" + mDriverPhone +
				", mDriverCar=" + mDriverCar +
				", mRadioGroup=" + mRadioGroup +
				", mRatingBar=" + mRatingBar +
				", imageCall=" + imageCall +
				", mFirebaseAnalytics=" + mFirebaseAnalytics +
				", type=" + type +
				", mSharedPreferences1=" + mSharedPreferences1 +
				", mview=" + mview +
				", radius=" + radius +
				", driverFound=" + driverFound +
				", driverFoundID='" + driverFoundID + '\'' +
				", geoQuery=" + geoQuery +
				", mDriverMarker=" + mDriverMarker +
				", driverLocationRef=" + driverLocationRef +
				", driverLocationRefListener=" + driverLocationRefListener +
				", driveHasEndedRef=" + driveHasEndedRef +
				", driveHasEndedRefListener=" + driveHasEndedRefListener +
				", mLocationCallback=" + mLocationCallback +
				", getDriversAroundStarted=" + getDriversAroundStarted +
				", markers=" + markers +
				", REQUEST_CALL=" + REQUEST_CALL +
				", REQUEST_Support_CALL=" + REQUEST_Support_CALL +
				'}';
	}

	public void onBackPressed()
	{
		new AlertDialog.Builder(mActivity)
				.setTitle("Really Exit?")
				.setMessage("Are you sure you want to exit?")
				.setNegativeButton(android.R.string.no, null)
				.setPositiveButton(android.R.string.yes, new OnClickListener() {

					public void onClick(DialogInterface arg0, int arg1) {
//					RIDER MAP ACTIVITY CALLED
						//	System.exit(0);
					}
				}).create().show();
	}


	private boolean isLocationEnabled()
	{
		LocationManager locationManager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
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
				.build(mActivity);
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
				Toast.makeText(mActivity, "ID: " + place.getId() + "address:" + place.getAddress() + "Name:" + place.getName() + " latlong: " + place.getLatLng(), Toast.LENGTH_LONG).show();
				String address = place.getAddress();
				// do query with address
				
			} else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
				// TODO: Handle the error.
				Status status = Autocomplete.getStatusFromIntent(data);
				Toast.makeText(mActivity, "Error: " + status.getStatusMessage(), Toast.LENGTH_LONG).show();
				Log.i("TAG", status.getStatusMessage());
			} else if (resultCode == RESULT_CANCELED) {
				// The user canceled the operation.
			}
		}
	}*/


