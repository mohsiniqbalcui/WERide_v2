package com.pool.Weride.Activities.Auth.PhoneNumber;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.pool.Weride.Activities.userType;
import com.pool.Weride.Users.Driver.Map.DriverMapActivity;
import com.pool.Weride.Users.Driver.NavHome.driver_nav_drawer;
import com.pool.Weride.Users.Rider.Map.RiderHomeMapFragment;
import com.pool.Weride.R;
import com.pool.Weride.Users.Rider.NavHome.rider_nav_layout;

import java.util.concurrent.TimeUnit;

public class verifyPhoneActivity extends AppCompatActivity {


    TextView number;
    Editor editor;
    SharedPreferences mSharedPreferences;
    //These are the objects needed
    //It is the verification id that will be sent to the user
    private String mVerificationId;
    //The edittext to input the code
    private EditText editTextCode;
    //firebase auth object
    private FirebaseAuth mAuth;
    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code

            if (code != null) {
                editTextCode.setText(code);
                //verifying the code

                try {
                    verifyVerificationCode(code);

                } catch (Exception pE) {
                    pE.printStackTrace();
                    Toast.makeText(verifyPhoneActivity.this, "" + pE.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(verifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        try {
            //initializing objects
            mAuth = FirebaseAuth.getInstance();
            editTextCode = findViewById(R.id.editTextCode);
            number = findViewById(R.id.tvnumber);

            //getting mobile number from the previous activity
            //and sending the verification code to the number
            Intent intent = getIntent();
            String mobile = intent.getStringExtra("mobile");
            number.setText("Enter one time security code sended to" + mobile);
            sendVerificationCode(mobile);


            mSharedPreferences = this.getSharedPreferences("phone", 0); // 0 - for private mode
            editor = mSharedPreferences.edit();


            //if the automatic sms detection did not work, user can also enter the code manually
            //so adding a click listener to the button
            findViewById(R.id.buttonSignIn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String code = editTextCode.getText().toString().trim();

                    if (code.isEmpty() || code.length() < 6) {
                        editTextCode.setError("Enter valid code");
                        Toast.makeText(verifyPhoneActivity.this, "Enter valid code", Toast.LENGTH_SHORT).show();
                        editTextCode.requestFocus();
                        return;
                    }

                    //verifying the code entered manually
                    verifyVerificationCode(code);
                }
            });
        } catch (Exception pE) {
            Log.i("verifyphone", "onCreate: ");
            Toast.makeText(this, "" + pE.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //the method is sending verification code
    //the country id is concatenated
    //you can take the country id as user input as well
    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+92" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }

    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(verifyPhoneActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            createSession();

                            String type = "";

                            SharedPreferences mSharedPreferences1 = getSharedPreferences("type", MODE_PRIVATE);
                            type = mSharedPreferences1.getString("type", "");

                            try {

                                /* in case driver and rider both are already */
                                if (type == "driver") {
                                    startActivity(new Intent(getApplicationContext(), driver_nav_drawer.class));
                                    finish();
                                }
                                if (type == "rider") {
                                    startActivity(new Intent(getApplicationContext(), rider_nav_layout.class));
                                    finish();
                                }

                            } catch (Exception e) {
                                Toast.makeText(verifyPhoneActivity.this, "shared Preference storage", Toast.LENGTH_SHORT).show();
                            }

                            //verification successful we will start the profile activity
                            Intent intent = new Intent(verifyPhoneActivity.this, RiderHomeMapFragment.class);
                            startActivity(intent);

                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(getApplicationContext(), userType.class));
                                    finish();
                                }
                            });
                            snackbar.show();
                        }
                    }
                });
    }

    public void createSession() {
        editor.putString("phone", "verify");
        editor.commit();
    }

}