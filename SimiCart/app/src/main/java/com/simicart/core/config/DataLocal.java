package com.simicart.core.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.simicart.core.checkout.entity.Cart;
import com.simicart.core.cms.entity.Cms;
import com.simicart.core.customer.entity.ConfigCustomerAddress;
import com.simicart.core.store.entity.Stores;

import java.util.ArrayList;

public class DataLocal {
	public static Context mContext;
	public static SharedPreferences mSharedPre;
	public static boolean isTablet;
	public static boolean isCloud = false;

	public static ArrayList<Cms> listCms;
	public static ArrayList<Stores> listStores;
	public static ArrayList<Cart> listCarts;
	public static ConfigCustomerAddress ConfigCustomerAddress;
	public static ConfigCustomerAddress ConfigCustomerProfile;
	public static boolean isLanguageRTL = false;
	public static boolean isNewSignIn = false;
	public static String qtyCartAuto = "";


	public static void init(Context context) {
		listCms = new ArrayList<Cms>();
		listStores = new ArrayList<Stores>();
		listCarts = new ArrayList<Cart>();
	}

}
