package com.pool.Weride.History;

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
import com.pool.Weride.R;
import com.pool.Weride.historyRecyclerView.HistoryAdapter;
import com.pool.Weride.historyRecyclerView.HistoryObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

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
world time series
* */

