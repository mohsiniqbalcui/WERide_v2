package com.pool.Weride;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class signup_activity extends AppCompatActivity {
	
	private EditText name, email_id, passwordcheck;
	private FirebaseAuth mAuth;
	private static final String TAG = "";
	private ProgressBar progressBar;
	
	private final static int RC_SIGN_IN = 123;
	GoogleSignInClient mGoogleSignInClient;
	FirebaseAuth.AuthStateListener mAuthListner;
	
	
	@Override
	protected void onStart() {
		Log.i("sign up start", " sign in");
		super.onStart();
		mAuth.addAuthStateListener(mAuthListner);
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		mAuth.removeAuthStateListener(mAuthListner);
		Log.i("sign up stop", " sign up");
	}
	
	//sign Up
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup_activity);
		mAuth = FirebaseAuth.getInstance();
		
		mAuthListner = new AuthStateListener() {
			@Override
			public void onAuthStateChanged(@NonNull final FirebaseAuth pFirebaseAuth) {
				FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
				if (user != null) {
					startActivity(new Intent(getApplicationContext(), PhoneNumberActivity.class));
					finish();
					return;
				}
			}
		};
		
		
		Button register = findViewById(R.id.btnRegister);
		email_id = findViewById(R.id.etEmail);
		progressBar = findViewById(R.id.progressBarsignup);
		passwordcheck = findViewById(R.id.etPassword);
		Button login = findViewById(R.id.btnRegisterLogin);
		login.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), signin_activity.class);
				Toast.makeText(signup_activity.this, "sign_in", Toast.LENGTH_SHORT).show();
				startActivity(intent);
				finish();
				return;
			}
		});
		
		register.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String email = email_id.getText().toString().trim();
				String password = passwordcheck.getText().toString().trim();
				
				if (TextUtils.isEmpty(password)) {
					Toast.makeText(getApplicationContext(), "Enter Password", Toast.LENGTH_SHORT).show();
					return;
				}
				if (password.length() <= 6) {
					Toast.makeText(signup_activity.this, "please enter password more then 7 digit", Toast.LENGTH_SHORT).show();
				}
				
				if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
					Toast.makeText(getApplicationContext(), "Enter Email Id", Toast.LENGTH_SHORT).show();
					return;
				}
				
				progressBar.setVisibility(View.VISIBLE);
				
				mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(signup_activity.this,
						new OnCompleteListener<AuthResult>() {
							@Override
							public void onComplete(@NonNull Task<AuthResult> task) {
								
								if (!task.isSuccessful()) {
									Toast.makeText(signup_activity.this, "sign up error" + task.getException(), Toast.LENGTH_LONG).show();
									Log.d(TAG, "createUserWithEmail:failure", task.getException());
									Log.d("register", "failed");
									
								} else {
									// sucess task
									
									String type = "";
									DatabaseReference current_user_db;
									
									SharedPreferences mSharedPreferences1 = getSharedPreferences("type", MODE_PRIVATE);
									type = mSharedPreferences1.getString("type", "");
									String user_id = mAuth.getCurrentUser().getUid();
									
									if (type == "driver") {
										current_user_db = FirebaseDatabase.getInstance().getReference().
												child("Users").child("Drivers").child(user_id);
									} else {
										current_user_db = FirebaseDatabase.getInstance().getReference().
												child("Users").child("Rider").child(user_id);
									}
									
									current_user_db.setValue(true);
									
									Intent intent = new Intent(signup_activity.this, PhoneNumberActivity.class);
									startActivity(intent);
									finish();
									
									
									Toast.makeText(signup_activity.this, "Successful", Toast.LENGTH_LONG).show();
									Log.d("register", "success");
									
								}
								
							}
						});
				return; // button onclick
			}
			
		});
		
		
	}
}


/*	mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(signup_activity.this,new OnCompleteListener<AuthResult>() {
							@Override
							public void onComplete(@NonNull Task<AuthResult> task) {
								
								progressBar.setVisibility(View.GONE);
								String user_id;
								if (!task.isSuccessful()) {
									// Sign in success, update UI with the signed-in user's information
									Log.d(TAG, "createUserWithEmail:success");
									
									DatabaseReference current_user_db;
								
									*//*if the driver is *//*
								
									if (type=="driver")
									{
										 current_user_db = FirebaseDatabase.getInstance().getReference().
												child("Users").child("drivers").child(user_id);
								}
									else
										{
											FirebaseUser user = mAuth.getCurrentUser();
										
										 user_id = mAuth.getCurrentUser().getUid();
										current_user_db = FirebaseDatabase.getInstance().getReference().
												child("Users").child("Customers").child(user_id);									}
									
		current_user_db.setValue(true);
									
									Intent intent = new Intent(signup_activity.this, PhoneNumberActivity.class);
									startActivity(intent);
									finish();
								} else {
									// If sign in fails, display a message to the user.
									Log.w(TAG, "createUserWithEmail:failure", task.getException());
									Toast.makeText(signup_activity.this, "Authentication failed.",
											Toast.LENGTH_SHORT).show();
								}
								
							}
							
							
						});*/
				