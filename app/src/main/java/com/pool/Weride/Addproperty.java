package com.pool.Weride;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Addproperty extends AppCompatActivity {
	
	private TextInputLayout textInputLayout;
	private static final int PERMISSION_REQUEST = 0;
	private static final int REQUEST_LOAD_IMAGE = 1;
	
	private static final int IMAGE_PICK_CODE = 1000;
	private static final int PERMISSION_CODE = 1001;
	
	int PICK_IMAGE_MULTIPLE = 1;
	String imageEncoded;
	List<String> imagesEncodedList;
	List<String> path;
	
	ImageView mImageView;
	Uri outPutfileUri;
	Button chooseimage;
	private AutoCompleteTextView dropDownText;
	
	@Override
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addproperty);
		
		
		path = new ArrayList<String>();
		// Spinner element
		final Spinner spinner = findViewById(R.id.spinner);
		Button button = findViewById(R.id.button);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(final View v) {
				onClick1();
			}
		});
		
		chooseimage = findViewById(R.id.chooseimage);
		mImageView = findViewById(R.id.image);
		
		// Spinner click listener
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
				
				// On selecting a spinner item
				String item = parent.getItemAtPosition(position).toString();
				// Showing selected spinner item
				//	Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
			}
			
			@Override
			public void onNothingSelected(final AdapterView<?> parent) {
			
			}
		});
		final Spinner spinner1 = findViewById(R.id.spinner1);
		
		final Spinner spinner2 = findViewById(R.id.spinner2);
		
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
			requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
			requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST);
		}
		
		chooseimage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			/*
					Intent mIntent = new Intent(Intent.ACTION_GET_CONTENT);
				    mIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
					mIntent.setType("images/*");
					startActivityForResult(Intent.createChooser(mIntent,"Select Pictures"), 1);
			*/
				FishBun.with(Addproperty.this)
						.setImageAdapter(new GlideAdapter())
						.setIsUseDetailView(false)
						.setMaxCount(5)
						.setMinCount(1)
						.setPickerSpanCount(6)
						.setActionBarColor(Color.parseColor("#795548"), Color.parseColor("#5D4037"), false)
						.setActionBarTitleColor(Color.parseColor("#ffffff"))
						//	.setArrayPaths(path)
						.setAlbumSpanCount(2, 4)
						.setButtonInAlbumActivity(false)
						.setCamera(true)
						.setReachLimitAutomaticClose(true)
