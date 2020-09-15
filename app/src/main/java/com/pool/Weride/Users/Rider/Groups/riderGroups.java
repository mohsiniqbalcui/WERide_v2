package com.pool.Weride.Users.Rider.Groups;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pool.Weride.R;
import com.pool.Weride.fragments.BaseFragment;


public class riderGroups extends BaseFragment {


    String riderslist;
    String Driverid;
    String FirstriderId;
    String FirstriderName;
    String SecondRiderid;
    String SecondRiderName;
    String thirdRiderid;
    String thirdRiderName;
    String selectedDrivername;
    String selectedDriverId;


    @Override
    protected void populateData() {

    }

    @Override
    protected void loadData() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rider_groups, container, false);
    }

    @Override
    protected void fragmentBackPressed() {

    }

}