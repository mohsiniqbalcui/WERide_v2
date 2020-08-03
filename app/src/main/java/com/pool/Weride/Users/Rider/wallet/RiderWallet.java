package com.pool.Weride.Users.Rider.wallet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.pool.Weride.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RiderWallet#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RiderWallet extends Fragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	
	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	private String walletAmount;
	private String totalRiderExpense;
	private String monthlyExpense;

	public RiderWallet() {
		// Required empty public constructor
	}


	//return the intance with
	// TODO: Rename and change types and number of parameters
	public static RiderWallet newInstance(String param1, String param2) {
		RiderWallet fragment = new RiderWallet();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		args.putString(ARG_PARAM2, param2);
		fragment.setArguments(args);
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
			mParam2 = getArguments().getString(ARG_PARAM2);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		
		return inflater.inflate(R.layout.fragment_rider_wallet, container, false);
	}
}