//							.setHomeAsUpIndicatorDrawable(ContextCompat.getDrawable(this, R.drawable.ic_customer_complain))
						//.setOkButtonDrawable(ContextCompat.getDrawable(Addproperty.this, R.drawable.ic_check_black_24dp))
						.setAllViewTitle("All")
						.setActionBarTitle("Image Library")
						.textOnImagesSelectionLimitReached("Limit Reached!")
						.textOnNothingSelected("Nothing Selected")
						.setSelectCircleStrokeColor(Color.BLACK)
						.isStartInAllView(false)
						.startAlbum();
				
			}
		});
		
		
		// Spinner Drop down elements
		List<String> categories = new ArrayList<String>();
		categories.add("Rent");
		categories.add("Sale");
		
		// Creating adapter for spinner
		ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
		// Drop down layout style - list view with radio button
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// attaching data adapter to spinner
		spinner.setAdapter(dataAdapter);
		
		// Spinner Drop down Property Type
		List<String> categories1 = new ArrayList<String>();
		categories1.add("Plot");
		categories1.add("Home");
		categories1.add("Commercial");
		
		
		// Creating adapter for spinner
		ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories1);
		
		// Drop down layout style - list view with radio button
		dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		
		// attaching data adapter to spinner
		spinner1.setAdapter(dataAdapter1);
		// Spinner click listener
		spinner1.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
				
				// On selecting a spinner item
				String item = parent.getItemAtPosition(position).toString();
				// Showing selected spinner item
				//	Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
			}
			
			@Override
			public void onNothingSelected(final AdapterView<?> parent) {
			
			}
		});
		
		
		// Spinner Drop down Unit
		List<String> categories2 = new ArrayList<String>();
		categories2.add("Marla");
		categories2.add("Kanal");
		categories2.add("Square Feet");
		categories2.add("Square Meter");
		categories2.add("Square Yards");
		
		// Creating adapter for spinner
		ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories2);
		
		// Drop down layout style - list view with radio button
		dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// attaching data adapter to spinner
		spinner2.setAdapter(dataAdapter2);
		//spinner click listiner
		spinner2.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(final AdapterView<?> parent, final View view, final int position, final long id) {
				
				// On selecting a spinner item
				String item = parent.getItemAtPosition(position).toString();
				// Showing selected spinner item
				
			}
			
			@Override
			public void onNothingSelected(final AdapterView<?> parent) {
			
			}
		});
	}
	
	
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		switch (requestCode) {
			case PERMISSION_REQUEST:
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
				}
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == 1) {
			List<Bitmap> bitmaps = new ArrayList<>();
			ClipData mClipData = data.getClipData();
			
			if (mClipData != null) {
				for (int i = 0; i < mClipData.getItemCount(); i++) {
					Uri imageMUri = mClipData.getItemAt(i).getUri();
					try {
						InputStream is = getContentResolver().openInputStream(imageMUri);
						Bitmap mBitmap = BitmapFactory.decodeStream(is);
						bitmaps.add(mBitmap);
						
					} catch (FileNotFoundException pE) {
						pE.printStackTrace();
					}
				}
			} else {
				Uri imageMUri = data.getData();
				
				try {
					InputStream mInputStream = getContentResolver().openInputStream(imageMUri);
					Bitmap mBitmap = BitmapFactory.decodeStream(mInputStream);
					bitmaps.add(mBitmap);
					
				} catch (FileNotFoundException pE) {
					pE.printStackTrace();
				}
			}
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					for (final Bitmap b : bitmaps) {
						
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								mImageView.setImageBitmap(b);
							}
						});
						try {
							Thread.sleep(3000);
						} catch (InterruptedException pE) {
							pE.printStackTrace();
						}
					}
				}
			}).start();
					
					
					/*
					
					Uri selectImage = data.getData();
					mImageView.setImageURI(selectImage);*/
		}
		
	}
	
	
	public void onClick1() {
		
		
		EditText tv1 = findViewById(R.id.title1);
		
		EditText tv2 = findViewById(R.id.address1);
		
		EditText tv6 = findViewById(R.id.area1);
		
		EditText tv7 = findViewById(R.id.price1);
		EditText tv8 = findViewById(R.id.city1);
		
		EditText tv9 = findViewById(R.id.location1);
		EditText tv10 = findViewById(R.id.description1);
		
		EditText tv11 = findViewById(R.id.phone1);
		EditText tv12 = findViewById(R.id.email1);
		
		EditText tv13 = findViewById(R.id.bedroom1);
		EditText tv14 = findViewById(R.id.bathroom1);
		TextView tv15 = findViewById(R.id.kitchen1);
		
		
		String st1 = tv1.getText().toString();
		String st2 = tv2.getText().toString();
		String st6 = tv6.getText().toString();
		String st7 = tv7.getText().toString();
		String st8 = tv8.getText().toString();
		String st9 = tv9.getText().toString();
		String st10 = tv10.getText().toString();
		String st11 = tv11.getText().toString();
		String st12 = tv12.getText().toString();
		String st13 = tv13.getText().toString();
		
		String st14 = tv14.getText().toString();
		String st15 = tv15.getText().toString();
		
		
		Intent mIntent = new Intent(this, property_details.class);
		mIntent.putExtra("1", st1);
		mIntent.putExtra("2", st2);
		
		mIntent.putExtra("6", st6);
		mIntent.putExtra("7", st7);
		mIntent.putExtra("8", st8);
		mIntent.putExtra("9", st9);
		mIntent.putExtra("10", st10);
		mIntent.putExtra("10", st11);
		mIntent.putExtra("12", st12);
		mIntent.putExtra("13", st13);
		mIntent.putExtra("14", st14);
		mIntent.putExtra("15", st15);
		
		startActivity(mIntent);
		
		
	}
}
