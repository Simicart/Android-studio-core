package com.simicart.plugins.storelocator.common;

import com.simicart.plugins.storelocator.controller.StoreLocatorMapController;
import com.simicart.plugins.storelocator.entity.StoreObject;

public class StoreLocatorConfig {

	public static StoreLocatorMapController mapController;
	
	public static int choose = 0;

	public static String convertAddress(StoreObject object) {
		String address = object.getAddress() + ", " + object.getCity();
		if (object.getState() != null && !object.getState().equals("null") && !object.getState().equals("")) {
			address += ", " + object.getState();
		}
		if (object.getZipcode() != null && !object.getZipcode().equals("null")
				&& !object.getZipcode().equals("")) {
			address += ", " + object.getZipcode();
		}
		address += ", " + object.getCountry_name();
		return address;
	}
	
}
