package com.pool.Weride.Users.Rider.Packages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.pool.Weride.R;
import com.pool.Weride.fragments.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Rider_packages#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Rider_packages extends BaseFragment {
	// TODO: Rename parameter arguments, choose names that match
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	private static final String ARG_PARAM2 = "param2";
	
	// TODO: Rename and change types of parameters
	private String mParam1;
	private String mParam2;
	private String packageDiscount1;
	private String packageDiscount2;
	private String packageDiscount3;

	
	public Rider_packages() {
		// Required empty public constructor
	}

	@Override
	protected void populateData() {

	}

	@Override
	protected void loadData() {

	}


	// TODO: Rename and change types and number of parameters
	public static Rider_packages newInstance(String param1, String param2) {
		Rider_packages fragment = new Rider_packages();
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
	protected void fragmentBackPressed() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		
		return inflater.inflate(R.layout.fragment_packages, container, false);
		
		
	}
}
