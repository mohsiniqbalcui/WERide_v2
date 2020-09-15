package com.pool.Weride.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toolbar;

import androidx.fragment.app.Fragment;

import com.pool.Weride.R;
import com.pool.Weride.Util.Utils;


public class FAQ extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Context mActivity;
    String TAG = "FAQ";
    Toolbar toolbar;

    public static FAQ newInstance() {
        return new FAQ();
    }

    public void FAQ() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void fragmentBackPressed() {

    }

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

        View view = inflater.inflate(R.layout.fragment_faq, container, false);
        initUI(view);
        return view;

    }

    private void initUI(View view) {
        Utils.showLoger(TAG);
        mActivity = getActivity();
        intiVIew(view);
        initClickListener();
    }

    private void initClickListener() {
        toolbar.setClickable(true);

    }

    private void intiVIew(View view) {
        toolbar = view.findViewById(R.id.tb1);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

    }

}
