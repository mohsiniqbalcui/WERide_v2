package com.pool.Weride.Util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import com.pool.Weride.R;
import com.pool.Weride.constants.Constants;
import com.pool.Weride.interfaces.ProgressBarInterFace;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;


public class Utils {
    public static ProgressBarInterFace progressBarInterFace;

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
    public static void showToastLong(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
    public static void showLoger(String msg) {
        Log.d("weride", msg);
    }
    public static String getDiviceId(Context context) {
     return    Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public static void showLogerException(String msg) {
        Log.d("comsats", msg);
    }

    public static void hideKeyboardFrom(Context ctx) {
        InputMethodManager inputManager = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        View v = ((Activity) ctx).getCurrentFocus();
        if (v == null)
            return;
        if (inputManager != null) {
            inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    public static void switchToFragment(FragmentActivity mActivity, Fragment toFragment) {
        String tag = toFragment.getTag();
        Log.d(Constants.DEBUG_KEY, "Previous stack Count " + mActivity.getSupportFragmentManager().getBackStackEntryCount() + " tag " + tag);
        FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        ft.addToBackStack(null).replace(R.id.container, toFragment).commit();

    }


    public static void switchToFragmentUsingAnimation(FragmentActivity mActivity, Fragment toFragment, boolean b, String animation, boolean isAdd) {
        Log.d(Constants.DEBUG_KEY, "Previous stack Count " + mActivity.getSupportFragmentManager().getBackStackEntryCount());
        FragmentTransaction tr1 = mActivity.getSupportFragmentManager().beginTransaction();
        if (b) {
            tr1.addToBackStack(null);
        }
        tr1.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
        if (isAdd) {
            tr1.add(R.id.container, toFragment);
        } else {
            tr1.replace(R.id.container, toFragment);
        }
        tr1.commitAllowingStateLoss();
    }


    public static void clearFragmentStack(FragmentActivity mActivity, boolean isAll) {
        FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
        showLoger(Constants.DEBUG_KEY + " stack Count " + fragmentManager.getBackStackEntryCount());
        if (isAll) {
            for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
                fragmentManager.popBackStack();
            }
        } else {
            fragmentManager.popBackStack();
        }
    }


    public static final int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }


    public static void showHtmlInButton(Button button, String html) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            button.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT));
        } else {
            button.setText(Html.fromHtml(html));
        }
    }

    public static void showHtmlInTextVIew(TextView textView, String html) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            textView.setText(Html.fromHtml(html, Html.FROM_HTML_MODE_COMPACT));
        } else {
            textView.setText(Html.fromHtml(html));
        }
    }


    public static boolean isValidEmail(EditText argEditText) {
        try {
            Pattern pattern = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
            Matcher matcher = pattern.matcher(argEditText.getText().toString().trim());
            return matcher.matches();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }




    public static void setProgressBarInterFace(ProgressBarInterFace interFace) {

        Utils.progressBarInterFace = interFace;
    }


    @SuppressLint("MissingPermission")
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo == null || !activeNetworkInfo.isConnected();
    }


    public static void storeValuesInSharedPreferences(Context context, String key, String value) {
        PreferencesUtils preferences = PreferencesUtils.getInstance(context);
        preferences.putString(key, value);
    }

    public static String retrieveValuesFromSharedPreferences(Context context, String key) {
        PreferencesUtils preferences = PreferencesUtils.getInstance(context);
        String value = preferences.getString(key, "");
        showLoger("retrieveValuesFromSharedPreferences   " + value);
        return value.trim();
    }

    public static int retrieveIntValuesFromSharedPreferences(Context context, String key) {
        PreferencesUtils preferences = PreferencesUtils.getInstance(context);
        int value = preferences.getInt(key, -1);
        showLoger("retrieveValuesFromSharedPreferences   " + value);
        return value;
    }

    public static void storeIntValuesInSharedPreferences(Context context, String key, int value) {
        PreferencesUtils preferences = PreferencesUtils.getInstance(context);
        preferences.putInt(key, value);
    }

    public static void loadUserProfile(final Context mActivity, String url, CircleImageView imageView) {
        RequestOptions options = new RequestOptions()
                .placeholder(R.drawable.ic_user_profile)
                .error(R.drawable.ic_user_profile)
                .diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(mActivity)
                .load(url)
                .apply(options)
                .into(imageView);


    }

}
