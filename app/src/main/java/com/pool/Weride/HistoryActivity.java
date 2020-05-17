package com.pool.Weride;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pool.Weride.historyRecyclerView.HistoryAdapter;
import com.pool.Weride.historyRecyclerView.HistoryObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
//import com.squareup.okhttp.Call;
//import com.squareup.okhttp.Callback;
//import com.squareup.okhttp.MediaType;
//import com.squareup.okhttp.OkHttpClient;
//import com.squareup.okhttp.RequestBody;
//import com.squareup.okhttp.Response;


public class HistoryActivity extends AppCompatActivity {
	private String customerOrDriver, userId;
	
	private RecyclerView mHistoryRecyclerView;
	private HistoryAdapter mHistoryAdapter;
	private RecyclerView.LayoutManager mHistoryLayoutManager;
	
	private TextView mBalance;
	private Double Balance = 0.0;
	private Button mPayout;
	private EditText mPayoutEmail;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_history);
		
		mBalance = findViewById(R.id.balance);
		mPayout = findViewById(R.id.payout);
		mPayoutEmail = findViewById(R.id.payoutEmail);
		
		mHistoryRecyclerView = findViewById(R.id.historyRecyclerView);
		mHistoryRecyclerView.setNestedScrollingEnabled(false);
		mHistoryRecyclerView.setHasFixedSize(true);
		mHistoryLayoutManager = new LinearLayoutManager(HistoryActivity.this);
		mHistoryRecyclerView.setLayoutManager(mHistoryLayoutManager);
		mHistoryAdapter = new HistoryAdapter(getDataSetHistory(), HistoryActivity.this);
		mHistoryRecyclerView.setAdapter(mHistoryAdapter);
		
		try {
			
			customerOrDriver = getIntent().getExtras().getString("customerOrDriver");
			userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
			getUserHistoryIds();
			
			if (customerOrDriver.equals("Drivers")) {
				mBalance.setVisibility(View.VISIBLE);
				mPayout.setVisibility(View.VISIBLE);
				mPayoutEmail.setVisibility(View.VISIBLE);
			}
			
			mPayout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					//      payoutRequest();
				}
			});
			
		} catch (Exception pE) {
			pE.printStackTrace();
		}
	}
	
	private void getUserHistoryIds() {
		DatabaseReference userHistoryDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(customerOrDriver).child(userId).child("history");
		userHistoryDatabase.addListenerForSingleValueEvent(new ValueEventListener()
		{
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				if (dataSnapshot.exists()) {
					for (DataSnapshot history : dataSnapshot.getChildren()) {
						FetchRideInformation(history.getKey());
					}
				}
			}
			
			@Override
			public void onCancelled(DatabaseError databaseError) {
			}
		});
	}
	
	private void FetchRideInformation(String rideKey) {
		DatabaseReference historyDatabase = FirebaseDatabase.getInstance().getReference().child("history").child(rideKey);
		historyDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
			@SuppressLint("SetTextI18n")
			@Override
			public void onDataChange(DataSnapshot dataSnapshot) {
				if (dataSnapshot.exists()) {
					String rideId = dataSnapshot.getKey();
					Long timestamp = 0L;
					String distance = "";
					Double ridePrice = 0.0;
					
					if (dataSnapshot.child("timestamp").getValue() != null) {
						timestamp = Long.valueOf(dataSnapshot.child("timestamp").getValue().toString());
					}
					
					if (dataSnapshot.child("customerPaid").getValue() != null && dataSnapshot.child("driverPaidOut").getValue() == null) {
						if (dataSnapshot.child("distance").getValue() != null) {
							ridePrice = Double.valueOf(dataSnapshot.child("price").getValue().toString());
							Balance += ridePrice;
							mBalance.setText("Balance: " + Balance + " $");
						}
					}
					
					
					HistoryObject obj = new HistoryObject(rideId, getDate(timestamp));
					resultsHistory.add(obj);
					mHistoryAdapter.notifyDataSetChanged();
				}
			}
			
			@Override
			public void onCancelled(DatabaseError databaseError) {
			}
		});
	}
	
	private String getDate(Long time) {
		Calendar cal = Calendar.getInstance(Locale.getDefault());
		cal.setTimeInMillis(time * 1000);
		String date = DateFormat.format("MM-dd-yyyy hh:mm", cal).toString();
		return date;
	}
	
	private ArrayList resultsHistory = new ArrayList<HistoryObject>();
	
	private ArrayList<HistoryObject> getDataSetHistory() {
		return resultsHistory;
	}
	
}


/*


    public static final MediaType MEDIA_TYPE = MediaType.parse("application/json");
    ProgressDialog progress;
    private void payoutRequest() {
        progress = new ProgressDialog(this);
       
        progress.setTitle("Processing your payout");
        progress.setMessage("Please Wait...");
        progress.setCancelable(true); // disable dismiss by tapping outside of the dialog
        progress.show();

        final OkHttpClient client = new OkHttpClient();
        JSONObject postData = new JSONObject();
        
        try {
            postData.put("uid", FirebaseAuth.getInstance().getCurrentUser().getUid());
            postData.put("email", mPayoutEmail.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        
*//*
        RequestBody body = RequestBody.create(MEDIA_TYPE,
                postData.toString());

        final Request request = new Request.Builder()
                .url("https://us-central1-uberapp-408c8.cloudfunctions.net/payout")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Your Token")
                .addHeader("cache-control", "no-cache")
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage();
                Log.w("failure Response", mMessage);
                progress.dismiss();
            }

            @Override
            public void onResponse(Call call, Response response)
                    throws IOException {

                int responseCode = response.code();


                if (response.isSuccessful())
                    switch (responseCode) {
                        case 200:
                            Snackbar.make(findViewById(R.id.layout), "Payout Successful!", Snackbar.LENGTH_LONG).show();
                            break;
                        case 501:
                            Snackbar.make(findViewById(R.id.layout), "Error: no payout available", Snackbar.LENGTH_LONG).show();
                            break;
                        default:
                            Snackbar.make(findViewById(R.id.layout), "Error: couldn't complete the transaction", Snackbar.LENGTH_LONG).show();
                            break;
                    }
                else
                    Snackbar.make(findViewById(R.id.layout), "Error: couldn't complete the transaction", Snackbar.LENGTH_LONG).show();

                progress.dismiss();
            }
        });*//*
    }*/
