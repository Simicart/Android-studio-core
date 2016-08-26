package com.simicart.plugins.locator.common;

import com.simicart.plugins.locator.controller.StoreLocatorMapController;
import com.simicart.plugins.locator.entity.StoreObject;

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
