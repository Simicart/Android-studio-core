package com.simicart.core.config;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;

import com.simicart.core.base.manager.SimiManager;
import com.simicart.core.checkout.entity.Cart;
import com.simicart.core.cms.entity.Cms;
import com.simicart.core.store.entity.Stores;

import java.util.ArrayList;

public class DataLocal {
	public static Context mContext;
	public static SharedPreferences mSharedPre;
	public static boolean isTablet;
	public static boolean isCloud = false;

	public static ArrayList<Cms> listCms;

	public static ArrayList<Cart> listCarts;
	public static boolean isLanguageRTL = false;
	public static boolean isNewSignIn = false;
	public static String qtyCartAuto = "";


	public static void init() {
		listCms = new ArrayList<>();
		listCarts = new ArrayList<>();
	}

	public static boolean isAccessLocation() {
		if (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) && hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) {
			return true;
		}
		return false;
	}

	public static boolean hasPermission(String perm) {
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			return (PackageManager.PERMISSION_GRANTED == SimiManager.getIntance().getCurrentActivity().checkSelfPermission(perm));
		}
		return true;
	}

}
