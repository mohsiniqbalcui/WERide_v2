package com.pool.Weride;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import java.net.InetAddress;

import static android.content.Context.CONNECTIVITY_SERVICE;


public class ApplicationUtility {
	ConnectivityManager connectivityManager;
	NetworkInfo info;
	
	public boolean checkConnection(Context context) {
		boolean flag = false;
		try {
			connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
			info = connectivityManager.getActiveNetworkInfo();
			
			if (info.getType() == ConnectivityManager.TYPE_WIFI) {
				System.out.println(info.getTypeName());
				flag = true;
			}
			
			if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
				System.out.println(info.getTypeName());
				flag = true;
			}
		} catch (Exception exception) {
			Toast.makeText(context, "Weak network connection.....", Toast.LENGTH_SHORT).show();
			System.out.println("Exception at network connection....."
					+ exception);
		}
		return flag;
	}
	
	
	public boolean isInternetAvailable() {
		try {
			InetAddress ipAddr = InetAddress.getByName("www.google.com");
			//You can replace it with your name
			return !ipAddr.equals("");
			
		} catch (Exception e) {
			return false;
		}
	}
}