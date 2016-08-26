package com.simicart.plugins.locator.delegate;

import com.simicart.core.base.delegate.SimiDelegate;
import com.simicart.plugins.locator.entity.StoreObject;

import java.util.ArrayList;

public interface StoreLocatorMapDelegate extends SimiDelegate {
	
	public void addMarkerToMap(ArrayList<StoreObject> listStores);
	
}
