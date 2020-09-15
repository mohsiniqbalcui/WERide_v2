package com.pool.Weride.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.pool.Weride.R;
import com.pool.Weride.Util.Utils;
import com.pool.Weride.constants.Constants;
import com.pool.Weride.interfaces.ProgressBarInterFace;


/**
 * A simple {@link Fragment} subclass.
 */

@SuppressLint("ValidFragment")
public abstract class BaseFragment extends Fragment implements ProgressBarInterFace {

    protected FragmentActivity mActivity;
    protected View mView;
    boolean isFreindList;
    private TextView txtErrorMsg;

    public BaseFragment() {

    }

    abstract protected void populateData();

    abstract protected void loadData();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mView = view;
        view.setFocusableInTouchMode(true);
        view.requestFocus();

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {

                        if (mActivity.getSupportFragmentManager().getBackStackEntryCount() == 1) {
                            Log.d(Constants.DEBUG_KEY, "back stack entry count zero");
                            mActivity.finish();
                            return false;
                        }

                        mActivity.getSupportFragmentManager().popBackStack();
                        Log.d(Constants.DEBUG_KEY, "back stack entry count non zero");
                        return true;
                    }
                }

                return false;
            }
        });
    }


    protected void hideErrorMsg() {
        if (txtErrorMsg != null)
            txtErrorMsg.setVisibility(View.GONE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity = getActivity();
        Utils.setProgressBarInterFace(this);
        loadData();
    }

    @Override
    public void onResume() {
        super.onResume();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    BaseFragment.this.fragmentBackPressed();

                    return true;
                }
                return false;
            }
        });

    }

    protected abstract void fragmentBackPressed();


    protected void showProgressDialog(boolean show) {

        try {
            if (getView() != null) {
                if (show)
                    getView().findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                else getView().findViewById(R.id.progressBar).setVisibility(View.GONE);
            } else {
                Utils.showLoger("getView null progressBar not working");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Utils.showLoger("showProgressDialog " + e.getMessage());
        }

    }

    public boolean performFilter(String strToFilter) {
        return true;
    }


    protected boolean isReadContactPermissionGuranted() {
        int permissionCheck = ContextCompat.checkSelfPermission(mActivity, Manifest.permission.READ_CONTACTS);

        return (permissionCheck == PackageManager.PERMISSION_GRANTED) ? true : false;
    }


    @Override
    public void onProgressBarInterFace(boolean isShow) {
        showProgressDialog(isShow);
    }
}
